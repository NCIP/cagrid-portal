package gov.nih.nci.cagrid.portal.utils;

import java.util.Iterator;

import gov.nih.nci.cagrid.portal.BaseSpringaAbstractTestCase;
import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import org.apache.axis.message.addressing.EndpointReferenceType;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 2, 2006
 * Time: 10:21:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class GridUtilsTestCaseCase extends BaseSpringaAbstractTestCase {

    /**
     * Test the GridUtils class in this method
     */
    public void testMetadatUtils() {

           for (Iterator iter = rootIndexSet.iterator(); iter.hasNext();) {

                  EndpointReferenceType[] services = null;


               try {
                   DiscoveryClient disc = new DiscoveryClient((String) iter.next());
                   services = disc.getAllServices(false);
               } catch (Exception e) {
                   fail(e.getMessage());
               }

               for (int i = 0; i < services.length; i++) {
                   assertNotNull(GridUtils.getServiceName(services[i]));
                   assertNotNull(GridUtils.getServiceDescription(services[i]));

               }
            }
    }
}
