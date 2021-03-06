<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert definition="df.layout.two-column" beanName="" flush="true">
  <tiles:put name="title" value="private.resourcemanagement.schedules.shifts.addClasses" />
  <tiles:put name="bundle" value="TITLES_RESOURCES" />
  <tiles:put name="serviceName" value="SOP - Servico de Organizacao Pedagogica" />
  <tiles:put name="navGeral" value="/commons/commonGeneralNavigationBar.jsp" />  
  <tiles:put name="navLocal" value="/resourceAllocationManager/commonNavLocalSchedulesSop.jsp" />
  <tiles:put name="body-context" value="/commons/blank.jsp"/>  
  <tiles:put name="body" value="/resourceAllocationManager/addClasses_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
  <tiles:put name="context" value="/commons/contextShiftAndExecutionCourseAndExecutionDegreeAndCurricularYear.jsp" />
</tiles:insert>