package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.DBTestBase;
import gov.nih.nci.cagrid.portal.domain.metadata.common.UMLAttribute;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.List;


/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class UmlAttributeDaoTest extends DBTestBase<UMLAttributeDao> {

    @Test
    public void testDao() {
        assertNotNull(getDao().getAll());
    }

    @Test
    public void testSemanticEquivalence() {
        UMLAttribute attr = getDao().getById(-1);
        List<UMLAttribute> attrs = getDao().getSemanticallyEquivalentAttributes(attr);
        assertNotNull(attrs);

        assertEquals(attrs.size(), 1);

        for (UMLAttribute attribute : attrs) {
            assertNotSame("Attribute returned is the same as original attribute",
                    attr.getId(), attribute.getId());
            assertNotSame("Attribute returned from the same class. Not possible",
                    attr.getUmlClass().getId(), attribute.getUmlClass());
        }
    }

     @Override
    protected String getDataSet() throws Exception {
        return "test/data/UMLAttributeDaoTest.xml";
    }

}
