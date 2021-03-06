package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.researchActivity;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.MergeEventEditionPageContainerBean;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.EventEditionParticipation;
import pt.ist.fenixframework.DomainObject;

public class MergeEventEditions extends FenixService {

    public void run(MergeEventEditionPageContainerBean mergeEventEditionPageContainerBean) {
        EventEdition eventEdition = new EventEdition(mergeEventEditionPageContainerBean.getEvent());
        eventEdition.setEdition(mergeEventEditionPageContainerBean.getEdition());
        eventEdition.setEventLocation(mergeEventEditionPageContainerBean.getEventLocation());
        eventEdition.setStartDate(mergeEventEditionPageContainerBean.getStartDate());
        eventEdition.setEndDate(mergeEventEditionPageContainerBean.getEndDate());
        eventEdition.setUrl(mergeEventEditionPageContainerBean.getUrl());
        eventEdition.setOrganization(mergeEventEditionPageContainerBean.getOrganization());

        for (DomainObject domainObject : mergeEventEditionPageContainerBean.getSelectedObjects()) {
            EventEdition edition = (EventEdition) domainObject;
            eventEdition.getEventConferenceArticlesAssociations().addAll(edition.getEventConferenceArticlesAssociations());
            eventEdition.getAssociatedProjects().addAll(edition.getAssociatedProjects());

            for (EventEditionParticipation eventEditionParticipation : edition.getParticipationsSet()) {
                eventEdition.addUniqueParticipation(eventEditionParticipation);
            }

            edition.delete();
        }
    }

}
