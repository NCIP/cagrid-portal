package gov.nih.nci.cagrid.portal.manager;


import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;
import gov.nih.nci.cagrid.portal.BaseSpringDataAccessAbstractTestCase;
import gov.nih.nci.cagrid.portal.domain.DomainModel;
import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.domain.ResearchCenter;
import gov.nih.nci.cagrid.portal.exception.MetadataRetreivalException;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import gov.nih.nci.cagrid.portal.utils.GridUtils;
import gov.nih.nci.cagrid.portal.utils.MetadataAggregatorUtils;
import org.apache.axis.types.URI;
import org.springframework.core.io.ClassPathResource;

import java.io.FileReader;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 23, 2006
 * Time: 11:14:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class RegisteredServiceLocalTestCase extends BaseSpringDataAccessAbstractTestCase {

    private GridServiceManager gridServiceManager;
    private BaseManagerImpl baseManager;
    private static final String DOMAIN_XML = "domainModel.xml";
    private static final String SERVICE_XML = "serviceMetadata.xml";

    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();    //To change body of overridden methods use File | Settings | File Templates.
    }


    public void testgisteredServiceWithModel() {

        MetadataAggregatorUtils mUtils = new MetadataAggregatorUtils();

        try {
            RegisteredService rService = new RegisteredService("http://test");
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
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    public void testRecusive() {
        for (int i = 0; i < 4; i++) {
            testRegisteredServiceWithOperations();
        }
    }

    public void testRegisteredServiceWithOperations() {
        MetadataAggregatorUtils mUtils = new MetadataAggregatorUtils();

        try {
            RegisteredService rService = new RegisteredService("http://test");
            org.springframework.core.io.Resource resource = new ClassPathResource(SERVICE_XML);
            gov.nih.nci.cagrid.metadata.ServiceMetadata mData = MetadataUtils.deserializeServiceMetadata(new FileReader(resource.getFile()));
            ResearchCenter rc = mUtils.loadRC(MetadataUtils.deserializeServiceMetadata(new FileReader(resource.getFile())));

            rService.setResearchCenter(rc);

            mUtils.loadOperations(rService, mData);

            gridServiceManager.save(rService);


            setComplete();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    public void testRealService() {
        RegisteredService service = null;
        try {

            try {
                service = new RegisteredService("https://cagrid02.bmi.ohio-state.edu:8442/wsrf/services/cagrid/SyncGTS");
            } catch (URI.MalformedURIException e) {
                fail(e.getMessage());
            }
            MetadataAggregatorUtils aggrUtil = new MetadataAggregatorUtils();
            ServiceMetadata mData = GridUtils.getServiceMetadata(service.getHandle());
            ResearchCenter domainRC = aggrUtil.loadRC(mData);

            service.setResearchCenter(domainRC);
            aggrUtil.loadOperations(service, mData);
        } catch (MetadataRetreivalException e) {
            e.printStackTrace();
        } catch (ResourcePropertyRetrievalException e) {
            e.printStackTrace();
        }

        try {
            gridServiceManager.save(service);
            setComplete();
        } catch (PortalRuntimeException e) {
            fail(e.getMessage());
        }


    }


    public void setGridServiceManager(GridServiceManager gridServiceManager) {
        this.gridServiceManager = gridServiceManager;
    }
}
