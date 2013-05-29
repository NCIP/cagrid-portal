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
