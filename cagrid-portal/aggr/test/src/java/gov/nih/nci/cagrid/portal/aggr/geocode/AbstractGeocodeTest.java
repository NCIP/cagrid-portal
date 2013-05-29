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
package gov.nih.nci.cagrid.portal.aggr.geocode;

import gov.nih.nci.cagrid.portal.util.PortalAggrIntegrationTestBase;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class AbstractGeocodeTest extends PortalAggrIntegrationTestBase {


    @Override
    protected String[] getConfigLocations() {
        return new String[]{
                "classpath*:applicationContext-aggr-geocode.xml",
                "applicationContext-db.xml"
        };
    }

}
