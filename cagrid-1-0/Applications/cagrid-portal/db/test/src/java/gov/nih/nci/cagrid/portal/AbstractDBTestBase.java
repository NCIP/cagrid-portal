package gov.nih.nci.cagrid.portal;

import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.orm.hibernate3.HibernateJdbcException;
import org.springframework.orm.hibernate3.support.OpenSessionInViewInterceptor;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public abstract class AbstractDBTestBase extends TestCase {

    protected MockHttpServletRequest request = new MockHttpServletRequest();
    protected MockHttpServletResponse response = new MockHttpServletResponse();
    protected WebRequest webRequest = new ServletWebRequest(request);
    org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(getClass().getSimpleName());
    private boolean shouldFlush = true;

    public ApplicationContext getApplicationContext() {
          return TestDB.getApplicationContext();
      }



    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TestDB.create();
        beginSession();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();    //To change body of overridden methods use File | Settings | File Templates.
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
