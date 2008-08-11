/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.regsvc;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.portal.aggr.ServiceUrlProvider;

import java.util.HashSet;
import java.util.Set;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DynamicServiceUrlProvider implements ServiceUrlProvider {

	private static final Log logger = LogFactory.getLog(DynamicServiceUrlProvider.class);
	
	private boolean requireMetadataCompliance;
	private long timeout;

	/**
	 * 
	 */
	public DynamicServiceUrlProvider() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.aggr.regsvc.ServiceUrlProvider#getUrls(java.lang.String)
	 */
	public Set<String> getUrls(String indexServiceUrl) {

		Set<String> urls = new HashSet<String>();
		
		QueryThread t = new QueryThread(indexServiceUrl, isRequireMetadataCompliance());
		t.start();
		try{
			t.join(getTimeout());
		}catch(InterruptedException ex){
			throw new RuntimeException("Index query thread interrupted");
		}
		
		if(t.getEx() != null){
			throw new RuntimeException("Error querying index service: " + t.getEx().getMessage(), t.getEx());
		}
		
		if(!t.isFinished()){
			throw new RuntimeException("Index query thread timed out (timeout = " + getTimeout() + ").");
		}
		
		EndpointReferenceType[] eprs = t.getEprs();
		if(eprs != null && eprs.length > 0){
		 	for(EndpointReferenceType epr : eprs){
				urls.add(epr.getAddress().toString());
			}
			
		}else{
			logger.debug("No EPRs retrieved from '" + indexServiceUrl + "'");
		}
		
		return urls;
	}
	
	
	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}	
	
	public boolean isRequireMetadataCompliance() {
		return requireMetadataCompliance;
	}

	public void setRequireMetadataCompliance(boolean requireMetadataCompliance) {
		this.requireMetadataCompliance = requireMetadataCompliance;
	}	
	
	private class QueryThread extends Thread {
		
		private String indexServiceUrl;
		private boolean requireMetadataCompliance;
		private boolean finished;
		private Exception ex;
		private EndpointReferenceType[] eprs;
		
		QueryThread(String indexServiceUrl, boolean requireMetadatCompliance){
			this.indexServiceUrl = indexServiceUrl;
			this.requireMetadataCompliance = requireMetadatCompliance;
		}
		
		public void run(){
			try{
				DiscoveryClient client = new DiscoveryClient(this.indexServiceUrl);
				this.eprs = client.getAllServices(this.requireMetadataCompliance);
				this.finished = true;
			}catch(Exception ex){
				this.ex = ex;
			}
		}
		
		boolean isFinished(){ return this.finished; }
		Exception getEx(){ return this.ex; };
		EndpointReferenceType[] getEprs(){ return this.eprs; }
	}

}
