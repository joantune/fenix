<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<em><bean:message key="administrative.office.services" bundle="STUDENT_RESOURCES"/></em>
<h2><bean:message key="documents.requirement" bundle="STUDENT_RESOURCES"/></h2>
<hr/><br/>

<logic:messagesPresent message="true">
	<span class="error"><!-- Error messages go here -->
		<html:messages id="message" message="true" bundle="STUDENT_RESOURCES">
			<bean:write name="message"/>
		</html:messages>
	</span>
</logic:messagesPresent>

<logic:messagesNotPresent>
	<p class="success0"><bean:message key="message.cancel.academicServiceRequest.sucess" bundle="STUDENT_RESOURCES"/></p>
</logic:messagesNotPresent>