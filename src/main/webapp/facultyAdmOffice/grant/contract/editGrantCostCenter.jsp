<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="label.facultyAdmOffice.portal.name"/></em>
<h2><bean:message key="label.grant.costcenter.edition"/></h2>

<html:messages id="errorMessage" message="true">
	<p class="mtop2"><span class="error0 mtop0">
		<bean:write name="errorMessage" filter="false"/>
	</span></p>
</html:messages>

<logic:present name="grantCostCenter">
	<fr:edit name="grantCostCenter" schema="edit.grantCostCenter" action="/manageGrantCostCenter.do?method=prepareManageGrantCostCenter">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 printborder" />
		</fr:layout>
	</fr:edit>
</logic:present>

<logic:notPresent name="grantCostCenter">
	<fr:create schema="edit.grantCostCenter" action="/manageGrantCostCenter.do?method=prepareManageGrantCostCenter"
	type="net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 printborder" />
		</fr:layout>
	</fr:create>
</logic:notPresent>