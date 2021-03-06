/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProjectAccess;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentProject;
import net.sourceforge.fenixedu.util.StringUtils;

/**
 * @author Susana Fernandes
 */
public class ReadProjectAccesses extends FenixService {

    public List run(String username, String costCenter, BackendInstance instance, String userNumber)
            throws FenixServiceException, ExcepcaoPersistencia {
        PersistentProject persistentProject = new PersistentProject();
        if (persistentProject.countUserProject(new Integer(userNumber), instance) == 0) {
            throw new InvalidArgumentsServiceException();
        }
        List<ProjectAccess> projectAcessesList =
                ProjectAccess.getAllByCoordinator(new Integer(userNumber), (!StringUtils.isEmpty(costCenter)), instance);
        List<InfoProjectAccess> infoProjectAcessesList = new ArrayList<InfoProjectAccess>();
        for (ProjectAccess projectAccess : projectAcessesList) {
            InfoProjectAccess infoProjectAccess = InfoProjectAccess.newInfoFromDomain(projectAccess);
            infoProjectAccess.setInfoProject(persistentProject.readProject(infoProjectAccess.getKeyProject(), instance));
            infoProjectAcessesList.add(infoProjectAccess);
        }

        return infoProjectAcessesList;
    }

    public List run(String userView, String costCenter, String username, BackendInstance instance, String userNumber)
            throws FenixServiceException, ExcepcaoPersistencia {
        Integer personCoordinator = new Integer(userNumber);
        List<InfoProjectAccess> infoProjectAcessesList = new ArrayList<InfoProjectAccess>();
        final Person person = Person.readPersonByUsername(username);
        if (person != null) {
            List<ProjectAccess> projectAcessesList = person.readProjectAccessesByCoordinator(personCoordinator, instance);
            PersistentProject persistentProject = new PersistentProject();
            for (ProjectAccess projectAccess : projectAcessesList) {
                InfoProjectAccess infoProjectAccess = InfoProjectAccess.newInfoFromDomain(projectAccess);
                infoProjectAccess.setInfoProject(persistentProject.readProject(infoProjectAccess.getKeyProject(), instance));
                infoProjectAcessesList.add(infoProjectAccess);
            }
        }
        return infoProjectAcessesList;
    }

    public InfoProjectAccess run(String username, String costCenter, Integer personCode, String projectCode,
            BackendInstance instance, String userNumber) throws ExcepcaoPersistencia {
        Person person = (Person) rootDomainObject.readPartyByOID(personCode);
        PersistentProject persistentProject = new PersistentProject();
        InfoProjectAccess infoProjectAccess =
                InfoProjectAccess.newInfoFromDomain(ProjectAccess.getByPersonAndProject(person, projectCode, instance));
        infoProjectAccess.setInfoProject(persistentProject.readProject(infoProjectAccess.getKeyProject(), instance));
        return infoProjectAccess;
    }

}