<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2>
	<bean:message key="title.evaluationMethod"/>
</h2>

<logic:notEmpty name="executionCourse" property="evaluationMethod">
	<logic:notEmpty name="executionCourse" property="evaluationMethod.evaluationElements">
		<div class="mtop1 coutput2" style="line-height: 1.5em;">
		<fr:view name="executionCourse" property="evaluationMethod.evaluationElements" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString">
			<fr:layout>
				<fr:property name="escaped" value="false" />
				<fr:property name="newlineAware" value="false" />
			</fr:layout>
		</fr:view>
	</div>
	</logic:notEmpty>

	<logic:empty name="executionCourse" property="evaluationMethod.evaluationElements">
		<logic:notEmpty name="executionCourse" property="evaluationMethodText" >
		<div class="mtop1 coutput2" style="line-height: 1.5em;">
				<fr:view name="executionCourse" property="evaluationMethodText">
					<fr:layout>
						<fr:property name="escaped" value="false" />
						<fr:property name="newlineAware" value="true" />
					</fr:layout>
				</fr:view>
			</div>
		</logic:notEmpty>	
		<logic:empty name="executionCourse" property="evaluationMethodText">
			<p>
				<em><bean:message key="message.evaluation.not.available"/></em>
			</p>
		</logic:empty>
	</logic:empty>
</logic:notEmpty>


<logic:empty name="executionCourse" property="evaluationMethod">
	<logic:notEmpty name="executionCourse" property="evaluationMethodText" >
	<div class="mtop1 coutput2" style="line-height: 1.5em;">
			<fr:view name="executionCourse" property="evaluationMethodText">
				<fr:layout>
					<fr:property name="escaped" value="false" />
					<fr:property name="newlineAware" value="true" />
				</fr:layout>
			</fr:view>
		</div>
	</logic:notEmpty>	
	<logic:empty name="executionCourse" property="evaluationMethodText">
		<p>
			<em><bean:message key="message.evaluation.not.available"/></em>
		</p>
	</logic:empty>
</logic:empty>
