package gov.nih.nci.cagrid.portal.util;

import java.util.Date;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public interface TimestampProvider {

    public void createTimestamp();

    public Date getTimestamp();
}
