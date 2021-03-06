<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<logic:present name="siteView">
	<logic:present name="siteView" property="commonComponent.executionCourse.nome">
		<% final String appContext = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("app.context"); %>
		<% final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : ""; %>

		<bean:define id="executionCourseName" name="siteView" property="commonComponent.executionCourse.nome" />

		<bean:define id="linkRSSa" type="java.lang.String"><%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%><%=context%><%="/publico/announcementsRSS.do?id=" + pageContext.findAttribute("executionCourseCode")%></bean:define>
		<link rel="alternate" type="application/rss+xml" title="<%= executionCourseName %>" href="<%= linkRSSa %>"/>

		<bean:define id="linkRSSs" type="java.lang.String"><%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%><%=context%><%="/publico/summariesRSS.do?id=" + pageContext.findAttribute("executionCourseCode")%></bean:define>
		<link rel="alternate" type="application/rss+xml" title="<%= executionCourseName %>" href="<%= linkRSSs %>"/>

	</logic:present>
</logic:present>