package gov.nih.nci.cagrid.portal.utils;

import gov.nih.nci.cagrid.portal.BaseSpringaAbstractTestCase;
import gov.nih.nci.cagrid.portal.exception.MetadataRetreivalException;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;

import java.util.Iterator;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jul 25, 2006
 * Time: 10:30:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class InitBeanTestCaseCase extends BaseSpringaAbstractTestCase {
    DatabaseInitUtility initBean;

    protected void onSetUp() throws Exception {
        super.onSetUp(); //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setInitBean(DatabaseInitUtility initBean) {
        this.initBean = initBean;
    }

    public void testNameRetreival() {
        for (Iterator iter = initBean.getIndexSet().iterator(); iter.hasNext();) {
            try {
                EndpointReferenceType epr = GridUtils.getEPR((String) iter.next());
                assertNotNull(epr);
                logger.debug("EPR: " + epr.toString());
                logger.debug("EPR Service Name: " + epr.getServiceName());

                try {
                    assertNotNull(GridUtils.getServiceName(epr));

                    assertNotNull(GridUtils.getServiceDescription(epr));
                } catch (MetadataRetreivalException e) {
                    //do nothing
                }
            } catch (URI.MalformedURIException e) {
                fail(e.getMessage());
            }
        }
    }
}
