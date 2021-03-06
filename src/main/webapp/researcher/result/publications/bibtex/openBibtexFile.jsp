<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.publications"/></em>
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex"/></h2>

<logic:messagesPresent name="messages" message="true">
	<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
		<p><span class="error0"><bean:write name="messages"/></span></p>
	</html:messages>
</logic:messagesPresent>

<p class="mtop2 mbottom05"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.openBibtexFile"/>:</p>

<fr:edit id="openFileBean" name="openFileBean" schema="publication.openBibtexFile" action="/publications/bibtexManagement.do?method=prepareBibtexImport">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thmiddle mtop0" />
	</fr:layout>
	<fr:destination name="invalid" path="/publications/bibtexManagement.do?method=prepareOpenBibtexFile"/>
	<fr:destination name="cancel" path="/publications/management.do?method=listPublications"/>
</fr:edit>
