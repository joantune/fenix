/*
 * Created on Dec 1, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author jpvl
 */
public class ReadDepartmentByTeacher extends FenixService {

    @Service
    public static InfoDepartment run(InfoTeacher infoTeacher) {
        Teacher teacher = rootDomainObject.readTeacherByOID(infoTeacher.getIdInternal());
        Department department = teacher.getCurrentWorkingDepartment();
        return InfoDepartment.newInfoFromDomain(department);
    }
}