<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<h2><bean:message key="title.journalIssue.chooseJournalIssueToMerge" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2> 

<p class="mtop2 mbottom05"><strong><bean:message key="label.journalIssue.merge.selected" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
<logic:notEmpty name="mergeBean" property="selectedObjects">
	<fr:form action="/mergeJournalIssues.do">
		<html:hidden property="method" value="removeFromMergeList"/>
		<fr:edit id="mergeList" name="mergeBean" 
			type="net.sourceforge.fenixedu.dataTransferObject.PageContainerBean">
			<fr:destination name="input" path="/mergeJournalIssues.do?method=prepare"/>
			<fr:layout name="pages">
				<fr:property name="classes" value="tstyle1 tdcenter mvert05"/>
				<fr:property name="columnClasses" value=""/>
				<fr:property name="subSchema" value="journal.issue.merge.list"/>
				<fr:property name="paged" value="false"/>
				<fr:property name="buttonLabel" value="button.researchActivity.remove"/>
				<fr:property name="bundle" value="SCIENTIFIC_COUNCIL_RESOURCES"/>
			</fr:layout>
		</fr:edit>
		<p class="mvert05">
			<html:submit onclick="this.form.method.value='prepareResearchActivityMerge';this.form.submit();"><bean:message key="button.merge" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></html:submit>
		</p>
	</fr:form>
</logic:notEmpty>

<logic:empty name="mergeBean" property="selectedObjects">
	<p class="mtop05">
		<em><bean:message key="label.journalIssue.merge.no.selected" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
	</p>
</logic:empty>


<p class="mtop2 mbottom05"><strong><bean:message key="label.merge.journalIssue.completeList" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>:</strong></p>

<logic:notEmpty name="mergeBean" property="pageContainerBean.objects">
	<fr:form action="/mergeJournalIssues.do?method=addToMergeList">
		<fr:edit id="scientificJournalList" name="mergeBean" property="pageContainerBean"
			type="net.sourceforge.fenixedu.dataTransferObject.PageContainerBean">
			<fr:destination name="input" path="/mergeJournalIssues.do?method=prepare2"/>
			<fr:layout name="pages">
				<fr:property name="classes" value="tstyle1 tdcenter mtop05"/>
				<fr:property name="columnClasses" value=""/>
				<fr:property name="subSchema" value="journal.issue.merge.list"/>
				<fr:property name="buttonLabel" value="button.researchActivity.add"/>
				<fr:property name="paged" value="false"/>
				<fr:property name="bundle" value="SCIENTIFIC_COUNCIL_RESOURCES"/>
			</fr:layout>
		</fr:edit>
		<fr:edit id="mergeListNotVisible" name="mergeBean" visible="false"/>
	</fr:form>
</logic:notEmpty>

<logic:empty name="mergeBean" property="pageContainerBean.objects">
	<p class="mtop05">
		<em><bean:message key="label.journalIssue.merge.all.selected" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
	</p>
</logic:empty>


<fr:form action="/editScientificJournalMergeJournalIssues.do?method=prepare">
	<p class="mtop2" style="border-top: 2px solid #ddd; background: #f5f5f5; padding: 1em 0.5em;">
		<html:submit><bean:message key="button.back" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></html:submit>
	</p>
</fr:form>

