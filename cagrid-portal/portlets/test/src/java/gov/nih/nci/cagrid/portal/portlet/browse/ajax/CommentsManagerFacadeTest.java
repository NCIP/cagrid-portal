/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
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
