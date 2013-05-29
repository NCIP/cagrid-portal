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
package gov.nih.nci.cagrid.portal.portlet;

import gov.nih.nci.cagrid.portal.TestDB;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.*;
import org.springframework.test.AbstractSingleSpringContextTests;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.RequestContextFilter;

import javax.servlet.FilterChain;


/**
 * Base class for all integration test
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public abstract class PortletIntegrationTestBase extends AbstractSingleSpringContextTests {

    private static MockServletContext context;

    private MockHttpServletResponse response;

    private MockHttpServletRequest request;

    private static MockHttpSession session;

    private static RequestContextFilter contextFilter;

    static {

        context = new MockServletContext();

        MockFilterConfig filterConfig = new MockFilterConfig(context,
                "filterConfig");

        contextFilter = new RequestContextFilter();


        session = new MockHttpSession(context);
    }

    @Override
    protected void onSetUp() throws Exception {
        super.onSetUp();

        TestDB.create();

        // new request/response for every test method
        request = new MockHttpServletRequest(context);
        request.setSession(session);
        response = new MockHttpServletResponse();
    }

    @Override
    protected ConfigurableApplicationContext loadContextLocations(
            String[] locations) throws Exception {

        if (logger.isInfoEnabled()) {
            logger.info("Loading web application context for: "
                    + StringUtils.arrayToCommaDelimitedString(locations));
        }

        XmlWebApplicationContext ctx = new XmlWebApplicationContext();
        ctx.setConfigLocations(locations);
        ctx.setServletContext(context);
        ctx.refresh();
        return ctx;
    }

    public final class FilterTest {

        private FilterChain chain;

        public FilterTest(FilterChain chain) {
            this.chain = chain;
        }

        // execute the filter
        public final void run() throws Exception {
            contextFilter.doFilter(request, response, chain);
        }
    }


    protected void onTearDown() throws Exception {
        TestDB.drop();
    }


}
