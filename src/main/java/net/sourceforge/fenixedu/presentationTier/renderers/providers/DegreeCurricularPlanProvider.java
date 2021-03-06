package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeCurricularPlanProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final List<DegreeCurricularPlan> degreeCurricularPlans =
                new ArrayList<DegreeCurricularPlan>(DegreeCurricularPlan.readNotEmptyDegreeCurricularPlans());
        Collections.sort(degreeCurricularPlans, DegreeCurricularPlan.COMPARATOR_BY_PRESENTATION_NAME);
        return degreeCurricularPlans;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
