package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDRealTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;

/**
 * 
 * @author jpmsit, amak
 */
public class AddTeacherToTeacherServiceDistribution extends FenixService {

    public void run(Integer tsdId, final Integer teacherId) throws FenixServiceException {

        TeacherServiceDistribution rootTSD = rootDomainObject.readTeacherServiceDistributionByOID(tsdId).getRootTSD();
        Teacher teacher = rootDomainObject.readTeacherByOID(teacherId);

        if (rootTSD.getTSDTeacherByTeacher(teacher) == null) {
            rootTSD.addTSDTeachers(new TSDRealTeacher(teacher));
        }
    }
}
