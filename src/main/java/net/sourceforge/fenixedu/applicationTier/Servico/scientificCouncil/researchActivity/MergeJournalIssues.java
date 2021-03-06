package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.researchActivity;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.MergeJournalIssuePageContainerBean;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssueParticipation;
import pt.ist.fenixframework.DomainObject;

public class MergeJournalIssues extends FenixService {

    public void run(MergeJournalIssuePageContainerBean mergeJournalIssuePageContainerBean) {
        JournalIssue journalIssue = new JournalIssue(mergeJournalIssuePageContainerBean.getScientificJournal());
        journalIssue.setVolume(mergeJournalIssuePageContainerBean.getVolume());
        journalIssue.setYear(mergeJournalIssuePageContainerBean.getYear());
        journalIssue.setNumber(mergeJournalIssuePageContainerBean.getNumber());
        journalIssue.setMonth(mergeJournalIssuePageContainerBean.getMonth());
        journalIssue.setUrl(mergeJournalIssuePageContainerBean.getUrl());
        journalIssue.setSpecialIssue(mergeJournalIssuePageContainerBean.getSpecialIssue());
        journalIssue.setSpecialIssueComment(mergeJournalIssuePageContainerBean.getSpecialIssueComment());

        for (DomainObject domainObject : mergeJournalIssuePageContainerBean.getSelectedObjects()) {
            JournalIssue issue = (JournalIssue) domainObject;
            journalIssue.getArticleAssociations().addAll(issue.getArticleAssociations());

            for (JournalIssueParticipation journalIssueParticipation : issue.getParticipationsSet()) {
                journalIssue.addUniqueParticipation(journalIssueParticipation);
            }

            issue.delete();
        }
    }

}
