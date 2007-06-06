package gov.nih.nci.cagrid.portal2.portlet.dataservice;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.client.DataServiceClient;

import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <A HREF="MAILTO:parmarv@mail.nih.gov">Vijay Parmar</A>
 *
 */
public class DataServiceManager {

	  /** Logger for this class and subclasses */
    protected final static Log logger = LogFactory.getLog(DataServiceManager.class);

	private URLQueryObject urlQueryObject;
	
	public String executeQuery(URLQueryObject urlQueryObject2) throws Exception {
		
		if(urlQueryObject2!=null){
			if(urlQueryObject2.getCqlQuery()!=null && urlQueryObject2.getUrl()!=null){
				return executeQuery(urlQueryObject2.getUrl(),urlQueryObject2.getCqlQuery());
			}else{
				logger.info("URL or CQL Query cannot be null. Required for performing data service query.");
			}
		}else{
			logger.info("URLQueryObject is null");
		}
		return null;
	}
	
	public static String executeQuery(String svcUrl, String xmlQuery)
			throws Exception {
			
		logger.info("DataServiceManager:: about to perform query. Invoking the DataServiceClient to query data service.");
				
		DataServiceClient client = new DataServiceClient(svcUrl);
		
		StringReader reader = new StringReader(xmlQuery.trim());
		CQLQuery query = (CQLQuery) Utils.deserializeObject(reader,CQLQuery.class);
					
		// Perform Query
		CQLQueryResults result = null;
		result = client.query(query);

		//Convert Result to String
		StringWriter writer = new StringWriter();
		try{
		
		Utils.serializeObject(result,DataServiceConstants.CQL_RESULT_SET_QNAME, writer);
		}catch(Throwable t){
			logger.info("Unable to serialize CQLQueryResults object."+t.getMessage());
			throw new Exception(t.getMessage());
		}
		
		String xmlResult = writer.getBuffer().toString();
		logger.info("DataServiceManager:: Query results received.");
		return xmlResult;
	}
	
	public URLQueryObject getUrlQueryObject() {
		return urlQueryObject;
	}

	public void setUrlQueryObject(URLQueryObject urlQueryObject) {
		this.urlQueryObject = urlQueryObject;
	}

	


	
}
