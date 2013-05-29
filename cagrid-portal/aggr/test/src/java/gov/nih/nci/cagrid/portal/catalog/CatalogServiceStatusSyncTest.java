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
package gov.nih.nci.cagrid.portal.catalog;

import gov.nih.nci.cagrid.portal.aggr.catalog.CatalogServiceStatusSync;
import org.junit.Test;

/**
 * @author kherm manav.kher@semanticbits.com
 */
public class CatalogServiceStatusSyncTest extends
        AbstractServiceMetadataCatalogEntryBuilderTest {

    /**
     * Test the sync method
     */
    @Test
    public void testSync() {

        CatalogServiceStatusSync catalogServiceStatusSync = (CatalogServiceStatusSync)
                getBean("catalogServiceStatusSync");

        catalogServiceStatusSync.sync();
    }


    @Override
    protected String[] getConfigLocations() {
        return new String[]{"applicationContext-db.xml",
                "applicationContext-cleanup-utils.xml",
                "applicationContext-aggr.xml"};
    }
}
