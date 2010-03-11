package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.portal.aggr.MetadataThread;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class MetadataUtils {


    public Metadata getMetadata(String serviceUrl, long timeout) {
        Metadata meta = new Metadata();
        MetadataThread t = new MetadataThread(serviceUrl);
        t.start();
        try {
            t.join(timeout);
        } catch (InterruptedException ex) {
            throw new RuntimeException("Metadata thread interrupted");
        }
        if (t.getEx() != null) {
            throw new RuntimeException("Metadata thread encountered error: "
                    + t.getEx().getMessage(), t.getEx());
        } else if (!t.isFinished()) {
            throw new RuntimeException("Metadata query to " + serviceUrl
                    + " timed out.");
        }
        meta.smeta = t.getServiceMetadata();
        meta.dmodel = t.getDomainModel();

        return meta;
    }

}
