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
package gov.nih.nci.cagrid.portal.catalog;

import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.portal.TestDB;
import gov.nih.nci.cagrid.portal.aggr.catalog.ServiceMetadataCatalogEntryBuilder;
import gov.nih.nci.cagrid.portal.dao.catalog.InformationModelCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.catalog.GridDataServiceEndPointCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.GridServiceEndPointCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.InformationModelCatalogEntry;
import gov.nih.nci.cagrid.portal.util.Metadata;
import static junit.framework.Assert.*;
import org.junit.Test;

import java.io.FileReader;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ServiceMetadataCatalogEntryBuilderDuplicateTest extends
        AbstractServiceMetadataCatalogEntryBuilderTest {


    @Test
    public void informationModel() throws Exception {
        String serviceUrl = "http://service.url";
        GridDataService dataService = new GridDataService();
        dataService.setUrl(serviceUrl);
        getGridServiceDao().save(dataService);
        Metadata meta = new Metadata();

        try {
            meta.dmodel = MetadataUtils.deserializeDomainModel(new FileReader(
                    "test/data/cabioModelSnippet.xml"));
            meta.smeta = MetadataUtils
                    .deserializeServiceMetadata(new FileReader(
                            "test/data/cabioServiceMetadata.xml"));
        } catch (Exception ex) {
            fail("Error deserializing test data: " + ex.getMessage());
            ex.printStackTrace();
        }
        try {
            getMetadataListener().loadMetadata(dataService, meta);
        } catch (Exception ex) {
            fail("Error loading metadata: " + ex.getMessage());
            ex.printStackTrace();
        }

        ServiceMetadataCatalogEntryBuilder b = (ServiceMetadataCatalogEntryBuilder)
                getApplicationContext().getBean(
                        "serviceMetadataCatalogEntryBuilder");

        final GridServiceEndPointCatalogEntry endpointCe = b.build(dataService);
        assertTrue(endpointCe instanceof GridDataServiceEndPointCatalogEntry);

        InformationModelCatalogEntryDao infoDao = (InformationModelCatalogEntryDao) TestDB
                .getApplicationContext().getBean("informationModelCatalogEntryDao");

        assertEquals(1, infoDao.getAll().size());

        InformationModelCatalogEntry infoEntry = (InformationModelCatalogEntry) infoDao.getAll().get(0);
        infoEntry.setAuthor(pUser);
        infoDao.save(infoEntry);

        GridServiceEndPointCatalogEntry endpointCe2 = b.build(dataService);
        assertEquals(endpointCe.getId(), endpointCe2.getId());

        assertEquals("Duplicate Information Model CE is being created", 1, infoDao.getAll().size());


    }

}
