<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>
<em><bean:message key="title.scientificCouncil.portalTitle" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
<h2><bean:message key="title.protocols.renew" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>
<span class="error0 mtop0">
	<html:messages id="message" message="true" bundle="SCIENTIFIC_COUNCIL_RESOURCES">
		<bean:write name="message" />
	</html:messages>
</span>
<fr:form action="/protocols.do?method=renewProtocol">

<fr:view name="protocolHistoryFactory" property="protocol.orderedProtocolHistories" schema="show.protocolHistories">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 tdcenter" />
	</fr:layout>
</fr:view> 

<table class="tstyle5">
	<tr>
		<td>
			<bean:message key="label.renewDuring" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>:
		</td>
		<td>
			<fr:edit name="protocolHistoryFactory" id="protocolHistoryFactory" schema="renew.protocolHistoryFactory">
				<fr:layout name="flow">
					<fr:property name="labelExcluded" value="true"/>
					<fr:property name="validatorClasses" value="error0"/>
				</fr:layout>
			</fr:edit>
		</td>
	</tr>
</table>

<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.submit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.back">
		<bean:message key="button.back" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
	</html:cancel>
</p>
</fr:form>