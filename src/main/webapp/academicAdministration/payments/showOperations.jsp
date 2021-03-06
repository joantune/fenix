<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.domain.Person"%>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<bean:define id="personId" name="person" property="idInternal" />
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"
	key="label.payments.management" /></h2>

<p class="mtop15 mbottom05"><strong><bean:message
	bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
<fr:view name="person"
	schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
		<fr:property name="rowClasses" value="tdhl1,," />
	</fr:layout>
</fr:view>

<!-- Operations -->
<logic:equal name="permission" value="true">
<ul>
	<li><html:link
		action="<%="/paymentsManagement.do?method=showEvents&amp;personId=" + personId %>">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"
			key="label.payments.currentEvents" />
	</html:link></li>
	<li><html:link
		action="<%="/paymentsManagement.do?method=showReceipts&amp;personId=" + personId%>">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"
			key="label.payments.receipts" />
	</html:link></li>
</ul>
</logic:equal>
<!-- End of Operations -->
