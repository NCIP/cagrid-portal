package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.DBTestBase;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;

import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class UmlClassDaoTest extends DBTestBase<UMLClassDao> {

	
    public void testSemanticEquivalence() {
        UMLClass uClass = getDao().getById(-1);
        List<UMLClass> classes = getDao().getSemanticalyEquivalentClassesBasedOnAtrributes(uClass);
        assertNotNull(classes);

        assertEquals(1, classes.size());

        for (UMLClass umlClass : classes) {
            assertNotSame("Class returned is the same as original class",
                    umlClass.getId(), uClass.getId());
            assertEquals("Unexpected class returned", "SomeClass", umlClass.getClassName());

            assertNotSame("Class returned is from the same model",
                    umlClass.getModel().getId(), uClass.getModel().getId());
        }
    }

    public void testgetSemanticallyEquivalentClassesBasedOnAssociations() {
        UMLClass uClass = getDao().getById(-1);
        List<UMLClass> classes = getDao().getSemanticallyEquivalentClassesBasedOnAssociations(uClass);
        assertNotNull(classes);
        assertEquals(1, classes.size());

        for (UMLClass umlClass : classes) {
            assertNotSame("Class returned is the same as original class",
                    umlClass.getId(), uClass.getId());
            assertEquals("Unexpected class returned", "RelatedClassByAssociation", umlClass.getClassName());

            assertNotSame("Class returned is from the same model",
                    umlClass.getModel().getId(), uClass.getModel().getId());

        }
    }


    public void testQueryByExample() {
        UMLClass example = getDao().getById(-1);
        List<UMLClass> result = getDao().getSameClassesInDifferentModel(example);
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
