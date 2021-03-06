/*
 * Created on Fev 10, 2004
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.gesdis;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseReport;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteCourseInformation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class TeachingReportAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        CoordinatedDegreeInfo.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    private String getReadService() {
        return "ReadCourseInformation";
    }

    private String getReadHistoricService() {
        return "ReadCourseHistoric";
    }

    private String getEditService() {
        return "EditCourseInformation";
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        InfoCourseReport infoCourseReport = getInfoCourseReportFromForm(form);
        Object[] args = { infoCourseReport.getIdInternal(), infoCourseReport, infoCourseReport.getReport() };
        ServiceUtils.executeService(getEditService(), args);
        return read(mapping, form, request, response);
    }

    protected InfoCourseReport getInfoCourseReportFromForm(ActionForm form) throws FenixActionException {
        try {
            DynaActionForm dynaForm = (DynaActionForm) form;
            InfoCourseReport infoCourseReport = new InfoCourseReport();
            BeanUtils.copyProperties(infoCourseReport, dynaForm);

            Integer executionCourseId = (Integer) dynaForm.get("executionCourseId");
            final ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseId);
            infoCourseReport.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));

            return infoCourseReport;
        } catch (Exception e) {
            throw new FenixActionException(e.getMessage());
        }
    }

    private boolean hasErrors(HttpServletRequest request) {

        return request.getAttribute(Globals.ERROR_KEY) != null;
    }

    protected void populateFormFromInfoCourseReport(ActionMapping mapping, InfoCourseReport infoCourseReport, ActionForm form,
            HttpServletRequest request) {
        try {
            DynaActionForm dynaForm = (DynaActionForm) form;
            BeanUtils.copyProperties(form, infoCourseReport);

            InfoExecutionCourse infoExecutionCourse = infoCourseReport.getInfoExecutionCourse();
            dynaForm.set("executionCourseId", infoExecutionCourse.getIdInternal());
            dynaForm.set("executionPeriodId", infoExecutionCourse.getInfoExecutionPeriod().getIdInternal());
            dynaForm.set("executionYearId", infoExecutionCourse.getInfoExecutionPeriod().getInfoExecutionYear().getIdInternal());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        SiteView siteView = readSiteView(mapping, form, request);
        List infoCoursesHistoric = readCoursesHistoric(mapping, form, request);
        if (!hasErrors(request) && siteView != null) {
            InfoSiteCourseInformation infoSiteCourseInformation = (InfoSiteCourseInformation) siteView.getComponent();
            populateFormFromInfoCourseReport(mapping, infoSiteCourseInformation.getInfoCourseReport(), form, request);
        }
        setSiteViewToRequest(request, siteView, mapping);
        setInfoCoursesHistoric(request, infoCoursesHistoric, mapping);
        return mapping.findForward("show-form");
    }

    public ActionForward read(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        SiteView siteView = readSiteView(mapping, form, request);
        setSiteViewToRequest(request, siteView, mapping);
        List infoCoursesHistoric = readCoursesHistoric(mapping, form, request);
        setInfoCoursesHistoric(request, infoCoursesHistoric, mapping);
        Integer degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }
        return mapping.findForward("successfull-read");
    }

    private void setInfoCoursesHistoric(HttpServletRequest request, List list, ActionMapping mapping) throws Exception {
        if (list != null) {
            request.setAttribute("infoCoursesHistoric", list);
        }
    }

    private List readCoursesHistoric(ActionMapping mapping, ActionForm form, HttpServletRequest request) throws Exception {
        IUserView userView = UserView.getUser();
        String executionCourseId = request.getParameter("executionCourseId");

        Object[] args = { new Integer(executionCourseId) };
        return (List) ServiceUtils.executeService(getReadHistoricService(), args);
    }

    private SiteView readSiteView(ActionMapping mapping, ActionForm form, HttpServletRequest request)
            throws FenixServiceException, FenixFilterException {
        IUserView userView = UserView.getUser();
        String executionCourseId = request.getParameter("executionCourseId");

        Object[] args = { new Integer(executionCourseId) };
        return (SiteView) ServiceUtils.executeService(getReadService(), args);
    }

    private void setSiteViewToRequest(HttpServletRequest request, SiteView siteView, ActionMapping mapping) {
        if (siteView != null) {
            request.setAttribute("siteView", siteView);
        }
    }
}