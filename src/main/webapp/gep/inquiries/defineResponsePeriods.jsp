<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<html:xhtml/>

<h3>
	<bean:message key="link.inquiries.define.response.period" bundle="INQUIRIES_RESOURCES"/>
</h3>

<br/>

<html:form action="/defineResponsePeriods?method=define">
	<fr:edit id="inquiryResponsePeriod" name="definitionPeriodBean" schema="net.sourceforge.fenixedu.domain.inquiry.SelectInquiryResponsePeriod.edit">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05 mbottom1"/>
			<fr:destination name="postBack" path="/defineResponsePeriods.do?method=prepare"/>
			<fr:destination name="cancel" path="/defineResponsePeriods.do?method=prepare"/>
	   	</fr:layout>	    	
	</fr:edit>
	<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
		<p><span class="success0"><bean:write name="message" /></span></p>
	</html:messages>
	<html:messages id="message" bundle="INQUIRIES_RESOURCES">
		<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
	</html:messages>
	<logic:present name="inquiryDoesntExist">
		<br/>
		<p class="warning0"><bean:message key="message.inquiry.doesntExistForSpecifiedParameters" bundle="INQUIRIES_RESOURCES"/></p>
		<br/>
	</logic:present>
	<logic:notPresent name="inquiryDoesntExist">
		<fr:edit id="inquiryResponsePeriodMessage" name="definitionPeriodBean" schema="net.sourceforge.fenixedu.domain.inquiry.InquiryResponsePeriodMessage.edit">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05 mbottom1"/>
	 			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				<fr:destination name="postBack" path="/defineResponsePeriods.do?method=changeLanguage"/>
				<fr:destination name="cancel" path="/defineResponsePeriods.do?method=prepare"/>
		   	</fr:layout>	    	
		</fr:edit>
		<html:submit><bean:message key="button.submit"/></html:submit>
		<html:cancel><bean:message key="button.cancel"/></html:cancel>
	</logic:notPresent>	
</html:form>