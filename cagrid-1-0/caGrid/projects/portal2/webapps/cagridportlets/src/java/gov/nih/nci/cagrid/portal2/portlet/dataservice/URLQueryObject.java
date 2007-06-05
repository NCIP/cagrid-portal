package gov.nih.nci.cagrid.portal2.portlet.dataservice;

import org.apache.commons.logging.Log;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
/**
 * @author <A HREF="MAILTO:parmarv@mail.nih.gov">Vijay Parmar</A>
 *
 */
public class URLQueryObject implements Serializable {

    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    private String url;
    private String cqlQuery;
	private String results;

   
	public URLQueryObject(){
		
	}
	
	public void setCqlQuery(String query) {
		this.cqlQuery = query;
		logger.info("CQLQuery set to " + query);
	}
	
	public String getCqlQuery() {
		return cqlQuery;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
		logger.info("URL set to " + url);
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	

}
