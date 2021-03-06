package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.listings;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCPlanFromChosenMasterDegree extends FenixService {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static List run(Integer idInternal) throws FenixServiceException {
        Degree degree = rootDomainObject.readDegreeByOID(idInternal);

        List<InfoDegreeCurricularPlan> result = new ArrayList<InfoDegreeCurricularPlan>();
        for (DegreeCurricularPlan dcp : degree.getDegreeCurricularPlans()) {
            if (!dcp.isBolonhaDegree()) {
                result.add(InfoDegreeCurricularPlan.newInfoFromDomain(dcp));
            }
        }

        return result;
    }

}