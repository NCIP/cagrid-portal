package gov.nih.nci.cagrid.browser.beans;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import gov.nih.nci.cagrid.browser.util.AppUtils;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.client.DataServiceClient;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.metadata.exceptions.InvalidResourcePropertyException;
import gov.nih.nci.cagrid.metadata.exceptions.RemoteResourcePropertyRetrievalException;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.log4j.Logger;

/**
 * Represent a caGrid Service <p/> Created by IntelliJ IDEA. User: kherm Date:
 * Nov 9, 2006 Time: 2:06:30 PM To change this template use File | Settings |
 * File Templates.
 */
public class CaGridService {

	private static final String QUERY_FAILED_EXECUTION = "queryFailed.execution";

	private static final String QUERY_FAILED_TIMEOUT = "queryFailed.timeout";

	private static final String QUERY_FAILED_INTERNAL = "queryFailed.internalError";

	private static final long QUERY_TIMEOUT = 60000;

	private static final String QUERY_FAILED_SAMPLE_ERROR = "queryFailed.sampleError";

	private static final String QUERY_FAILED_SAMPLE_NOT_FOUND = "queryFailed.sampleNotFound";

	private static final String SAMPLE_QUERY_PARAMETER = "sampleQuery";

	private static final String SERVICE_CART_BEAN = "serviceCart";

	private static Logger logger = Logger.getLogger(CaGridService.class);

	private EndpointReferenceType epr;

	private ServiceMetadata serviceMetadata;

	private DomainModel domainModel;

	private UMLClassBean navigatedClass;

	private String query;

	private String queryResult;

	private String queryFailureMessage;
	
	private List sampleQueries;

	public String getQueryFailureMessage() {
		return queryFailureMessage;
	}

	public void setQueryFailureMessage(String queryFailureMessage) {
		this.queryFailureMessage = queryFailureMessage;
	}

	public CaGridService() {

	}

	public CaGridService(EndpointReferenceType epr) {
		this.epr = epr;
	}

	public void loadMetadata() throws InvalidResourcePropertyException,
			RemoteResourcePropertyRetrievalException,
			ResourcePropertyRetrievalException {
		ServiceMetadata svcMeta = MetadataUtils.getServiceMetadata(getEpr());
		setServiceMetadata(svcMeta);
		try {
			DomainModel model = MetadataUtils.getDomainModel(getEpr());
			setDomainModel(model);
		} catch (InvalidResourcePropertyException ex) {
			logger.debug("No domain model found for '" + getEpr() + "'");
		}
	}

	public String navigateToServiceDetails() {
		String result = AppUtils.FAILED_METHOD;
		try {
			DiscoveredServices disc = (DiscoveredServices) AppUtils
					.getBean("discoveryResult");
			if (disc == null) {
				logger.error("discoveryResult not found");
			} else {
				disc.setNavigatedService(this);
				result = AppUtils.SUCCESS_METHOD;
			}
		} catch (Exception ex) {
			throw new RuntimeException("Error navigating to service details: "
					+ ex.getMessage(), ex);
		}

		return result;
	}

