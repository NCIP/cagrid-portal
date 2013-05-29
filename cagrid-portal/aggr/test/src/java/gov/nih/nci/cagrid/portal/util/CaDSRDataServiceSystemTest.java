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

import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.DomainModel;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CaDSRDataServiceSystemTest extends PortalAggrIntegrationTestBase{


    private CaDSRClient caDSRClient;

    public void testContext() throws Exception {
        DomainModel model = new DomainModel();
        model.setProjectShortName("caTIES");
        model.setProjectVersion("3.2");
        try {
            assertNotNull(caDSRClient.getContext(model));
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    public CaDSRClient getCaDSRClient() {
        return caDSRClient;
    }

    public void setCaDSRClient(CaDSRClient caDSRClient) {
        this.caDSRClient = caDSRClient;
    }
}
 
