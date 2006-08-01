package gov.nih.nci.cagrid.portal.tests;

import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.utils.GridUtils;
import junit.framework.TestCase;
import org.apache.axis.types.URI;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jul 26, 2006
 * Time: 5:33:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegisteredServiceLocalTest extends TestCase {
    public void testCreateRegisteredService() {
        try {
            RegisteredService service = new RegisteredService(GridUtils.getEPR(
                        "http://cagrid04.bmi.ohio-state.edu:7080/wsrf/services/cagrid/CaDSRService"));
            assertNotNull(service);

            IndexService idx = new IndexService(GridUtils.getEPR(
                        "http://cagrid01.bmi.ohio-state.edu:8080/wsrf/services/DefaultIndexService"));
            assertNotNull(idx);
        } catch (URI.MalformedURIException e) {
            fail(e.getMessage());
        }
    }
}
