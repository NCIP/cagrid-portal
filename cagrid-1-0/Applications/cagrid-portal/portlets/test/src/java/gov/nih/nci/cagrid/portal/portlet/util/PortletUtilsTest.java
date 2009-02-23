/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.util;

import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.domain.StatusChange;
import gov.nih.nci.cagrid.portal.domain.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.portal.domain.metadata.service.Service;
import gov.nih.nci.cagrid.portal.portlet.discovery.filter.BaseServiceFilter;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 */
public class PortletUtilsTest extends TestCase {

    /**
     *
     */
    public PortletUtilsTest() {

    }


    public PortletUtilsTest(String name) {
        super(name);

    }




    public void testFilterInvalidMetadataServices() {
        GridService svc1 = new GridService();
        svc1.setUrl("http://one");

        List<GridService> svcs = new ArrayList<GridService>();
        svcs.add(svc1);

        assertTrue(BaseServiceFilter.filterServicesByInvalidMetadata(svcs).size() == 0);

        ServiceMetadata mData = new ServiceMetadata();
        mData.setServiceDescription(null);
        svc1.setServiceMetadata(mData);

        assertTrue(BaseServiceFilter.filterServicesByInvalidMetadata(svcs).size() == 0);

        Service desc = new Service();
        mData.setServiceDescription(desc);
        assertTrue(BaseServiceFilter.filterServicesByInvalidMetadata(svcs).size() == 1);

        desc.setName("dummyService");
        assertTrue(BaseServiceFilter.filterServicesByInvalidMetadata(svcs).size() == 1);

    }


}
