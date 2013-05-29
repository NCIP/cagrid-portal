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
package gov.nih.nci.cagrid.portal.sample;

import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;
import gov.nih.nci.cagrid.portal.portlet.PortletIntegrationTestBase;
import gov.nih.nci.cagrid.portal.portlet.query.QueryService;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ExecuteQueryTest extends PortletIntegrationTestBase {


    protected String[] getConfigLocations() {
        return new String[]{
                "classpath*:applicationContext-aggr.xml",
                "applicationContext-db.xml",
                "applicationContext-service.xml",
                "applicationContext-security.xml",
                "common.xml"

        };
    }

    @Test
    public void testScopedBean() throws Exception {

        new FilterTest(new FilterChain() {

            public void doFilter(ServletRequest arg0, ServletResponse arg1)
                    throws IOException, ServletException {
                QueryService ser = (QueryService) applicationContext.getBean("queryService");
                QueryInstance instance = ser.submitQuery("<xml/>", "http://");
                assertNotNull(instance);
            }

        }).run();

    }


}
