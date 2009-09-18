package gov.nih.nci.cagrid.portal.portlet.query.builder;

import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.dao.UMLClassDao;
import org.springframework.beans.factory.annotation.Required;


import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class AssociationBasedForeignTargetsProvider implements ForeignTargetsProvider {

    private UMLClassDao umlClassDao;

    public List<UMLClass> getSemanticallyEquivalentClasses(UMLClass uClass) {
        return getUmlClassDao().getSemanticallyEquivalentClassesBasedOnAssociations(uClass);
    }

    public UMLClassDao getUmlClassDao() {
        return umlClassDao;
    }

    @Required
    public void setUmlClassDao(UMLClassDao umlClassDao) {
        this.umlClassDao = umlClassDao;
    }
}
