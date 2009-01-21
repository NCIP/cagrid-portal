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

    public void testFilterBannedServices() {
        GridService svc1 = new GridService();
        svc1.setUrl("http://one");
        StatusChange status = new StatusChange();
        status.setTime(new Date());
        status.setStatus(ServiceStatus.ACTIVE);
        status.setService(svc1);
        svc1.getStatusHistory().add(status);

        GridService svc2 = new GridService();
        svc2.setUrl("http://two");
        status = new StatusChange();
        status.setTime(new Date());
        status.setStatus(ServiceStatus.BANNED);
        status.setService(svc2);
        svc2.getStatusHistory().add(status);

        List<GridService> svcs = new ArrayList<GridService>();
        svcs.add(svc1);
        svcs.add(svc2);

        svcs = BaseServiceFilter.filterBannedServices(svcs);
        assertTrue("Should have only one service in list", svcs.size() == 1);
        GridService svc = svcs.get(0);
        assertEquals("Filtered wrong service", "http://one", svc.getUrl());
    }

    public void testFilterDormantServices() {
        GridService svc1 = new GridService();
        svc1.setUrl("http://one");
        StatusChange status = new StatusChange();
        status.setTime(new Date());
        status.setStatus(ServiceStatus.ACTIVE);
        status.setService(svc1);
        svc1.getStatusHistory().add(status);

        GridService svc2 = new GridService();
        svc2.setUrl("http://two");
        status = new StatusChange();
        status.setTime(new Date());
        status.setStatus(ServiceStatus.DORMANT);
        status.setService(svc2);
        svc2.getStatusHistory().add(status);

        List<GridService> svcs = new ArrayList<GridService>();
        svcs.add(svc1);
        svcs.add(svc2);

        svcs = BaseServiceFilter.filterDormantServices(svcs);
        assertTrue("Should have only one service in list", svcs.size() == 1);
        GridService svc = svcs.get(0);
        assertEquals("Filtered wrong service", "http://one", svc.getUrl());
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

        assertTrue(BaseServiceFilter.filterServicesByInvalidMetadata(svcs).size() == 1);

        Service desc = new Service();
        mData.setServiceDescription(desc);
        assertTrue(BaseServiceFilter.filterServicesByInvalidMetadata(svcs).size() == 0);

        desc.setName("dummyService");
        assertTrue(BaseServiceFilter.filterServicesByInvalidMetadata(svcs).size() == 1);

    }


}
