/**
 * $Id $
 */
package gov.nih.nci.cagrid.browser.search;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.log4j.Logger;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;



/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public abstract class AbstractSearch implements ServiceDiscoveryThread {
	
	private static Logger logger = Logger.getLogger(AbstractSearch.class);
	
	private String[] indexServiceURLs;
	private EndpointReferenceType[] eprs;
	private Exception ex;
	private boolean finished;


	public boolean isFinished() {
		return finished;
	}
	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.browser.beans.search.ServiceDiscoveryThread#getEPRs()
	 */
	public EndpointReferenceType[] getEPRs() {
		return this.eprs;
	}
	
	public void setEPRs(EndpointReferenceType[] eprs){
		this.eprs = eprs;
		if(this.eprs != null){
			for(int i = 0; i < this.eprs.length; i++){
				logger.debug("\t" + this.eprs[i]);
			}
		}
	}
	
	public void setException(Exception ex){
		this.ex = ex;
		logger.debug("Got exception: " + ex.getMessage(), ex);
	}
	public Exception getException(){
		return this.ex;
	}
	
	public void reset(){
		setEPRs(null);
		setException(null);
		setFinished(false);
	}
	public String[] getIndexServiceURLs() {
		return indexServiceURLs;
	}
	public void setIndexServiceURLs(String[] indexServiceURLs) {
		this.indexServiceURLs = indexServiceURLs;
	}
	
	public void run() {
		EndpointReferenceType[] results = null;
		Set allResults = new HashSet();
		try {
			String[] urls = getIndexServiceURLs();
			for(int i = 0; i < urls.length; i++){
				DiscoveryClient client = new DiscoveryClient(urls[i]);
				EndpointReferenceType[] eprs = doRun(client);
				if(eprs != null){
					allResults.addAll(Arrays.asList(eprs));
				}
			}
			results = (EndpointReferenceType[])allResults.toArray(new EndpointReferenceType[allResults.size()]);
		} catch (Exception ex) {
			setException(ex);
		}
		if (results == null) {
			results = new EndpointReferenceType[0];
		}
		setEPRs(results);
		setFinished(true);
	}
	
	protected abstract EndpointReferenceType[] doRun(DiscoveryClient client) throws Exception;

}
