package gov.nih.nci.cagrid.portal;

import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.portal.domain.DomainModel;
import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.domain.ResearchCenter;
import gov.nih.nci.cagrid.portal.manager.GridServiceManager;
import gov.nih.nci.cagrid.portal.utils.MetadataAggregatorUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.FileReader;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Nov 2, 2006
 * Time: 10:03:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class InsertDataService extends BaseSpringDataAccessAbstractTestCase {
    private GridServiceManager gridServiceManager;

    private static final String DOMAIN_XML = "domainModel.xml";
    private static final String SERVICE_XML = "serviceMetadata.xml";

    public void testgisteredServiceWithModel() {


        MetadataAggregatorUtils mUtils = new MetadataAggregatorUtils();

        try {
            RegisteredService rService = new RegisteredService("http://nci.nih.gov/Test");
            rService.setName("Test Service");
            rService.setDescription("Portal Test Service");
            org.springframework.core.io.Resource resource = new ClassPathResource(DOMAIN_XML);

            try {
                DomainModel domainModel = mUtils.loadDomainModel(MetadataUtils.deserializeDomainModel(new FileReader(resource.getFile())));

                rService.setObjectModel(domainModel);
            } catch (Exception e) {
                fail(e.getMessage());
            }

            //Add RC
            try {
                org.springframework.core.io.Resource sMetaDataResource = new ClassPathResource(SERVICE_XML);
                ResearchCenter rc = mUtils.loadRC(MetadataUtils.deserializeServiceMetadata(new FileReader(sMetaDataResource.getFile())));

                rService.setResearchCenter(rc);

            } catch (Exception e) {
                fail(e.getMessage());
            }


            gridServiceManager.save(rService);

            RegisteredService newService = (RegisteredService) gridServiceManager.getObjectByPrimaryKey(RegisteredService.class, rService.getPk());
            assertEquals(rService.getEPR(), newService.getEPR());


            setComplete();
        } catch (Exception e) {
            fail(e.getMessage());
        }


    }

    public void setGridServiceManager(GridServiceManager gridServiceManager) {
        this.gridServiceManager = gridServiceManager;
    }
}
