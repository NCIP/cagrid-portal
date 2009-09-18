package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.portal.TestDB;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.orm.hibernate3.HibernateJdbcException;
import org.springframework.orm.hibernate3.support.OpenSessionInViewInterceptor;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PortalAggrIntegrationTestBase extends AbstractDependencyInjectionSpringContextTests {

    public PortalAggrIntegrationTestBase() {
        super();
        setPopulateProtectedVariables(true);
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[]{
                "classpath*:applicationContext-aggr.xml",
                "applicationContext-db.xml"
        };
    }


    protected MockHttpServletRequest request = new MockHttpServletRequest();
    protected MockHttpServletResponse response = new MockHttpServletResponse();
    protected WebRequest webRequest = new ServletWebRequest(request);
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