	public String navigateToUMLClassDetails() {
		String result = AppUtils.FAILED_METHOD;
		try {
			UMLClass klass = (UMLClass) AppUtils.getParameter("umlClass");
			if (klass == null) {
				logger.error("umlClass not found");
			} else {
				DiscoveredServices disc = (DiscoveredServices) AppUtils
						.getBean("discoveryResult");
				if (disc == null) {
					logger.error("discoveryResult not found");
				} else {
					logger.debug("Found umlClass");

					UMLClassBean classBean = AppUtils.buildUMLClassBean(klass,
							getDomainModel());

					CaGridService svc = disc.getNavigatedService();
					svc.setNavigatedClass(classBean);

					result = AppUtils.SUCCESS_METHOD;
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException("Error navigating to service details: "
					+ ex.getMessage(), ex);
		}
		return result;
	}

	public String navigateToQuery() {
		String result = AppUtils.FAILED_METHOD;
		
		try{
			Map samplesMap = (Map)AppUtils.getBean(AppUtils.SAMPLE_QUERIES);
			if(samplesMap != null){
				List samples = (List)samplesMap.get(getEpr().getAddress().toString());
				if(samples != null){
					setSampleQueries(samples);
				}
			}
			result = AppUtils.SUCCESS_METHOD;
		}catch(Exception ex){
			setQueryFailureMessage(AppUtils.getMessage(QUERY_FAILED_INTERNAL));
			logger.error(getQueryFailureMessage(), ex);
		}
		
		return result;
	}

	public String executeQuery() {
		String result = AppUtils.FAILED_METHOD;

		try {

			DataServiceQueryThread t = new DataServiceQueryThread();
			t.setServiceUrl(getEpr().getAddress().toString());
			t.setXmlQuery(getQuery());
			t.start();
			t.join(QUERY_TIMEOUT);

			Exception ex = t.getException();
			if (ex != null) {
				setQueryFailureMessage(AppUtils
						.getMessage(QUERY_FAILED_EXECUTION));
				logger.error(getQueryFailureMessage(), ex);
			} else if (!t.isFinished()) {
				setQueryFailureMessage(AppUtils
						.getMessage(QUERY_FAILED_TIMEOUT));
				logger.error(getQueryFailureMessage());
			} else {

				String xmlResult = t.getXmlResult();
				setQueryResult(xmlResult);
				result = AppUtils.SUCCESS_METHOD;
			}

		} catch (Exception ex) {
			setQueryFailureMessage(AppUtils.getMessage(QUERY_FAILED_INTERNAL));
			logger.error(getQueryFailureMessage(), ex);
		}

		return result;
	}
	
	public String fillInSampleQuery(){
		String result = AppUtils.FAILED_METHOD;
		
		try{
			SampleQuery sampleQuery = (SampleQuery)AppUtils.getParameter(SAMPLE_QUERY_PARAMETER);
			if(sampleQuery == null){
				setQueryFailureMessage(AppUtils.getMessage(QUERY_FAILED_SAMPLE_NOT_FOUND));
			}else{
				setQuery(sampleQuery.getQuery());
				result = AppUtils.SUCCESS_METHOD;
			}
		}catch(Exception ex){
			setQueryFailureMessage(AppUtils.getMessage(QUERY_FAILED_SAMPLE_ERROR));
			logger.error(getQueryFailureMessage(), ex);
		}
		
		return result;
	}

	public static String executeQuery(String svcUrl, String xmlQuery)
			throws Exception {
		String xmlResult = null;

		StringReader reader = new StringReader(xmlQuery);
		CQLQuery cql = (CQLQuery) Utils.deserializeObject(reader,
				CQLQuery.class);

		DataServiceClient client = new DataServiceClient(svcUrl);
		CQLQueryResults result = client.query(cql);
		StringWriter writer = new StringWriter();
		Utils.serializeObject(result,
				DataServiceConstants.CQL_RESULT_SET_QNAME, writer);
		
		xmlResult = writer.getBuffer().toString();
		return xmlResult;
	}
	
	public String addToServiceCart(){
		String result = AppUtils.FAILED_METHOD;
		
		try{
			ServiceCart serviceCart = (ServiceCart)AppUtils.getBean(ServiceCart.SERVICE_CART_BEAN);
			if(serviceCart == null){
				serviceCart.setServiceCartFailureMessage(AppUtils.getMessage(ServiceCart.SERVICE_CART_FAILED_NOT_FOUND));
			}else{

				serviceCart.addService(this);
				serviceCart.setServiceCartFailureMessage(null);
				
				result = AppUtils.SUCCESS_METHOD;
			}
		}catch(Exception ex){
			logger.error(getQueryFailureMessage(), ex);
		}
		
		return result;
	}
	
	public String removeFromServiceCart(){
		String result = AppUtils.FAILED_METHOD;
		
		try{
			ServiceCart serviceCart = (ServiceCart)AppUtils.getBean(ServiceCart.SERVICE_CART_BEAN);
			if(serviceCart != null){
				serviceCart.removeService(this);
				serviceCart.setServiceCartFailureMessage(null);
				result = AppUtils.SUCCESS_METHOD;
			}
		}catch(Exception ex){
			logger.error(getQueryFailureMessage(), ex);
		}
		
		return result;
	}

	// public String navigateToSemanticMetadata(){
	// String result = AppUtils.FAILED_METHOD;
	//		
	// try{
	//			
	// }catch(Exception ex){
	//			
	// }
	//		
	// return result;
	// }

	public EndpointReferenceType getEpr() {
		return epr;
	}

	public void setEpr(EndpointReferenceType epr) {
		this.epr = epr;
	}

	public DomainModel getDomainModel() {
		return domainModel;
	}

	public void setDomainModel(DomainModel domainModel) {
		this.domainModel = domainModel;
	}

	public ServiceMetadata getServiceMetadata() {
		return serviceMetadata;
	}

	public void setServiceMetadata(ServiceMetadata serviceMetadata) {
		this.serviceMetadata = serviceMetadata;
	}

	public UMLClassBean getNavigatedClass() {
		return navigatedClass;
	}

	public void setNavigatedClass(UMLClassBean navigatedClass) {
		this.navigatedClass = navigatedClass;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQueryResult() {
		return queryResult;
	}

	public void setQueryResult(String queryResults) {
		this.queryResult = queryResults;
	}

	private static class DataServiceQueryThread extends Thread {

		private String serviceUrl;

		private String xmlQuery;

		private String xmlResult;

		private Exception ex;

		private boolean finished;

		public DataServiceQueryThread() {

		}

		public DataServiceQueryThread(String name) {
			super(name);
		}

		public void run() {
			try {
				String xml = executeQuery(getServiceUrl(), getXmlQuery());
				setXmlResult(xml);
				setFinished(true);
			} catch (Exception ex) {
				setException(ex);
			}
		}

		public Exception getException() {
			return ex;
		}

		public void setException(Exception ex) {
			this.ex = ex;
		}

		public boolean isFinished() {
			return finished;
		}

		public void setFinished(boolean finished) {
			this.finished = finished;
		}

		public String getXmlQuery() {
			return xmlQuery;
		}

		public void setXmlQuery(String xmlQuery) {
			this.xmlQuery = xmlQuery;
		}

		public String getXmlResult() {
			return xmlResult;
		}

		public void setXmlResult(String xmlResult) {
			this.xmlResult = xmlResult;
		}

		public String getServiceUrl() {
			return serviceUrl;
		}

		public void setServiceUrl(String serviceUrl) {
			this.serviceUrl = serviceUrl;
		}

	}

	public List getSampleQueries() {
		return sampleQueries;
	}

	public void setSampleQueries(List sampleQueries) {
		this.sampleQueries = sampleQueries;
	}

}
