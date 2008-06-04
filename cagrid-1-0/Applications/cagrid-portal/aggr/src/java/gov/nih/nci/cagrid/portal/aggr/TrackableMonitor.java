package gov.nih.nci.cagrid.portal.aggr;

import java.util.Date;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public interface TrackableMonitor {

    public Date getLastExecutedOn() throws RuntimeException;
}
