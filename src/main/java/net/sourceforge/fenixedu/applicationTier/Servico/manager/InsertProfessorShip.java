package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;

public class InsertProfessorShip extends FenixService {

    public void run(final Integer executionCourseId, final String teacherId, final Boolean responsibleFor, final Double hours)
            throws FenixServiceException {

        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
        if (executionCourse == null) {
            throw new NonExistingServiceException("message.nonExisting.executionCourse", null);
        }

        final Teacher teacher = Teacher.readByIstId(teacherId);
        if (teacher == null) {
            throw new NonExistingServiceException("message.non.existing.teacher", null);
        }
        Professorship.create(responsibleFor, executionCourse, teacher, hours);
    }
}