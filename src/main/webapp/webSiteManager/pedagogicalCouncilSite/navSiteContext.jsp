<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="siteActionName" value="/managePedagogicalCouncilSite.do" toScope="request"/>
<bean:define id="siteContextParam" value="oid" toScope="request"/>
<bean:define id="siteContextParamValue" name="site" property="idInternal" toScope="request"/>

<bean:define id="unitId" name="site" property="unit.idInternal"/>
<bean:define id="announcementsActionName" value="/managePedagogicalCouncilAnnouncements.do" toScope="request"/>
