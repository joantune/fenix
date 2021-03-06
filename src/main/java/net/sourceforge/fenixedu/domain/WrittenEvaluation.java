package net.sourceforge.fenixedu.domain;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.CurricularCourseScope.DegreeModuleScopeCurricularCourseScope;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.Context.DegreeModuleScopeContext;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.space.WrittenEvaluationSpaceOccupation;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.icalendar.EventBean;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.EvaluationType;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

abstract public class WrittenEvaluation extends WrittenEvaluation_Base {

    public static final Comparator<WrittenEvaluation> COMPARATOR_BY_BEGIN_DATE = new Comparator<WrittenEvaluation>() {

        @Override
        public int compare(WrittenEvaluation o1, WrittenEvaluation o2) {
            final int c1 = o1.getDayDateYearMonthDay().compareTo(o2.getDayDateYearMonthDay());
            if (c1 != 0) {
                return c1;
            }
            final int c2 = o1.getBeginningDateHourMinuteSecond().compareTo(o2.getBeginningDateHourMinuteSecond());
            return c2 == 0 ? AbstractDomainObject.COMPARATOR_BY_ID.compare(o1, o2) : c2;
        }

    };

    public static List<WrittenEvaluation> readWrittenEvaluations() {
        List<WrittenEvaluation> result = new ArrayList<WrittenEvaluation>();

        for (Evaluation evaluation : RootDomainObject.getInstance().getEvaluations()) {
            if (evaluation instanceof Evaluation) {
                result.add((WrittenEvaluation) evaluation);
            }
        }

        return result;
    }

    protected WrittenEvaluation() {
        super();
        this.setVigilantsReport(false);
    }

    public String getName() {
        List<ExecutionCourse> courses = this.getAssociatedExecutionCourses();
        String name = "";
        int i = 0;
        for (ExecutionCourse course : courses) {
            if (i > 0) {
                name = name + ", ";
            }
            name = name + " " + course.getSigla();
            i++;
        }
        return name;
    }

    public String getFullName() {
        List<ExecutionCourse> courses = this.getAssociatedExecutionCourses();
        String fullName = "";
        int i = 0;
        for (ExecutionCourse course : courses) {
            if (i > 0) {
                fullName = fullName + ", ";
            }
            fullName = fullName + " " + course.getNome();
            i++;
        }
        return fullName;
    }

    public Campus getCampus() {
        List<AllocatableSpace> rooms = getAssociatedRooms();
        if (rooms.size() > 0) {
            return rooms.get(0).getSpaceCampus();
        } else {
            return null;
        }
    }

    public ExecutionYear getExecutionYear() {
        return this.getAssociatedExecutionCourses().get(0).getExecutionYear();
    }

    public ExecutionDegree getExecutionDegree() {
        for (ExecutionCourse cource : getAssociatedExecutionCourses()) {
            for (CurricularCourse curricularCource : cource.getAssociatedCurricularCourses()) {
                return curricularCource.getExecutionDegreeFor(getExecutionYear().getAcademicInterval());
            }
        }
        return null;
    }

    public Boolean getIsAfterCurrentDate() {
        DateTime currentDate = new DateTime();
        return currentDate.isBefore(this.getBeginningDateTime());
    }

    public DateTime getBeginningDateTime() {
        HourMinuteSecond begin = this.getBeginningDateHourMinuteSecond();
        YearMonthDay yearMonthDay = this.getDayDateYearMonthDay();
        return new DateTime(yearMonthDay.getYear(), yearMonthDay.getMonthOfYear(), yearMonthDay.getDayOfMonth(), begin.getHour(),
                begin.getMinuteOfHour(), begin.getSecondOfMinute(), 0);

    }

    public DateTime getEndDateTime() {
        HourMinuteSecond end = this.getEndDateHourMinuteSecond();
        YearMonthDay yearMonthDay = this.getDayDateYearMonthDay();
        return new DateTime(yearMonthDay.getYear(), yearMonthDay.getMonthOfYear(), yearMonthDay.getDayOfMonth(), end.getHour(),
                end.getMinuteOfHour(), end.getSecondOfMinute(), 0);

    }

    @Override
    public EvaluationType getEvaluationType() {
        return EvaluationType.EXAM_TYPE;
    }

