
<%@ page language="java" %>

<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<%@ page import="java.lang.String" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoLesson"%>
<%@ page import="DataBeans.InfoShift"%>

<%@ page import="java.util.Calendar" %>




<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />
	
	<logic:empty name="component" property="infoSiteGroupsByShiftList">
		<h2><bean:message key="message.shifts.not.available" /></h2>
	</logic:empty>
	
<logic:notEmpty name="component" property="infoSiteGroupsByShiftList">
	<h2><bean:message key="title.ShiftsAndGroups"/></h2>
	
	<br/>
	
	
	<html:link page="<%= "/viewSite.do" + "?method=viewAllStudentsAndGroupsAction&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;groupProperties=" + pageContext.findAttribute("groupProperties") %>" >
			<bean:message key="link.viewAllStudentsAndGroups"/>
	</html:link>
		
		<br/>
		<br/>
		
<table align="center" width="95%" cellspacing='1' cellpadding='1'>
	<tbody>	
			
	<tr>
		<td class="listClasses-header" width="20%" rowspan="2">
			<bean:message key="property.shift"/>
		</td>
		<td class="listClasses-header" colspan="4" width="60%">
			<bean:message key="property.lessons"/>
		</td>
		<td class="listClasses-header" width="20%" rowspan="2">
			<bean:message key="property.groups"/>
		</td>
	</tr>
					
	<tr>
		<td class="listClasses-header" width="20%">
			<bean:message key="property.lesson.weekDay"/>
		</td>
		<td class="listClasses-header" width="10%">
			<bean:message key="property.lesson.beginning"/>
		</td>
		<td class="listClasses-header" width="10%">
			<bean:message key="property.lesson.end"/>
		</td>
		<td class="listClasses-header" width="20%">
			<bean:message key="property.lesson.room"/>
		</td>
	</tr>		
    		
     <logic:iterate id="infoSiteGroupsByShift" name="component" property="infoSiteGroupsByShiftList" >
		<logic:empty name="infoSiteGroupsByShift" property="infoSiteShift.infoShift">
			<tr>
								
						<td  class="listClasses">
						<html:link page="<%= "/viewSite.do" + "?method=viewStudentsAndGroupsWithoutShiftAction&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;groupProperties=" + pageContext.findAttribute("groupProperties") %>" >
							Sem Turno
						</html:link>	
							
						</td>
								
						<td class="listClasses">
								---
						</td>
								
						<td class="listClasses">
								---	
						</td>
								
						<td class="listClasses">
								---	
						</td>
							
		               	<td class="listClasses">
								---	
						 </td>
				 		
				 		 <td class="listClasses" >
                        <logic:notEmpty name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList">
                        [<logic:iterate id="infoSiteStudentGroup" name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList" >
							<bean:define id="infoStudentGroup" name="infoSiteStudentGroup" property="infoStudentGroup"/>	
                        	<html:link page="<%= "/viewSite.do" + "?method=viewStudentGroupInformationAction&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;groupProperties=" + pageContext.findAttribute("groupProperties") %>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
								<bean:write name="infoStudentGroup" property="groupNumber"/>
							</html:link>
						</logic:iterate>]
						</logic:notEmpty>
						
						<logic:empty name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList">
							<bean:message key="message.shift.without.groups"/>
						</logic:empty>
						
						 </td>
							
						 						
						 	</tr>
			</logic:empty>		
		
		<logic:notEmpty name="infoSiteGroupsByShift" property="infoSiteShift.infoShift">
		<bean:define id="infoSiteShift" name="infoSiteGroupsByShift" property="infoSiteShift"/>	
			<bean:define id="infoShift" name="infoSiteShift" property="infoShift"/>	
			<bean:define id="shiftCode" name="infoShift" property="idInternal"/>
				<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" length="1" indexId="infoLessonIndex">
            		<% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
                	<% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
                	<% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
                	<% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
					<tr>
						
						<td  class="listClasses" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size() %>">
						<html:link page="<%= "/viewSite.do" + "?method=viewStudentsAndGroupsByShiftAction&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;groupProperties=" + pageContext.findAttribute("groupProperties")+ "&amp;shift=" + shiftCode.toString() %>" >
							<bean:write name="infoShift" property="nome"/>
						</html:link>	
						</td>
						<td class="listClasses">
							<bean:write name="infoLesson" property="diaSemana"/> &nbsp;
						</td>
						<td class="listClasses">
							<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
						</td>
						<td class="listClasses">
							<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>								
						</td>
							
		               	<td class="listClasses">
							<bean:write name="infoLesson" property="infoSala.nome"/>
				 		</td>
				 		
				 		 <td class="listClasses" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size()%>">
                        <logic:notEmpty name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList">
                        [<logic:iterate id="infoSiteStudentGroup" name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList" >
							<bean:define id="infoStudentGroup" name="infoSiteStudentGroup" property="infoStudentGroup"/>	
                        	<html:link page="<%= "/viewSite.do" + "?method=viewStudentGroupInformationAction&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;groupProperties=" + pageContext.findAttribute("groupProperties") %>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
								<bean:write name="infoStudentGroup" property="groupNumber"/>
							</html:link>
						</logic:iterate>]
						</logic:notEmpty>
						
						<logic:empty name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList">
							<bean:message key="message.shift.without.groups"/>
						</logic:empty>
						
						 </td>
				 						
				 	</tr>
				</logic:iterate>
				
				<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" offset="1">
                       <% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
                       <% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
						<tr >
							<td class="listClasses">
								<bean:write name="infoLesson" property="diaSemana"/> &nbsp;
							</td>
							<td class="listClasses">
								<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
							</td>
							<td class="listClasses">
								<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>
							</td>
							<td class="listClasses">
								<bean:write name="infoLesson" property="infoSala.nome"/>
							</td>
						</tr>
					
					</logic:iterate>
					</logic:notEmpty>      
  </logic:iterate>
       
      </tbody>
</table>
</logic:notEmpty>
</logic:present>

<logic:notPresent name="siteView" property="component">
<h2>
<bean:message key="message.shifts.not.available" />
</h2>
</logic:notPresent>