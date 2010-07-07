package net.sourceforge.fenixedu.presentationTier.Action.personnelSection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateNewInternalPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.dataTransferObject.person.InternalPersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Person.AnyPersonSearchBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@Mapping(path = "/personnelManagePeople", module = "personnelSection")
@Forwards( {
    	@Forward(name = "searchPeople", path = "manage-people-search"),
    	@Forward(name = "createPerson", path = "manage-people-create"),
    	@Forward(name = "createPersonFillInfo", path = "manage-people-create-fill-info"),
    	@Forward(name = "viewPerson", path = "manage-people-view-person")
})
public class ManagePeople extends FenixDispatchAction {

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return mapping.findForward("searchPeople");
    }

    public ActionForward prepareCreatePerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final AnyPersonSearchBean anyPersonSearchBean = new AnyPersonSearchBean();
	request.setAttribute("anyPersonSearchBean", anyPersonSearchBean);
	return mapping.findForward("createPerson");
    }

    public ActionForward showExistentPersonsWithSameMandatoryDetails(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	final IViewState viewState = RenderUtils.getViewState("anyPersonSearchBeanId");
	AnyPersonSearchBean bean = (AnyPersonSearchBean) viewState.getMetaObject().getObject();

	SearchParameters searchParameters = new SearchPerson.SearchParameters(bean.getName(), null, null, bean
		.getDocumentIdNumber(), bean.getIdDocumentType() != null ? bean.getIdDocumentType().getName() : null, null, null,
		null, null, null, null, null);

	SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(searchParameters);

	IUserView userView = UserView.getUser();
	CollectionPager<Person> result = null;
	Object[] args = { searchParameters, predicate };

	try {
	    result = (CollectionPager<Person>) ServiceManagerServiceFactory.executeService("SearchPerson", args);

	} catch (FenixServiceException e) {
	    request.setAttribute("anyPersonSearchBean", bean);
	    return mapping.findForward("createPerson");
	}

	request.setAttribute("resultPersons", result.getCollection());
	request.setAttribute("anyPersonSearchBean", bean);
	return mapping.findForward("createPerson");
    }

    private void setRequestParametersToCreateInvitedPerson(final HttpServletRequest request,
	    final InternalPersonBean personBean) {

	final String name = request.getParameter("name");
	if (isSpecified(name)) {
	    personBean.setName(name);
	}
	final String idDocumentType = request.getParameter("idDocumentType");
	if (isSpecified(idDocumentType)) {
	    personBean.setIdDocumentType(IDDocumentType.valueOf(idDocumentType));
	}
	final String documentIdNumber = request.getParameter("documentIdNumber");
	if (isSpecified(documentIdNumber)) {
	    personBean.setDocumentIdNumber(documentIdNumber);
	}

	request.setAttribute("personBean", personBean);
    }

    private boolean isSpecified(final String string) {
	return !StringUtils.isEmpty(string);
    }

    public ActionForward prepareCreatePersonFillInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	setRequestParametersToCreateInvitedPerson(request, new InternalPersonBean());
	request.setAttribute("initialUnit", UnitUtils.readInstitutionUnit());
	return mapping.findForward("createPersonFillInfo");
    }

    public ActionForward createNewPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final InternalPersonBean bean = (InternalPersonBean) getRenderedObject();
	try {
	    final Person person = CreateNewInternalPerson.run(bean);
	    return viewPerson(person, mapping, request);
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	    request.setAttribute("invitedPersonBean", bean);
	    return mapping.findForward("createPersonFillInfo");
	}
    }

    public ActionForward invalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	InternalPersonBean bean = (InternalPersonBean) getRenderedObject();
	request.setAttribute("invitedPersonBean", bean);
	return mapping.findForward("createPersonFillInfo");
    }

    public ActionForward viewPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final Person person = getDomainObject(request, "personId");
	return viewPerson(person, mapping, request);
    }

    public ActionForward viewPerson(final Person person, final ActionMapping mapping, final HttpServletRequest request)
    		throws Exception {
	request.setAttribute("person", person);
	return mapping.findForward("viewPerson");
    }

}