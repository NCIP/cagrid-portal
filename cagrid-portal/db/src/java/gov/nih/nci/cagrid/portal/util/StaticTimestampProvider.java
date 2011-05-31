package gov.nih.nci.cagrid.portal.util;

import java.util.Date;

/**
 * User: kherm
 * <p/>
 * Creates current timestamps when requested.
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class StaticTimestampProvider implements TimestampProvider {

    public void createTimestamp() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Date getTimestamp() {
        return new Date();
    }
}
