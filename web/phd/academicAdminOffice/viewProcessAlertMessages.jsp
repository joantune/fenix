<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
<bean:define id="processId" name="process" property="externalId" />


<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.alertMessages" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + processId.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="PhdIndividualProgramProcess.view" name="process">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>
<%--  ### End Of Context Information  ### --%>

<br/><br/>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>
<strong><bean:message  key="label.phd.messages" bundle="PHD_RESOURCES"/></strong><br/>
<logic:notEmpty name="alertMessages">
	<fr:view schema="PhdAlertMessage.view.for.process" name="alertMessages">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
				<fr:property name="linkFormat(markTaskAsPerformed)" value="/phdIndividualProgramProcess.do?method=markAlertMessageTaskAsPerformed&global=false&processId=${process.externalId}&alertMessageId=${externalId}"/>
				<fr:property name="key(markTaskAsPerformed)" value="label.mark.task.as.performed"/>
				<fr:property name="bundle(markTaskAsPerformed)" value="PHD_RESOURCES"/>
				<fr:property name="order(markTaskAsPerformed)" value="0"/>
				<fr:property name="visibleIfNot(markTaskAsPerformed)" value="taskPerformed"/>
				<fr:property name="confirmationKey(markTaskAsPerformed)" value="message.confirm.alertMessage.mark.task.as.performed" />
				<fr:property name="confirmationBundle(markTaskAsPerformed)" value="PHD_RESOURCES" />

		</fr:layout>
	</fr:view>	
</logic:notEmpty>
<logic:empty name="alertMessages">
	<bean:message  key="label.phd.noAlertMessages" bundle="PHD_RESOURCES"/>
</logic:empty>


<%--  ### End of Operation Area  ### --%>



<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>


</logic:present>