package net.sourceforge.fenixedu.presentationTier.Action.protocols;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits.CreateExternalUnitByNameAndCountry;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ExecuteFactoryMethod;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.InsertExternalPerson;
import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFactory;
import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFactory.EditProtocolAction;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.protocols.Protocol;
import net.sourceforge.fenixedu.domain.protocols.ProtocolFile;
import net.sourceforge.fenixedu.domain.protocols.ProtocolHistory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class EditProtocolDispatchAction extends FenixDispatchAction {

    public ActionForward viewProtocol(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Integer protocolID = getIntegerFromRequest(request, "idInternal");
        Protocol protocol = (Protocol) RootDomainObject.readDomainObjectByOID(Protocol.class, protocolID);
        request.setAttribute("protocolFactory", new ProtocolFactory(protocol));
        return mapping.findForward("view-protocol");
    }

    public ActionForward prepareEditProtocolData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Integer protocolID = getIntegerFromRequest(request, "protocolID");
        Protocol protocol = (Protocol) RootDomainObject.readDomainObjectByOID(Protocol.class, protocolID);
        request.setAttribute("protocolFactory", new ProtocolFactory(protocol));
        return mapping.findForward("edit-protocol-data");
    }

    public ActionForward editProtocolData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolFactory protocolFactory = getRenderedObject();
        if (isCancelled(request)) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("view-protocol");
        }
        if (isProtocolNumberValid(protocolFactory)) {
            if (validateDates(protocolFactory, request)) {
                protocolFactory.setEditProtocolAction(EditProtocolAction.EDIT_PROTOCOL_DATA);
                Protocol protocol = (Protocol) ExecuteFactoryMethod.run(protocolFactory);
                request.setAttribute("protocolFactory", new ProtocolFactory(protocol));
                return mapping.findForward("view-protocol");
            } else {
                request.setAttribute("protocolFactory", protocolFactory);
                return mapping.findForward("edit-protocol-data");
            }
        } else {
            request.setAttribute("protocolFactory", protocolFactory);
            validateDates(protocolFactory, request);
            setError(request, "errorMessage", new ActionMessage("error.protocol.number.alreadyExists"));
            return mapping.findForward("edit-protocol-data");
        }
    }

    private boolean validateDates(ProtocolFactory protocolFactory, HttpServletRequest request) {
        for (ProtocolHistory protocolHistory : protocolFactory.getProtocolHistories()) {
            if (protocolHistory.getBeginDate() != null && protocolHistory.getEndDate() != null
                    && !protocolHistory.getBeginDate().isBefore(protocolHistory.getEndDate())) {
                setError(request, "errorDateMessage", new ActionMessage("error.protocol.dates.notContinuous"));
                return false;
            }
        }
        return true;
    }

    private boolean isProtocolNumberValid(ProtocolFactory protocolFactory) {
        for (Protocol protocol : rootDomainObject.getProtocols()) {
            if (protocol.getProtocolNumber().equals(protocolFactory.getProtocolNumber())
                    && protocol != protocolFactory.getProtocol()) {
                return false;
            }
        }
        return true;
    }

    public ActionForward prepareEditResponsibles(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Integer protocolID = getIntegerFromRequest(request, "protocolID");
        ProtocolFactory protocolFactory = null;
        if (protocolID != null) {
            Protocol protocol = (Protocol) RootDomainObject.readDomainObjectByOID(Protocol.class, protocolID);
            protocolFactory = new ProtocolFactory(protocol);
        } else {
            protocolFactory = getRenderedObject();
        }
        RenderUtils.invalidateViewState();
        protocolFactory.resetSearches();
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("edit-protocol-responsibles");
    }

    public ActionForward editResponsibles(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolFactory protocolFactory = getRenderedObject();
        RenderUtils.invalidateViewState();
        if (request.getParameter("back") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("view-protocol");
        }
        if (request.getParameter("cancel") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("edit-protocol-responsibles");
        }
        if (request.getParameter("createNew") != null) {
            request.setAttribute("createExternalPerson", "true");
            protocolFactory.setInternalUnit(false);
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("edit-protocol-responsibles");
        }

        if (protocolFactory.getPartnerResponsible() == null
                && (protocolFactory.getResponsible() == null || !protocolFactory.getIstResponsibleIsPerson())
                && protocolFactory.getResponsibleFunction() == null) {
            if (StringUtils.isEmpty(protocolFactory.getResponsibleName()) || protocolFactory.getIstResponsible()) {
                setError(request, "errorMessage", new ActionMessage("error.protocol.person.selectFromList"));
            } else if (!protocolFactory.getIstResponsible()) {
                request.setAttribute("needToCreatePerson", "true");
            }
        } else {
            protocolFactory.setEditProtocolAction(EditProtocolAction.ADD_RESPONSIBLE);
            if (protocolFactory.getIstResponsible()) {
                if (protocolFactory.getIstResponsibleIsPerson()) {
                    protocolFactory.setResponsibleToAdd(protocolFactory.getResponsible().getPerson());
                } else {

                }
            } else {
                protocolFactory.setResponsibleToAdd(protocolFactory.getPartnerResponsible().getPerson());
            }
            if (protocolFactory.getResponsibles().contains(protocolFactory.getResponsibleToAdd())
                    || protocolFactory.getPartnerResponsibles().contains(protocolFactory.getResponsibleToAdd())) {
                setError(request, "errorMessage", new ActionMessage("error.protocol.duplicated.responsible"));
            } else if (protocolFactory.getResponsibleFunction() != null
                    && protocolFactory.getResponsibleFunctions().contains(protocolFactory.getResponsibleFunction())) {
                setError(request, "errorMessage", new ActionMessage("error.protocol.duplicated.responsibleFunction"));
            } else {
                Protocol protocol = (Protocol) ExecuteFactoryMethod.run(protocolFactory);
                protocolFactory = new ProtocolFactory(protocol);
            }
        }

        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("edit-protocol-responsibles");
    }

    public ActionForward createExternalResponsible(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = getRenderedObject();
        if (request.getParameter("cancel") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("edit-protocol-responsibles");
        }
        if (protocolFactory.getUnitObject() != null) {
            ExternalContract externalContract =
                    InsertExternalPerson.run(new InsertExternalPerson.ServiceArguments(protocolFactory.getResponsibleName(),
                            protocolFactory.getUnitObject().getUnit()));
            protocolFactory.setResponsibleToAdd(externalContract.getPerson());
            protocolFactory.setEditProtocolAction(EditProtocolAction.ADD_RESPONSIBLE);
            Protocol protocol = (Protocol) ExecuteFactoryMethod.run(protocolFactory);
            protocolFactory = new ProtocolFactory(protocol);
        } else if (request.getParameter("createNew") != null) {
            request.setAttribute("createExternalUnit", "true");
        } else {
            request.setAttribute("createExternalPerson", "true");
            request.setAttribute("needToCreateUnit", "true");
        }
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("edit-protocol-responsibles");
    }

    public ActionForward createExternalPersonAndUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = getRenderedObject();
        if (request.getParameter("cancel") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            request.setAttribute("createExternalPerson", "true");
            return mapping.findForward("edit-protocol-responsibles");
        }

        ExternalContract externalContract =
                InsertExternalPerson.run(protocolFactory.getResponsibleName(), protocolFactory.getUnitName(),
                        protocolFactory.getCountry());
        protocolFactory.setResponsibleToAdd(externalContract.getPerson());
        protocolFactory.setEditProtocolAction(EditProtocolAction.ADD_RESPONSIBLE);
        Protocol protocol = (Protocol) ExecuteFactoryMethod.run(protocolFactory);
        protocolFactory = new ProtocolFactory(protocol);

        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("edit-protocol-responsibles");
    }

    public ActionForward prepareEditUnits(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Integer protocolID = getIntegerFromRequest(request, "protocolID");
        ProtocolFactory protocolFactory = null;
        if (protocolID != null) {
            Protocol protocol = (Protocol) RootDomainObject.readDomainObjectByOID(Protocol.class, protocolID);
            protocolFactory = new ProtocolFactory(protocol);
        } else {
            protocolFactory = getRenderedObject();
        }
        protocolFactory.resetSearches();
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("edit-protocol-units");
    }

    public ActionForward editUnits(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolFactory protocolFactory = getRenderedObject();
        if (request.getParameter("back") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("view-protocol");
        }
        if (request.getParameter("cancel") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("edit-protocol-units");
        }
        if (request.getParameter("createNew") != null) {
            request.setAttribute("createExternalUnit", "true");
            protocolFactory.setInternalUnit(false);
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("edit-protocol-units");
        }

        if (protocolFactory.getUnitObject() == null) {
            if (StringUtils.isEmpty(protocolFactory.getUnitName()) || protocolFactory.getInternalUnit()) {
                setError(request, "errorMessage", new ActionMessage("error.protocol.unit.selectFromList"));
            } else if (!protocolFactory.getInternalUnit()) {
                request.setAttribute("needToCreateUnit", "true");
            }
        } else {
            protocolFactory.setUnitToAdd(protocolFactory.getUnitObject().getUnit());
            if (protocolFactory.getUnits().contains(protocolFactory.getUnitToAdd())
                    || protocolFactory.getPartnerUnits().contains(protocolFactory.getUnitToAdd())) {
                setError(request, "errorMessage", new ActionMessage("error.protocol.duplicated.unit"));
            } else {
                protocolFactory.setEditProtocolAction(EditProtocolAction.ADD_UNIT);
                Protocol protocol = (Protocol) ExecuteFactoryMethod.run(protocolFactory);
                protocolFactory = new ProtocolFactory(protocol);
            }
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("edit-protocol-units");
    }

    public ActionForward createExternalUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = getRenderedObject();
        if (request.getParameter("cancel") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("edit-protocol-units");
        }

        Unit externalUnit = CreateExternalUnitByNameAndCountry.run(protocolFactory.getUnitName(), protocolFactory.getCountry());
        protocolFactory.setUnitToAdd(externalUnit);
        protocolFactory.setEditProtocolAction(EditProtocolAction.ADD_UNIT);
        Protocol protocol = (Protocol) ExecuteFactoryMethod.run(protocolFactory);
        protocolFactory = new ProtocolFactory(protocol);

        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("edit-protocol-units");
    }

    public ActionForward removeISTResponsible(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = getRenderedObject();
        if ((protocolFactory.getResponsibles() == null || protocolFactory.getResponsibles().size() == 1)
                && (protocolFactory.getResponsibleFunctions() == null || protocolFactory.getResponsibleFunctions().size() == 0)) {
            setError(request, "errorMessage", new ActionMessage("error.protocol.empty.istResponsibles"));
        } else {
            protocolFactory = prepareRemoveResponsible((DynaActionForm) actionForm, protocolFactory);
            protocolFactory.setIstResponsible(true);
            Protocol protocol = (Protocol) ExecuteFactoryMethod.run(protocolFactory);
            protocolFactory = new ProtocolFactory(protocol);
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("edit-protocol-responsibles");
    }

    public ActionForward removeISTResponsibleFunction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = getRenderedObject();
        if ((protocolFactory.getResponsibleFunctions() == null || protocolFactory.getResponsibleFunctions().size() == 1)
                && (protocolFactory.getResponsibles() == null || protocolFactory.getResponsibles().size() == 0)) {
            setError(request, "errorMessage", new ActionMessage("error.protocol.empty.istResponsibles"));
        } else {
            protocolFactory = prepareRemoveResponsibleFunction((DynaActionForm) actionForm, protocolFactory);
            protocolFactory.setIstResponsible(true);
            Protocol protocol = (Protocol) ExecuteFactoryMethod.run(protocolFactory);
            protocolFactory = new ProtocolFactory(protocol);
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("edit-protocol-responsibles");
    }

    public ActionForward removePartnerResponsible(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = getRenderedObject();
        protocolFactory = prepareRemoveResponsible((DynaActionForm) actionForm, protocolFactory);
        protocolFactory.setIstResponsible(false);
        Protocol protocol = (Protocol) ExecuteFactoryMethod.run(protocolFactory);
        RenderUtils.invalidateViewState();
        request.setAttribute("protocolFactory", new ProtocolFactory(protocol));
        return mapping.findForward("edit-protocol-responsibles");
    }

    private ProtocolFactory prepareRemoveResponsible(DynaActionForm actionForm, ProtocolFactory protocolFactory) {
        Person responsible =
                (Person) RootDomainObject.readDomainObjectByOID(Person.class, getInteger(actionForm, "responsibleID"));
        protocolFactory.setResponsibleToRemove(responsible);
        protocolFactory.setEditProtocolAction(EditProtocolAction.REMOVE_RESPONSIBLE);
        return protocolFactory;
    }

    private ProtocolFactory prepareRemoveResponsibleFunction(DynaActionForm actionForm, ProtocolFactory protocolFactory) {
        Function responsibleFunction =
                (Function) RootDomainObject.readDomainObjectByOID(Function.class, getInteger(actionForm, "responsibleID"));
        protocolFactory.setResponsibleFunctionToRemove(responsibleFunction);
        protocolFactory.setEditProtocolAction(EditProtocolAction.REMOVE_RESPONSIBLE);
        return protocolFactory;
    }

    public ActionForward removeISTUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = getRenderedObject();
        if (protocolFactory.getUnits() == null || protocolFactory.getUnits().size() == 1) {
            setError(request, "errorMessage", new ActionMessage("error.protocol.empty.units"));
        } else {
            protocolFactory = prepareRemoveUnit((DynaActionForm) actionForm, protocolFactory);
            protocolFactory.setInternalUnit(true);
            Protocol protocol = (Protocol) ExecuteFactoryMethod.run(protocolFactory);
            protocolFactory = new ProtocolFactory(protocol);
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("edit-protocol-units");
    }

    public ActionForward removePartnerUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = getRenderedObject();
        if (protocolFactory.getPartnerUnits() == null || protocolFactory.getPartnerUnits().size() == 1) {
            setError(request, "errorMessage", new ActionMessage("error.protocol.empty.partnerUnits"));
        } else {
            protocolFactory = prepareRemoveUnit((DynaActionForm) actionForm, protocolFactory);
            protocolFactory.setInternalUnit(false);
            Protocol protocol = (Protocol) ExecuteFactoryMethod.run(protocolFactory);
            protocolFactory = new ProtocolFactory(protocol);
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("edit-protocol-units");
    }

    private ProtocolFactory prepareRemoveUnit(DynaActionForm actionForm, ProtocolFactory protocolFactory) {
        Unit unit = (Unit) RootDomainObject.readDomainObjectByOID(Unit.class, getInteger(actionForm, "unitID"));
        protocolFactory.setUnitToRemove(unit);
        protocolFactory.setEditProtocolAction(EditProtocolAction.REMOVE_UNIT);
        return protocolFactory;
    }

    public ActionForward prepareEditProtocolFiles(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Integer protocolID = getIntegerFromRequest(request, "protocolID");
        ProtocolFactory protocolFactory = null;
        if (protocolID != null) {
            Protocol protocol = (Protocol) RootDomainObject.readDomainObjectByOID(Protocol.class, protocolID);
            protocolFactory = new ProtocolFactory(protocol);
        } else {
            protocolFactory = getRenderedObject();
        }
        protocolFactory.resetFile();
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("edit-protocol-files");
    }

    public ActionForward addProtocolFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = getRenderedObject();
        if (request.getParameter("back") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("view-protocol");
        }
        if (protocolFactory.getInputStream() != null) {
            Protocol protocol =
                    (Protocol) executeService(
                            "AddFileToProtocol",
                            new Object[] { protocolFactory.getProtocol(), protocolFactory.getInputStream(),
                                    protocolFactory.getFileName(), protocolFactory.getFilePermissionType() });
            protocolFactory = new ProtocolFactory(protocol);
        }
        RenderUtils.invalidateViewState();
        protocolFactory.resetFile();
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("edit-protocol-files");
    }

    public ActionForward deleteProtocolFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Integer fileID = getIntegerFromRequest(request, "fileID");
        ProtocolFile protocolFile = (ProtocolFile) RootDomainObject.readDomainObjectByOID(ProtocolFile.class, fileID);
        ProtocolFactory protocolFactory = new ProtocolFactory(protocolFile.getProtocol());
        protocolFactory.setFileToDelete(protocolFile);
        protocolFactory.setEditProtocolAction(EditProtocolAction.DELETE_FILE);
        Protocol protocol = (Protocol) ExecuteFactoryMethod.run(protocolFactory);
        request.setAttribute("protocolFactory", new ProtocolFactory(protocol));

        return mapping.findForward("edit-protocol-files");
    }

    public ActionForward changeProtocolFilePermission(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Integer fileID = getIntegerFromRequest(request, "fileID");
        ProtocolFile protocolFile = (ProtocolFile) RootDomainObject.readDomainObjectByOID(ProtocolFile.class, fileID);
        protocolFile.changePermissions();
        request.setAttribute("protocolFactory", new ProtocolFactory(protocolFile.getProtocol()));

        return mapping.findForward("edit-protocol-files");
    }

    private void setError(HttpServletRequest request, String error, ActionMessage actionMessage) {
        ActionMessages actionMessages = getMessages(request);
        actionMessages.add(error, actionMessage);
        saveMessages(request, actionMessages);
    }
}