/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import gov.nih.nci.cagrid.portal.dao.CQLQueryInstanceDao;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstanceState;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultTable;
import gov.nih.nci.cagrid.portal.portlet.query.results.XMLQueryResultToQueryResultTableHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class DefaultCQLQueryInstanceListener implements
		CQLQueryInstanceListener, ApplicationContextAware {

	private static final Log logger = LogFactory
			.getLog(DefaultCQLQueryInstanceListener.class);

	private CQLQueryInstanceDao cqlQueryInstanceDao;
	private ApplicationContext applicationContext;

	/**
	 * 
	 */
	public DefaultCQLQueryInstanceListener() {

	}

	public void onCancelled(CQLQueryInstance instance, boolean cancelled) {
		if (!cancelled) {
			logger.warn("Couldn't cancel CQLQueryInstance:" + instance.getId());
		} else {
			instance.setState(QueryInstanceState.CANCELLED);
			getCqlQueryInstanceDao().save(instance);
		}
	}

	public void onTimeout(CQLQueryInstance instance, boolean cancelled) {
		if (!cancelled) {
			logger.warn("Couldn't cancel CQLQueryInstance:" + instance.getId());
		} else {
			instance.setState(QueryInstanceState.TIMEDOUT);
			getCqlQueryInstanceDao().save(instance);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstanceListener
	 * #onComplete
	 * (gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance,
	 * java.lang.String)
	 */
	public void onComplete(CQLQueryInstance instance, String results) {
		if (!QueryInstanceState.CANCELLED.equals(instance.getState())) {
			instance.setFinishTime(new Date());
			instance.setState(QueryInstanceState.COMPLETE);
			getCqlQueryInstanceDao().save(instance);
			instance.setResult(results);
			try {
				SAXParserFactory fact = SAXParserFactory.newInstance();
				fact.setNamespaceAware(true);
				SAXParser parser = fact.newSAXParser();
				XMLQueryResultToQueryResultTableHandler handler = (XMLQueryResultToQueryResultTableHandler) applicationContext
						.getBean("xmlQueryResultToQueryResultTableHandlerPrototype");
				handler.setDataServiceUrl(instance.getDataService().getUrl());
				handler.getTable().setQueryInstance(instance);
				parser.parse(new ByteArrayInputStream(instance.getResult().getBytes()), handler);
			} catch (Exception ex) {
				String msg = "Error storing results in table: "
						+ ex.getMessage();
				logger.error(msg, ex);
				throw new RuntimeException(msg, ex);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstanceListener
	 * #onError(gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance,
	 * java.lang.Exception)
	 */
	public void onError(CQLQueryInstance instance, Exception error) {
		if (!QueryInstanceState.CANCELLED.equals(instance.getState())) {
			String msg = error.getMessage();
			logger.info("CQLQueryInstance:" + instance.getId()
					+ " encountered error: " + msg, error);
			StringWriter w = new StringWriter();
			error.printStackTrace(new PrintWriter(w));
			instance.setError(w.getBuffer().toString());
			instance.setState(QueryInstanceState.ERROR);
			getCqlQueryInstanceDao().save(instance);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstanceListener
	 * #onRunning(gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance)
	 */
	public void onRunning(CQLQueryInstance instance) {
		instance.setState(QueryInstanceState.RUNNING);
		instance.setStartTime(new Date());
		getCqlQueryInstanceDao().save(instance);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstanceListener
	 * #onSheduled
	 * (gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance)
	 */
	public void onSheduled(CQLQueryInstance instance) {
		instance.setState(QueryInstanceState.SCHEDULED);
		getCqlQueryInstanceDao().save(instance);
	}

	public CQLQueryInstanceDao getCqlQueryInstanceDao() {
		return cqlQueryInstanceDao;
	}

	public void setCqlQueryInstanceDao(CQLQueryInstanceDao cqlQueryInstanceDao) {
		this.cqlQueryInstanceDao = cqlQueryInstanceDao;
	}

	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		this.applicationContext = ctx;
	}

}
