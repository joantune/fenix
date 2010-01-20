package net.sourceforge.fenixedu.domain.phd;

import static net.sourceforge.fenixedu.util.StringUtils.EMPTY;
import static net.sourceforge.fenixedu.util.StringUtils.isEmpty;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.util.StringUtils;
import dml.runtime.RelationAdapter;

public class InternalPhdParticipant extends InternalPhdParticipant_Base {

    static {
	InternalPhdParticipantPerson.addListener(new RelationAdapter<InternalPhdParticipant, Person>() {

	    @Override
	    public void beforeAdd(InternalPhdParticipant participant, Person person) {
		if (participant != null && person != null) {
		    for (final PhdParticipant each : participant.getIndividualProcess().getParticipants()) {
			if (each.isInternal() && ((InternalPhdParticipant) each).isFor(person)) {
			    throw new DomainException("phd.InternalPhdParticipant.process.already.has.participant.for.person");
			}
		    }
		}
	    }

	});
    }

    private InternalPhdParticipant() {
	super();
    }

    InternalPhdParticipant(PhdIndividualProgramProcess process, PhdParticipantBean bean) {
	this();
	checkPerson(process, bean.getPerson());
	init(process);
	setPerson(bean.getPerson());
	setTitle(bean.getTitle());

	if (!StringUtils.isEmpty(bean.getInstitution())) {
	    setInstitution(bean.getInstitution());
	}

	if (!StringUtils.isEmpty(bean.getWorkLocation())) {
	    setWorkLocation(bean.getWorkLocation());
	}
    }

    private void checkPerson(PhdIndividualProgramProcess process, final Person person) {
	check(person, "error.InternalGuiding.person.cannot.be.null");
	check(process, "error.InternalGuiding.process.cannot.be.null");

	for (final PhdParticipant participant : process.getParticipantsSet()) {
	    if (participant.isFor(person)) {
		throw new DomainException("error.InternalGuiding.person.already.is.participant");
	    }
	}
    }

    @Override
    public String getName() {
	return getPerson().getName();
    }

    @Override
    public String getQualification() {
	final Qualification qualification = getPerson().getLastQualification();
	return qualification != null ? qualification.getType().getLocalizedName() : EMPTY;
    }

    @Override
    public String getCategory() {
	final String category = getPerson().getEmployee().getCurrentEmployeeProfessionalCategoryName();
	return !isEmpty(category) ? category : (hasTeacher() ? getTeacher().getCategory().getName().getContent() : EMPTY);
    }

    private Teacher getTeacher() {
	return getPerson().getTeacher();
    }

    private boolean hasTeacher() {
	return getPerson().hasTeacher();
    }

    @Override
    public String getWorkLocation() {
	if (super.getWorkLocation() != null) {
	    return super.getWorkLocation();

	} else if (getPerson().hasEmployee()) {
	    final Unit workingPlace = getPerson().getEmployee().getCurrentWorkingPlace();
	    if (workingPlace != null) {
		workingPlace.getName();
	    }
	}
	return null;
    }

    @Override
    public String getInstitution() {
	return super.getInstitution() != null ? super.getInstitution() : getRootDomainObject().getInstitutionUnit().getName();
    }

    @Override
    public String getAddress() {
	final PhysicalAddress address = getPerson().getDefaultPhysicalAddress();
	return address != null ? writeAddress(address) : EMPTY;
    }

    private String writeAddress(final PhysicalAddress address) {
	final StringBuilder buffer = new StringBuilder();

	buffer.append(isEmpty(address.getAddress()) ? EMPTY : address.getAddress());

	if (!isEmpty(address.getAreaCode())) {
	    buffer.append(", ").append(address.getAreaCode());
	}

	if (!isEmpty(address.getAreaOfAreaCode())) {
	    buffer.append(", ").append(address.getAreaOfAreaCode());
	}

	return buffer.toString();
    }

    @Override
    public String getEmail() {
	return getPerson().getInstitutionalOrDefaultEmailAddressValue();
    }

    @Override
    public String getPhone() {
	final String phone = getPerson().getDefaultPhoneNumber();
	return !isEmpty(phone) ? phone : getPerson().getDefaultMobilePhoneNumber();
    }

    @Override
    protected void disconnect() {
	removePerson();
	super.disconnect();
    }

    @Override
    public boolean isFor(Person person) {
	return getPerson() == person;
    }

    @Override
    public boolean isInternal() {
	return true;
    }

}