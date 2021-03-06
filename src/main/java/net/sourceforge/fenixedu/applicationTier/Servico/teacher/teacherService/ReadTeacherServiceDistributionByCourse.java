package net.sourceforge.fenixedu.applicationTier.Servico.teacher.teacherService;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.teacher.distribution.DistributionTeacherServicesByCourseDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.distribution.DistributionTeacherServicesByCourseDTO.ExecutionCourseDistributionServiceEntryDTO;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;

import org.joda.time.Duration;

/**
 * 
 * @author jpmsit, amak
 */
public class ReadTeacherServiceDistributionByCourse extends FenixService {

    public List run(Integer departmentId, List<Integer> executionPeriodsIDs) throws FenixServiceException {

        Department department = rootDomainObject.readDepartmentByOID(departmentId);

        // List<CompetenceCourse> competenceCourseList =
        // department.getCompetenceCourses();
        List<CompetenceCourse> competenceCourseList = department.getDepartmentUnit().getCompetenceCourses();

        List<ExecutionSemester> executionPeriodList = new ArrayList<ExecutionSemester>();
        for (Integer executionPeriodID : executionPeriodsIDs) {
            executionPeriodList.add(rootDomainObject.readExecutionSemesterByOID(executionPeriodID));
        }

        DistributionTeacherServicesByCourseDTO returnDTO = new DistributionTeacherServicesByCourseDTO();

        Map<Integer, Boolean> executionCoursesMap = new HashMap<Integer, Boolean>();

        for (CompetenceCourse cc : competenceCourseList) {
            for (CurricularCourse curricularCourseEntry : cc.getAssociatedCurricularCourses()) {

                for (ExecutionSemester executionPeriodEntry : executionPeriodList) {

                    Set<String> curricularYearsSet = buildCurricularYearsSet(curricularCourseEntry, executionPeriodEntry);

                    for (ExecutionCourse executionCourseEntry : curricularCourseEntry
                            .getExecutionCoursesByExecutionPeriod(executionPeriodEntry)) {

                        if (executionCoursesMap.containsKey(executionCourseEntry.getIdInternal())) {
                            returnDTO.addDegreeNameToExecutionCourse(executionCourseEntry.getIdInternal(), curricularCourseEntry
                                    .getDegreeCurricularPlan().getDegree().getSigla());
                            returnDTO.addCurricularYearsToExecutionCourse(executionCourseEntry.getIdInternal(),
                                    curricularYearsSet);
                            continue;
                        }

                        // performance enhancement
                        int executionCourseFirstTimeEnrollementStudentNumber =
                                executionCourseEntry.getFirstTimeEnrolmentStudentNumber();
                        int totalStudentsNumber = executionCourseEntry.getTotalEnrolmentStudentNumber();
                        int executionCourseSecondTimeEnrollementStudentNumber =
                                totalStudentsNumber - executionCourseFirstTimeEnrollementStudentNumber;

                        int theoreticalShiftsNumber = executionCourseEntry.getNumberOfShifts(ShiftType.TEORICA);
                        int praticalShiftsNumber = executionCourseEntry.getNumberOfShifts(ShiftType.PRATICA);
                        int theoPratShiftsNumber = executionCourseEntry.getNumberOfShifts(ShiftType.TEORICO_PRATICA);
                        int laboratorialShiftsNumber = executionCourseEntry.getNumberOfShifts(ShiftType.LABORATORIAL);
                        int seminaryShiftsNumber = executionCourseEntry.getNumberOfShifts(ShiftType.SEMINARY);
                        int problemsShiftsNumber = executionCourseEntry.getNumberOfShifts(ShiftType.PROBLEMS);
                        int tutorialOrientationShiftsNumber =
                                executionCourseEntry.getNumberOfShifts(ShiftType.TUTORIAL_ORIENTATION);

                        int fieldWorkShiftsNumber = executionCourseEntry.getNumberOfShifts(ShiftType.FIELD_WORK);
                        int trainingPeriodShiftsNumber = executionCourseEntry.getNumberOfShifts(ShiftType.TRAINING_PERIOD);

                        double theoreticalStudentsNumberPerShift =
                                theoreticalShiftsNumber == 0 ? 0 : (double) totalStudentsNumber / theoreticalShiftsNumber;
                        double praticalStudentsNumberPerShift =
                                praticalShiftsNumber == 0 ? 0 : (double) totalStudentsNumber / praticalShiftsNumber;
                        double theoPratStudentsNumberPerShift =
                                theoPratShiftsNumber == 0 ? 0 : (double) totalStudentsNumber / theoPratShiftsNumber;
                        double laboratorialStudentsNumberPerShift =
                                laboratorialShiftsNumber == 0 ? 0 : (double) totalStudentsNumber / laboratorialShiftsNumber;

                        double seminaryStudentsNumberPerShift =
                                seminaryShiftsNumber == 0 ? 0 : (double) totalStudentsNumber / seminaryShiftsNumber;
                        double problemsStudentsNumberPerShift =
                                problemsShiftsNumber == 0 ? 0 : (double) totalStudentsNumber / problemsShiftsNumber;
                        double tutorialOrientationStudentsNumberPerShift =
                                tutorialOrientationShiftsNumber == 0 ? 0 : (double) totalStudentsNumber
                                        / tutorialOrientationShiftsNumber;
                        double fieldWorkStudentsNumberPerShift =
                                fieldWorkShiftsNumber == 0 ? 0 : (double) totalStudentsNumber / fieldWorkShiftsNumber;
                        double trainingPeriodStudentsNumberPerShift =
                                trainingPeriodShiftsNumber == 0 ? 0 : (double) totalStudentsNumber / trainingPeriodShiftsNumber;

                        String campus =
                                getCampusForCurricularCourseAndExecutionPeriod(curricularCourseEntry, executionPeriodEntry);

                        returnDTO.addExecutionCourse(executionCourseEntry.getIdInternal(), executionCourseEntry.getNome(),
                                campus, curricularCourseEntry.getDegreeCurricularPlan().getDegree().getSigla(),
                                curricularYearsSet, executionCourseEntry.getExecutionPeriod().getSemester(),
                                executionCourseFirstTimeEnrollementStudentNumber,
                                executionCourseSecondTimeEnrollementStudentNumber, executionCourseEntry.getTotalShiftsDuration(),
                                executionCourseEntry.getAllShiftUnitHours(ShiftType.TEORICA).doubleValue(), executionCourseEntry
                                        .getAllShiftUnitHours(ShiftType.PRATICA).doubleValue(), executionCourseEntry
                                        .getAllShiftUnitHours(ShiftType.LABORATORIAL).doubleValue(), executionCourseEntry
                                        .getAllShiftUnitHours(ShiftType.TEORICO_PRATICA).doubleValue(), executionCourseEntry
                                        .getAllShiftUnitHours(ShiftType.SEMINARY).doubleValue(), executionCourseEntry
                                        .getAllShiftUnitHours(ShiftType.PROBLEMS).doubleValue(), executionCourseEntry
                                        .getAllShiftUnitHours(ShiftType.TUTORIAL_ORIENTATION).doubleValue(), executionCourseEntry
                                        .getAllShiftUnitHours(ShiftType.FIELD_WORK).doubleValue(), executionCourseEntry
                                        .getAllShiftUnitHours(ShiftType.TRAINING_PERIOD).doubleValue(),
                                theoreticalStudentsNumberPerShift, praticalStudentsNumberPerShift,
                                laboratorialStudentsNumberPerShift, theoPratStudentsNumberPerShift,
                                seminaryStudentsNumberPerShift, problemsStudentsNumberPerShift,
                                tutorialOrientationStudentsNumberPerShift, fieldWorkStudentsNumberPerShift,
                                trainingPeriodStudentsNumberPerShift);

                        fillExecutionCourseDTOWithTeachers(returnDTO, executionCourseEntry, department);

                        executionCoursesMap.put(executionCourseEntry.getIdInternal(), true);

                    }
                }
            }
        }

        ArrayList<ExecutionCourseDistributionServiceEntryDTO> returnArraylist =
                new ArrayList<ExecutionCourseDistributionServiceEntryDTO>();

        for (ExecutionCourseDistributionServiceEntryDTO teacherDTO : returnDTO.getExecutionCourseMap().values()) {
            returnArraylist.add(teacherDTO);
        }

        Collections.sort(returnArraylist);

        return returnArraylist;
    }

