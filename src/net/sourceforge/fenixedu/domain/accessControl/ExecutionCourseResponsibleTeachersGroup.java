package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class ExecutionCourseResponsibleTeachersGroup extends AbstractExecutionCourseTeachersGroup {

    private static final long serialVersionUID = 1L;

    public ExecutionCourseResponsibleTeachersGroup(ExecutionCourse executionCourse) {
	super(executionCourse);
    }

    @Override
    public String getName() {
	return RenderUtils.getResourceString("SITE_RESOURCES",
		"label.net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseResponsibleTeachersGroupWithName",
		new Object[] { getExecutionCourse().getNome() });
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] { new IdOperator(getObject()) };
    }

    public static class Builder implements GroupBuilder {

	public Group build(Object[] arguments) {
	    try {
		return new ExecutionCourseResponsibleTeachersGroup((ExecutionCourse) arguments[0]);
	    } catch (ClassCastException e) {
		throw new GroupDynamicExpressionException("accessControl.group.builder.executionCourse.notExecutionCourse",
			arguments[0].toString());
	    }
	}

	public int getMinArguments() {
	    return 0;
	}

	public int getMaxArguments() {
	    return 1;
	}

    }

    @Override
    public Collection<Professorship> getProfessorships() {
	return getExecutionCourse().responsibleFors();
    }

}