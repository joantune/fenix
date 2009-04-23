/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.gep.inquiries;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.UploadStudentInquiriesCourseResultsBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.UploadStudentInquiriesResultsBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.UploadStudentInquiriesTeachingResultsBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiriesCourseResult;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiriesTeachingResult;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.FileUtils;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(path = "/uploadStudentInquiriesResults", module = "gep")
@Forwards( { @Forward(name = "uploadPage", path = "/gep/inquiries/uploadStudentInquiriesResults.jsp") })
public class UploadStudentInquiriesResults extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("uploadTeachingFileBean", new UploadStudentInquiriesTeachingResultsBean());
	request.setAttribute("uploadCourseFileBean", new UploadStudentInquiriesCourseResultsBean());
	return mapping.findForward("uploadPage");
    }

    public ActionForward submitCourseFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	UploadStudentInquiriesCourseResultsBean resultsBean = (UploadStudentInquiriesCourseResultsBean) getRenderedObject("uploadCourseFileBean");
	RenderUtils.invalidateViewState("uploadCourseFileBean");

	try {
	    final String[] splittedResults = readFile(resultsBean);
	    StudentInquiriesCourseResult.importResults(splittedResults[0], splittedResults[1], resultsBean);
	    addActionMessage(request, "message.StudentInquiriesResult.uploadSucess");
	} catch (IOException e) {
	    addErrorMessage(request, e.getMessage(), e.getMessage());
	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getKey(), e.getArgs());
	}

	return prepare(mapping, actionForm, request, response);
    }

    private String[] readFile(UploadStudentInquiriesResultsBean resultsBean) throws IOException {
	String results = FileUtils.readFile(resultsBean.getFile());
	return results.split("\n", 2);
    }

    public ActionForward submitTeachingFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	UploadStudentInquiriesTeachingResultsBean resultsBean = (UploadStudentInquiriesTeachingResultsBean) getRenderedObject("uploadTeachingFileBean");
	RenderUtils.invalidateViewState("uploadTeachingFileBean");

	try {
	    final String[] splittedResults = readFile(resultsBean);
	    StudentInquiriesTeachingResult.importResults(splittedResults[0], splittedResults[1], resultsBean);
	    addActionMessage(request, "message.StudentInquiriesResult.uploadSucess");
	} catch (IOException e) {
	    addErrorMessage(request, e.getMessage(), e.getMessage());
	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getKey(), e.getArgs());
	}

	return prepare(mapping, actionForm, request, response);
    }

}