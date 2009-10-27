/**
 *
 */
package gov.nih.nci.cagrid.portal.catalog;

import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.portal.TestDB;
import gov.nih.nci.cagrid.portal.aggr.catalog.ServiceMetadataCatalogEntryBuilder;
import gov.nih.nci.cagrid.portal.aggr.metachange.AbstractMetadataChangeTestBase;
import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryRelationshipInstanceDao;
import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryRoleInstanceDao;
import gov.nih.nci.cagrid.portal.dao.catalog.GridServiceEndPointCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRelationshipInstance;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRoleInstance;
import gov.nih.nci.cagrid.portal.domain.catalog.GridServiceEndPointCatalogEntry;
import gov.nih.nci.cagrid.portal.util.DefaultCatalogEntryRelationshipTypesFactory;
import gov.nih.nci.cagrid.portal.util.Metadata;
import static junit.framework.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author kherm manav.kher@semanticbits.com
 */
public class ServiceMetadataCatalogEntryBuilderTest extends
        AbstractMetadataChangeTestBase {

    protected String[] getConfigLocations() {
        return new String[]{"applicationContext-db.xml",
                "applicationContext-db-relationships.xml",
                "applicationContext-aggr.xml"};
    }

    @Before
    public void initRelationships() {
        DefaultCatalogEntryRelationshipTypesFactory b = (DefaultCatalogEntryRelationshipTypesFactory) TestDB
                .getApplicationContext().getBean(
                        "defaultCatalogEntryRelationshipTypesFactory");
        b.init();
    }

    @Test
    public void testBuild() throws Exception {
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

        ServiceMetadataCatalogEntryBuilder b = (ServiceMetadataCatalogEntryBuilder) TestDB
                .getApplicationContext().getBean(
                        "serviceMetadataCatalogEntryBuilder");

        final GridServiceEndPointCatalogEntry endpointCe = b.build(dataService);

        GridServiceEndPointCatalogEntryDao endpointDao = (GridServiceEndPointCatalogEntryDao) TestDB
                .getApplicationContext().getBean(
                        "gridServiceEndPointCatalogEntryDao");
        CatalogEntryRelationshipInstanceDao relInstDao = (CatalogEntryRelationshipInstanceDao) TestDB
                .getApplicationContext().getBean(
                        "catalogEntryRelationshipInstanceDao");
        CatalogEntryRoleInstanceDao roleInstDao = (CatalogEntryRoleInstanceDao) TestDB
                .getApplicationContext().getBean("catalogEntryRoleInstanceDao");

        List<CatalogEntryRelationshipInstance> relInsts = relInstDao
                .getRelationshipsForCatalogEntry(endpointCe.getId());
        assertTrue(relInsts.size() > 0);
        assertNotNull(endpointCe.getName());

        for (CatalogEntryRelationshipInstance rel : relInsts) {

            CatalogEntryRoleInstance roleA = rel.getRoleA();
            roleA.setRelationship(null);
            roleA.setCatalogEntry(null);
            roleInstDao.save(roleA);
            CatalogEntryRoleInstance roleB = rel.getRoleB();
            roleB.setRelationship(null);
            roleB.setCatalogEntry(null);
            roleInstDao.save(roleB);
            rel.setRoleA(null);
            rel.setRoleB(null);
            relInstDao.save(rel);

            roleInstDao.delete(roleA);
            roleInstDao.delete(roleB);
            relInstDao.delete(rel);
        }

        relInstDao.getHibernateTemplate().flush();

        Integer endpointId = endpointCe.getId();
        endpointDao.delete(endpointCe);

        relInsts = relInstDao.getRelationshipsForCatalogEntry(endpointId);
        assertTrue(relInsts.size() == 0);


        dataService.getServiceMetadata().getServiceDescription().setName("");
        // should throw an exception
        b.build(dataService);

        assertEquals("No CE should be created for service with no name", 0, endpointDao.getAll().size());

    }


}
