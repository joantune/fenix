<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="DataBeans.InfoLesson" %>
	
	<% ArrayList iA = (ArrayList) session.getAttribute("listaAulas"); %>
<table width="100%" cellspacing="0">
	<tr>
	    <td class="infoselected"><p>O curso seleccionado &eacute;:</p>
			<strong><jsp:include page="context.jsp"/></strong>
	     </td>
	</tr>
	</table>
<br />
<h2><bean:message key="title.manage.aulas"/></h2>
<br />
<span class="error"><html:errors/></span>
<p><b><bean:message key="listAulas.existing"/></b></p>
	<html:form action="/manipularAulasForm">
    <logic:present name="listaAulas" scope="session">
<table>
	<tr>
    	<td class="listClasses-header"></td>
       	<td class="listClasses-header"><bean:message key="property.aula.weekDay"/></td>
        <td class="listClasses-header"><bean:message key="property.aula.time.begining"/></td>
    	<td class="listClasses-header"><bean:message key="property.aula.time.end"/></td>
       	<td class="listClasses-header"><bean:message key="property.aula.type"/></td>
        <td class="listClasses-header"><bean:message key="property.aula.sala"/></td>
  	</tr>
		<% int i = 0; %>
<logic:iterate id="elem" name="listaAulas">
     	<% Integer iH = new Integer(((InfoLesson) iA.get(i)).getInicio().get(Calendar.HOUR_OF_DAY)); %>
        <% Integer iM = new Integer(((InfoLesson) iA.get(i)).getInicio().get(Calendar.MINUTE)); %>
        <% Integer fH = new Integer(((InfoLesson) iA.get(i)).getFim().get(Calendar.HOUR_OF_DAY)); %>
        <% Integer fM = new Integer(((InfoLesson) iA.get(i)).getFim().get(Calendar.MINUTE)); %>
        <% String appendStartMinute = ""; %>
        <% String appendEndMinute = ""; %>
        <% if (iM.intValue() == 0) { %>
        <% 	appendStartMinute = "0"; } %>
        <% if (fM.intValue() == 0) { %>
        <% 	appendEndMinute = "0"; } %>
    <tr>
 		<td class="listClasses"><html:radio property="indexAula" value="<%= (new Integer(i)).toString() %>"/></td>
        <td class="listClasses"><bean:write name="elem" property="diaSemana" /></td>
       	<td class="listClasses"><%= iH.toString()%> : <%= iM.toString() + appendStartMinute %></td>
        <td class="listClasses"><%= fH.toString()%> : <%= fM.toString() + appendEndMinute %></td>
        <td class="listClasses"><%= ((InfoLesson) iA.get(i)).getTipo().toString()%></td>
        <td class="listClasses"><bean:write name="elem" property="infoSala.nome"/></td>
   	</tr>
       	<% i++; %>
</logic:iterate>           
</table>
<br />
<br />
<html:submit property="operation" styleClass="inputbutton"><bean:message key="label.edit.Aula"/></html:submit>
<html:submit property="operation" styleClass="inputbutton"><bean:message key="label.delete.Aula"/></html:submit>
	</logic:present>
    <logic:notPresent name="listaAulas" scope="session">
<span class="errors"><bean:message key="errors.existAulas"/></span>
<br />
<br />          
<html:submit property="operation" styleClass="inputbutton"><bean:message key="label.back"/></html:submit>
  	</logic:notPresent>
</html:form>