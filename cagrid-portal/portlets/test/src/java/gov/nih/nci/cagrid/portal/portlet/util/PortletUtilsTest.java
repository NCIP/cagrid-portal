/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.util;

import gov.nih.nci.cagrid.portal.PortalTestUtils;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.portal.domain.metadata.service.Service;
import gov.nih.nci.cagrid.portal.util.filter.BaseServiceFilter;
import static junit.framework.Assert.*;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 */
public class PortletUtilsTest {

    /**
     *
     */
    public PortletUtilsTest() {

    }


    @Test
    public void validate() {
        try {
            String dcqlXML = PortalTestUtils.readFileASString("test/data/microArrayLargeDataDCQL.xml");
            String cqlXML = PortalTestUtils.readFileASString("test/data/sampleCQL1.xml");

            assertFalse("Not a CQL query", PortletUtils.isCQL(dcqlXML));
            assertTrue("Is a DCQL Query", PortletUtils.isDCQL(dcqlXML));

            assertFalse("Not a DCQL Query", PortletUtils.isDCQL(cqlXML));
            assertTrue("Is a CQL Query", PortletUtils.isCQL(cqlXML));

        } catch (IOException e) {
            fail(e.getMessage());
        }

    }

    @Test
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
