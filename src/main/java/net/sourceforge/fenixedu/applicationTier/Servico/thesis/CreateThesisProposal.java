package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreateThesisProposal extends FenixService {

    @Service
    public static Thesis run(DegreeCurricularPlan degreeCurricularPlan, Student student, MultiLanguageString title, String comment)
            throws NotAuthorizedFilterException {
        final Degree degree = degreeCurricularPlan.getDegree();
        final Enrolment enrolment = student.getDissertationEnrolment(degreeCurricularPlan);

        final Thesis thesis = new Thesis(degree, enrolment, title);
        thesis.checkIsScientificCommission();
        thesis.setComment(comment);
        return thesis;
    }

}