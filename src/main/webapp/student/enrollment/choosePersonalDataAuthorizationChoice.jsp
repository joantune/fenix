<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="STUDENT">
    <em><bean:message key="title.student.portalTitle" bundle="STUDENT_RESOURCES" /></em>
    <h2><bean:message key="label.enrollment.personalData.inquiry" /></h2>

    <div class="infoop2">
    <p><bean:message key="label.info.dislocatedStudent.inquiry" /></p>
    </div>

    <p class="mtop2 mbottom05"><bean:message key="label.enrollment.personalData.authorization" /></p>

    <fr:form action="/studentEnrollmentManagement.do?method=choosePersonalDataAuthorizationChoice">

        <fr:edit name="student" schema="Student.editPersonalDataAuthorization">
            <fr:schema type="net.sourceforge.fenixedu.domain.student.Student" bundle="APPLICATION_RESOURCES">
                <fr:slot name="personalDataAuthorization" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" layout="menu-select">
                    <fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.student.PersonalDataAuthorizationProvider" />
                </fr:slot>
            </fr:schema>
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle2 thlight thright" />
                <fr:property name="columnClasses" value=",,tdclear tderror1" />
            </fr:layout>
        </fr:edit>

        <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
            <bean:message bundle="APPLICATION_RESOURCES" key="label.continue" />
        </html:submit>
    </fr:form>

    <p class="mtop2"><em><bean:message key="label.enrollment.personalData.changes" /></em></p>

</logic:present>

