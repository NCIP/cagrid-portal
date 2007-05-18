/**
 * 
 */
package gov.nih.nci.cagrid.portal2.dao;

import java.io.File;
import java.io.FileInputStream;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.support.OpenSessionInViewInterceptor;
import org.springframework.web.context.request.WebRequest;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public abstract class AbstractDaoTest extends DBTestCase {
	
	protected final Log logger = LogFactory.getLog(getClass());
    protected WebRequest webRequest = new StubWebRequest();
    private boolean shouldFlush = true;
	private ApplicationContext ctx;
	private String dataFilePrefix;

	/**
	 * 
	 */
	public AbstractDaoTest() {
		init();
	}

	/**
	 * @param arg0
	 */
	public AbstractDaoTest(String arg0) {
		super(arg0);
		init();
	}

	public ApplicationContext getApplicationContext() {
		return this.ctx;
	}

	protected String[] getConfigLocations() {
		return new String[] { "classpath:applicationContext-*.xml" };
	}

	protected DatabaseOperation getSetUpOperation() throws Exception {
		return DatabaseOperation.INSERT;
	}

	protected DatabaseOperation getTearDownOperation() throws Exception {
		return DatabaseOperation.DELETE;
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		String fileName = getDataSetFileName();
		File testFile = new File(fileName);
		if (!testFile.exists()) {
			throw new RuntimeException(fileName + " not found.");
		}

		return new FlatXmlDataSet(new FileInputStream(testFile));
	}

	protected abstract String getDataSetFileName();

	private void init() {

		this.dataFilePrefix = System.getProperty("data.file.prefix", "");
		
		Properties props = new Properties();
		try {
			props.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("hibernate.properties"));
		} catch (Exception ex) {
			throw new RuntimeException("Error loading properties: "
					+ ex.getMessage(), ex);
		}

		String driver = props.getProperty("hibernate.connection.driver_class",
				"com.mysql.jdbc.Driver");
		String url = props.getProperty("hibernate.connection.url",
				"jdbc:mysql://localhost:3306/testapp");
		String usr = props.getProperty("hibernate.connection.username",
				"testapp");
		String pwd = props.getProperty("hibernate.connection.password",
				"testapp");

		System.setProperty(
				PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, driver);
		System.setProperty(
				PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, url);
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME,
				usr);
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD,
				pwd);

		this.ctx = new ClassPathXmlApplicationContext(getConfigLocations());
	}
	
	protected String getDataSetFileName(String path){
		return this.dataFilePrefix + path;
	}
	
	public void setUp() throws Exception{
		try {
			HibernateTransactionManager txMgr = (HibernateTransactionManager) this.ctx
					.getBean("transactionManager");
			Session session = SessionFactoryUtils.getSession(txMgr
					.getSessionFactory(), true);
			if (session != null) {
				session.clear();
			}
			LocalSessionFactoryBean sessionFactoryBean = (LocalSessionFactoryBean) this.ctx
					.getBean("&sessionFactory");
			sessionFactoryBean.createDatabaseSchema();
			beginSession();
		} catch (Exception ex) {
			throw new RuntimeException("Error encountered: " + ex.getMessage(),
					ex);
		}
		super.setUp();
	}
	
	public void tearDown() throws Exception{
		super.tearDown();
		try {
			endSession();
			HibernateTransactionManager txMgr = (HibernateTransactionManager) this.ctx
					.getBean("transactionManager");
			Session session = SessionFactoryUtils.getSession(txMgr
					.getSessionFactory(), true);
			if (session != null) {
				session.clear();
			}
			LocalSessionFactoryBean sessionFactoryBean = (LocalSessionFactoryBean) this.ctx
					.getBean("&sessionFactory");
			sessionFactoryBean.dropDatabaseSchema();
		} catch (Exception ex) {
			throw new RuntimeException("Error encountered: " + ex.getMessage(),
					ex);
		}
	}
	

    private void beginSession() {
        for (OpenSessionInViewInterceptor interceptor : interceptors()) {
            interceptor.preHandle(webRequest);
        }
    }

    private void endSession() {
        for (OpenSessionInViewInterceptor interceptor : reverseInterceptors()) {
            if (shouldFlush) {
                interceptor.postHandle(webRequest, null);
            }
            interceptor.afterCompletion(webRequest, null);
        }
    }

    protected void interruptSession() {
        endSession();
        beginSession();
    }

    private List<OpenSessionInViewInterceptor> interceptors() {
        return Arrays.asList(
            (OpenSessionInViewInterceptor) getApplicationContext().getBean("openSessionInViewInterceptor")
        );
    }

    private List<OpenSessionInViewInterceptor> reverseInterceptors() {
        List<OpenSessionInViewInterceptor> interceptors = interceptors();
        Collections.reverse(interceptors);
        return interceptors;
    }
    
    private static class StubWebRequest implements WebRequest {
        public String getParameter(String paramName) {
            return null;
        }

        public String[] getParameterValues(String paramName) {
            return null;
        }


        public Object getAttribute(String name, int scope) {
            return null;
        }

        public void setAttribute(String name, Object value, int scope) {
        }

        public void removeAttribute(String name, int scope) {
        }

        public void registerDestructionCallback(String name, Runnable callback, int scope) {
        }

        public String getSessionId() {
            return null;
        }

        public Object getSessionMutex() {
            return null;
        }

		public Locale getLocale() {
			return null;
		}

		public Map getParameterMap() {
			return null;
		}

		public String getContextPath() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getRemoteUser() {
			// TODO Auto-generated method stub
			return null;
		}

		public Principal getUserPrincipal() {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean isSecure() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isUserInRole(String arg0) {
			// TODO Auto-generated method stub
			return false;
		}
    }
	
}
