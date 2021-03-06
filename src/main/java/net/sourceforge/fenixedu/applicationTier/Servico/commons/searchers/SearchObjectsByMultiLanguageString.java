package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtils;

import pt.ist.fenixframework.DomainObject;
import pt.utl.ist.fenix.tools.util.StringNormalizer;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class SearchObjectsByMultiLanguageString extends FenixService implements AutoCompleteSearchService {

    @Override
    public Collection run(Class type, String value, int limit, Map<String, String> arguments) {
        List<DomainObject> result = new ArrayList<DomainObject>();

        String slotName = arguments.get("slot");
        Collection<DomainObject> objects = RootDomainObject.readAllDomainObjects(type);

        if (value == null) {
            result.addAll(objects);
        } else {
            String[] values = StringNormalizer.normalize(value).toLowerCase().split("\\p{Space}+");

            for (DomainObject object : objects) {
                try {
                    MultiLanguageString objectMLS = (MultiLanguageString) PropertyUtils.getProperty(object, slotName);

                    if (objectMLS == null || objectMLS.getAllContents().size() == 0) {
                        continue;
                    }

                    for (Language language : objectMLS.getAllLanguages()) {
                        String objectValue = objectMLS.getContent(language);

                        String normalizedValue = StringNormalizer.normalize(objectValue).toLowerCase();

                        boolean matches = true;
                        for (String value2 : values) {
                            String part = value2;

                            if (!normalizedValue.contains(part)) {
                                matches = false;
                            }
                        }

                        if (matches) {
                            result.add(object);
                            break;
                        }
                    }

                    if (result.size() >= limit) {
                        break;
                    }

                } catch (IllegalAccessException e) {
                    throw new DomainException("searchObject.type.notFound", e);
                } catch (InvocationTargetException e) {
                    throw new DomainException("searchObject.failed.read", e);
                } catch (NoSuchMethodException e) {
                    throw new DomainException("searchObject.failed.read", e);
                }
            }
        }

        Collections.sort(result, new BeanComparator(slotName + ".content"));
        return result;
    }
}
