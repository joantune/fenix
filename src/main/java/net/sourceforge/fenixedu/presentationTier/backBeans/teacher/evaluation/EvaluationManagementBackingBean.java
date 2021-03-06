package net.sourceforge.fenixedu.presentationTier.backBeans.teacher.evaluation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceMultipleException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.GOPSendMessageService;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.WriteMarks;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.WriteMarks.AttendsMark;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.WriteMarks.StudentMark;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.FinalEvaluation;
import net.sourceforge.fenixedu.domain.FinalMark;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.WrittenEvaluationSpaceOccupation;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.util.Season;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.myfaces.component.html.util.MultipartRequestWrapper;
import org.apache.struts.util.MessageResources;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class EvaluationManagementBackingBean extends FenixBackingBean {

    private static final String ENROLMENT_TYPE_FILTER_ALL = "all";

    private static final String ENROLMENT_TYPE_FILTER_NOT_ENROLLED = "not.enrolled";

    protected final ResourceBundle enumerationBundle = getResourceBundle("resources/EnumerationResources");

    protected Integer executionCourseID;

    private HtmlInputHidden executionCourseIdHidden;

    protected Integer evaluationID;

    private HtmlInputHidden evaluationIdHidden;

    protected String evaluationTypeClassname;

    protected Integer day;

    protected Integer month;

    protected Integer year;

    protected Integer beginHour;

    protected Integer beginMinute;

    protected Integer endHour;

    protected Integer endMinute;

    protected Integer enrolmentBeginDay;

    protected Integer enrolmentBeginMonth;

    protected Integer enrolmentBeginYear;

    protected Integer enrolmentBeginHour;

    protected Integer enrolmentBeginMinute;

    protected Integer enrolmentEndDay;

    protected Integer enrolmentEndMonth;

    protected Integer enrolmentEndYear;

    protected Integer enrolmentEndHour;

    protected Integer enrolmentEndMinute;

    protected String description;

    private String season;

    protected List<WrittenEvaluationEnrolment> writtenEvaluationEnrolments;

    protected Evaluation evaluation;

    protected String distributeEnroledStudentsOption;

    private boolean resetPosition = false;

    private String publishMarksMessage;

    private Boolean sendSMS;

    private String originPage;

    private String selectedBegin;

    private String selectedEnd;

    protected Map<Integer, String> marks = new HashMap<Integer, String>();

    protected String[] attendsIDs;

    protected String submitEvaluationDateTextBoxValue;

    protected HtmlInputText submitEvaluationDateTextBox;

    protected List<FinalMark> alreadySubmitedMarks;

    protected List<Attends> notSubmitedMarks;

    protected Integer[] roomsToDelete;

    protected Integer[] roomsToAssociate;

    protected Map<Integer, Boolean> canManageRoomsMap = new HashMap<Integer, Boolean>();

    protected GradeScale gradeScale;

    protected String enrolmentTypeFilter;

    public EvaluationManagementBackingBean() {
        /*
         * HACK: it's necessary set the executionCourseID for struts menu
         */
        getAndHoldIntegerParameter("executionCourseID");

        initializeEnrolmentFilter();
    }

    public Integer getExecutionCourseID() {
        if (this.executionCourseID == null) {
            if (this.executionCourseIdHidden != null && this.executionCourseIdHidden.getValue() != null) {
                this.executionCourseID = Integer.valueOf(this.executionCourseIdHidden.getValue().toString());
            } else {
                String executionCourseIDString = this.getRequestParameter("executionCourseID");
                if (executionCourseIDString != null && executionCourseIDString.length() > 0) {
                    this.executionCourseID = Integer.valueOf(executionCourseIDString);
                }
            }
        }
        return this.executionCourseID;
    }

    public void setExecutionCourseID(Integer executionCourseId) {
        this.executionCourseID = executionCourseId;
    }

    public HtmlInputHidden getExecutionCourseIdHidden() {
        if (this.executionCourseIdHidden == null) {
            Integer executionCourseId = this.getExecutionCourseID();

            this.executionCourseIdHidden = new HtmlInputHidden();
            this.executionCourseIdHidden.setValue(executionCourseId);
            this.executionCourseID = executionCourseId;
        }
        return this.executionCourseIdHidden;
    }

    public void setExecutionCourseIdHidden(HtmlInputHidden executionCourseIdHidden) {
        if (executionCourseIdHidden != null) {
            this.executionCourseID = Integer.valueOf(executionCourseIdHidden.getValue().toString());
            /*
             * HACK: it's necessary set the executionCourseID for struts menu
             */
            setRequestAttribute("executionCourseID", executionCourseIdHidden.getValue());
        }
        this.executionCourseIdHidden = executionCourseIdHidden;
    }

    public Integer getEvaluationID() {
        if (this.evaluationID == null) {
            if (this.evaluationIdHidden != null && this.evaluationIdHidden.getValue() != null
                    && !this.evaluationIdHidden.getValue().equals("")) {
                this.evaluationID = Integer.valueOf(this.evaluationIdHidden.getValue().toString());
            } else if (this.getRequestParameter("evaluationID") != null && !this.getRequestParameter("evaluationID").equals("")) {
                this.evaluationID = Integer.valueOf(this.getRequestParameter("evaluationID"));
            }
        }
        return this.evaluationID;
    }

    public void setEvaluationID(Integer evaluationId) {
        this.evaluationID = evaluationId;
    }

    public HtmlInputHidden getEvaluationIdHidden() {
        if (this.evaluationIdHidden == null) {
            Integer evaluationId = this.getEvaluationID();
            this.evaluationIdHidden = new HtmlInputHidden();
            this.evaluationIdHidden.setValue(evaluationId);
        }

        return this.evaluationIdHidden;
    }

    public void setEvaluationIdHidden(HtmlInputHidden evaluationIdHidden) {
        if (evaluationIdHidden != null && evaluationIdHidden.getValue() != null
                && !evaluationIdHidden.getValue().toString().equals("")) {
            this.evaluationID = Integer.valueOf(evaluationIdHidden.getValue().toString());
        }

        this.evaluationIdHidden = evaluationIdHidden;
    }

    public Integer getDay() throws FenixFilterException, FenixServiceException {
        if (this.day == null && this.getEvaluation() != null) {
            this.day = ((WrittenEvaluation) getEvaluation()).getDay().get(Calendar.DAY_OF_MONTH);
        } else if (this.getRequestParameter("day") != null && !this.getRequestParameter("day").equals("")) {
            this.day = Integer.valueOf(this.getRequestParameter("day"));
        }
        return this.day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() throws FenixFilterException, FenixServiceException {
        if (this.month == null && this.getEvaluation() != null) {
            this.month = ((WrittenEvaluation) getEvaluation()).getDay().get(Calendar.MONTH) + 1;
        } else if (this.getRequestParameter("month") != null && !this.getRequestParameter("month").equals("")) {
            this.month = Integer.valueOf(this.getRequestParameter("month"));
        }
        return this.month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() throws FenixFilterException, FenixServiceException {
        if (this.year == null && this.getEvaluation() != null) {
            this.year = ((WrittenEvaluation) getEvaluation()).getDay().get(Calendar.YEAR);
        } else if (this.getRequestParameter("year") != null && !this.getRequestParameter("year").equals("")) {
            this.year = Integer.valueOf(this.getRequestParameter("year"));
        }
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getBeginHour() throws FenixFilterException, FenixServiceException {
        if (this.beginHour == null && this.getEvaluation() != null) {
            this.beginHour = ((WrittenEvaluation) getEvaluation()).getBeginning().get(Calendar.HOUR_OF_DAY);
        }
        return this.beginHour;
    }

    public void setBeginHour(Integer beginHour) {
        this.beginHour = beginHour;
    }

    public Integer getBeginMinute() throws FenixFilterException, FenixServiceException {
        if (this.beginMinute == null && this.getEvaluation() != null) {
            this.beginMinute = ((WrittenEvaluation) getEvaluation()).getBeginning().get(Calendar.MINUTE);
        }
        return this.beginMinute;
    }

    public void setBeginMinute(Integer beginMinute) {
        this.beginMinute = beginMinute;
    }

    public Integer getEndHour() throws FenixFilterException, FenixServiceException {
        if (this.endHour == null && this.getEvaluation() != null && ((WrittenEvaluation) getEvaluation()).getEnd() != null) {
            this.endHour = ((WrittenEvaluation) getEvaluation()).getEnd().get(Calendar.HOUR_OF_DAY);
        }
        return this.endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public Integer getEndMinute() throws FenixFilterException, FenixServiceException {
        if (this.endMinute == null && this.getEvaluation() != null && ((WrittenEvaluation) getEvaluation()).getEnd() != null) {
            this.endMinute = ((WrittenEvaluation) getEvaluation()).getEnd().get(Calendar.MINUTE);
        }
        return this.endMinute;
    }

    public void setEndMinute(Integer endMinute) {
        this.endMinute = endMinute;
    }

    public String getDescription() throws FenixFilterException, FenixServiceException {
        if (this.description == null && this.getEvaluation() != null) {
            final Evaluation writtenEvaluation = getEvaluation();
            if (writtenEvaluation instanceof WrittenTest) {
                this.description = ((WrittenTest) writtenEvaluation).getDescription();
            }
        } else if (this.getViewState().getAttribute("description") != null
                && !this.getViewState().getAttribute("description").equals("")) {
            this.description = (String) this.getViewState().getAttribute("description");
        }
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.getViewState().setAttribute("description", description);
    }

    public Integer getEnrolmentBeginDay() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentBeginDay == null && ((WrittenEvaluation) this.getEvaluation()).getEnrollmentBeginDay() != null) {
            this.enrolmentBeginDay =
                    ((WrittenEvaluation) this.getEvaluation()).getEnrollmentBeginDay().get(Calendar.DAY_OF_MONTH);
        }
        return this.enrolmentBeginDay;
    }

    public void setEnrolmentBeginDay(Integer enrolmentBeginDay) {
        this.enrolmentBeginDay = enrolmentBeginDay;
    }

    public Integer getEnrolmentBeginHour() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentBeginHour == null && ((WrittenEvaluation) this.getEvaluation()).getEnrollmentBeginTime() != null) {
            this.enrolmentBeginHour =
                    ((WrittenEvaluation) this.getEvaluation()).getEnrollmentBeginTime().get(Calendar.HOUR_OF_DAY);
        }
        return this.enrolmentBeginHour;
    }

    public void setEnrolmentBeginHour(Integer enrolmentBeginHour) {
        this.enrolmentBeginHour = enrolmentBeginHour;
    }

    public Integer getEnrolmentBeginMinute() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentBeginMinute == null && ((WrittenEvaluation) this.getEvaluation()).getEnrollmentBeginTime() != null) {
            this.enrolmentBeginMinute = ((WrittenEvaluation) this.getEvaluation()).getEnrollmentBeginTime().get(Calendar.MINUTE);
        }
        return this.enrolmentBeginMinute;
    }

    public void setEnrolmentBeginMinute(Integer enrolmentBeginMinute) {
        this.enrolmentBeginMinute = enrolmentBeginMinute;
    }

    public Integer getEnrolmentBeginMonth() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentBeginMonth == null && ((WrittenEvaluation) this.getEvaluation()).getEnrollmentBeginDay() != null) {
            this.enrolmentBeginMonth = ((WrittenEvaluation) this.getEvaluation()).getEnrollmentBeginDay().get(Calendar.MONTH) + 1;
        }
        return enrolmentBeginMonth;
    }

    public void setEnrolmentBeginMonth(Integer enrolmentBeginMonth) {
        this.enrolmentBeginMonth = enrolmentBeginMonth;
    }

    public Integer getEnrolmentBeginYear() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentBeginYear == null && ((WrittenEvaluation) this.getEvaluation()).getEnrollmentBeginDay() != null) {
            this.enrolmentBeginYear = ((WrittenEvaluation) this.getEvaluation()).getEnrollmentBeginDay().get(Calendar.YEAR);
        }
        return enrolmentBeginYear;
    }

    public void setEnrolmentBeginYear(Integer enrolmentBeginYear) {
        this.enrolmentBeginYear = enrolmentBeginYear;
    }

    public Integer getEnrolmentEndDay() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentEndDay == null && ((WrittenEvaluation) this.getEvaluation()).getEnrollmentEndDay() != null) {
            this.enrolmentEndDay = ((WrittenEvaluation) this.getEvaluation()).getEnrollmentEndDay().get(Calendar.DAY_OF_MONTH);
        }
        return this.enrolmentEndDay;
    }

    public void setEnrolmentEndDay(Integer enrolmentEndDay) {
        this.enrolmentEndDay = enrolmentEndDay;
    }

    public Integer getEnrolmentEndHour() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentEndHour == null && ((WrittenEvaluation) this.getEvaluation()).getEnrollmentEndTime() != null) {
            this.enrolmentEndHour = ((WrittenEvaluation) this.getEvaluation()).getEnrollmentEndTime().get(Calendar.HOUR_OF_DAY);
        }
        return this.enrolmentEndHour;
    }

    public void setEnrolmentEndHour(Integer enrolmentEndHour) {
        this.enrolmentEndHour = enrolmentEndHour;
    }

    public Integer getEnrolmentEndMinute() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentEndMinute == null && ((WrittenEvaluation) this.getEvaluation()).getEnrollmentEndTime() != null) {
            this.enrolmentEndMinute = ((WrittenEvaluation) this.getEvaluation()).getEnrollmentEndTime().get(Calendar.MINUTE);
        }
        return this.enrolmentEndMinute;
    }

    public void setEnrolmentEndMinute(Integer enrolmentEndMinute) {
        this.enrolmentEndMinute = enrolmentEndMinute;
    }

    public Integer getEnrolmentEndMonth() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentEndMonth == null && ((WrittenEvaluation) this.getEvaluation()).getEnrollmentEndDay() != null) {
            this.enrolmentEndMonth = ((WrittenEvaluation) this.getEvaluation()).getEnrollmentEndDay().get(Calendar.MONTH) + 1;
        }
        return enrolmentEndMonth;
    }

    public void setEnrolmentEndMonth(Integer enrolmentEndMonth) {
        this.enrolmentEndMonth = enrolmentEndMonth;
    }

    public Integer getEnrolmentEndYear() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentEndYear == null && ((WrittenEvaluation) this.getEvaluation()).getEnrollmentEndDay() != null) {
            this.enrolmentEndYear = ((WrittenEvaluation) this.getEvaluation()).getEnrollmentEndDay().get(Calendar.YEAR);
        }
        return enrolmentEndYear;
    }

    public void setEnrolmentEndYear(Integer enrolmentEndYear) {
        this.enrolmentEndYear = enrolmentEndYear;
    }

    public String getDistributeEnroledStudentsOption() {
        return (distributeEnroledStudentsOption == null) ? "true" : this.distributeEnroledStudentsOption;
    }

    public void setDistributeEnroledStudentsOption(String distributeEnroledStudentsOption) {
        this.distributeEnroledStudentsOption = distributeEnroledStudentsOption;
    }

    public Integer getRoomToChangeID() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("roomToChangeID") == null) {
            getViewState().setAttribute("roomToChangeID", getElementKeyFor(getEvaluationRoomsPositions(), 1));
        }
        return (Integer) getViewState().getAttribute("roomToChangeID");
    }

    public void setRoomToChangeID(Integer roomToChangeID) {
        getViewState().setAttribute("roomToChangeID", roomToChangeID);
    }

    public Integer getNewRoomPosition() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("newRoomPosition") == null || this.resetPosition) {
            getViewState().setAttribute("newRoomPosition", 0);
        }
        return (Integer) getViewState().getAttribute("newRoomPosition");
    }

    public void setNewRoomPosition(Integer newRoomPosition) {
        getViewState().setAttribute("newRoomPosition", newRoomPosition);
    }

    public List<Exam> getExamList() throws FenixFilterException, FenixServiceException {
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(getExecutionCourseID());

        List<Exam> examsList = new ArrayList(executionCourse.getAssociatedExams());
        Collections.sort(examsList, new BeanComparator("dayDate"));
        return examsList;
    }

    public List<OnlineTest> getOnlineTestList() throws FenixFilterException, FenixServiceException {
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(getExecutionCourseID());

        List<OnlineTest> onlineTestList = new ArrayList(executionCourse.getAssociatedOnlineTests());
        Collections.sort(onlineTestList, new BeanComparator("distributedTest.beginDateDate"));
        return onlineTestList;
    }

    public List<WrittenTest> getWrittenTestList() throws FenixFilterException, FenixServiceException {
        ExecutionCourse executionCourse = getExecutionCourse();
        Teacher teacher = AccessControl.getPerson().getTeacher();

        List<WrittenTest> writtenTestList = new ArrayList();
        for (WrittenTest writtenTest : executionCourse.getAssociatedWrittenTests()) {
            writtenTestList.add(writtenTest);
            canManageRoomsMap.put(writtenTest.getIdInternal(), writtenTest.canTeacherChooseRoom(executionCourse, teacher));
        }

        Collections.sort(writtenTestList, new BeanComparator("dayDate"));
        return writtenTestList;
    }

    public Evaluation getEvaluation() {
        if (this.evaluation == null) {
            if (this.getEvaluationID() != null) {
                evaluation = rootDomainObject.readEvaluationByOID(getEvaluationID());
            } else { // Should not happen
                return null;
            }
        }
        return this.evaluation;
    }

    private Calendar getEnrolmentBegin() throws FenixFilterException, FenixServiceException {
        Calendar result = Calendar.getInstance();

        result.set(getEnrolmentBeginYear(), getEnrolmentBeginMonth() - 1, getEnrolmentBeginDay(), getEnrolmentBeginHour(),
                getEnrolmentBeginMinute());

        return result;
    }

    private Calendar getEnrolmentEnd() throws FenixFilterException, FenixServiceException {
        Calendar result = Calendar.getInstance();

        result.set(getEnrolmentEndYear(), getEnrolmentEndMonth() - 1, getEnrolmentEndDay(), getEnrolmentEndHour(),
                getEnrolmentEndMinute());

        return result;
    }

    public String editEvaluationEnrolmentPeriod() throws FenixFilterException, FenixServiceException {
        Calendar enrolmentBeginDay = getEnrolmentBegin();
        Calendar enrolmentBeginTime = getEnrolmentBegin();
        Calendar enrolmentEndDay = getEnrolmentEnd();
        Calendar enrolmentEndTime = getEnrolmentEnd();

        final Object[] args =
                { getExecutionCourseID(), getEvaluationID(), enrolmentBeginDay.getTime(), enrolmentEndDay.getTime(),
                        enrolmentBeginTime.getTime(), enrolmentEndTime.getTime() };

        try {
            ServiceUtils.executeService("EditWrittenEvaluationEnrolmentPeriod", args);
        } catch (Exception e) {
            addErrorMessage(e.getMessage());
            String errorMessage = e.getMessage();
            if (e instanceof NotAuthorizedFilterException) {
                errorMessage = "message.error.notAuthorized";
            }
            this.setErrorMessage(errorMessage);
            return "";
        }

        return this.getEvaluation().getClass().getSimpleName();
    }

    public String editMarks() throws FenixFilterException, FenixServiceException {
        if (getEvaluationID() == null) {
            WriteMarks.writeByAttend(getExecutionCourseID(), getEvaluationID(), buildAttendsMark());
            return "";
        }
        try {
            WriteMarks.writeByAttend(getExecutionCourseID(), getEvaluationID(), buildAttendsMark());
        } catch (FenixServiceMultipleException e) {
            for (DomainException domainException : e.getExceptionList()) {
                addErrorMessage(getFormatedMessage("resources/ApplicationResources", domainException.getKey(),
                        domainException.getArgs()));
            }
            return "";
        }
        return getEvaluation().getClass().getSimpleName();
    }

    private List<AttendsMark> buildAttendsMark() {
        final List<AttendsMark> result = new ArrayList<AttendsMark>();
        for (Entry<Integer, String> entry : this.marks.entrySet()) {
            result.add(new AttendsMark(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    public List<WrittenEvaluationEnrolment> getWrittenEvaluationEnrolments() throws FenixFilterException, FenixServiceException {

        if (this.writtenEvaluationEnrolments == null) {
            this.writtenEvaluationEnrolments =
                    new ArrayList(((WrittenEvaluation) getEvaluation()).getWrittenEvaluationEnrolments());
            Collections.sort(this.writtenEvaluationEnrolments, new BeanComparator("student.number"));
        }
        return this.writtenEvaluationEnrolments;
    }

    public Calendar getBegin() throws FenixFilterException, FenixServiceException {
        Calendar result = Calendar.getInstance();

        result.set(this.getYear(), this.getMonth() - 1, this.getDay(), this.getBeginHour(), this.getBeginMinute());

        return result;
    }

    public Calendar getEnd() throws FenixFilterException, FenixServiceException {
        Calendar result = Calendar.getInstance();

        result.set(this.getYear(), this.getMonth() - 1, this.getDay(), this.getEndHour(), this.getEndMinute());

        return result;
    }

    public String createWrittenTest() throws FenixFilterException, FenixServiceException {
        List<String> executionCourseIDs = new ArrayList<String>();
        executionCourseIDs.add(this.getExecutionCourseID().toString());

        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(getExecutionCourseID());

        final List<String> degreeModuleScopesIDs = getDegreeModuleScopeIDs(executionCourse);

        final Season season = (getSeason() != null) ? new Season(getSeason()) : null;
        final Object[] args =
                { this.getExecutionCourseID(), this.getBegin().getTime(), this.getBegin().getTime(), this.getEnd().getTime(),
                        executionCourseIDs, degreeModuleScopesIDs, null, this.getGradeScale(), season, this.getDescription() };
        try {
            ServiceUtils.executeService("CreateWrittenEvaluation", args);

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (e instanceof NotAuthorizedFilterException) {
                errorMessage = "message.error.notAuthorized";
            }
            if (e instanceof IllegalDataAccessException) {
                errorMessage = "message.error.notAuthorized.create.evaluation.during.exan.period";
            }
            this.setErrorMessage(errorMessage);
            return "";
        }

        return WrittenTest.class.getSimpleName();
    }

    public ExecutionCourse getExecutionCourse() {
        return rootDomainObject.readExecutionCourseByOID(getExecutionCourseID());
    }

    public Map<Integer, String> getMarks() throws FenixFilterException, FenixServiceException {
        final Evaluation evaluation = getEvaluation();
        final ExecutionCourse executionCourse = getExecutionCourse();
        if (executionCourse != null) {
            for (final Attends attends : getExecutionCourseAttends()) {
                final Mark mark = attends.getMarkByEvaluation(evaluation);
                if (mark != null && !marks.containsKey(attends.getIdInternal())) {
                    marks.put(attends.getIdInternal(), mark.getMark());
                }
            }
        }
        return marks;
    }

    public void setMarks(Map<Integer, String> marks) {
        this.marks = marks;
    }

    public String loadMarks() throws FenixFilterException, FenixServiceException, FileUploadException {
        final HttpServletRequest httpServletRequest =
                (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        final MultipartRequestWrapper multipartRequestWrapper =
                (MultipartRequestWrapper) httpServletRequest.getAttribute("multipartRequestWrapper");

        final FileItem fileItem = multipartRequestWrapper.getFileItem("theFile");
        InputStream inputStream = null;
        try {
            inputStream = fileItem.getInputStream();
            final Map<Integer, String> marks = loadMarks(inputStream);

            WriteMarks.writeByStudent(getExecutionCourseID(), getEvaluationID(), buildStudentMarks(marks));

            return "success";

        } catch (FenixServiceMultipleException e) {
            for (DomainException domainException : e.getExceptionList()) {
                addErrorMessage(getFormatedMessage("resources/ApplicationResources", domainException.getKey(),
                        domainException.getArgs()));
            }
            return "";
        } catch (IOException e) {
            addErrorMessages(getResourceBundle("resources/ApplicationResources"), e.getMessage());
            return "";
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // Nothing to do ...
                }
            }
        }
    }

    private List<StudentMark> buildStudentMarks(Map<Integer, String> marks) {
        final List<StudentMark> result = new ArrayList<StudentMark>();
        for (final Entry<Integer, String> entry : marks.entrySet()) {
            result.add(new StudentMark(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    private Map<Integer, String> loadMarks(final InputStream inputStream) throws IOException {
        final Map<Integer, String> marks = new HashMap<Integer, String>();

        final InputStreamReader input = new InputStreamReader(inputStream);
        final BufferedReader reader = new BufferedReader(input);

        char[] buffer = new char[4096];
        StringBuilder fileContents = new StringBuilder();
        int i = 0;
        while ((i = reader.read(buffer)) != -1) {
            fileContents.append(buffer, 0, i);
        }

        try {
            final StringTokenizer stringTokenizer = new StringTokenizer(fileContents.toString());
            while (true) {
                String studentNumber = getNextToken(stringTokenizer);
                if (studentNumber == null) {
                    return marks;
                }

                String mark = getNextToken(stringTokenizer);
                if (mark == null) {
                    throw new Exception();
                }
                marks.put(Integer.valueOf(studentNumber), mark);
            }
        } catch (Exception e) {
            throw new IOException("error.file.badFormat");
        }
    }

    private String getNextToken(StringTokenizer stringTokenizer) {
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken().trim();
            if (token.length() > 0) {
                return token;
            }
        }
        return null;
    }

    public String editWrittenTest() throws Exception {
        final List<String> executionCourseIDs = new ArrayList<String>();
        executionCourseIDs.add(this.getExecutionCourseID().toString());

        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(getExecutionCourseID());

        final List<String> degreeModuleScopesIDs = getDegreeModuleScopeIDs(executionCourse);

        final Season season = (getSeason() != null) ? new Season(getSeason()) : null;

        final Object[] args =
                { this.getExecutionCourseID(), this.getBegin().getTime(), this.getBegin().getTime(), this.getEnd().getTime(),
                        executionCourseIDs, degreeModuleScopesIDs, null, this.evaluationID, season, this.getDescription(),
                        this.getGradeScale() };
        try {
            ServiceUtils.executeService("EditWrittenEvaluation", args);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (e instanceof NotAuthorizedFilterException) {
                errorMessage = "message.error.notAuthorized";
            }
            if (e instanceof IllegalDataAccessException) {
                errorMessage = "message.error.notAuthorized.create.evaluation.during.exan.period";
            }
            this.setErrorMessage(errorMessage);
            return "";
        }

        final String originPage = getOriginPage();
        if (originPage != null) {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getApplicationContext());
            stringBuilder
                    .append("/resourceAllocationManager/searchWrittenEvaluationsByDate.do?method=returnToSearchPage&amp;page=0&date=");
            stringBuilder.append(DateFormatUtil.format("yyyy/MM/dd", this.getBegin().getTime()));
            stringBuilder.append("&");
            stringBuilder.append(PresentationConstants.EXECUTION_PERIOD_OID);
            stringBuilder.append("=");
            stringBuilder.append(executionCourse.getExecutionPeriod().getIdInternal());
            if (selectedBegin != null && selectedBegin.length() > 0 && selectedBegin.equals("true")) {
                stringBuilder.append("selectedBegin=");
                stringBuilder.append(DateFormatUtil.format("HH:mm", this.getBegin().getTime()));
            }
            if (selectedEnd != null && selectedEnd.length() > 0 && selectedEnd.equals("true")) {
                stringBuilder.append("selectedEnd=");
                stringBuilder.append(DateFormatUtil.format("HH:mm", this.getEnd().getTime()));
            }
            FacesContext.getCurrentInstance().getExternalContext().redirect(stringBuilder.toString());
            return originPage;
        } else {
            return this.getEvaluation().getClass().getSimpleName();
        }
    }

    private List<String> getDegreeModuleScopeIDs(ExecutionCourse executionCourse) {
        final List<String> degreeModuleScopesIDs = new ArrayList<String>();
        for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            List<DegreeModuleScope> degreeModuleScopes = curricularCourse.getDegreeModuleScopes();
            for (DegreeModuleScope degreeModuleScope : degreeModuleScopes) {
                if (degreeModuleScope.getCurricularSemester().equals(executionCourse.getExecutionPeriod().getSemester())) {
                    degreeModuleScopesIDs.add(degreeModuleScope.getKey());
                }
            }
        }
        return degreeModuleScopesIDs;
    }

    protected String getApplicationContext() {
        final String appContext = PropertiesManager.getProperty("app.context");
        return (appContext != null && appContext.length() > 0) ? "/" + appContext : "";
    }

    public String deleteWrittenTest() throws FenixFilterException, FenixServiceException {
        final Object args[] = { this.getExecutionCourseID(), this.getEvaluationID() };
        try {
            ServiceUtils.executeService("DeleteWrittenEvaluation", args);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (e instanceof NotAuthorizedFilterException) {
                errorMessage = "message.error.notAuthorized";
            }
            this.setErrorMessage(errorMessage);
            return "";
        }
        return WrittenTest.class.getSimpleName();
    }

    public List<AllocatableSpace> getEvaluationRooms() throws FenixFilterException, FenixServiceException {
        final AllocatableSpace[] result = new AllocatableSpace[getEvaluationRoomsPositions().size()];
        for (final Entry<Integer, Integer> entry : getEvaluationRoomsPositions().entrySet()) {
            final AllocatableSpace room = getRoom(entry.getKey());
            result[entry.getValue() - 1] = room;
        }
        return Arrays.asList(result);
    }

    private AllocatableSpace getRoom(final Integer roomID) throws FenixFilterException, FenixServiceException {
        for (final WrittenEvaluationSpaceOccupation roomOccupation : ((WrittenEvaluation) getEvaluation())
                .getWrittenEvaluationSpaceOccupations()) {
            if (roomOccupation.getRoom().getIdInternal().equals(roomID)) {
                return roomOccupation.getRoom();
            }
        }
        return null;
    }

    public Map<Integer, Integer> getEvaluationRoomsPositions() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("evaluationRooms") == null) {
            final Map<Integer, Integer> evaluationRooms = initializeEvaluationRoomsPositions();
            getViewState().setAttribute("evaluationRooms", evaluationRooms);
        }
        return (Map<Integer, Integer>) getViewState().getAttribute("evaluationRooms");
    }

    private Map<Integer, Integer> initializeEvaluationRoomsPositions() throws FenixFilterException, FenixServiceException {
        final Map<Integer, Integer> evaluationRooms = new TreeMap();
        final List<WrittenEvaluationSpaceOccupation> roomOccupations =
                new ArrayList(((WrittenEvaluation) getEvaluation()).getWrittenEvaluationSpaceOccupations());
        Collections.sort(roomOccupations, new ReverseComparator(new BeanComparator("room.examCapacity")));
        int count = 0;
        for (final WrittenEvaluationSpaceOccupation roomOccupation : roomOccupations) {
            evaluationRooms.put(roomOccupation.getRoom().getIdInternal(), Integer.valueOf(++count));
        }
        return evaluationRooms;
    }

    public void changeRoom(ValueChangeEvent valueChangeEvent) {
        this.resetPosition = true;
    }

    public void changePosition(ValueChangeEvent valueChangeEvent) throws FenixFilterException, FenixServiceException {
        final Integer roomToChangeNewPosition = (Integer) valueChangeEvent.getNewValue();
        if (roomToChangeNewPosition != 0) {
            final Integer roomToChangeOldPosition = getEvaluationRoomsPositions().get(getRoomToChangeID());
            final Integer elementKey = getElementKeyFor(getEvaluationRoomsPositions(), roomToChangeNewPosition);
            getEvaluationRoomsPositions().put(elementKey, roomToChangeOldPosition);
            getEvaluationRoomsPositions().put(getRoomToChangeID(), roomToChangeNewPosition);
        }
    }

    private Integer getElementKeyFor(Map<Integer, Integer> evaluationRooms, Integer roomPosition) {
        for (final Entry<Integer, Integer> entry : evaluationRooms.entrySet()) {
            if (entry.getValue().equals(roomPosition)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public List<SelectItem> getNames() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result =
                new ArrayList<SelectItem>(((WrittenEvaluation) getEvaluation()).getWrittenEvaluationSpaceOccupationsCount());
        for (final WrittenEvaluationSpaceOccupation roomOccupation : ((WrittenEvaluation) getEvaluation())
                .getWrittenEvaluationSpaceOccupations()) {
            result.add(new SelectItem(roomOccupation.getRoom().getIdInternal(), (roomOccupation.getRoom()).getIdentification()));
        }
        return result;
    }

    public List<SelectItem> getPositions() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result = new ArrayList(getEvaluationRoomsPositions().size());
        result.add(new SelectItem(0, ""));
        for (final Integer value : getEvaluationRoomsPositions().values()) {
            result.add(new SelectItem(value, value.toString()));
        }
        Collections.sort(result, new BeanComparator("label"));
        this.resetPosition = true;
        return result;
    }

    public String distributeStudentsByRooms() throws FenixFilterException, FenixServiceException {
        try {
            final Boolean distributeOnlyEnroledStudents = Boolean.valueOf(this.getDistributeEnroledStudentsOption());
            final Object[] args =
                    { getExecutionCourseID(), getEvaluationID(), getRoomIDs(), Boolean.FALSE, distributeOnlyEnroledStudents };
            ServiceUtils.executeService("WrittenEvaluationRoomDistribution", args);
            return "enterShowStudentsEnroled";
        } catch (Exception e) {
            setErrorMessage(e.getMessage());
            return "";
        }
    }

    private List<Integer> getRoomIDs() throws FenixFilterException, FenixServiceException {
        final List<AllocatableSpace> rooms = getEvaluationRooms();
        final List<Integer> result = new ArrayList(rooms.size());
        for (final AllocatableSpace room : rooms) {
            result.add(room.getIdInternal());
        }
        return result;
    }

    public String checkIfCanDistributeStudentsByRooms() throws FenixFilterException, FenixServiceException {
        try {
            final Evaluation writtenEvaluation = getEvaluation();
            ((WrittenEvaluation) writtenEvaluation).checkIfCanDistributeStudentsByRooms();
            return "enterDistributeStudentsByRooms";
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
            return "";
        }
    }

    public boolean getExistsADistribution() {
        final Evaluation writtenEvaluation = getEvaluation();
        if (writtenEvaluation != null) {
            return ((WrittenEvaluation) writtenEvaluation).hasAnyWrittenEvaluationEnrolments();
        }
        return false;
    }

    public int getNumberOfAttendingStudents() throws FenixFilterException, FenixServiceException {
        int numberOfAttendingStudents = 0;
        for (final ExecutionCourse executionCourse : getEvaluation().getAssociatedExecutionCourses()) {
            numberOfAttendingStudents += executionCourse.getAttendsCount();
        }
        return numberOfAttendingStudents;
    }

    public FinalEvaluation getFinalEvaluation() {
        for (final Evaluation evaluation : getExecutionCourse().getAssociatedEvaluations()) {
            if (evaluation instanceof FinalEvaluation) {
                return (FinalEvaluation) evaluation;
            }
        }
        return null;
    }

    public List<Attends> getExecutionCourseAttends() {
        final List<Attends> result = new ArrayList<Attends>();
        String filter = getEnrolmentTypeFilter();

        for (final Attends attends : getExecutionCourse().getAttendsSet()) {
            if (!attends.hasEnrolment() || !attends.getEnrolment().isImpossible()) {
                if (filter.equals(ENROLMENT_TYPE_FILTER_ALL)) {
                    result.add(attends);
                } else if (filter.equals(ENROLMENT_TYPE_FILTER_NOT_ENROLLED)) {
                    if (!attends.hasEnrolment()) {
                        result.add(attends);
                    }
                } else if (attends.hasEnrolment()) {
                    if (attends.getEnrolment().getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.valueOf(filter))) {
                        result.add(attends);
                    }
                }
            }
        }
        Collections.sort(result, Attends.COMPARATOR_BY_STUDENT_NUMBER);
        return result;
    }

    public List<Student> getStudentsWithImpossibleEnrolments() {
        final List<Student> result = new ArrayList<Student>();
        for (final Attends attends : getExecutionCourse().getAttendsSet()) {
            if (attends.hasEnrolment() && attends.getEnrolment().isImpossible()) {
                final Student student = attends.getEnrolment().getStudentCurricularPlan().getRegistration().getStudent();
                if (!result.contains(student)) {
                    result.add(student);
                }
            }
        }
        return result;
    }

    public String getPublishMarksMessage() {
        return publishMarksMessage;
    }

    public void setPublishMarksMessage(String publishMarksMessage) {
        this.publishMarksMessage = publishMarksMessage;
    }

    public Boolean getSendSMS() {
        return sendSMS;
    }

    public void setSendSMS(Boolean sendSMS) {
        this.sendSMS = sendSMS;
    }

    public String publishMarks() throws FenixFilterException, FenixServiceException {
        final MessageResources messages = MessageResources.getMessageResources("resources/ApplicationResources");
        final String announcementTitle =
                (getPublishMarksMessage() != null && getPublishMarksMessage().length() > 0) ? messages
                        .getMessage("message.publishment") : null;

        try {
            final Object[] args =
                    { this.getExecutionCourseID(), this.getEvaluationID(), this.getPublishMarksMessage(), this.getSendSMS(),
                            announcementTitle };
            ServiceUtils.executeService("PublishMarks", args);
        } catch (Exception e) {
            this.setErrorMessage(e.getMessage());
            return "";
        }

        return this.getEvaluation().getClass().getSimpleName();
    }

    public String getEvaluationTypeClassname() throws FenixFilterException, FenixServiceException {
        String evaluationTypeClassname = (String) this.getViewState().getAttribute("evaluationTypeClassname");
        if (evaluationTypeClassname == null) {
            evaluationTypeClassname = this.getRequestParameter("evaluationTypeClassname");
            if (evaluationTypeClassname != null) {
                setEvaluationTypeClassname(evaluationTypeClassname);
            }
        }
        if (evaluationTypeClassname == null) {
            final Evaluation evaluation = getEvaluation();
            if (evaluation != null) {
                evaluationTypeClassname = evaluation.getClass().getName();
                setEvaluationTypeClassname(evaluationTypeClassname);
            }
        }
        return evaluationTypeClassname;
    }

    public void setEvaluationTypeClassname(String evaluationTypeClassname) {
        this.getViewState().setAttribute("evaluationTypeClassname", evaluationTypeClassname);
    }

    public String getSeason() throws FenixFilterException, FenixServiceException {
        if (season == null) {
            final Evaluation evaluation = getEvaluation();
            if (evaluation != null && evaluation instanceof Exam) {
                final Exam exam = (Exam) evaluation;
                season = exam.getSeason().toString();
            }
        }
        if (this.getViewState().getAttribute("season") != null && !this.getViewState().getAttribute("season").equals("")) {
            this.season = (String) this.getViewState().getAttribute("season");
        }
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
        this.getViewState().setAttribute("season", season);
    }

    public String getOriginPage() {
        if (originPage == null) {
            originPage = getRequestParameter("originPage");
        }
        return originPage;
    }

    public void setOriginPage(String originPage) {
        this.originPage = originPage;
    }

    public String getSelectedBegin() {
        if (selectedBegin == null) {
            selectedBegin = getRequestParameter("selectedBegin");
        }
        return selectedBegin;
    }

    public void setSelectedBegin(String selectedBegin) {
        this.selectedBegin = selectedBegin;
    }

    public String getSelectedEnd() {
        if (selectedEnd == null) {
            selectedEnd = getRequestParameter("selectedEnd");
        }
        return selectedEnd;
    }

    public void setSelectedEnd(String selectedEnd) {
        this.selectedEnd = selectedEnd;
    }

    // Submit Marks
    public List<FinalMark> getAlreadySubmitedMarks() throws FenixFilterException, FenixServiceException {
        if (this.alreadySubmitedMarks == null) {
            FinalEvaluation evaluation = (FinalEvaluation) getEvaluation();
            ExecutionCourse executionCourse = getExecutionCourse();
            this.alreadySubmitedMarks = evaluation.getAlreadySubmitedMarks(executionCourse);
            Collections.sort(this.alreadySubmitedMarks, new BeanComparator("attend.aluno.number"));
        }
        return this.alreadySubmitedMarks;
    }

    public List<Attends> getNotSubmitedMarks() throws FenixFilterException, FenixServiceException {
        if (this.notSubmitedMarks == null) {
            FinalEvaluation evaluation = (FinalEvaluation) getEvaluation();
            ExecutionCourse executionCourse = getExecutionCourse();
            this.notSubmitedMarks = evaluation.getNotSubmitedMarkAttends(executionCourse);
            Collections.sort(this.notSubmitedMarks, new BeanComparator("aluno.number"));
        }
        return this.notSubmitedMarks;
    }

    public String submitMarks() {
        HttpServletRequest request = getRequest();
        attendsIDs = request.getParameterValues("selectedMarks");
        if (attendsIDs == null || attendsIDs.length == 0) {
            setErrorMessage("error.noStudents.selected");
            return "";
        }
        this.getViewState().setAttribute("attendIDs", attendsIDs);
        return "enterSubmitMarksList2";
    }

    public String getSubmitEvaluationDateTextBoxValue() throws FenixFilterException, FenixServiceException {
        if (submitEvaluationDateTextBoxValue == null) {
            List<Exam> exams = getExamList();
            if (exams != null && !exams.isEmpty()) {
                Exam exam = exams.get(exams.size() - 1);
                submitEvaluationDateTextBoxValue = exam.getDayDateYearMonthDay().toString("dd/MM/yyyy");
            }
        }
        return submitEvaluationDateTextBoxValue;
    }

    public void setSubmitEvaluationDateTextBoxValue(String submitEvaluationDateTextBoxValue) {
        this.submitEvaluationDateTextBoxValue = submitEvaluationDateTextBoxValue;
    }

    public HtmlInputText getSubmitEvaluationDateTextBox() {
        return submitEvaluationDateTextBox;
    }

    public void setSubmitEvaluationDateTextBox(HtmlInputText submitEvaluationDateTextBox) {
        this.submitEvaluationDateTextBox = submitEvaluationDateTextBox;
    }

    public List<SelectItem> getAvailableRooms() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        WrittenTest writtenTest = (WrittenTest) getEvaluation();
        Teacher teacher = AccessControl.getPerson().getTeacher();
        for (AllocatableSpace room : writtenTest.getAvailableRooms()) {
            SelectItem selectItem = new SelectItem(room.getIdInternal(), room.getIdentification());
            selectItem.setDisabled(!writtenTest.canTeacherRemoveRoom(getExecutionCourse().getExecutionPeriod(), teacher, room));
            result.add(selectItem);
        }
        return result;
    }

    public Integer[] getRoomsToAssociate() {
        if (roomsToAssociate == null) {
            List<Integer> roomIds = new ArrayList<Integer>();
            for (AllocatableSpace room : ((WrittenTest) getEvaluation()).getAssociatedRooms()) {
                roomIds.add(room.getIdInternal());
            }
            roomsToAssociate = roomIds.toArray(new Integer[] {});
        }
        return roomsToAssociate;
    }

    public void setRoomsToAssociate(Integer[] roomsToAssociate) {
        this.roomsToAssociate = roomsToAssociate;
    }

    public String editEvaluationRooms() throws FenixFilterException, FenixServiceException {

        ServiceUtils.executeService("TeacherEditWrittenTestRooms", new Object[] { getExecutionCourse(),
                AccessControl.getPerson().getTeacher(), (WrittenTest) getEvaluation(), getRooms(getRoomsToAssociate()) });
        setRoomsToAssociate(null);

        return "";
    }

    private List<AllocatableSpace> getRooms(Integer[] roomsToAssociate) {
        List<AllocatableSpace> rooms = new ArrayList<AllocatableSpace>();
        for (Integer roomId : roomsToAssociate) {
            AllocatableSpace space = (AllocatableSpace) rootDomainObject.readResourceByOID(roomId);
            if (space == null) {
                throw new IllegalArgumentException();
            }
            rooms.add(space);
        }
        return rooms;
    }

    public Map<Integer, Boolean> getCanManageRoomsMap() {
        return canManageRoomsMap;
    }

    public void setCanManageRoomsMap(Map<Integer, Boolean> canManageRoomsMap) {
        this.canManageRoomsMap = canManageRoomsMap;
    }

    public GradeScale getGradeScale() {
        if (gradeScale == null && this.getEvaluation() != null) {
            this.gradeScale = getEvaluation().getGradeScale();
        }
        return this.gradeScale;
    }

    public void setGradeScale(GradeScale gradeScale) {
        this.gradeScale = gradeScale;
    }

    public List<SelectItem> getGradeScaleOptions() {
        List<GradeScale> scales = GradeScale.getPublicGradeScales();
        List<SelectItem> items = new ArrayList<SelectItem>();

        for (GradeScale s : scales) {
            items.add(new SelectItem(s, enumerationBundle.getString(s.getName())));
        }

        return items;
    }

    public String getGradeScaleDescription() {
        GradeScale gradeScale = getGradeScale();
        if (gradeScale != null) {
            return getGradeScale().getPossibleValueDescription(getEvaluation().isFinal());
        }
        return "";
    }

    public void exportStudentsEnroledToExcel() throws FenixFilterException, FenixServiceException {
        exportToExcel();
    }

    private String getFileName(Date date) throws FenixFilterException, FenixServiceException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        return (day + "_" + month + "_" + year + "-" + hour + ":" + minutes);
    }

    public void exportToExcel() throws FenixFilterException, FenixServiceException {
        String filename =
                getResourceBundle("resources/ApplicationResources").getString("title.enrolments") + "-"
                        + getFileName(Calendar.getInstance().getTime());
        try {
            exportToXls(filename.replace(" ", "_"));
        } catch (IOException e) {
            throw new FenixServiceException();
        }
    }

    private void exportToXls(String filename) throws IOException, FenixFilterException, FenixServiceException {
        this.getResponse().setContentType("application/vnd.ms-excel");
        this.getResponse().setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
        ServletOutputStream outputStream = this.getResponse().getOutputStream();

        String spreadSheetName = getResourceBundle("resources/ApplicationResources").getString("title.enrolments");
        List<Object> headers = getStudentsEnroledListHeaders();
        Spreadsheet spreadsheet = new Spreadsheet(spreadSheetName, headers);

        reportInfo(spreadsheet);

        spreadsheet.exportToXLSSheet(outputStream);
        outputStream.flush();
        this.getResponse().flushBuffer();
        FacesContext.getCurrentInstance().responseComplete();
    }

    private List<Object> getStudentsEnroledListHeaders() {
        final List<Object> headers = new ArrayList<Object>();
        ResourceBundle bundle = getResourceBundle("resources/ApplicationResources");
        headers.add(bundle.getString("label.number"));
        headers.add(bundle.getString("label.name"));
        headers.add(bundle.getString("label.room"));
        headers.add(bundle.getString("label.degree.name"));
        return headers;
    }

    private void reportInfo(Spreadsheet spreadsheet) throws FenixFilterException, FenixServiceException {
        fillStudentsEnroled(spreadsheet);
    }

    private void fillStudentsEnroled(Spreadsheet spreadsheet) throws FenixFilterException, FenixServiceException {

        for (WrittenEvaluationEnrolment enrolment : getWrittenEvaluationEnrolments()) {
            final Row newRow = spreadsheet.addRow();
            newRow.setCell(enrolment.getStudent().getNumber().toString());
            newRow.setCell(enrolment.getStudent().getPerson().getName());
            newRow.setCell(enrolment.hasRoom() ? enrolment.getRoom().getIdentification() : "-");
            newRow.setCell(enrolment.getStudent().getDegree().getNameI18N().getContent());
        }
    }

    public boolean getMixedGrades() {
        final Evaluation evaluation = getEvaluation();
        final GradeScale gradeScale = getGradeScale();

        if (gradeScale == null) {
            return false;
        }

        try {
            for (String mark : getMarks().values()) {
                if (!gradeScale.isValid(mark, evaluation.getEvaluationType())) {
                    return true;
                }
            }
        } catch (FenixFilterException e) {
            // nothing
        } catch (FenixServiceException e) {
            // nothing
        }
        return false;
    }

    public List<SelectItem> getEnrolmentTypeFilterOptions() {
        List<SelectItem> options = new ArrayList<SelectItem>();

        options.add(new SelectItem(ENROLMENT_TYPE_FILTER_ALL, enumerationBundle.getString("filter.all")));
        for (EnrolmentEvaluationType type : EnrolmentEvaluationType.values()) {
            options.add(new SelectItem(type.getName(), type.getDescription()));
        }
        options.add(new SelectItem(ENROLMENT_TYPE_FILTER_NOT_ENROLLED, enumerationBundle.getString("filter.not.enrolled")));

        return options;
    }

    public String getEnrolmentTypeFilter() {
        return enrolmentTypeFilter;
    }

    public void setEnrolmentTypeFilter(String filter) {
        enrolmentTypeFilter = filter;
    }

    public String filterByEnrolmentType() {
        marks.clear();
        return null;
    }

    private UIComponent findComponent(UIComponent c, String id) {
        if (id.equals(c.getId())) {
            return c;
        }

        Iterator<UIComponent> childIt = c.getFacetsAndChildren();
        while (childIt.hasNext()) {
            UIComponent found = findComponent(childIt.next(), id);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private void initializeEnrolmentFilter() {
        Map map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        UIComponent filterComponent = findComponent(FacesContext.getCurrentInstance().getViewRoot(), "enrolmentFilter");

        if (filterComponent != null) {
            String clientId = filterComponent.getClientId(FacesContext.getCurrentInstance());
            enrolmentTypeFilter = (String) map.get(clientId);
        } else {
            enrolmentTypeFilter = ENROLMENT_TYPE_FILTER_ALL;
        }
    }

    @Service
    public String sendEmailRequestRoom() {
        GOPSendMessageService.requestRoom((WrittenTest) getEvaluation());
        return null;
    }
}