    private Set<String> buildCurricularYearsSet(CurricularCourse curricularCourseEntry, ExecutionSemester executionPeriodEntry) {

        List<CurricularCourseScope> scopesList = curricularCourseEntry.getActiveScopesInExecutionPeriod(executionPeriodEntry);

        if (scopesList.isEmpty()) {
            scopesList = curricularCourseEntry.getActiveScopesIntersectedByExecutionPeriod(executionPeriodEntry);
        }

        Set<String> curricularYearsSet = new LinkedHashSet<String>();
        for (CurricularCourseScope scopeEntry : scopesList) {
            CurricularYear curricularYear =
                    curricularCourseEntry.getCurricularYearByBranchAndSemester(scopeEntry.getBranch(), scopeEntry
                            .getCurricularSemester().getSemester());
            if (curricularYear != null) {
                curricularYearsSet.add(curricularYear.getYear().toString());
            }
        }
        return curricularYearsSet;
    }

    private void fillExecutionCourseDTOWithTeachers(DistributionTeacherServicesByCourseDTO dto, ExecutionCourse executionCourse,
            Department department) {

        for (Professorship professorShipEntry : executionCourse.getProfessorships()) {
            Teacher teacher = professorShipEntry.getTeacher();

            if (teacher == null) {
                continue;
            }

            Integer teacherIdInternal = teacher.getIdInternal();
            String teacherName = teacher.getPerson().getName();

            DecimalFormat df = new DecimalFormat("#.##");
            DecimalFormatSymbols decimalFormatSymbols = df.getDecimalFormatSymbols();
            decimalFormatSymbols.setDecimalSeparator('.');
            df.setDecimalFormatSymbols(decimalFormatSymbols);
            Double teacherRequiredHours = new Double(df.format(teacher.getHoursLecturedOnExecutionCourse(executionCourse)));

            Duration teacherLecturedTime = teacher.getLecturedDurationOnExecutionCourse(executionCourse);

            boolean teacherBelongsToDepartment = false;

            if (teacher.getCurrentWorkingDepartment() == department) {
                teacherBelongsToDepartment = true;
            }

            dto.addTeacherToExecutionCourse(executionCourse.getIdInternal(), teacherIdInternal, teacherName,
                    teacherRequiredHours, teacherLecturedTime, teacherBelongsToDepartment);
        }

    }

    private String getCampusForCurricularCourseAndExecutionPeriod(CurricularCourse curricularCourse,
            ExecutionSemester executionSemester) {
        String campus = "";

        for (ExecutionDegree executionDegreeEntry : curricularCourse.getDegreeCurricularPlan().getExecutionDegrees()) {
            if (executionDegreeEntry.getExecutionYear() == executionSemester.getExecutionYear()) {
                campus = executionDegreeEntry.getCampus().getName();
                break;
            }
        }

        return campus;
    }

}
