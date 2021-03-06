<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp" %>

<bean:define id="showAction" name="showAction" scope="request" type="java.lang.String" />
<bean:define id="searchAction" name="searchAction" scope="request" type="java.lang.String" />
<bean:define id="showContextPath" name="showContextPath" scope="request" type="java.lang.String" />
<bean:define id="searchContextPath" name="searchContextPath" scope="request" type="java.lang.String" />

<bean:define id="showMethod" value="&method=showPublications" toScope="request" />
<bean:define id="searchMethod" value="&method=prepareSearchPublication" toScope="request" />
<bean:define id="siteID" name="<%= FilterFunctionalityContext.CONTEXT_KEY%>" property="selectedContainer.idInternal"/>
<bean:define id="showArguments" value="<%= "siteID=" + siteID %>" toScope="request" />
<bean:define id="searchArguments" value="<%=  "siteID=" + siteID %>" toScope="request" />

<bean:define id="searchPublicationLabelKey" name="searchPublicationLabelKey" scope="request" type="java.lang.String" />

<p>
	<bean:message key="link.Search" bundle="RESEARCHER_RESOURCES" />: 
	<html:link page="<%= showContextPath + showAction + showArguments + showMethod %>">
		<bean:message key="<%=searchPublicationLabelKey%>" bundle="RESEARCHER_RESOURCES" />
	</html:link> | 
	<html:link	page="<%= searchContextPath + searchAction + searchArguments + searchMethod %>">
		<bean:message key="label.search.publications.ist" bundle="RESEARCHER_RESOURCES" />
	</html:link>
</p>

<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.dataTransferObject.SearchDSpacePublicationBean" />

<fr:form id="searchForm" action="<%= searchContextPath + searchAction + searchArguments + "&method=searchPublication" %>">
	<fr:hasMessages for="search" type="validation">
		<p><span class="error0"><bean:message key="label.requiredFieldsNotPresent" /></span></p>
	</fr:hasMessages>

	<p class="mbottom025 color888"><bean:message key="label.choosen.keywords" bundle="RESEARCHER_RESOURCES"/>:</p>

	<fr:edit id="search" name="bean" visible="false" />

	<table class="tstyle5 thlight thright thmiddle">
		<logic:iterate id="searchElement" indexId="index" name="bean" property="searchElements">
		<tr>
			<th>
				<logic:equal name="index" value="0">
					<bean:message key="label.searchField"/>:
				</logic:equal>
				
				<logic:notEqual name="index" value="0">
				<fr:edit id="<%="conjunctionType" + index%>" name="searchElement" slot="conjunction">
				<fr:layout>
					<fr:property name="defaultText" value=""/>
				</fr:layout>
				</fr:edit>
				</logic:notEqual>
				
			</th>
			<td>
				<fr:edit id="<%="valueField" + index%>" name="searchElement" slot="queryValue" >
					<fr:layout>
						<fr:property name="size" value="40"/>
					</fr:layout>
				</fr:edit>

				<bean:message key="label.in" bundle="APPLICATION_RESOURCES"/>
				<fr:edit id="<%= "searchTypeField" + index%>" name="searchElement" slot="searchField">
				<fr:layout>
					<fr:property name="excludedValues" value="TYPE, DATE, COURSE, INFORMATIONS"/>
					<fr:property name="sort" value="true"/>
				</fr:layout>
				</fr:edit>
				
				<logic:equal name="index" value="0">
					<div class="switchNone">
					<html:link page="<%= searchContextPath + searchAction + searchArguments + "&amp;method=addNewSearchCriteria" + bean.getSearchElementsAsParameters()  + "&amp;addIndex=" + (index+1) %>"><bean:message key="label.add" bundle="APPLICATION_RESOURCES"/></html:link>			
					<logic:greaterThan name="bean" property="searchElementsSize" value="1">
					 , 
					<html:link page="<%= searchContextPath + searchAction + searchArguments + "&amp;method=removeSearchCriteria" + bean.getSearchElementsAsParameters()  + "&amp;removeIndex=" + index %>"><bean:message key="label.remove" bundle="APPLICATION_RESOURCES"/></html:link>								
					</logic:greaterThan>
					</div>
					<div class="switchInline">
					<a href="#" onclick="<%= "javascript:getElementById('searchForm').action='" + searchAction + searchArguments + "&amp;method=addNewSearchCriteria" + bean.getSearchElementsAsParameters() + "&amp;addIndex=" + (index+1) + "'; getElementById('searchForm').submit();" %>"><bean:message key="label.add" bundle="APPLICATION_RESOURCES"/></a>
					<logic:greaterThan name="bean" property="searchElementsSize" value="1">
					 , 
					<a href="#" onclick="<%= "javascript:getElementById('searchForm').action='" + searchAction + searchArguments + "&amp;method=removeSearchCriteria" + bean.getSearchElementsAsParameters() + "&amp;removeIndex=" + index + "'; getElementById('searchForm').submit();" %>"><bean:message key="label.remove" bundle="APPLICATION_RESOURCES"/></a>
					</logic:greaterThan>
					</div>
				</logic:equal>
				<logic:notEqual name="index" value="0">
					<div class="switchNone">
					<html:link page="<%= searchContextPath + searchAction + searchArguments + "&amp;method=addNewSearchCriteria" + bean.getSearchElementsAsParameters() %>"><bean:message key="label.add" bundle="APPLICATION_RESOURCES"/></html:link> , 			
					<html:link page="<%= searchContextPath + searchAction + searchArguments + "&amp;method=removeSearchCriteria" + bean.getSearchElementsAsParameters() + "&amp;removeIndex=" + index%>"><bean:message key="link.remove" bundle="APPLICATION_RESOURCES"/></html:link>
					</div>
					<div class="switchInline">
					<a href="#" onclick="<%= "javascript:getElementById('searchForm').action='" + searchAction + searchArguments + "&amp;method=addNewSearchCriteria" + bean.getSearchElementsAsParameters() + "&amp;addIndex=" + (index+1) +  "'; getElementById('searchForm').submit();" %>"><bean:message key="label.add" bundle="APPLICATION_RESOURCES"/></a> , 
					<a href="#" onclick="<%= "javascript:getElementById('searchForm').action='" + searchAction + searchArguments + "&amp;method=removeSearchCriteria" + bean.getSearchElementsAsParameters() + "&amp;removeIndex=" + index + "'; getElementById('searchForm').submit();"%>"><bean:message key="link.remove" bundle="APPLICATION_RESOURCES"/></a>
					</div>
				</logic:notEqual>
				

			</td>
		</tr>
		</logic:iterate>


		</table>
			<html:submit><bean:message key="label.search" /></html:submit>
	</fr:form>