    public Calendar getBeginning() {
        if (this.getBeginningDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getBeginningDate());
            return result;
        }
        return null;
    }

    public void setBeginning(Calendar calendar) {
        if (calendar != null) {
            this.setBeginningDate(calendar.getTime());
        } else {
            this.setBeginningDate(null);
        }
    }

    public Calendar getDay() {
        if (this.getDayDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getDayDate());
            return result;
        }
        return null;
    }

    public void setDay(Calendar calendar) {
        if (calendar != null) {
            this.setDayDate(calendar.getTime());
        } else {
            this.setDayDate(null);
        }
    }

    public List<AllocatableSpace> getAssociatedRooms() {
        final List<AllocatableSpace> result = new ArrayList<AllocatableSpace>();
        for (final WrittenEvaluationSpaceOccupation roomOccupation : getWrittenEvaluationSpaceOccupations()) {
            result.add(roomOccupation.getRoom());
        }
        return result;
    }

    protected void checkIntervalBetweenEvaluations() {
        final IUserView userView = AccessControl.getUserView();
        if (userView == null || !userView.hasRoleType(RoleType.RESOURCE_ALLOCATION_MANAGER)) {
            checkIntervalBetweenEvaluationsCondition();
        }
    }

    public void checkIntervalBetweenEvaluationsCondition() {
        if (getDayDateYearMonthDay() != null && getBeginningDateHourMinuteSecond() != null) {
            for (final ExecutionCourse executionCourse : getAssociatedExecutionCoursesSet()) {
                for (final Evaluation evaluation : executionCourse.getAssociatedEvaluationsSet()) {
                    if (evaluation != this && evaluation instanceof WrittenEvaluation) {
                        final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) evaluation;
                        if (isIntervalBetweenEvaluationsIsLessThan48Hours(this, writtenEvaluation)
                                && hasMatchingCurricularCourseScopeOrContext(writtenEvaluation)) {
                            throw new DomainException("two.evaluations.cannot.occur.withing.48.hours");
                        }
                    }
                }
            }
        }
    }

    private boolean hasMatchingCurricularCourseScopeOrContext(WrittenEvaluation writtenEvaluation) {
        for (final CurricularCourseScope curricularCourseScope : getAssociatedCurricularCourseScopeSet()) {
            if (writtenEvaluation.getAssociatedCurricularCourseScopeSet().contains(curricularCourseScope)) {
                return true;
            }
        }
        for (final Context context : getAssociatedContextsSet()) {
            if (writtenEvaluation.getAssociatedContextsSet().contains(context)) {
                return true;
            }
        }
        return false;
    }

    private boolean isIntervalBetweenEvaluationsIsLessThan48Hours(final WrittenEvaluation writtenEvaluation1,
            final WrittenEvaluation writtenEvaluation2) {
        if (writtenEvaluation1.getBeginningDateTime().isBefore(writtenEvaluation2.getBeginningDateTime())) {
            return !writtenEvaluation1.getBeginningDateTime().plusHours(48).isBefore(writtenEvaluation2.getBeginningDateTime());
        } else {
            return !writtenEvaluation2.getBeginningDateTime().plusHours(48).isBefore(writtenEvaluation1.getBeginningDateTime());
        }
    }

    public Calendar getEnd() {
        if (this.getEndDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEndDate());
            return result;
        }
        return null;
    }

    public void setEnd(Calendar calendar) {
        if (calendar != null) {
            this.setEndDate(calendar.getTime());
        } else {
            this.setEndDate(null);
        }
    }

    public Calendar getEnrollmentBeginDay() {
        if (this.getEnrollmentBeginDayDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnrollmentBeginDayDate());
            return result;
        }
        return null;
    }

    public void setEnrollmentBeginDay(Calendar calendar) {
        if (calendar != null) {
            this.setEnrollmentBeginDayDate(calendar.getTime());
        } else {
            this.setEnrollmentBeginDayDate(null);
        }
    }

    public Calendar getEnrollmentBeginTime() {
        if (this.getEnrollmentBeginTimeDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnrollmentBeginTimeDate());
            return result;
        }
        return null;
    }

    public void setEnrollmentBeginTime(Calendar calendar) {
        if (calendar != null) {
            this.setEnrollmentBeginTimeDate(calendar.getTime());
        } else {
            this.setEnrollmentBeginTimeDate(null);
        }
    }

    public Calendar getEnrollmentEndDay() {
        if (this.getEnrollmentEndDayDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnrollmentEndDayDate());
            return result;
        }
        return null;
    }

    public void setEnrollmentEndDay(Calendar calendar) {
        if (calendar != null) {
            this.setEnrollmentEndDayDate(calendar.getTime());
        } else {
            this.setEnrollmentEndDayDate(null);
        }
    }

    public Calendar getEnrollmentEndTime() {
        if (this.getEnrollmentEndTimeDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnrollmentEndTimeDate());
            return result;
        }
        return null;
    }

    public void setEnrollmentEndTime(Calendar calendar) {
        if (calendar != null) {
            this.setEnrollmentEndTimeDate(calendar.getTime());
        } else {
            this.setEnrollmentEndTimeDate(null);
        }
    }

    protected void setAttributesAndAssociateRooms(Date day, Date beginning, Date end,
            List<ExecutionCourse> executionCoursesToAssociate, List<DegreeModuleScope> curricularCourseScopesToAssociate,
            List<AllocatableSpace> rooms) {

        if (rooms == null) {
            rooms = new ArrayList<AllocatableSpace>(0);
        }

        checkValidHours(beginning, end);

        // Associate ExecutionCourses and Context/Scopes
        getAssociatedExecutionCourses().addAll(executionCoursesToAssociate);
        for (DegreeModuleScope degreeModuleScope : curricularCourseScopesToAssociate) {
            if (degreeModuleScope instanceof DegreeModuleScopeCurricularCourseScope) {
                addAssociatedCurricularCourseScope(((DegreeModuleScopeCurricularCourseScope) degreeModuleScope)
                        .getCurricularCourseScope());
            } else if (degreeModuleScope instanceof DegreeModuleScopeContext) {
                addAssociatedContexts(((DegreeModuleScopeContext) degreeModuleScope).getContext());
            }
        }

        setDayDate(day);
        setBeginningDate(beginning);
        setEndDate(end);

        // Associate New Rooms
        List<WrittenEvaluationSpaceOccupation> newOccupations = associateNewRooms(rooms);

        // Edit Existent Rooms
        final Set<WrittenEvaluationSpaceOccupation> roomOccupationsToDelete = new HashSet<WrittenEvaluationSpaceOccupation>();
        for (final WrittenEvaluationSpaceOccupation roomOccupation : getWrittenEvaluationSpaceOccupations()) {
            if (!newOccupations.contains(roomOccupation)) {
                final AllocatableSpace room = roomOccupation.getRoom();
                if (!rooms.contains(room)) {
                    roomOccupationsToDelete.add(roomOccupation);
                } else {
                    roomOccupation.edit(this);
                }
            }
        }

        // Delete Rooms
        for (Iterator<WrittenEvaluationSpaceOccupation> iter = roomOccupationsToDelete.iterator(); iter.hasNext();) {
            WrittenEvaluationSpaceOccupation occupation = iter.next();
            occupation.removeWrittenEvaluations(this);
            iter.remove();
            occupation.delete();
        }
    }

    private boolean checkValidHours(Date beginning, Date end) {
        if (beginning.after(end)) {
            throw new DomainException("error.data.exame.inv�lida");
        }
        return true;
    }

    private void deleteAllRoomOccupations() {
        while (hasAnyWrittenEvaluationSpaceOccupations()) {
            WrittenEvaluationSpaceOccupation occupation = getWrittenEvaluationSpaceOccupations().get(0);
            occupation.removeWrittenEvaluations(this);
            occupation.delete();
        }
    }

    public void removeRoomOccupation(AllocatableSpace room) {
        if (hasOccupationForRoom(room)) {
            WrittenEvaluationSpaceOccupation occupation =
                    (WrittenEvaluationSpaceOccupation) room
                            .getFirstOccurrenceOfResourceAllocationByClass(WrittenEvaluationSpaceOccupation.class);
            removeWrittenEvaluationSpaceOccupations(occupation);
        }
    }

    protected List<WrittenEvaluationSpaceOccupation> associateNewRooms(final List<AllocatableSpace> rooms) {

        List<WrittenEvaluationSpaceOccupation> newInsertedOccupations = new ArrayList<WrittenEvaluationSpaceOccupation>();
        for (final AllocatableSpace room : rooms) {
            WrittenEvaluationSpaceOccupation spaceOccupation = associateNewRoom(room);
            if (spaceOccupation != null) {
                newInsertedOccupations.add(spaceOccupation);
            }
        }
        return newInsertedOccupations;
    }

    protected WrittenEvaluationSpaceOccupation associateNewRoom(AllocatableSpace room) {
        if (!hasOccupationForRoom(room)) {

            WrittenEvaluationSpaceOccupation occupation =
                    (WrittenEvaluationSpaceOccupation) room
                            .getFirstOccurrenceOfResourceAllocationByClass(WrittenEvaluationSpaceOccupation.class);

            occupation = occupation == null ? new WrittenEvaluationSpaceOccupation(room) : occupation;
            occupation.edit(this);
            return occupation;
        } else {
            return null;
        }
    }

    private boolean hasOccupationForRoom(AllocatableSpace room) {
        for (final WrittenEvaluationSpaceOccupation roomOccupation : this.getWrittenEvaluationSpaceOccupations()) {
            if (roomOccupation.getRoom() == room) {
                return true;
            }
        }
        return false;
    }

    protected void edit(Date day, Date beginning, Date end, List<ExecutionCourse> executionCoursesToAssociate,
            List<DegreeModuleScope> curricularCourseScopesToAssociate, List<AllocatableSpace> rooms, GradeScale gradeScale) {

        setAttributesAndAssociateRooms(day, beginning, end, executionCoursesToAssociate, curricularCourseScopesToAssociate, rooms);

        if (getGradeScale() != gradeScale) {
            if (gradeScale != null) {
                setGradeScale(gradeScale);
            } else {
                setGradeScale(GradeScale.TYPE20);
            }
        }

        checkIntervalBetweenEvaluations();
    }

    @Override
    public void delete() {
        if (hasAnyWrittenEvaluationEnrolments()) {
            throw new DomainException("error.notAuthorizedWrittenEvaluationDelete.withStudent");
        }
        logRemove();
        deleteAllVigilanciesAssociated();
        deleteAllRoomOccupations();
        getAssociatedCurricularCourseScope().clear();
        getAssociatedContextsSet().clear();
        super.delete();
    }

    private void deleteAllVigilanciesAssociated() {
        for (; !this.getVigilancies().isEmpty(); this.getVigilancies().get(0).delete()) {
            ;
        }
    }

    public void editEnrolmentPeriod(Date enrolmentBeginDay, Date enrolmentEndDay, Date enrolmentBeginTime, Date enrolmentEndTime)
            throws DomainException {

        checkEnrolmentDates(enrolmentBeginDay, enrolmentEndDay, enrolmentBeginTime, enrolmentEndTime);

        this.setEnrollmentBeginDayDate(enrolmentBeginDay);
        this.setEnrollmentEndDayDate(enrolmentEndDay);
        this.setEnrollmentBeginTimeDate(enrolmentBeginTime);
        this.setEnrollmentEndTimeDate(enrolmentEndTime);
        for (ExecutionCourse ec : getAssociatedExecutionCourses()) {
            EvaluationManagementLog.createLog(ec, "resources.MessagingResources",
                    "log.executionCourse.evaluation.generic.edited.enrolment", getPresentationName(), ec.getName(),
                    ec.getDegreePresentationString());
        }
    }

    private void checkEnrolmentDates(final Date enrolmentBeginDay, final Date enrolmentEndDay, final Date enrolmentBeginTime,
            final Date enrolmentEndTime) throws DomainException {

        final DateTime enrolmentBeginDate = createDate(enrolmentBeginDay, enrolmentBeginTime);
        final DateTime enrolmentEndDate = createDate(enrolmentEndDay, enrolmentEndTime);

        if (getEnrollmentBeginDayDate() == null && enrolmentBeginDate.isBeforeNow()) {
            throw new DomainException("error.beginDate.sooner.today");
        }
        if (enrolmentEndDate.isBefore(enrolmentBeginDate)) {
            throw new DomainException("error.endDate.sooner.beginDate");
        }
        if (getBeginningDateTime().isBefore(enrolmentEndDate)) {
            throw new DomainException("error.examDate.sooner.endDate");
        }
    }

    private DateTime createDate(Date dateDay, Date dateTime) {
        final Calendar day = Calendar.getInstance();
        day.setTime(dateDay);

        final Calendar time = Calendar.getInstance();
        time.setTime(dateTime);

        return new DateTime(day.get(Calendar.YEAR), day.get(Calendar.MONTH) + 1, day.get(Calendar.DAY_OF_MONTH),
                time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), 0, 0);
    }

    public void enrolStudent(Registration registration) {
        for (WrittenEvaluationEnrolment writtenEvaluationEnrolment : registration.getWrittenEvaluationEnrolments()) {
            if (writtenEvaluationEnrolment.getWrittenEvaluation() == this) {
                throw new DomainException("error.alreadyEnrolledError");
            }
        }
        new WrittenEvaluationEnrolment(this, registration);
    }

    public void unEnrolStudent(Registration registration) {
        if (!this.validUnEnrollment()) {
            throw new DomainException("error.notAuthorizedUnEnrollment");
        }

        WrittenEvaluationEnrolment writtenEvaluationEnrolmentToDelete = this.getWrittenEvaluationEnrolmentFor(registration);
        if (writtenEvaluationEnrolmentToDelete == null) {
            throw new DomainException("error.studentNotEnroled");
        }

        writtenEvaluationEnrolmentToDelete.delete();
    }

    private boolean validUnEnrollment() {
        if (this.getEnrollmentEndDay() != null && this.getEnrollmentEndTime() != null) {
            DateTime enrolmentEnd = createDate(this.getEnrollmentEndDayDate(), this.getEnrollmentEndTimeDate());
            if (enrolmentEnd.isAfterNow()) {
                return true;
            }
        }
        return false;
    }

    public void distributeStudentsByRooms(List<Registration> studentsToDistribute, List<AllocatableSpace> selectedRooms) {

        this.checkIfCanDistributeStudentsByRooms();
        this.checkRoomsCapacityForStudents(selectedRooms, studentsToDistribute.size());

        for (final AllocatableSpace room : selectedRooms) {
            for (int numberOfStudentsInserted = 0; numberOfStudentsInserted < room.getCapacidadeExame()
                    && !studentsToDistribute.isEmpty(); numberOfStudentsInserted++) {
                final Registration registration = getRandomStudentFromList(studentsToDistribute);
                final WrittenEvaluationEnrolment writtenEvaluationEnrolment = this.getWrittenEvaluationEnrolmentFor(registration);
                if (writtenEvaluationEnrolment == null) {
                    new WrittenEvaluationEnrolment(this, registration, room);
                } else {
                    writtenEvaluationEnrolment.setRoom(room);
                }
            }
            if (studentsToDistribute.isEmpty()) {
                break;
            }
        }

        for (ExecutionCourse ec : getAssociatedExecutionCourses()) {
            EvaluationManagementLog.createLog(ec, "resources.MessagingResources",
                    "log.executionCourse.evaluation.generic.edited.rooms.distributed", getPresentationName(), ec.getName(),
                    ec.getDegreePresentationString());
        }

    }

    public void checkIfCanDistributeStudentsByRooms() {
        if (!this.hasAnyWrittenEvaluationSpaceOccupations()) {
            throw new DomainException("error.no.roms.associated");
        }

        final Date todayDate = Calendar.getInstance().getTime();
        final Date evaluationDateAndTime;
        try {
            evaluationDateAndTime =
                    DateFormatUtil.parse("yyyy/MM/dd HH:mm", DateFormatUtil.format("yyyy/MM/dd ", this.getDayDate())
                            + DateFormatUtil.format("HH:mm", this.getBeginningDate()));
        } catch (ParseException e) {
            // This should never happen, the string where obtained from other
            // dates.
            throw new Error(e);
        }

        DateTime enrolmentEndDate = null;
        // This can happen if the Enrolment OccupationPeriod for Evaluation
        // wasn't defined
        if (this.getEnrollmentEndDayDate() != null && this.getEnrollmentEndTimeDate() != null) {
            enrolmentEndDate = createDate(this.getEnrollmentEndDayDate(), this.getEnrollmentEndTimeDate());
        }
        if (DateFormatUtil.isBefore("yyyy/MM/dd HH:mm", evaluationDateAndTime, todayDate)
                || (enrolmentEndDate != null && enrolmentEndDate.isAfterNow())) {
            throw new DomainException("error.out.of.period.enrollment.period");
        }
    }

    private void checkRoomsCapacityForStudents(final List<AllocatableSpace> selectedRooms, int studentsToDistributeSize) {
        int totalCapacity = 0;
        for (final AllocatableSpace room : selectedRooms) {
            totalCapacity += room.getCapacidadeExame();
        }
        if (studentsToDistributeSize > totalCapacity) {
            throw new DomainException("error.not.enough.room.space");
        }
    }

    private Registration getRandomStudentFromList(List<Registration> studentsToDistribute) {
        final Random randomizer = new Random();
        int pos = randomizer.nextInt(Math.abs(randomizer.nextInt()));
        return studentsToDistribute.remove(pos % studentsToDistribute.size());
    }

    public WrittenEvaluationEnrolment getWrittenEvaluationEnrolmentFor(final Registration registration) {
        for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : registration.getWrittenEvaluationEnrolments()) {
            if (writtenEvaluationEnrolment.getWrittenEvaluation() == this) {
                return writtenEvaluationEnrolment;
            }
        }
        return null;
    }

    public boolean isInEnrolmentPeriod() {
        if (this.getEnrollmentBeginDayDate() == null || this.getEnrollmentBeginTimeDate() == null
                || this.getEnrollmentEndDayDate() == null || this.getEnrollmentEndTimeDate() == null) {
            throw new DomainException("error.enrolmentPeriodNotDefined");
        }
        final DateTime enrolmentBeginDate = createDate(this.getEnrollmentBeginDayDate(), this.getEnrollmentBeginTimeDate());
        final DateTime enrolmentEndDate = createDate(this.getEnrollmentEndDayDate(), this.getEnrollmentEndTimeDate());
        return enrolmentBeginDate.isBeforeNow() && enrolmentEndDate.isAfterNow();
    }

    public boolean getIsInEnrolmentPeriod() {
        try { // Used for sorting purpose
            return isInEnrolmentPeriod();
        } catch (final DomainException e) {
            return false;
        }
    }

    public Integer getCountStudentsEnroledAttendingExecutionCourses() {
        int i = 0;
        for (final ExecutionCourse executionCourse : getAssociatedExecutionCourses()) {
            for (final Attends attends : executionCourse.getAttends()) {
                if (attends.getEnrolment() != null) {
                    i++;
                }
            }
        }
        return i;
    }

    public Integer getCountNumberReservedSeats() {
        int i = 0;
        for (final WrittenEvaluationSpaceOccupation roomOccupation : getWrittenEvaluationSpaceOccupations()) {
            i += (roomOccupation.getRoom()).getCapacidadeExame().intValue();
        }
        return i;
    }

    public Integer getCountVacancies() {
        final int writtenEvaluationEnrolmentsCount = getWrittenEvaluationEnrolmentsCount();
        final int countNumberReservedSeats = getCountNumberReservedSeats().intValue();
        return Integer.valueOf(countNumberReservedSeats - writtenEvaluationEnrolmentsCount);
    }

    public List<DegreeModuleScope> getDegreeModuleScopes() {
        return DegreeModuleScope.getDegreeModuleScopes(this);
    }

    public String getDegreesAsString() {
        Set<Degree> degrees = new HashSet<Degree>();
        for (ExecutionCourse course : this.getAssociatedExecutionCourses()) {
            degrees.addAll(course.getDegreesSortedByDegreeName());
        }
        String degreesAsString = "";
        int i = 0;
        for (Degree degree : degrees) {
            if (i > 0) {
                degreesAsString += ", ";
            }
            degreesAsString += degree.getSigla();
            i++;
        }
        return degreesAsString;
    }

    public List<Vigilancy> getTeachersVigilancies() {
        List<Vigilancy> vigilancies = new ArrayList<Vigilancy>();
        for (Vigilancy vigilancy : this.getVigilancies()) {
            if (vigilancy.isOwnCourseVigilancy()) {
                vigilancies.add(vigilancy);
            }
        }
        return vigilancies;
    }

    public List<Vigilancy> getOthersVigilancies() {
        List<Vigilancy> vigilancies = new ArrayList<Vigilancy>();
        for (Vigilancy vigilancy : this.getVigilancies()) {
            if (vigilancy.isOtherCourseVigilancy()) {
                vigilancies.add(vigilancy);
            }
        }
        return vigilancies;
    }

    public List<Vigilancy> getActiveOtherVigilancies() {
        List<Vigilancy> vigilancies = new ArrayList<Vigilancy>();
        for (Vigilancy vigilancy : this.getVigilancies()) {
            if (vigilancy.isOtherCourseVigilancy() && vigilancy.isActive()) {
                vigilancies.add(vigilancy);
            }
        }
        return vigilancies;
    }

    public List<Vigilancy> getAllActiveVigilancies() {
        List<Vigilancy> vigilancies = new ArrayList<Vigilancy>();
        for (Vigilancy vigilancy : this.getVigilancies()) {
            if (vigilancy.isActive()) {
                vigilancies.add(vigilancy);
            }
        }
        return vigilancies;
    }

    public Set<VigilantGroup> getAssociatedVigilantGroups() {
        Set<VigilantGroup> groups = new HashSet<VigilantGroup>();
        for (ExecutionCourse course : getAssociatedExecutionCourses()) {
            if (course.hasVigilantGroup()) {
                groups.add(course.getVigilantGroup());
            }
        }
        return groups;
    }

    public String getAssociatedRoomsAsString() {
        String rooms = "";
        for (AllocatableSpace room : getAssociatedRooms()) {
            rooms += room.getName() + "\n";
        }
        return rooms;
    }

    public List<Vigilancy> getActiveVigilancies() {
        List<Vigilancy> vigilancies = new ArrayList<Vigilancy>();
        for (Vigilancy vigilancy : this.getVigilancies()) {
            if (vigilancy.isActive()) {
                vigilancies.add(vigilancy);
            }
        }
        return vigilancies;
    }

    public boolean hasScopeFor(final Integer year, final Integer semester, DegreeCurricularPlan degreeCurricularPlan) {
        for (final DegreeModuleScope degreeModuleScope : getDegreeModuleScopes()) {
            if (degreeModuleScope.getCurricularYear().equals(year) && degreeModuleScope.getCurricularSemester().equals(semester)
                    && degreeModuleScope.getCurricularCourse().getDegreeCurricularPlan().equals(degreeCurricularPlan)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasScopeOrContextFor(List<Integer> curricularYears, DegreeCurricularPlan degreeCurricularPlan) {
        if (curricularYears != null && degreeCurricularPlan != null) {
            for (final DegreeModuleScope scope : getDegreeModuleScopes()) {
                if (curricularYears.contains(scope.getCurricularYear())
                        && degreeCurricularPlan.equals(scope.getCurricularCourse().getDegreeCurricularPlan())) {
                    return true;
                }
            }
        }
        return false;
    }

    public DiaSemana getDayOfWeek() {
        return new DiaSemana(DiaSemana.getDiaSemana(getDayDateYearMonthDay()));
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
        HourMinuteSecond beginTime = getBeginningDateHourMinuteSecond();
        HourMinuteSecond endTime = getEndDateHourMinuteSecond();
        return getDayDateYearMonthDay() != null && beginTime != null && endTime != null && endTime.isAfter(beginTime);
    }

    public boolean isExam() {
        return false;
    }

    public boolean contains(final CurricularCourse curricularCourse) {
        for (final DegreeModuleScope each : getDegreeModuleScopes()) {
            if (each.getCurricularCourse() == curricularCourse) {
                return true;
            }
        }

        return false;
    }

    public Set<DegreeModuleScope> getDegreeModuleScopesFor(CurricularCourse curricularCourse) {
        final Set<DegreeModuleScope> result = new HashSet<DegreeModuleScope>();
        for (final DegreeModuleScope each : getDegreeModuleScopes()) {
            if (each.getCurricularCourse() == curricularCourse) {
                result.add(each);
            }
        }

        return result;
    }

    public abstract boolean canBeAssociatedToRoom(AllocatableSpace room);

    public void fillVigilancyReport() {
        if (!getVigilantsReport()) {
            setVigilantsReport(Boolean.TRUE);
        }
    }

    // couldn't find a smarter way to conver ymdhms to DateTiem
    private DateTime convertTimes(YearMonthDay yearMonthDay, HourMinuteSecond hourMinuteSecond) {
        return new DateTime(yearMonthDay.getYear(), yearMonthDay.getMonthOfYear(), yearMonthDay.getDayOfMonth(),
                hourMinuteSecond.getHour(), hourMinuteSecond.getMinuteOfHour(), hourMinuteSecond.getSecondOfMinute(), 0);
    }

    protected List<EventBean> getAllEvents(String description, Registration registration, String scheme, String serverName,
            int serverPort) {
        List<EventBean> result = new ArrayList<EventBean>();
        String url = scheme + "://" + serverName + ((serverPort == 80 || serverPort == 443) ? "" : ":" + serverPort);

        String courseName = "";
        ExecutionCourse executionCourse = null;
        if (this.getAttendingExecutionCoursesFor(registration).size() > 1) {
            Iterator<ExecutionCourse> it = this.getAttendingExecutionCoursesFor(registration).iterator();
            for (executionCourse = it.next(); it.hasNext(); executionCourse = it.next()) {
                if (it.hasNext()) {
                    courseName += executionCourse.getSigla() + "; ";
                } else {
                    courseName += executionCourse.getSigla();
                }
            }
        } else {
            courseName = this.getAttendingExecutionCoursesFor(registration).get(0).getNome();
            executionCourse = this.getAttendingExecutionCoursesFor(registration).get(0);
        }

        if (this.getEnrollmentBeginDayDateYearMonthDay() != null) {
            DateTime enrollmentBegin =
                    convertTimes(this.getEnrollmentBeginDayDateYearMonthDay(), this.getEnrollmentBeginTimeDateHourMinuteSecond());
            DateTime enrollmentEnd =
                    convertTimes(this.getEnrollmentEndDayDateYearMonthDay(), this.getEnrollmentEndTimeDateHourMinuteSecond());

            result.add(new EventBean("Inicio das inscri��es para " + description + " : " + courseName, enrollmentBegin,
                    enrollmentBegin.plusHours(1), false, "Sistema F�nix", url + "/privado", null));

            result.add(new EventBean("Fim das inscri��es para " + description + " : " + courseName, enrollmentEnd.minusHours(1),
                    enrollmentEnd, false, "Sistema F�nix", url + "/privado", null));
        }

        String room = "";

        if (registration.getRoomFor(this) != null) {
            room = registration.getRoomFor(this).getName();
        } else {
            for (WrittenEvaluationSpaceOccupation weSpaceOcupation : this.getWrittenEvaluationSpaceOccupations()) {
                if (!room.isEmpty()) {
                    room += "; ";
                }
                room += weSpaceOcupation.getRoom().getName();
            }
        }

        result.add(new EventBean(description + " : " + courseName, this.getBeginningDateTime(), this.getEndDateTime(), false,
                room, url + executionCourse.getSite().getReversePath(), null));

        return result;
    }

    public Set<Person> getTeachers() {
        Set<Person> persons = new HashSet<Person>();
        for (ExecutionCourse course : getAssociatedExecutionCourses()) {
            for (Professorship professorship : course.getProfessorships()) {
                persons.add(professorship.getPerson());
            }
        }
        return persons;
    }

    public abstract List<EventBean> getAllEvents(Registration registration, String scheme, String serverName, int serverPort);

    public String getAssociatedRoomsAsStringList() {
        StringBuilder builder = new StringBuilder("(");
        Iterator<AllocatableSpace> iterator = getAssociatedRooms().iterator();
        while (iterator.hasNext()) {
            builder.append(iterator.next().getIdentification());
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        builder.append(")");
        return builder.toString();
    }

    public Interval getDurationInterval() {
        return new Interval(getBeginningDateTime(), getEndDateTime());
    }

    @Deprecated
    public java.util.Date getBeginningDate() {
        net.sourceforge.fenixedu.util.HourMinuteSecond hms = getBeginningDateHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setBeginningDate(java.util.Date date) {
        if (date == null) {
            setBeginningDateHourMinuteSecond(null);
        } else {
            setBeginningDateHourMinuteSecond(net.sourceforge.fenixedu.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getDayDate() {
        org.joda.time.YearMonthDay ymd = getDayDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setDayDate(java.util.Date date) {
        if (date == null) {
            setDayDateYearMonthDay(null);
        } else {
            setDayDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEndDate() {
        net.sourceforge.fenixedu.util.HourMinuteSecond hms = getEndDateHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setEndDate(java.util.Date date) {
        if (date == null) {
            setEndDateHourMinuteSecond(null);
        } else {
            setEndDateHourMinuteSecond(net.sourceforge.fenixedu.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEnrollmentBeginDayDate() {
        org.joda.time.YearMonthDay ymd = getEnrollmentBeginDayDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setEnrollmentBeginDayDate(java.util.Date date) {
        if (date == null) {
            setEnrollmentBeginDayDateYearMonthDay(null);
        } else {
            setEnrollmentBeginDayDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEnrollmentBeginTimeDate() {
        net.sourceforge.fenixedu.util.HourMinuteSecond hms = getEnrollmentBeginTimeDateHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setEnrollmentBeginTimeDate(java.util.Date date) {
        if (date == null) {
            setEnrollmentBeginTimeDateHourMinuteSecond(null);
        } else {
            setEnrollmentBeginTimeDateHourMinuteSecond(net.sourceforge.fenixedu.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEnrollmentEndDayDate() {
        org.joda.time.YearMonthDay ymd = getEnrollmentEndDayDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setEnrollmentEndDayDate(java.util.Date date) {
        if (date == null) {
            setEnrollmentEndDayDateYearMonthDay(null);
        } else {
            setEnrollmentEndDayDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEnrollmentEndTimeDate() {
        net.sourceforge.fenixedu.util.HourMinuteSecond hms = getEnrollmentEndTimeDateHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setEnrollmentEndTimeDate(java.util.Date date) {
        if (date == null) {
            setEnrollmentEndTimeDateHourMinuteSecond(null);
        } else {
            setEnrollmentEndTimeDateHourMinuteSecond(net.sourceforge.fenixedu.util.HourMinuteSecond.fromDateFields(date));
        }
    }

}
