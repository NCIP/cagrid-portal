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
package gov.nih.nci.cagrid.portal.portlet.latestcontent;

import gov.nih.nci.cagrid.portal.portlet.TestPortletContextClassBase;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class LatestContextContext extends TestPortletContextClassBase {
    public String[] getConfigLocations() {
        return new String[]{"classpath*:latestContent-portlet.xml", "classpath*:common.xml",
                "classpath*:applicationContext-db.xml", "classpath*:applicationContext-service.xml",
                "classpath*:applicationContext-security.xml"};
    }

}


