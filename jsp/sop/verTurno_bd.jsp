<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="DataBeans.InfoLesson" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
  		<td class="infoselected"><p>O curso seleccionado &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
       	</td>
   	</tr>
</table>
<br />
   	<% ArrayList iA = (ArrayList) session.getAttribute("infoAulasDeTurno"); %>
<h2 class="inline"><bean:message key="property.turno"/>: </h2><h3 class="inline"><bean:write name="infoTurno" property="nome" scope="session" filter="true"/></h3>
<p><b><bean:message key="listAulas.OfTurno"/></b></p>
    <logic:present name="infoAulasDeTurno" scope="session">
<table>
	<tr>
    	<td class="listClasses-header"><bean:message key="property.aula.weekDay"/></td>
     	<td class="listClasses-header"><bean:message key="property.aula.time.begining"/></td>
        <td class="listClasses-header"><bean:message key="property.aula.time.end"/></td>
        <td class="listClasses-header"><bean:message key="property.aula.type"/></td>
        <td class="listClasses-header"><bean:message key="property.aula.sala"/></td>
 	</tr>
	<% int i = 0; %>
    <logic:iterate id="elem" name="infoAulasDeTurno">
    <% Integer iH = new Integer(((InfoLesson) iA.get(i)).getInicio().get(Calendar.HOUR_OF_DAY)); %>
    <% Integer iM = new Integer(((InfoLesson) iA.get(i)).getInicio().get(Calendar.MINUTE)); %>
    <% Integer fH = new Integer(((InfoLesson) iA.get(i)).getFim().get(Calendar.HOUR_OF_DAY)); %>
    <% Integer fM = new Integer(((InfoLesson) iA.get(i)).getFim().get(Calendar.MINUTE)); %>
   	<tr>
      	<td class="listClasses"><bean:write name="elem" property="diaSemana" /></td>
     	<td class="listClasses"><%= iH.toString()%> : <%= iM.toString()%></td>
       	<td class="listClasses"><%= fH.toString()%> : <%= fM.toString()%></td>
        <td class="listClasses"><%= ((InfoLesson) iA.get(i)).getTipo().toString()%></td>
        <td class="listClasses"><bean:write name="elem" property="infoSala.nome"/></td>
  	</tr>
    <% i++; %>
    </logic:iterate>           
</table>
    </logic:present>
    <logic:notPresent name="infoAulasDeTurno" scope="session">
<span class="error"><bean:message key="errors.existAulas"/></span>
</logic:notPresent>