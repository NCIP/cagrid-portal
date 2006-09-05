package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.portal.BaseSpringDataAccessAbstractTestCase;
import gov.nih.nci.cagrid.portal.domain.DomainModel;
import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.domain.ResearchCenter;
import gov.nih.nci.cagrid.portal.domain.UMLClass;

import java.io.FileReader;
import java.util.List;

import org.springframework.core.io.ClassPathResource;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 23, 2006
 * Time: 11:14:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class RegisteredServiceLocalTestCase extends BaseSpringDataAccessAbstractTestCase {

    private GridServiceManager gridServiceManager;
    private static final String DOMAIN_XML = "domainModel.xml";
    private static final String SERVICE_XML = "serviceMetadata.xml";
    private RegisteredService rService;


    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();    //To change body of overridden methods use File | Settings | File Templates.
        rService = new RegisteredService("http://test");
    }


    public void testRegisteredServiceWithModel() {
        try {
            org.springframework.core.io.Resource resource = new ClassPathResource(DOMAIN_XML);

            gov.nih.nci.cagrid.metadata.dataservice.DomainModel dModel = MetadataUtils.deserializeDomainModel(new FileReader(resource.getFile()));


            DomainModel modelDomain = new DomainModel();
            modelDomain.setLongName(dModel.getProjectLongName());
            modelDomain.setProjectShortName(dModel.getProjectShortName());
            modelDomain.setProjectDescription(dModel.getProjectDescription());
            modelDomain.setProjectVersion(dModel.getProjectVersion());
            gov.nih.nci.cagrid.metadata.common.UMLClass classes[] = dModel.getExposedUMLClassCollection().getUMLClass();

            for (int i = 0; i < classes.length; i++) {
                UMLClass dClass = new UMLClass();
                dClass.setClassName(classes[i].getClassName());
                modelDomain.getUmlClassCollection().add(dClass);
            }

            rService.setDomainModel(modelDomain);
            modelDomain.setRegisteredService(rService);

            //Add RC
            ResearchCenter rc = new ResearchCenter();
            rc.setDisplayName("Test");
            rService.setResearchCenter(rc);

            gridServiceManager.save(rService);

            List services = gridServiceManager.loadAll(RegisteredService.class);
            assertNotNull(services);


        } catch (Exception e) {

            fail(e.getMessage());
        }
        setComplete();
    }


    public void testRegisteredServiceWithOperations() {

        try {
            org.springframework.core.io.Resource resource = new ClassPathResource(SERVICE_XML);

            gov.nih.nci.cagrid.metadata.ServiceMetadata mData = MetadataUtils.deserializeServiceMetadata(new FileReader(resource.getFile()));
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }


    public void setGridServiceManager(GridServiceManager gridServiceManager) {
        this.gridServiceManager = gridServiceManager;
    }
}
