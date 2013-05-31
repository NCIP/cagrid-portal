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
package gov.nih.nci.cagrid.portal.portlet;

import gov.nih.nci.cagrid.portal.TestDB;
import org.springframework.mock.web.portlet.MockPortletRequest;
import org.springframework.mock.web.portlet.MockPortletResponse;
import org.springframework.orm.hibernate3.HibernateJdbcException;
import org.springframework.orm.hibernate3.support.OpenSessionInViewInterceptor;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.web.portlet.context.PortletWebRequest;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PortalPortletIntegrationTestBase extends AbstractDependencyInjectionSpringContextTests {

    public PortalPortletIntegrationTestBase() {
        super();
        setPopulateProtectedVariables(true);
    }

    protected Object getBean(String beanName) {
        return getApplicationContext().getBean(beanName);
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[]{
                "classpath*:applicationContext-aggr.xml",
                "applicationContext-db.xml",
                "applicationContext-service.xml",
                "common.xml",
                "applicationContext-security.xml",
                "browse-portlet.xml",
                "facades.xml",
                "query-portlet.xml",
                "greeting-portlet.xml",
                "applicationContext-portal-search.xml"
        };
    }


    protected MockPortletRequest request = new MockPortletRequest();
    protected MockPortletResponse response = new MockPortletResponse();
    protected PortletWebRequest webRequest = new PortletWebRequest(request);
    org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(getClass().getSimpleName());
    private boolean shouldFlush = true;


    @Override
    protected void onSetUp() throws Exception {
        TestDB.create();
        beginSession();
    }


    @Override
    protected void onTearDown() throws Exception {
        TestDB.drop();
        endSession();
    }


    protected void interruptSession() {
        endSession();
        logger.info("-- interrupted DaoTestCase session --");
        beginSession();
    }

    private void beginSession() {
        logger.info("-- beginning DaoTestCase interceptor session --");
        findOpenSessionInViewInterceptor().preHandle(webRequest);
    }

    private void endSession() {
        logger.info("--    ending DaoTestCase interceptor session --");
        OpenSessionInViewInterceptor interceptor = findOpenSessionInViewInterceptor();
        if (shouldFlush) {
            try {
                interceptor.postHandle(webRequest, null);
            }
            catch (Exception exception) {
                if (exception instanceof HibernateJdbcException) {
                    if (exception != null) {
                        System.out.println(exception); // Log the exception
                        // Get cause if present
                        Throwable t = ((HibernateJdbcException) exception).getRootCause();

                        while (t != null) {
                            System.out.println("*************Cause: " + t);
                            t = t.getCause();
                            // procees to the next exception
                            //  exception = ((SQLGrammarException)exception).get
                        }

                    }
                }
            }
        }
        interceptor.afterCompletion(webRequest, null);

    }

    private OpenSessionInViewInterceptor findOpenSessionInViewInterceptor() {
        return (OpenSessionInViewInterceptor) getApplicationContext().getBean("openSessionInViewForTesting");
    }


}
 
