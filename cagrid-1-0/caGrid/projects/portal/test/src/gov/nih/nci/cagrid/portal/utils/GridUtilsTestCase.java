package gov.nih.nci.cagrid.portal.utils;

import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.portal.BaseSpringAbstractTestCase;
import gov.nih.nci.cagrid.portal.domain.ResearchCenter;
import gov.nih.nci.cagrid.portal.exception.MetadataRetreivalException;

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

            assertNotNull(validServiceEPR);
            ServiceMetadata sMetadata = GridUtils.getServiceMetadata(validServiceEPR);
            assertNotNull(validServiceEPR);

            MetadataAggregatorUtils mUtils = new MetadataAggregatorUtils();
            ResearchCenter rc = mUtils.loadRC(sMetadata);
            assertNotNull(rc.getShortName());
        } catch (MetadataRetreivalException e) {
            fail(e.getMessage());
        }

    }


}
