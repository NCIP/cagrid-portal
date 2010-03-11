package gov.nih.nci.cagrid.portal.catalog;

import gov.nih.nci.cagrid.portal.TestDB;
import gov.nih.nci.cagrid.portal.aggr.metachange.AbstractMetadataChangeTestBase;
import gov.nih.nci.cagrid.portal.util.DefaultCatalogEntryRelationshipTypesFactory;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class AbstractServiceMetadataCatalogEntryBuilderTest extends
        AbstractMetadataChangeTestBase {

    @Override
    protected String[] getConfigLocations() {
        return new String[]{"applicationContext-db.xml",
                "applicationContext-db-relationships.xml",
                "applicationContext-service.xml",
                "applicationContext-aggr-catalog.xml",
                "applicationContext-aggr.xml"};
    }

    @Before
    public void initRelationships() {
        DefaultCatalogEntryRelationshipTypesFactory b = (DefaultCatalogEntryRelationshipTypesFactory) TestDB
                .getApplicationContext().getBean(
                        "defaultCatalogEntryRelationshipTypesFactory");
        b.init();
    }

    public ApplicationContext getApplicationContext() {
        return new ClassPathXmlApplicationContext(getConfigLocations());
    }

}