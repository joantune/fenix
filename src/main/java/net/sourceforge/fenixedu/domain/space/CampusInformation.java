package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Campus.CampusFactoryEditor;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class CampusInformation extends CampusInformation_Base {

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageSpaceInformation")
    @FenixDomainObjectActionLogAnnotation(actionName = "Created campus information", parameters = { "campus", "name", "begin",
            "end", "blueprintNumber" })
    public CampusInformation(Campus campus, String name, YearMonthDay begin, YearMonthDay end, String blueprintNumber) {
        super();
        super.setSpace(campus);
        setName(name);
        setBlueprintNumber(blueprintNumber);
        setFirstTimeInterval(begin, end);
    }

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToEditSpaceInformation")
    @FenixDomainObjectActionLogAnnotation(actionName = "Edited campus information", parameters = { "name", "begin", "end",
            "blueprintNumber" })
    public void editCampusCharacteristics(String name, YearMonthDay begin, YearMonthDay end, String blueprintNumber) {
        setName(name);
        setBlueprintNumber(blueprintNumber);
        editTimeInterval(begin, end);
    }

    @Override
    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageSpaceInformation")
    @FenixDomainObjectActionLogAnnotation(actionName = "Deleted campus information", parameters = {})
    public void delete() {
        super.delete();
    }

    @Override
    public void setName(final String name) {
        if (StringUtils.isEmpty(name)) {
            throw new DomainException("error.campus.name.cannot.be.null");
        }
        super.setName(name);
    }

    @Override
    public void setSpace(final Space space) {
        throw new DomainException("error.incompatible.space");
    }

    public void setSpace(final Campus campus) {
        throw new DomainException("error.cannot.change.campus");
    }

    @Override
    public CampusFactoryEditor getSpaceFactoryEditor() {
        final CampusFactoryEditor campusFactoryEditor = new CampusFactoryEditor();
        campusFactoryEditor.setSpace((Campus) getSpace());
        campusFactoryEditor.setName(getName());
        campusFactoryEditor.setBegin(getNextPossibleValidFromDate());
        return campusFactoryEditor;
    }

    @Override
    public String getPresentationName() {
        return getName();
    }

    @Override
    public RoomClassification getRoomClassification() {
        // Necessary for Renderers
        return null;
    }

}
