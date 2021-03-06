package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CopyLastYearRealDataService;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;

public class CopyLastYearRealDataToTSDProcessPhase extends FenixService {
    public void run(Integer tsdProcessPhaseId) {
        TSDProcessPhase tsdProcessPhase = rootDomainObject.readTSDProcessPhaseByOID(tsdProcessPhaseId);

        CopyLastYearRealDataService service = CopyLastYearRealDataService.getInstance();

        service.copyLastYearRealDataToTSDProcessPhase(tsdProcessPhase);
    }
}
