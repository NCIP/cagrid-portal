/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

/**
 * Creates and mantains timestamps as instance variable
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class InMemoryTimestampProvider implements TimestampProvider {
    private static final Log logger = LogFactory
            .getLog(InMemoryTimestampProvider.class);

    private Date lastExecutedOn;

    public void createTimestamp() {
        logger.debug("Creating timestamp for" + this.getClass().getName());
        this.lastExecutedOn = new Date();
    }

    public Date getTimestamp() {
        if (lastExecutedOn == null)
            throw new RuntimeException("No timestamp created yet");
        return lastExecutedOn;
    }

}
