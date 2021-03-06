<%@page import="org.joda.time.Weeks"%>
<%@page import="org.joda.time.Days"%>
<%@page import="org.joda.time.Period"%>
<%@page import="net.sourceforge.fenixedu.domain.OccupationPeriodReference"%>
<%@page import="net.sourceforge.fenixedu.dataTransferObject.InfoLesson"%>
<%@page import="org.joda.time.Interval"%>
<%@page import="java.util.HashSet"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionDegree"%>
<%@page import="java.util.Set"%>
<%@page import="net.sourceforge.fenixedu.domain.OccupationPeriod"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionCourse"%>
<%@page import="org.joda.time.YearMonthDay"%>
<%@page import="net.sourceforge.fenixedu.domain.Lesson"%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>
<html:xhtml/>

<h2><bean:message key="title.show.all.lesson.dates" bundle="SOP_RESOURCES"/></h2>

<script type="text/javascript">
	function invertSelection() {
		$('input[name="lessonDatesToDelete"]').each(function() {
			var inputVal = $(this).val();
			$(this).attr('checked', !($(this).is(':checked')));
		});
	};
</script>

<logic:present role="RESOURCE_ALLOCATION_MANAGER">

	<p>
	<span class="error0"><!-- Error messages go here -->
		<html:errors/>
	</span>
	</p>

	<bean:define id="parameters"><%=PresentationConstants.LESSON_OID%>=<bean:write name="lesson_" property="idInternal"/>&amp;<%=PresentationConstants.SHIFT_OID%>=<bean:write name="shift" property="idInternal"/>&amp;<%=PresentationConstants.EXECUTION_COURSE_OID%>=<bean:write name="execution_course" property="idInternal"/>&amp;<%=PresentationConstants.ACADEMIC_INTERVAL%>=<%= pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL).toString()%>&amp;<%=PresentationConstants.CURRICULAR_YEAR_OID%>=<bean:write name="curricular_year" property="idInternal"/>&amp;<%=PresentationConstants.EXECUTION_DEGREE_OID%>=<bean:write name="execution_degree" property="idInternal"/></bean:define>	
	<bean:define id="linkToReturn">/manageShift.do?method=prepareEditShift&amp;page=0&amp;<bean:write name="parameters" filter="false"/></bean:define>
	<bean:define id="linkToCreateNewLessonInstance">/manageLesson.do?method=prepareCreateNewLessonInstance&amp;page=0&amp;<bean:write name="parameters" filter="false"/></bean:define>
	
	<p class="mtop20">
		<ul class="mvert">
			<li>						
				<html:link page="<%= linkToReturn %>">
					<bean:message key="link.return"/>
				</html:link>		
			</li>			
		</ul>	
	</p>

	<%
		final Lesson lesson = ((InfoLesson) request.getAttribute("lesson")).getLesson();
		final ExecutionCourse executionCourse = lesson.getExecutionCourse();
		final Set<ExecutionDegree> executionDegrees = executionCourse.getExecutionDegrees();
		final YearMonthDay firstPossibleLessonDay = executionCourse.getMaxLessonsPeriod().getLeft();
	%>
		<h4>
			<bean:message key="label.lesson.period" bundle="SOP_RESOURCES"/>:
		</h4>
		<ul>
	<%
		for (final OccupationPeriod occupationPeriod : executionCourse.getLessonPeriods()) {
	%>
			<li>
				<% for (final Interval interval : occupationPeriod.getIntervals()) { %>
					<% if (!interval.getStart().equals(occupationPeriod.getIntervals().iterator().next().getStart())) { %>
						;
					<% } %>
					<%= interval.getStart().toString("yyyy-MM-dd") %>
					-
					<%= interval.getEnd().toString("yyyy-MM-dd") %>
				<% } %>
				<span style="color: gray;">
				(
				<% for (final ExecutionDegree executionDegree : executionDegrees) {
				    for (final OccupationPeriodReference ref : occupationPeriod.getExecutionDegreesSet()) {
				        if (ref.getExecutionDegree() == executionDegree) {
				%>
							<%= executionDegree.getDegree().getSigla() %>
				<%  } } } %>
				)
				</span>
			</li>
	<%
		}
	%>
		</ul>
				
	<%-- Delete Lesson Instances --%>		
	<bean:define id="linkToDelete">/manageLesson.do?method=deleteLessonInstance&amp;<bean:write name="parameters" filter="false"/></bean:define>
	<bean:define id="linkToDeleteMultiple">/resourceAllocationManager/manageLesson.do?method=deleteLessonInstances&amp;<bean:write name="parameters" filter="false"/></bean:define>
	<a href="#" onclick="invertSelection();"><bean:message key="label.invert.selection" bundle="SOP_RESOURCES"/></a>
	<form action="<%= request.getContextPath() + linkToDeleteMultiple %>" method="post">
		<table class="tstyle1 mtop025 mbottom0 tdcenter">
			<tr>
				<th></th>
				<th><bean:message bundle="SOP_RESOURCES" key="label.lesson.week"/></th>
				<th><bean:message bundle="SOP_RESOURCES" key="label.lesson.day"/></th>
				<th><bean:message bundle="SOP_RESOURCES" key="label.lesson.month"/></th>
				<th><bean:message bundle="SOP_RESOURCES" key="label.lesson.year"/></th>
				<th><bean:message bundle="SOP_RESOURCES" key="label.lesson"/></th>
				<th><bean:message bundle="SOP_RESOURCES" key="label.lesson.type"/></th>
				<th><bean:message bundle="SOP_RESOURCES" key="label.shift"/></th>
				<th><bean:message bundle="SOP_RESOURCES" key="label.lesson.summary"/></th>
				<th><bean:message bundle="SOP_RESOURCES" key="label.lesson.instance"/></th>
				<th></th>
			</tr>
			<logic:iterate id="lessonDate" name="lessonDates" type="net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.NextPossibleSummaryLessonsAndDatesBean">
				<bean:define id="selectableValue" name="lessonDate" property="checkBoxValue"/>
				<tr>
					<td>
						<logic:equal name="lessonDate" property="isPossibleDeleteLessonInstance" value="true">
							<input type="checkbox" name="lessonDatesToDelete" value="<%= selectableValue %>"/>
						</logic:equal>
					</td>
					<td>
						<%= Weeks.weeksBetween(firstPossibleLessonDay, lessonDate.getDate()).getWeeks() + 1 %>
					</td>
					<td><bean:write name="lessonDate" property="date.dayOfMonth"/></td>
					<td><bean:write name="lessonDate" property="monthString"/></td>
					<td><bean:write name="lessonDate" property="date.year"/></td>
					<td><bean:write name="lessonDate" property="lessonInstancePrettyPrint"/></td>
					<td><bean:write name="lessonDate" property="shiftTypesPrettyPrint"/></td>
					<td class="smalltxt"><fr:view name="lessonDate" property="shift" layout="shift-plain"/></td>
					<td><fr:view name="lessonDate" property="writtenSummary"/></td>
					<td><fr:view name="lessonDate" property="hasLessonInstance"/></td>
					<td>
						<logic:equal name="lessonDate" property="isPossibleDeleteLessonInstance" value="true">
							<html:link action="<%= linkToDelete %>" paramId="lessonDate" paramName="lessonDate" paramProperty="checkBoxValue"><bean:message bundle="SOP_RESOURCES" key="label.delete"/></html:link>
						</logic:equal>
					</td>
				</tr>
			</logic:iterate>
		</table>
		<html:submit><bean:message bundle="SOP_RESOURCES" key="label.delete"/></html:submit>
	</form>
</logic:present>