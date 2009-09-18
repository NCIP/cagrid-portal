package gov.nih.nci.cagrid.portal.portlet.query.builder;

import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;

import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public interface ForeignTargetsProvider {

    public List<UMLClass> getSemanticallyEquivalentClasses(UMLClass uClass);
}
