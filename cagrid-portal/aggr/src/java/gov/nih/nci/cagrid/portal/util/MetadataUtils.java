/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
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
