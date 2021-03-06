package net.sourceforge.fenixedu.applicationTier;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.logging.ServiceExecutionLog;
import net.sourceforge.fenixedu.applicationTier.logging.SystemInfo;
import net.sourceforge.fenixedu.applicationTier.logging.UserExecutionLog;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.FastHashMap;
import org.apache.log4j.Logger;

import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.ist.fenixframework.pstm.IllegalWriteException;
import pt.ist.fenixframework.pstm.ServiceInfo;
import pt.ist.fenixframework.pstm.Transaction;
import pt.utl.ist.berserk.logic.filterManager.FilterInvocationTimingType;
import pt.utl.ist.berserk.logic.filterManager.exceptions.ClassNotIFilterException;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterRetrieveException;
import pt.utl.ist.berserk.logic.filterManager.exceptions.IncompatibleFilterException;
import pt.utl.ist.berserk.logic.filterManager.exceptions.InvalidFilterException;
import pt.utl.ist.berserk.logic.filterManager.exceptions.InvalidFilterExpressionException;
import pt.utl.ist.berserk.logic.serviceManager.IServiceManager;
import pt.utl.ist.berserk.logic.serviceManager.ServiceManager;
import pt.utl.ist.berserk.logic.serviceManager.exceptions.FilterChainFailedException;
import pt.utl.ist.berserk.logic.serviceManager.exceptions.InvalidServiceException;
import pt.utl.ist.berserk.logic.serviceManager.exceptions.ServiceManagerException;

public class ServiceManagerDefaultImpl implements IServiceManagerWrapper {

    private static final Logger logger = Logger.getLogger(ServiceManagerDefaultImpl.class);

    private static boolean serviceLoggingIsOn;

    private static FastHashMap mapServicesToWatch;

    private static boolean userLoggingIsOn;

    private static FastHashMap mapUsersToWatch;

    private static final Map<String, String> KNOWN_WRITE_SERVICES = new ConcurrentHashMap<String, String>();

    static {
        serviceLoggingIsOn = false;
        mapServicesToWatch = new FastHashMap();
        mapServicesToWatch.setFast(true);

        userLoggingIsOn = false;
        mapUsersToWatch = new FastHashMap();
        mapUsersToWatch.setFast(true);
    }

    public class ExceptionWrapper extends RuntimeException {
        public ExceptionWrapper(Throwable t) {
            super(t);
        }
    }

    /**
     * Executes a given service.
     * 
     * @param id
     *            represents the identification of the entity that desires to
     *            run the service
     * 
     * @param service
     *            is a string containing the name of the service to execute
     * 
     * @param argumentos
     *            is a vector with the arguments of the service to execute.
     * @throws FenixFilterException
     * 
     */
    @Override
    public Object execute(IUserView id, String service, Object args[]) throws FenixServiceException, FenixFilterException {
        return execute(id, service, "run", args);
    }

    @Override
    public Object execute(IUserView id, String service, String method, Object[] args) throws FenixServiceException,
            FenixFilterException {
        Calendar serviceStartTime = null;
        Calendar serviceEndTime = null;

        try {
            IServiceManager manager = ServiceManager.getInstance();
            if (serviceLoggingIsOn || (userLoggingIsOn && id != null)) {
                serviceStartTime = Calendar.getInstance();
            }
            Object serviceResult = null;

            if (pt.ist.fenixWebFramework.services.ServiceManager.isInsideService()) {
                serviceResult = manager.execute(id, service, method, args);
            } else {
                int tries = 0;

                try {
                    pt.ist.fenixWebFramework.services.ServiceManager.enterBerserkService();
                    // Replace this line with the following block if conflicting
                    // transactions should restart automatically
                    // Object serviceResult = manager.execute(id, service,
                    // method,
                    // args);

                    final String username = id != null ? id.getUtilizador() : null;
                    ServiceInfo.setCurrentServiceInfo(username, service, args);

                    // try read-only transaction first, but only for non-public
                    // sessions...
                    Transaction.setDefaultReadOnly(!KNOWN_WRITE_SERVICES.containsKey(service));

                    while (true) {
                        tries++;
                        try {
                            serviceResult = manager.execute(id, service, method, args);
                            break;
                        } catch (jvstm.CommitException ce) {
                            // ce.printStackTrace();
                            if (LogLevel.INFO) {
                                System.out.println("Restarting TX because of CommitException");
                            }
                            // repeat service
                            if (tries > 3) {
                                System.out.println("Service " + service + " has been restarted " + tries
                                        + " times because of CommitException.");
                            }
                        } catch (AbstractDomainObject.UnableToDetermineIdException ce) {
                            // ce.printStackTrace();
                            if (LogLevel.INFO) {
                                System.out.println("Restarting TX because of UnableToDetermineIdException");
                            }
                            // repeat service
                            if (tries > 3) {
                                System.out.println("Service " + service + " has been restarted " + tries
                                        + " times because of UnableToDetermineIdException.");
                            }
                        } catch (IllegalWriteException iwe) {
                            KNOWN_WRITE_SERVICES.put(service, service);
                            Transaction.setDefaultReadOnly(false);
                            // repeat service
                            if (tries > 3) {
                                System.out.println("Service " + service + " has been restarted " + tries
                                        + " times because of IllegalWriteException.");
                            }
                        }
                    }
                } finally {
                    pt.ist.fenixWebFramework.services.ServiceManager.exitBerserkService();
                    Transaction.setDefaultReadOnly(false);
                    if (LogLevel.INFO) {
                        if (tries > 1) {
                            System.out.println("Service " + service + " took " + tries + " tries.");
                        }
                    }
                }
            }

            if (serviceLoggingIsOn || (userLoggingIsOn && id != null)) {
                serviceEndTime = Calendar.getInstance();
            }
            if (serviceLoggingIsOn && serviceStartTime != null && serviceEndTime != null) {
                registerServiceExecutionTime(service, method, args, serviceStartTime, serviceEndTime);
            }
            if (userLoggingIsOn && id != null && serviceStartTime != null && serviceEndTime != null) {
                registerUserExecutionOfService(id, service, method, args, serviceStartTime, serviceEndTime);
            }
            return serviceResult;
        } catch (jvstm.CommitException e) {
            // we are inside another service, let him handle the repeat
            throw e;
        } catch (AbstractDomainObject.UnableToDetermineIdException e) {
            // we are inside another service, let him handle the id retry
            throw e;
        } catch (IllegalWriteException e) {
            // we are inside another service, let him handle the write
            // transaction conversion
            throw e;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        } catch (InvalidServiceException e) {
            throw new FenixServiceException(e);
        } catch (FilterRetrieveException e) {
            throw new FenixServiceException(e);
        } catch (InvalidFilterExpressionException e) {
            throw new FenixServiceException(e);
        } catch (InvalidFilterException e) {
            throw new FenixServiceException(e);
        } catch (ClassNotIFilterException e) {
            throw new FenixServiceException(e);
        } catch (IncompatibleFilterException e) {
            throw new FenixServiceException(e);
        } catch (FilterChainFailedException e) {
            FilterChainFailedException filterChainFailedException = e;
            Map failedPreFilters = filterChainFailedException.getFailedFilters(FilterInvocationTimingType.PRE);
            Map failedPostFilters = filterChainFailedException.getFailedFilters(FilterInvocationTimingType.POST);
            if (failedPreFilters != null && !failedPreFilters.isEmpty()) {
                for (List failledExceptions : (Collection<List>) failedPreFilters.values()) {
                    if (!failledExceptions.isEmpty()) {
                        throw (FenixFilterException) failledExceptions.get(0);
                    }
                }
            }
            if (failedPostFilters != null && !failedPostFilters.isEmpty()) {
                for (List failledExceptions : (Collection<List>) failedPostFilters.values()) {
                    if (!failledExceptions.isEmpty()) {
                        throw (FenixFilterException) failledExceptions.get(0);
                    }
                }
            }
            throw new FenixServiceException(e);
        } catch (ServiceManagerException e) {
            throw new ExceptionWrapper(e);
        } catch (Throwable t) {
            if (LogLevel.WARN) {
                t.printStackTrace();
            }
            throw new ExceptionWrapper(t);
        }
    }

