package gov.nih.nci.cagrid.portal.portlet.query.builder;

import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.dao.UMLClassDao;

import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class AttributeBasedForeignTargetsProvider implements ForeignTargetsProvider {

    private UMLClassDao umlClassDao;

    public List<UMLClass> getSemanticallyEquivalentClasses(UMLClass uClass) {
        return umlClassDao.getSemanticalyEquivalentClassesBasedOnAtrributes(uClass);
    }

    public UMLClassDao getUmlClassDao() {
        return umlClassDao;
    }

    public void setUmlClassDao(UMLClassDao umlClassDao) {
        this.umlClassDao = umlClassDao;
    }
}
