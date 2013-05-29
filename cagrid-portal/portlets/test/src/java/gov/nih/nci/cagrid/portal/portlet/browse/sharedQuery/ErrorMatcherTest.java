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
package gov.nih.nci.cagrid.portal.portlet.browse.sharedQuery;

import org.junit.Test;
import gov.nih.nci.cagrid.portal.portlet.query.results.ServiceErrorInterpretor;
import gov.nih.nci.cagrid.portal.portlet.PortalPortletIntegrationTestBase;
import gov.nih.nci.cagrid.portal.PortalTestUtils;
import gov.nih.nci.cagrid.data.client.DataServiceClient;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;

import java.util.List;
import java.io.StringReader;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ErrorMatcherTest extends PortalPortletIntegrationTestBase {
    public void testMatch() {
        List<ServiceErrorInterpretor> serviceErrorInterpretors = (List<ServiceErrorInterpretor>) getApplicationContext().getBean("serviceErrorInterpretors");

        String erroStr = "faultString: GSSException: Defective credential detected [Caused by: Proxy file (/tmp/x509up_u502) not found.]";

        String message = "";
        for (ServiceErrorInterpretor interpretor : serviceErrorInterpretors) {
            try {
                message = interpretor.getErrorMessage(erroStr);
                if (message != null) {
                    break;
                }
            } catch (Exception ex) {
                fail(ex.getMessage());
            }

        }
        assertEquals("You must be logged in in order to query this service.",message);


    }
}
