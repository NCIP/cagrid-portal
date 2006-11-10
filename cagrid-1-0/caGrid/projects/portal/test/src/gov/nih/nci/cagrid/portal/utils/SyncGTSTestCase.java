package gov.nih.nci.cagrid.portal.utils;

import gov.nih.nci.cagrid.portal.BaseSpringAbstractTest;
import gov.nih.nci.cagrid.portal.exception.PortalInitializationException;
import org.springframework.core.io.ClassPathResource;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 4, 2006
 * Time: 10:22:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class SyncGTSTestCase extends BaseSpringAbstractTest {
    public void testSync() {
        SyncGTSInitUtility syncBean = new SyncGTSInitUtility();

        org.springframework.core.io.Resource resource = new ClassPathResource(
                "sync-description.xml");
        syncBean.setSyncGTSDescriptionFile(resource);

        try {
            syncBean.afterPropertiesSet();
        } catch (PortalInitializationException e) {
            fail(e.getMessage());
        }
    }
}
