package net.sourceforge.fenixedu.presentationTier.Action.commons.person;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "person", path = "/voidAction", attribute = "voidForm", formBean = "voidForm", scope = "request")
@Forwards(value = { @Forward(name = "Success", path = "/teacher/teacherInformation.do?method=prepareEdit&page=0",
        redirect = true, contextRelative = true) })
public class VoidActionForPerson extends net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction {
}