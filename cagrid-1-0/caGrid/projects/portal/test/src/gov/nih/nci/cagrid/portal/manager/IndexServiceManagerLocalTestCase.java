package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.portal.BaseSpringDataAccessAbstractTestCase;
import gov.nih.nci.cagrid.portal.exception.MetadataRetreivalException;
import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.utils.GridUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.common.ResearchCenter;

import org.apache.axis.message.addressing.EndpointReferenceType;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jul 31, 2006
 * Time: 10:30:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class IndexServiceManagerLocalTestCase
        extends BaseSpringDataAccessAbstractTestCase {
    GridServiceManager gridServiceManager;

    /**
     * WIll test storing index services
     * into the DB
     */
    public void testStoreIndex() {
        for (Iterator iter = rootIndexSet.iterator(); iter.hasNext();) {
            try {
                EndpointReferenceType idxService = GridUtils.getEPR((String) iter.next());

                IndexService idx = new IndexService(idxService);
                logger.debug("Storing index service using manager");
                gridServiceManager.save(idx);
            } catch (Exception e) {
                fail(e.getMessage());
            }
        }
    }

    /**
     * Mock insert services into index
     */
    public void testIndexServiceDAO() {
        List indexes = gridServiceManager.loadAll(IndexService.class);

        for (ListIterator iter = indexes.listIterator(); iter.hasNext();) {
            IndexService idx = (IndexService) iter.next();

            try {
                DiscoveryClient disc = new DiscoveryClient(idx.getEPR());
                EndpointReferenceType[] services = disc.getAllServices(true);

                logger.debug("Index service has " +
                        idx.getRegisteredServicesCollection().size() +
                        " registered services");

                for (int i = 0; i < services.length; i++) {
                    RegisteredService rService = new RegisteredService(services[i],
                            true);
                    logger.debug("name:" + rService.getName() +
                            " from GridUtils" +
                            GridUtils.getServiceName(rService.getHandle()));
                    logger.debug("desc:" + rService.getDescription());

                    rService.setIndex(idx);
                    gridServiceManager.save(rService);
                }

                logger.debug("New Index Service has " +
                        idx.getRegisteredServicesCollection().size() +
                        "regisgetered Services");
            } catch (Exception e) {
                fail(e.getMessage());
            }
        }


    }

    public void testMetadataRetreival(){
        for (Iterator iter = rootIndexSet.iterator(); iter.hasNext();) {
            EndpointReferenceType[] services = new EndpointReferenceType[0];
            try {
                EndpointReferenceType idxService = GridUtils.getEPR((String) iter.next());
                DiscoveryClient disc = new DiscoveryClient(idxService);
                services = disc.getAllServices(true);
            } catch (Exception e) {
                fail(e.getMessage());
            }

            for (int i = 0; i < services.length; i++) {
                try {
                    if(GridUtils.getServiceName(services[i]) != null){

                        ServiceMetadata mData = GridUtils.getServiceMetadata(services[i]);

                        ResearchCenter rc = mData.getHostingResearchCenter().getResearchCenter();
                        assertNotNull(rc.getDisplayName());
                        assertNotNull(rc.getShortName());
                        //assertNotNull(rc.getResearchCenterDescription().getDescription());
                    }
                } catch (MetadataRetreivalException e) {
                    //do nothing
                }
        }
        }
    }

            public void setIndexManager(GridServiceManager gridServiceManager) {
            this.gridServiceManager = gridServiceManager;
        }
        }
