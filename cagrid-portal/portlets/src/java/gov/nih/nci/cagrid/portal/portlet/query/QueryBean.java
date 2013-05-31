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
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query;

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
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class QueryBean {
	
	private static final Log logger = LogFactory.getLog(QueryBean.class);

	private String url;

	private String query;

	private String results;

	private String error;
	
	private long timeout;

	/**
	 * 
	 */
	public QueryBean() {

	}

	public void query() {
		QueryThread t = new QueryThread(getUrl(), getQuery());
		t.start();
		try{
			t.join(getTimeout());
		}catch(InterruptedException ex){
			logger.error("Query thread interrupted: " + ex.getMessage(), ex);
		}
		if(t.getEx() != null){
			setError("Error querying: " + t.getEx().getMessage());
		}
		if(!t.isFinished()){
			
		}
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	
	private class QueryThread extends Thread {

		private String url;
		private String query;
		private Exception ex;
		private boolean finished;
		private String result;
		
		QueryThread(String url, String query){
			this.url = url;
			this.query = query;
		}
		
		public void run(){
			try {
				DataServiceClient client = new DataServiceClient(this.url);

				StringReader reader = new StringReader(this.query.trim());
				CQLQuery query = (CQLQuery) Utils.deserializeObject(reader,
						CQLQuery.class);
				CQLQueryResults result = client.query(query);
				StringWriter writer = new StringWriter();
				Utils.serializeObject(result,
						DataServiceConstants.CQL_RESULT_SET_QNAME, writer);
				this.result = writer.getBuffer().toString();
				this.finished = true;
			} catch (Exception ex) {
				this.ex = ex;
				
			}			
		}
		
		boolean isFinished(){
			return finished;
		}
		Exception getEx(){
			return ex;
		}
		String getResult(){
			return result;
		}
	}

}
