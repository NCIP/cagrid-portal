/**
 * 
 */
package gov.nih.nci.cagrid.hibernate;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class OpenSessionInOperationFilter extends BasicHandler {

	private static final Log logger = LogFactory
			.getLog(OpenSessionInOperationFilter.class);
	private static final String SESSION_FACTORY_BEAN_NAME_PARAM = "sessionFactoryBeanName";
	private static final String DEFAULT_SESSION_FACTORY_BEAN_NAME = "sessionFactory";
	private static final String SINGLE_SESSION_PARAM = "singleSession";
	private static final String DEFAULT_SINGLE_SESSION = "true";

	/**
	 * 
	 */
	public OpenSessionInOperationFilter() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.axis.Handler#invoke(org.apache.axis.MessageContext)
	 */
	public void invoke(MessageContext messageContext) throws AxisFault {
		try {
			SessionFactory sessionFactory = lookupSessionFactory();
			Session session = null;
			boolean participate = false;

			boolean open = !messageContext.getPastPivot();
			if (open) {
				if (isSingleSession()) {
					// single session mode
					if (TransactionSynchronizationManager
							.hasResource(sessionFactory)) {
						// Do not modify the Session: just set the participate
						// flag.
						participate = true;
					} else {
						logger
								.debug("Opening single Hibernate Session in OpenSessionInOperationFilter");
						session = getSession(sessionFactory);
						TransactionSynchronizationManager.bindResource(
								sessionFactory, new SessionHolder(session));
					}
				} else {
					// deferred close mode
					if (SessionFactoryUtils
							.isDeferredCloseActive(sessionFactory)) {
						// Do not modify deferred close: just set the
						// participate
						// flag.
						participate = true;
					} else {
						SessionFactoryUtils.initDeferredClose(sessionFactory);
					}
				}
			} else {
				if (!participate) {
					if (isSingleSession()) {
						// single session mode
						TransactionSynchronizationManager
								.unbindResource(sessionFactory);
						logger
								.debug("Closing single Hibernate Session in OpenSessionInOperationFilter");
						closeSession(session, sessionFactory);
					} else {
						// deferred close mode
						SessionFactoryUtils
								.processDeferredClose(sessionFactory);

					}
				}
			}
		} catch (Exception ex) {
			String msg = "Error handling session: " + ex.getMessage();
			logger.error(msg, ex);
			throw new AxisFault(msg, ex);
		}
	}

	protected SessionFactory lookupSessionFactory() {

		WebApplicationContext wac = (WebApplicationContext) MessageContext
				.getCurrentContext()
				.getProperty(
						WebApplicationContextFilter.WEB_APPLICATION_CONTEXT_PROPERTY);
		return (SessionFactory) wac.getBean(getSessionFactoryBeanName());

	}

	public String getSessionFactoryBeanName() {
		return AxisUtils.getOption(SESSION_FACTORY_BEAN_NAME_PARAM,
				DEFAULT_SESSION_FACTORY_BEAN_NAME);
	}

	public boolean isSingleSession() {
		boolean singleSession = true;
		String value = null;
		try {
			value = AxisUtils.getOption(SINGLE_SESSION_PARAM,
					DEFAULT_SINGLE_SESSION);
			singleSession = Boolean.parseBoolean(value);
		} catch (Exception ex) {
			throw new RuntimeException("Bad value for " + SINGLE_SESSION_PARAM
					+ " parameter: '" + value + "'");
		}
		return singleSession;
	}

	protected Session getSession(SessionFactory sessionFactory)
			throws DataAccessResourceFailureException {
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		session.setFlushMode(FlushMode.NEVER);
		return session;
	}

	protected void closeSession(Session session, SessionFactory sessionFactory) {
		SessionFactoryUtils.closeSession(session);
	}

}
