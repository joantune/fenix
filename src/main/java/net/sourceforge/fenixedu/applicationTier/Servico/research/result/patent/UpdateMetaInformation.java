package net.sourceforge.fenixedu.applicationTier.Servico.research.result.patent;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.util.researcher.ResearchResultMetaDataManager;
import pt.ist.fenixWebFramework.services.Service;

public class UpdateMetaInformation extends FenixService {

    @Service
    public static void run(ResearchResult result) {
        ResearchResultMetaDataManager.updateMetaDataInStorageFor(result);
    }
}