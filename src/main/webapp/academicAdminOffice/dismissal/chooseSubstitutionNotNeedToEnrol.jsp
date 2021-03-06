<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ page import="org.apache.struts.action.ActionMessages" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="link.student.create.substitution" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" property="<%= ActionMessages.GLOBAL_MESSAGE %>" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<p class="mtop2">
<span class="showpersonid">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
	<fr:view name="dismissalBean" property="studentCurricularPlan.student" schema="student.show.personAndStudentInformation.short">
		<fr:layout name="flow">
			<fr:property name="labelExcluded" value="true"/>
		</fr:layout>
	</fr:view>
</span>
</p>

<h3 class="mtop2 mbottom05"><bean:message key="label.studentDismissal.add.courses" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

<bean:define id="dismissalTypeName" name="dismissalBean" property="dismissalType.name" />
<bean:define id="scpID" name="dismissalBean" property="studentCurricularPlan.idInternal" />

<fr:form action="<%= "/studentSubstitutions.do?scpID=" + scpID.toString() %>">
	<html:hidden property="method" value="stepThree"/>
	
	<html:messages property="error" message="true" id="errMsg" bundle="ACADEMIC_OFFICE_RESOURCES">
		<p>
			<span class="error0"><!-- Error messages go here --><bean:write name="errMsg" /></span>
		</p>
	</html:messages>

	<fr:edit id="choosDismissalBeanNotNeedToEnrol" name="dismissalBean" >
		<fr:layout name="student-dismissal">
			<fr:property name="dismissalType" value="CURRICULAR_COURSE_CREDITS"/>
		</fr:layout>	
	</fr:edit>
	
	<p class="mtop15 mbottom15">
		<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>	
		<html:cancel onclick="this.form.method.value='stepThree'; return true;"><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>		
	</p>

	<p class="color888 mbottom025 smalltxt"><em><bean:message key="label.studentDismissal.ects.message" bundle="ACADEMIC_OFFICE_RESOURCES"/></em></p>
	<p class="color888 mvert025 smalltxt"><em><bean:message key="label.studentDismissal.min.message" bundle="ACADEMIC_OFFICE_RESOURCES"/></em></p>
	<p class="color888 mtop025 smalltxt"><em><bean:message key="label.studentDismissal.max.message" bundle="ACADEMIC_OFFICE_RESOURCES"/></em></p>

</fr:form>