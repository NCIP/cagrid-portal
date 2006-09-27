package gov.nih.nci.cagrid.portal.utils;

import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.portal.BaseSpringAbstractTestCase;
import gov.nih.nci.cagrid.portal.domain.ResearchCenter;
import gov.nih.nci.cagrid.portal.exception.MetadataRetreivalException;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 25, 2006
 * Time: 6:02:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class GridUtilsTestCase extends BaseSpringAbstractTestCase {

    public void testMetadataRetreival() {

        try {
            EndpointReferenceType epr = GridUtils.getEPR("http://cagrid04.bmi.ohio-state.edu:7080/wsrf/services/cagrid/CaDSRService");
            assertNotNull(epr);
            ServiceMetadata sMetadata = GridUtils.getServiceMetadata(epr);
            assertNotNull(epr);

            MetadataAggregatorUtils mUtils = new MetadataAggregatorUtils();
            ResearchCenter rc = mUtils.loadRC(sMetadata);
            assertNotNull(rc.getShortName());
        } catch (URI.MalformedURIException e) {
            fail(e.getMessage());
        } catch (MetadataRetreivalException e) {
            fail(e.getMessage());
        }

    }


}
