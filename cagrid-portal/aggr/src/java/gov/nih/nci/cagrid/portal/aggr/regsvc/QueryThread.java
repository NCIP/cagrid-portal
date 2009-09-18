package gov.nih.nci.cagrid.portal.aggr.regsvc;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;

import org.apache.axis.message.addressing.EndpointReferenceType;

public class QueryThread extends Thread {
	private String indexServiceUrl;
	private boolean requireMetadataCompliance;
	private boolean finished;
	private Exception ex;
	private EndpointReferenceType[] eprs;
	
	public QueryThread(String indexServiceUrl, boolean requireMetadatCompliance){
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
	
	public boolean isFinished(){ return this.finished; }
	public Exception getEx(){ return this.ex; };
	public EndpointReferenceType[] getEprs(){ return this.eprs; }
}
