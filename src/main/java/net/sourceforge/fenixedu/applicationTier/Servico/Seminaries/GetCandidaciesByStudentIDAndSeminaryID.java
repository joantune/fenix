/*
 * Created on 5/Ago/2003, 19:44:39
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacy;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 5/Ago/2003, 19:44:39
 * 
 */
public class GetCandidaciesByStudentIDAndSeminaryID extends FenixService {

    public List run(Integer studentID, Integer seminaryID) throws BDException {
        List candidaciesInfo = new LinkedList();

        Registration registration = rootDomainObject.readRegistrationByOID(studentID);
        Seminary seminary = rootDomainObject.readSeminaryByOID(seminaryID);

        List<SeminaryCandidacy> candidacies = SeminaryCandidacy.getByStudentAndSeminary(registration, seminary);

        for (SeminaryCandidacy candidacy : candidacies) {
            InfoCandidacy infoCandidacy = InfoCandidacy.newInfoFromDomain(candidacy);
            infoCandidacy.setSeminaryName(seminary.getName());

            candidaciesInfo.add(infoCandidacy);
        }

        return candidaciesInfo;
    }
}