package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.portal.BaseSpringDataAccessAbstractTestCase;
import gov.nih.nci.cagrid.portal.exception.MetadataRetreivalException;
import gov.nih.nci.cagrid.portal.utils.GridUtils;

import org.apache.axis.message.addressing.EndpointReferenceType;

import java.util.Iterator;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jul 20, 2006
 * Time: 2:31:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndexServiceDAOLocalTestCase
    extends BaseSpringDataAccessAbstractTestCase {
    // Dependency injected by spring
    IndexDAO indexDAO;

    /**
     * Root index should exist in DB
     */
    public void testIndexDAO() {
        Integer id = null;

        try {
            id = indexDAO.getID4EPR(
                    "http://cagrid01.bmi.ohio-state.edu:8080/wsrf/services/DefaultIndexService");
        } catch (Exception e) {
            fail("ID Not Found ");
        }

        logger.debug("ID Found in DB " + id);
        assertEquals(id.intValue(), 1);
    }

    /**
     * Get the list of ALL services in the index
     */
    public void testIndexServicesDiscovery() {
        for (Iterator iter = rootIndexSet.iterator(); iter.hasNext();) {
            EndpointReferenceType[] services = null;

            try {
                DiscoveryClient disc = new DiscoveryClient((String) iter.next());
                services = disc.getAllServices(false);
                logger.debug("Getting list of ALL services:\n");

                for (int i = 0; i < services.length; i++) {
                    logger.debug(services[i]);
                }
            } catch (Exception e) {
                fail(e.getMessage());
            }

            EndpointReferenceType[] caGridServices = null;

            try {
                DiscoveryClient disc = new DiscoveryClient((String) iter.next());
                caGridServices = disc.getAllServices(true);
            } catch (Exception e) {
                fail(e.getMessage());
            }

            logger.debug("Getting list of caGrid Compliant services:\n");

            for (int i = 0; i < caGridServices.length; i++) {
                logger.debug(caGridServices[i]);

                try {
                    logger.debug("Service name: " +
                        GridUtils.getServiceName(caGridServices[i]));
                    logger.debug("Service desc: " +
                        GridUtils.getServiceDescription(caGridServices[i]));
                } catch (MetadataRetreivalException e) {
                    // do nothing as this is expected
                }
            }
        }
    }

    public void setIndexDAO(IndexDAO indexDAO) {
        this.indexDAO = indexDAO;
    }
}
