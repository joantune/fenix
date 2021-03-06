/*
 * Created on 1/Set/2003, 14:47:35
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacyDetails;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudyChoice;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoClassification;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoModality;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminaryWithEquivalencies;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoTheme;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Seminaries.CaseStudy;
import net.sourceforge.fenixedu.domain.Seminaries.CaseStudyChoice;
import net.sourceforge.fenixedu.domain.Seminaries.Modality;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import net.sourceforge.fenixedu.domain.Seminaries.Theme;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 1/Set/2003, 14:47:35
 * 
 */
public class ReadCandidacies extends FenixService {

    public List run(Integer modalityID, Integer seminaryID, Integer themeID, Integer case1Id, Integer case2Id, Integer case3Id,
            Integer case4Id, Integer case5Id, Integer curricularCourseID, Integer degreeCurricularPlanID, Boolean approved)
            throws BDException {
        // IDs == -1 => not selected
        // approved == nulll => not selected
        //
        // case[1-5]Id => case study ids in the desired order

        Modality modality = modalityID.intValue() == -1 ? null : rootDomainObject.readModalityByOID(modalityID);
        Seminary seminary = seminaryID.intValue() == -1 ? null : rootDomainObject.readSeminaryByOID(seminaryID);
        Theme theme = themeID.intValue() == -1 ? null : rootDomainObject.readThemeByOID(themeID);

        DegreeCurricularPlan degreeCurricularPlan =
                degreeCurricularPlanID.intValue() == -1 ? null : rootDomainObject
                        .readDegreeCurricularPlanByOID(degreeCurricularPlanID);
        CurricularCourse curricularCourse =
                curricularCourseID.intValue() == -1 ? null : (CurricularCourse) rootDomainObject
                        .readDegreeModuleByOID(curricularCourseID);

        CaseStudy caseStudy1 = case1Id.intValue() == -1 ? null : rootDomainObject.readCaseStudyByOID(case1Id);
        CaseStudy caseStudy2 = case2Id.intValue() == -1 ? null : rootDomainObject.readCaseStudyByOID(case2Id);
        CaseStudy caseStudy3 = case3Id.intValue() == -1 ? null : rootDomainObject.readCaseStudyByOID(case3Id);
        CaseStudy caseStudy4 = case4Id.intValue() == -1 ? null : rootDomainObject.readCaseStudyByOID(case4Id);
        CaseStudy caseStudy5 = case5Id.intValue() == -1 ? null : rootDomainObject.readCaseStudyByOID(case5Id);

        List<SeminaryCandidacy> filteredCandidacies = new ArrayList<SeminaryCandidacy>();

        outter: for (SeminaryCandidacy candidacy : SeminaryCandidacy.getAllCandidacies()) {
            if (modality != null && !candidacy.getModality().equals(modality)) {
                continue;
            }

            if (seminary != null && !candidacy.getSeminary().equals(seminary)) {
                continue;
            }

            if (curricularCourse != null && !candidacy.getCurricularCourse().equals(curricularCourse)) {
                continue;
            }

            // TODO: converte Modality into a enumeration
            if (theme != null) {
                if (!candidacy.getTheme().equals(theme) && !(candidacy.getModality().getIdInternal().intValue() == 1)) {
                    continue;
                }
            }

            if (approved != null && !candidacy.getApproved().equals(approved)) {
                continue;
            }

            if (degreeCurricularPlan != null
                    && !degreeCurricularPlan.getCurricularCourses().contains(candidacy.getCurricularCourse())) {
                continue;
            }

            CaseStudy choices[] = { caseStudy1, caseStudy2, caseStudy3, caseStudy4, caseStudy5 };
            for (int i = 0; i < choices.length; i++) {
                CaseStudy caseStudy = choices[i];

                if (caseStudy == null) {
                    continue;
                }

                for (CaseStudyChoice choice : candidacy.getCaseStudyChoices()) {
                    if (choice.getOrder() != null) {
                        if (choice.getOrder() == i && !choice.getCaseStudy().equals(caseStudy)) {
                            continue outter; // the case study in that order
                            // is not what the user
                            // requested
                        }
                    }
                }
            }

            filteredCandidacies.add(candidacy);
        }

        List infoCandidacies = new LinkedList();

        for (SeminaryCandidacy candidacy : filteredCandidacies) {
            Registration registration = candidacy.getStudent();
            StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
            if (studentCurricularPlan != null) {
                List enrollments = studentCurricularPlan.getEnrolments();

                InfoCandidacyDetails candidacyDTO = new InfoCandidacyDetails();
                candidacyDTO.setCurricularCourse(InfoCurricularCourse.newInfoFromDomain(candidacy.getCurricularCourse()));
                candidacyDTO.setIdInternal(candidacy.getIdInternal());
                candidacyDTO.setInfoClassification(getInfoClassification(enrollments));
                candidacyDTO.setModality(InfoModality.newInfoFromDomain(candidacy.getModality()));
                candidacyDTO.setMotivation(candidacy.getMotivation());
                candidacyDTO.setSeminary(InfoSeminaryWithEquivalencies.newInfoFromDomain(candidacy.getSeminary()));
                candidacyDTO.setStudent(InfoStudent.newInfoFromDomain(registration));
                candidacyDTO.setTheme(InfoTheme.newInfoFromDomain(candidacy.getTheme()));
                List<InfoCaseStudyChoice> infos = new ArrayList<InfoCaseStudyChoice>();
                for (Object element2 : candidacy.getCaseStudyChoices()) {
                    CaseStudyChoice element = (CaseStudyChoice) element2;
                    infos.add(InfoCaseStudyChoice.newInfoFromDomain(element));
                }
                candidacyDTO.setCases(infos);
                if (candidacy.getApproved() != null) {
                    candidacyDTO.setApproved(candidacy.getApproved());
                } else {
                    candidacyDTO.setApproved(Boolean.FALSE);
                }
                infoCandidacies.add(candidacyDTO);
            }
        }

        return infoCandidacies;
    }

    /**
     * @param enrolments
     * @param infoClassification
     */
    private InfoClassification getInfoClassification(List<Enrolment> enrolments) {
        InfoClassification infoClassification = new InfoClassification();
        int auxInt = 0;
        float acc = 0;
        for (final Enrolment enrolment : enrolments) {
            final Grade grade = enrolment.getGrade();
            if (grade.isApproved() && grade.isNumeric()) {
                acc += Float.valueOf(grade.getValue()).floatValue();
                auxInt++;
            }
        }
        if (auxInt != 0) {
            String value = new DecimalFormat("#0.0").format(acc / auxInt);
            infoClassification.setAritmeticClassification(value);
        }
        infoClassification.setCompletedCourses(Integer.valueOf(auxInt).toString());
        return infoClassification;
    }

}