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
package gov.nih.nci.cagrid.portal;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
public abstract class AbstractDBTestBase {

    protected MockHttpServletRequest request = new MockHttpServletRequest();
    protected MockHttpServletResponse response = new MockHttpServletResponse();
    protected WebRequest webRequest = new ServletWebRequest(request);
    org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(getClass().getSimpleName());
    private boolean shouldFlush = true;

    public ApplicationContext getApplicationContext() {
          return TestDB.getApplicationContext();
      }

    @Before
    public void begin() throws Exception {
        TestDB.create();
        beginSession();
    }

    @After  
    public void end() throws Exception {
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
