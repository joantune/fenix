<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<table width="100%" cellspacing="0">
	<tr>
    	<td class="infoselected"><p>O curso seleccionado &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
       	</td>
   	</tr>
</table>
<br />
<h2><bean:message key="title.editAula"/></h2>
<span class="error"><html:errors/></span>
	<html:form action="/editarAulaForm">
<table> 
	<tr>
    	<td class="formTD"><bean:message key="property.aula.weekDay"/>: </td>
      	<td><span class="grey-txt"><b><html:hidden property="diaSemana"/><bean:write name="weekDayString"/></b></span></td>
   	</tr>
    <tr>
    	<td class="formTD"><bean:message key="property.aula.time.begining"/>: </td>
        <td><span class="grey-txt"><b><html:hidden property="horaInicio" write="true"/> :
	        <html:hidden property="minutosInicio" write="true"/></b>                        
      		</span>
      	</td>
  	</tr>
    <tr>
    	<td class="formTD"><bean:message key="property.aula.time.end"/>: </td>
        <td><span class="grey-txt"><b><html:hidden property="horaFim" write="true"/> :
			<html:hidden property="minutosFim" write="true"/></b>
       		</span>
       	</td>
    </tr>
    <tr>
       	<td class="formTD"><bean:message key="property.aula.type"/>: </td>
       	<td><span class="grey-txt"><b><html:select property="tipoAula">
             	<html:options collection="tiposAula" property="value" labelProperty="label"/>
           	</html:select></b>
           	</span>
      	</td>
   	</tr>
    <tr>
    	<td class="formTD"><bean:message key="property.aula.sala"/>: </td>
       	<td><html:select property="nomeSala" size="1" >
            	<html:options collection="listaSalas" property="value" labelProperty="label"/>
           	</html:select>
     	</td>
  	</tr>
</table>
<br />
<html:submit property="operation" styleClass="inputbutton"><bean:message key="label.save"/></html:submit>
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>
</html:form>