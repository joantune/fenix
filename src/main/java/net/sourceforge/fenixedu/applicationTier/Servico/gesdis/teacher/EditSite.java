/*
 * Created on 13/Mar/2003 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;

/**
 * @author PTRLV
 */
public class EditSite extends FenixService {

    public Boolean run(InfoSite infoSiteOld, final String alternativeSite, final String mail, final String initialStatement,
            final String introduction) throws FenixServiceException {
        final ExecutionCourse executionCourse =
                rootDomainObject.readExecutionCourseByOID(infoSiteOld.getInfoExecutionCourse().getIdInternal());
        final ExecutionCourseSite site = executionCourse.getSite();

        site.edit(initialStatement, introduction, mail, alternativeSite);

        return true;
    }
}