/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.portlet.discovery.filter;

import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.domain.StatusChange;
import gov.nih.nci.cagrid.portal.domain.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.portal.domain.metadata.service.Service;
import gov.nih.nci.cagrid.portal.util.filter.BaseServiceFilter;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class BaseServiceFilterTest extends TestCase {

    GridService svc1, svc2;
    List<GridService> svcs;

    @Override
    protected void setUp() throws Exception {
        svc1 = new GridService();
        svc1.setUrl("http://one");
        StatusChange _status = new StatusChange();
        _status.setTime(new Date());
        _status.setStatus(ServiceStatus.ACTIVE);
        _status.setService(svc1);
        svc1.getStatusHistory().add(_status);

        svc2 = new GridService();
        svc2.setUrl("http://two");

        svc2.getStatusHistory().add(_status);
        svcs = new ArrayList<GridService>();
        svcs.add(svc1);
        svcs.add(svc2);

    }

    public void testFilterBannedServices() {

        StatusChange status = new StatusChange();
        status.setTime(new Date());
        status.setStatus(ServiceStatus.BANNED);
        status.setService(svc2);
        svc2.getStatusHistory().add(status);

        assertEquals("Should have only one service in list", 1, BaseServiceFilter.filterBannedServices(svcs).size());
        assertEquals("Filtered wrong service", "http://one", svcs.get(0).getUrl());
    }

    public void testFilterDormantServices() {

        StatusChange status = new StatusChange();
        status.setTime(new Date());
        status.setStatus(ServiceStatus.DORMANT);
        status.setService(svc2);
        svc2.getStatusHistory().add(status);

        assertEquals("Should have only one service in list", 1, BaseServiceFilter.filterDormantServices(svcs).size());
        assertEquals("Filtered wrong service", "http://one", svcs.get(0).getUrl());
    }

    public void testFilterServicesByInvalidMetadata() {

        assertEquals(0, BaseServiceFilter.filterServicesByInvalidMetadata(svcs).size());

        ServiceMetadata mData = new ServiceMetadata();
        mData.setServiceDescription(null);
        svc1.setServiceMetadata(mData);

        assertEquals(0, BaseServiceFilter.filterServicesByInvalidMetadata(svcs).size());

        Service desc = new Service();
        mData.setServiceDescription(desc);
        assertEquals(1, BaseServiceFilter.filterServicesByInvalidMetadata(svcs).size());

        desc.setName("dummyService");
        assertEquals(1, BaseServiceFilter.filterServicesByInvalidMetadata(svcs).size());

    }

    public void testFilterServicesByStatus() {
        List<GridService> svcs = new ArrayList<GridService>();
        svcs.add(svc1);
        svcs.add(svc2);

        StatusChange status = new StatusChange();
        status.setTime(new Date());
        status.setStatus(ServiceStatus.DORMANT);
        status.setService(svc2);
        svc2.getStatusHistory().add(status);


        assertEquals("Service 1 is Active. Should not be filtered",
                1, BaseServiceFilter.filterServicesByStatus(svcs, ServiceStatus.BANNED, ServiceStatus.DORMANT).size());

        StatusChange status2 = new StatusChange();
        status2.setTime(new Date());
        status2.setStatus(ServiceStatus.BANNED);
        status2.setService(svc1);
        svc1.getStatusHistory().add(status2);

        assertEquals("All Services should be filtered",
                0, BaseServiceFilter.filterServicesByStatus(svcs, ServiceStatus.BANNED, ServiceStatus.DORMANT).size());

    }
}
