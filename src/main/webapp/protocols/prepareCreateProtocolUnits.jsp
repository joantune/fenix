<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>
<em><bean:message key="title.scientificCouncil.portalTitle" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
<h2><bean:message key="title.protocols.create" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>


<p class="breadcumbs">
	<span><bean:message key="label.protocol.create.step1" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></span> > 
	<span><bean:message key="label.protocol.create.step2" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></span> > 
	<span class="actual"><bean:message key="label.protocol.create.step3" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></span>
</p>


<!-- IST Units -->
<fr:form action="/createProtocol.do">
<html:hidden bundle="HTMLALT_RESOURCES" name="protocolsForm" property="method" value="removeISTUnit"/>
<html:hidden bundle="HTMLALT_RESOURCES" name="protocolsForm" property="unitID"/>
<fr:edit id="protocolFactory" name="protocolFactory" visible="false"/>

<p class="mbottom05"><strong><bean:message key="label.protocol.internalUnits" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
<logic:notEmpty name="protocolFactory" property="units">
<table class="tstyle1 mtop05">
	<tr>
		<th><bean:message key="label.name" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></th>
		<th></th>				
	</tr>
	<logic:iterate id="unit" name="protocolFactory" property="units" type="net.sourceforge.fenixedu.domain.organizationalStructure.Unit">
	<tr>
		<td><bean:write name="unit" property="name"/></td>
		<td>
			<html:submit onclick="<%= "this.form.unitID.value=" + unit.getIdInternal().toString()%>">
				<bean:message key="button.remove" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
			</html:submit>
		</td>				
	</tr>
	</logic:iterate>
</table>
</logic:notEmpty>

<logic:empty name="protocolFactory" property="units">
	<p><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>.</p>
</logic:empty>



<!-- Partner Units -->
<p class="mbottom05"><strong><bean:message key="label.protocol.externalUnits" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
<logic:notEmpty name="protocolFactory" property="partnerUnits">
<table class="tstyle1 mtop05">
	<tr>
		<th><bean:message key="label.name" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></th>
		<th><bean:message key="label.protocol.country" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></th>
		<th></th>			
	</tr>
	<logic:iterate id="partnerUnit" name="protocolFactory" property="partnerUnits" type="net.sourceforge.fenixedu.domain.organizationalStructure.Unit">
	<tr>
		<td><bean:write name="partnerUnit" property="presentationNameWithParents"/></td>
		<td>
			<logic:notEmpty name="partnerUnit" property="country">
				<bean:write name="partnerUnit" property="country.name"/>
			</logic:notEmpty>
			<logic:empty name="partnerUnit" property="country">
				-
			</logic:empty>
		</td>
		<td>
			<html:submit onclick="<%= "this.form.unitID.value=" + partnerUnit.getIdInternal().toString() + ";this.form.method.value='removePartnerUnit'"%>">
				<bean:message key="button.remove" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
			</html:submit>
		</td>				
	</tr>
	</logic:iterate>
</table>
</logic:notEmpty>

<logic:empty name="protocolFactory" property="partnerUnits">
	<p><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>.</p>
</logic:empty>
</fr:form>

<!-- Add Unit -->
<logic:notPresent name="createExternalUnit">
<fr:form action="/createProtocol.do?method=insertUnit">

<p class="mtop15">
	<span class="error0">
		<html:errors bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
		<html:messages id="message" message="true" bundle="SCIENTIFIC_COUNCIL_RESOURCES">
			<bean:write name="message" />
			<br />
		</html:messages>
	</span>
</p>

<logic:present name="needToCreateUnit">
	<div class="warning0 mvert1" style="padding: 0.25em 0.5em;">
		<strong><bean:message key="label.attention" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>:</strong><br/>
		<bean:message key="message.protocol.createNewUnit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
	</div>
</logic:present>

<fieldset class="fieldset1">
<legend><bean:message key="label.add.unit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></legend>
<logic:equal name="protocolFactory" property="internalUnit" value="true">
<fr:edit id="unit" name="protocolFactory" schema="search.unit">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle9 thlight mvert05 thright"/>
        <fr:property name="columnClasses" value="width10em,,tderror1"/>
	</fr:layout>
	<fr:destination name="changeUnitType" path="/createProtocol.do?method=prepareCreateProtocolUnits"/>
</fr:edit>
</logic:equal>

<logic:equal name="protocolFactory" property="internalUnit" value="false">
<fr:edit id="partnerUnit" name="protocolFactory" schema="search.partnerUnit">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle9 thlight mvert05 thright"/>
        <fr:property name="columnClasses" value="width10em,,tderror1"/>
	</fr:layout>
	<fr:destination name="changeUnitType" path="/createProtocol.do?method=prepareCreateProtocolUnits"/>
</fr:edit>
</logic:equal>
<p class="mtop05">
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.insert" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
	</html:submit>
	<logic:present name="needToCreateUnit">
		<html:submit bundle="HTMLALT_RESOURCES" property="createNew">
			<bean:message key="button.insertNewExternalUnit" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
		</html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="cancel">
			<bean:message key="button.cancel" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
		</html:cancel>	
	</logic:present>
</p>
</fieldset>

<p>
	<logic:notPresent name="needToCreateUnit">
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="back">
			<bean:message key="button.back" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
		</html:cancel>
		<html:cancel bundle="HTMLALT_RESOURCES" property="createProtocol">
			<bean:message key="button.createProtocol" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
		</html:cancel>		
	</logic:notPresent>
</p>
</fr:form>
</logic:notPresent>

<!-- Create External Unit -->
<logic:present name="createExternalUnit">
<fr:form action="/createProtocol.do?method=createExternalUnit">
<fieldset class="fieldset1">
<legend><bean:message key="label.protocol.insertNewExternalUnit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></legend>
<fr:view name="protocolFactory" schema="partnerUnit.creation">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle9 thlight mvert05 thright"/>
        <fr:property name="columnClasses" value="width10em,,tderror1"/>
	</fr:layout>
</fr:view>

<fr:edit id="country" name="protocolFactory" schema="partnerUnit.country">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle9 thlight mvert05 thright"/>
        <fr:property name="columnClasses" value="width10em,,tderror1"/>
	</fr:layout>
</fr:edit>
<fr:edit id="protocolFactory" name="protocolFactory" visible="false"/>
<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.insert" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="cancel">
		<bean:message key="button.cancel" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
	</html:cancel>
</p>
</fieldset>
</fr:form>
</logic:present>