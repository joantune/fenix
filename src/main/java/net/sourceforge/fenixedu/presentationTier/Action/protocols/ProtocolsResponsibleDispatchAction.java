package net.sourceforge.fenixedu.presentationTier.Action.protocols;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.protocols.Protocol;
import net.sourceforge.fenixedu.domain.protocols.ProtocolHistory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.Months;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentMember", path = "/protocols", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "show-protocols", path = "/departmentMember/protocols/showProtocols.jsp"),
        @Forward(name = "view-protocol", path = "/departmentMember/protocols/viewProtocol.jsp") })
public class ProtocolsResponsibleDispatchAction extends FenixDispatchAction {

    public ActionForward showProtocols(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IUserView userView = UserView.getUser();

        YearMonthDay now = new YearMonthDay();
        List<ProtocolHistory> protocolHistories = new ArrayList<ProtocolHistory>();
        for (Protocol protocol : userView.getPerson().getProtocols()) {
            ProtocolHistory protocolHistory = protocol.getLastProtocolHistory();
            if (protocolHistory.getEndDate() != null && !protocolHistory.getEndDate().isBefore(now)
                    && Months.monthsBetween(now, protocolHistory.getEndDate()).getMonths() <= 1) {
                protocolHistories.add(protocolHistory);
            }
        }

        request.setAttribute("protocolHistories", protocolHistories);
        return mapping.findForward("show-protocols");
    }

    public ActionForward viewProtocol(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IUserView userView = UserView.getUser();
        Integer protocolId = new Integer(request.getParameter("idInternal"));

        Protocol protocol = rootDomainObject.readProtocolByOID(protocolId);
        if (!protocol.getResponsibles().contains(userView.getPerson())) {
            return showProtocols(mapping, actionForm, request, response);
        }

        request.setAttribute("protocol", protocol);
        return mapping.findForward("view-protocol");
    }

}