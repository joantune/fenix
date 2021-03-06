<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="dcpId" name="degreeCurricularPlan" property="externalId"/>
<bean:define id="executionYearId" name="executionYearId"/>
<bean:define id="thesisId" name="thesis" property="externalId"/>

<html:xhtml/>

<h2><bean:message key="title.coordinator.thesis.edit"/></h2>

<p class="mtop15 mbottom05">
	<strong><bean:message key="title.coordinator.thesis.changeInformation"/></strong>
</p>

<bean:define id="returnMethod" value="<%= request.getParameter("return") != null ? request.getParameter("return") : "viewConfirmed" %>" type="java.lang.String"/>

<fr:edit name="thesis"
         action="<%= String.format("/manageThesis.do?method=editProposalWithDocs&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>"
         schema="thesis.jury.proposal.information.edit">
     <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 tdtop thlight thright mtop05"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
    </fr:layout>
    
    <fr:destination name="cancel" path="<%= String.format("/manageThesis.do?method=%s&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", returnMethod, dcpId, executionYearId, thesisId) %>"/>
 </fr:edit>
