<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:present name="executionCourse">

	<em>
		<%--<bean:message key="message.course.editing"/>--%>
		<bean:write name="executionCourse" property="nome"/>

		-

		<bean:write name="executionCourse" property="executionPeriod.semester" />
		<bean:message bundle="PUBLIC_DEGREE_INFORMATION" locale="pt_PT" key="public.degree.information.label.ordinal.semester.abbr" />
		<bean:write name="executionCourse" property="executionPeriod.executionYear.year" />

		(
		<logic:iterate id="degree" name="executionCourse" property="degreesSortedByDegreeName">
			<bean:write name="degree" property="sigla"/>
		</logic:iterate>
		)
    </em>

<%--
	<hr style='color:#ccc'/>
--%>

</logic:present>
