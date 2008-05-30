package gov.nih.nci.cagrid.portal.portlet.status;

import gov.nih.nci.cagrid.portal.aggr.TrackableMonitor;
import junit.framework.TestCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

import java.util.Date;


/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class StatusBeanTest extends TestCase {

    public void testLastUpdate() {
        StatusBean bean = new StatusBean();

        TrackableMonitor mockMonitor = mock(TrackableMonitor.class);
        bean.setMonitor(mockMonitor);

        stub(mockMonitor.getLastExecutedOn()).toReturn(new Date(110));
        assertTrue(bean.getLastUpdated().length() > 0);
        assertNotNull(bean.getLastUpdated());
        assertTrue(bean.getLastUpdated().indexOf("minutes") > -1);

        stub(mockMonitor.getLastExecutedOn()).toReturn(new Date());
        assertTrue(bean.getLastUpdated().length() > 0);
        assertNotNull(bean.getLastUpdated());
        assertTrue(bean.getLastUpdated().indexOf("seconds") > -1);


        stub(mockMonitor.getLastExecutedOn()).toThrow(new RuntimeException());
        System.out.println(bean.getLastUpdated());
        assertNotNull(bean.getLastUpdated());
        assertTrue(bean.getLastUpdated().length() == 0);


    }
}