<logic:present name="bean" property="results">
<logic:notEmpty name="bean" property="results">

<p><bean:message key="label.hitCount" />: <strong><fr:view name="bean" property="totalItems"/></strong></p>
<logic:notEqual name="numberOfPages" value="1">
<p>
<bean:message key="label.page" bundle="SITE_RESOURCES"/>: 
<cp:collectionPages module="publico" url="<%= searchContextPath + searchAction + searchArguments + "&amp;method=moveIndex&amp;" + bean.getSearchElementsAsParameters() %>" 
	pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" numberOfVisualizedPages="11"/>
</p>
</logic:notEqual>


<ul>
<logic:iterate id="result" name="bean" property="results" type="net.sourceforge.fenixedu.domain.research.result.ResearchResult">
	<logic:present name="result">
	<bean:define id="resultId" name="result" property="idInternal"/>
	<bean:define id="schema" name="result" property="schema" type="java.lang.String"/>
	
	<li class="mtop1">
	
		<fr:view name="result" layout="nonNullValues" schema="<%= schema %>">
			<fr:layout>
				<fr:property name="classes" value="mbottom025"/>
				<fr:property name="htmlSeparator" value=", "/>
				<fr:property name="indentation" value="false"/>
			</fr:layout>
																
			<fr:destination name="view.publication" path="<%="/showResearchResult.do?resultId=" + resultId + "&method=showPublication" %>"/>
		</fr:view>
		
        		<logic:notEqual name="result" property="class.simpleName" value="Unstructured">
        		(<html:link target="_blank" page="<%="/bibtexExport.do?method=exportPublicationToBibtex&publicationId=" + resultId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.exportToBibTeX" /></html:link>)
        		</logic:notEqual> 
	
			<%-- <p class="mvert0" style="color: #777;"><bean:message key="label.files" bundle="RESEARCHER_RESOURCES"/>:</p> --%>
			<ul class="nobullet mvert05" style="color: #777;">						
				<logic:iterate id="file" name="result" property="resultDocumentFiles">
					<li class="mvert025"><img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="icon_file" bundle="IMAGE_RESOURCES"/>">
						<fr:view name="file" property="displayName"/> 
 
		(<a href="<fr:view name="file" property="downloadUrl"/>"><fr:view name="file" property="filename"/></a>),  
						<fr:view name="file" property="size" layout="fileSize"/>, 
						<bean:message key="label.fileAvailableFor" bundle="RESEARCHER_RESOURCES"/>:
						<em><fr:view name="file" property="fileResultPermittedGroupType"/></em>
					</li>
				</logic:iterate>
			</ul>
	</li>
	</logic:present> 
</logic:iterate>

</ul>


<logic:notEqual name="numberOfPages" value="1">
<p>
<bean:message key="label.page" bundle="SITE_RESOURCES"/>: 

<cp:collectionPages module="publico" url="<%= searchContextPath + searchAction + searchArguments + "&amp;method=moveIndex&amp;" + bean.getSearchElementsAsParameters() %>" 
	pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" numberOfVisualizedPages="11"/>
</p>
</logic:notEqual>

</logic:notEmpty>

<logic:empty name="bean" property="results">
	<bean:message key="label.search.noResultsFound" /> 
</logic:empty>
</logic:present>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>
