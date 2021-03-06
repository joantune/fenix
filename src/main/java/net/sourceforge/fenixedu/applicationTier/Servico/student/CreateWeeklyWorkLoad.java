package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Attends;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateWeeklyWorkLoad extends FenixService {

    @Checked("RolePredicates.STUDENT_PREDICATE")
    @Service
    public static void run(final Integer attendsID, final Integer contact, final Integer autonomousStudy, final Integer other) {
        final Attends attends = rootDomainObject.readAttendsByOID(attendsID);
        attends.createWeeklyWorkLoad(contact, autonomousStudy, other);
    }

}