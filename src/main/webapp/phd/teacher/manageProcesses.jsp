<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:equal name="isTeacher" value="true">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.teacher.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.manageProcesses" bundle="PHD_RESOURCES" /></h2>

<%-- ### End of Title ### --%>



<%--  ###  Return Links  ### --%>

<%--  ### Return Links  ### --%>




<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>



<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<ul>
	<li><jsp:include page="/phd/alertMessagesNotifier.jsp?global=true" /></li>

<%--  ### End Of Context Information  ### --%>



<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

	<li><html:link action="/phdIndividualProgramProcess.do?method=viewInactiveProcesses">
		<bean:message bundle="PHD_RESOURCES" key="label.viewInactiveProcesses"/>
	</html:link></li>
</ul>

<jsp:include page="/phd/common/manageProcesses.jsp"/>

</logic:equal>