    @Override
    public synchronized void turnServiceLoggingOn(IUserView id) {
        serviceLoggingIsOn = true;
    }

    @Override
    public synchronized void turnServiceLoggingOff(IUserView id) {
        serviceLoggingIsOn = false;
    }

    @Override
    public synchronized void clearServiceLogHistory(IUserView id) {
        mapServicesToWatch.clear();
    }

    @Override
    public synchronized void turnUserLoggingOn(IUserView id) {
        userLoggingIsOn = true;
    }

    @Override
    public synchronized void turnUserLoggingOff(IUserView id) {
        userLoggingIsOn = false;
    }

    @Override
    public synchronized void clearUserLogHistory(IUserView id) {
        mapUsersToWatch.clear();
    }

    private void registerServiceExecutionTime(String service, String method, Object[] args, Calendar serviceStartTime,
            Calendar serviceEndTime) {
        String hashKey = generateServiceHashKey(service, method, args);
        long serviceExecutionTime = calculateServiceExecutionTime(serviceStartTime, serviceEndTime);
        ServiceExecutionLog serviceExecutionLog = (ServiceExecutionLog) mapServicesToWatch.get(hashKey);
        if (serviceExecutionLog == null) {
            serviceExecutionLog = new ServiceExecutionLog(hashKey);
            mapServicesToWatch.put(hashKey, serviceExecutionLog);
        }

        serviceExecutionLog.addExecutionTime(serviceExecutionTime);
    }

    private void registerUserExecutionOfService(IUserView id, String service, String method, Object[] args,
            Calendar serviceStartTime, Calendar serviceEndTime) {
        UserExecutionLog userExecutionLog = (UserExecutionLog) mapUsersToWatch.get(id.getUtilizador());
        if (userExecutionLog == null) {
            userExecutionLog = new UserExecutionLog(id);
            mapUsersToWatch.put(id.getUtilizador(), userExecutionLog);
        }

        userExecutionLog.addServiceCall(generateServiceHashKey(service, method, args), serviceStartTime);
    }

    private long calculateServiceExecutionTime(Calendar serviceStartTime, Calendar serviceEndTime) {
        return serviceEndTime.getTimeInMillis() - serviceStartTime.getTimeInMillis();
    }

    private String generateServiceHashKey(String service, String method, Object[] args) {
        String hashKey = service + "." + method + "(";
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null) {
                    hashKey += args[i].getClass().getName();
                } else {
                    hashKey += "null";
                }
                if (i + 1 < args.length) {
                    hashKey += ", ";
                }
            }
        }
        hashKey += ")";
        return hashKey;
    }

    @Override
    public HashMap getMapServicesToWatch(IUserView id) {
        return mapServicesToWatch;
    }

    @Override
    public Boolean serviceLoggingIsOn(IUserView id) {
        return new Boolean(serviceLoggingIsOn);
    }

    @Override
    public HashMap getMapUsersToWatch(IUserView id) {
        return mapUsersToWatch;
    }

    @Override
    public Boolean userLoggingIsOn(IUserView id) {
        return new Boolean(userLoggingIsOn);
    }

    @Override
    public SystemInfo getSystemInfo(IUserView id) {
        return new SystemInfo();
    }

}
