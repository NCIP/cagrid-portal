/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 *
 */
package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.DBTestBase;
import gov.nih.nci.cagrid.portal.domain.ConceptHierarchy;
import gov.nih.nci.cagrid.portal.domain.ConceptHierarchyNode;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 */
public class ConceptHierarchyNodeDaoTest extends
        DBTestBase<ConceptHierarchyNodeDao> {

    @Test
    public void testHierarchy() {
        try {
            Set<String> childSet1 = new HashSet<String>();
            childSet1.add("node1");
            childSet1.add("node2");
            childSet1.add("node3");
            Set<String> childSet2 = new HashSet<String>();
            childSet2.add("node2-1");
            childSet2.add("node2-2");
            childSet2.add("node2-3");
            Set<String> descendantSet = new HashSet<String>();
            descendantSet.addAll(childSet1);
            descendantSet.addAll(childSet2);
            Set<String> ancestorSet = new HashSet<String>();
            ancestorSet.add("node2");
            ancestorSet.add("root");
            String[] ancestorList = new String[]{"node2", "root"};

            ConceptHierarchyNode root = getDao().getById(-1);
            assertNotNull("root is null", root);
            ConceptHierarchyNode node2 = getDao().getById(-3);
            assertNotNull("node2 is null", node2);
            ConceptHierarchyNode node22 = getDao().getById(-6);
            assertNotNull("node2-2 is null", node22);

            assertTrue("root children set doesn't match", getConceptNameSet(
                    root.getChildren()).containsAll(childSet1));

            assertTrue("node2 children set doesn't match", getConceptNameSet(
                    node2.getChildren()).containsAll(childSet2));

            assertTrue("root descendant set doesn't match", getConceptNameSet(
                    root.getDescendants()).containsAll(descendantSet));

            assertTrue("node22 ancestor set doesn't match", getConceptNameSet(
                    node22.getAncestors()).containsAll(ancestorSet));

            int i = 0;
            for (ConceptHierarchyNode node : node22.getAncestors()) {
                assertEquals("ancestor nodes in wrong order", ancestorList[i],
                        node.getName());
                i++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error testing hierarchy: " + ex.getMessage());
        }
    }

    @Test
    public void testGetByConceptInHierarchy() {

        ConceptHierarchy hierarchy = getDao().getHierarchyByUri(
                "http://some.hierarchy.uri");
        assertNotNull("hierarchy is null", hierarchy);
        ConceptHierarchyNode root = getDao().getById(-1);
        assertNotNull("root is null", root);
        ConceptHierarchyNode node2 = getDao().getById(-3);
        assertNotNull("node2 is null", node2);
        ConceptHierarchyNode node22 = getDao().getById(-6);
        assertNotNull("node2-2 is null", node22);

        List<ConceptHierarchyNode> pathFromRoot = new ArrayList<ConceptHierarchyNode>();
        pathFromRoot.add(root);
        pathFromRoot.add(node2);

        ConceptHierarchyNode node = getDao().getByConceptInHierarchy(hierarchy,
                pathFromRoot, node22.getCode());
    }

    @Test
    public void testUpdateHierarchy() {
        try {
            Set<String> childSet1 = new HashSet<String>();
            childSet1.add("node1");
            childSet1.add("node2");
            childSet1.add("node3");
            Set<String> childSet2 = new HashSet<String>();
            childSet2.add("node2-1");
            childSet2.add("node2-2");
            childSet2.add("node2-3");
            childSet2.add("node2-4");
            Set<String> descendantSet = new HashSet<String>();
            descendantSet.addAll(childSet1);
            descendantSet.addAll(childSet2);
            Set<String> ancestorSet = new HashSet<String>();
            ancestorSet.add("node2");
            ancestorSet.add("root");
            String[] ancestorList = new String[]{"node2", "root"};

            ConceptHierarchyNode root = getDao().getById(-1);
            assertNotNull("root is null", root);
            ConceptHierarchyNode node2 = getDao().getById(-3);
            assertNotNull("node2 is null", node2);

            ConceptHierarchyNode node24 = new ConceptHierarchyNode();
            node24.setName("node2-4");
            node24.setParent(node2);
            node24.getAncestors().add(node2);
            node24.getAncestors().add(root);
            getDao().save(node24);

            getDao().getHibernateTemplate().flush();

            assertTrue("root children set doesn't match", getConceptNameSet(
                    root.getChildren()).containsAll(childSet1));

            assertTrue("node2 children set doesn't match", getConceptNameSet(
                    node2.getChildren()).containsAll(childSet2));

            assertTrue("root descendant set doesn't match: " + descendantSet
                    + " vs. " + getConceptNameSet(root.getDescendants()),
                    getConceptNameSet(root.getDescendants()).containsAll(
                            descendantSet));

            assertTrue("node22 ancestor set doesn't match", getConceptNameSet(
                    node24.getAncestors()).containsAll(ancestorSet));

            int i = 0;
            for (ConceptHierarchyNode node : node24.getAncestors()) {
                assertEquals("ancestor nodes in wrong order", ancestorList[i],
                        node.getName());
                i++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error testing update hierarchy: " + ex.getMessage());
        }
    }

    @Test
    public void testSearchByName() {
        assertTrue("node returned null", getDao().getByName("node2").size() > 0);
        assertEquals("node returned null", 8, getDao().getByName("node2").size());
        assertNotNull(getDao().getByConceptCode("node1_code"));
    }

    @Test
    public void testServicesByCode() {
        assertEquals(1, getDao().getServicesByCode("root_code").size());
    }

    private Set<String> getConceptNameSet(List<ConceptHierarchyNode> nodes) {
        Set<String> s = new HashSet<String>();
        for (ConceptHierarchyNode node : nodes) {
            s.add(node.getName());
        }
        return s;
    }

}
