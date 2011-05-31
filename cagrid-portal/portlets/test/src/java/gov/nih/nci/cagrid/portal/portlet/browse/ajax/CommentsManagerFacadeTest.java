package gov.nih.nci.cagrid.portal.portlet.browse.ajax;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CommentsManagerFacadeTest extends AbstractCatalogManagerFacadeTest<CommentsManagerFacade> {

    public void testSave() {
        assertNotNull(facade.addComment(""));

        assertEquals(facade.getAll().size(), 1);


    }
}
