/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.status;

import gov.nih.nci.cagrid.metadata.MetadataConstants;
import gov.nih.nci.cagrid.metadata.ResourcePropertyHelper;
import gov.nih.nci.cagrid.metadata.exceptions.InvalidResourcePropertyException;
import gov.nih.nci.cagrid.metadata.exceptions.RemoteResourcePropertyRetrievalException;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReference;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class DynamicServiceStatusProvider implements ServiceStatusProvider {

	private long timeout;
	
	private static final Log logger = LogFactory.getLog(DynamicServiceStatusProvider.class);
	/**
	 * 
	 */
	public DynamicServiceStatusProvider() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.aggr.status.ServiceStatusProvider#getStatus(java.lang.String)
	 */
	public ServiceStatus getStatus(String serviceUrl) {

		ServiceStatus status = ServiceStatus.UNKNOWN;
		
		PingThread t = new PingThread(serviceUrl);
		t.start();
		try{
			t.join(getTimeout());
		}catch(InterruptedException ex){
			logger.error("Ping thread interrupted: "+ ex.getMessage(), ex);
		}
		if(!t.isFinished()){
			//logger.error("Ping thread timed out.");
			status = ServiceStatus.INACTIVE;
		}else if(t.getEx() != null){
			logger.error("Ping thread encoutered error: " + t.getEx().getMessage(), t.getEx());
		}else{
			status = t.getStatus();
		}
		
		return status;
	}

	private class PingThread extends Thread {

		private String url;

		private boolean finished;

		private Exception ex;

		private ServiceStatus status;

		PingThread(String url) {
			this.url = url;
		}

		public void run() {
			try {
				EndpointReference epr = new EndpointReference(new Address(url));
				ResourcePropertyHelper.getResourceProperty(epr,
						MetadataConstants.CAGRID_MD_QNAME);
				this.finished = true;
			} catch (InvalidResourcePropertyException ex) {
				this.status = ServiceStatus.INVALID;
			} catch (RemoteResourcePropertyRetrievalException ex) {
				this.status = ServiceStatus.INACTIVE;
			} catch (Exception ex) {
				this.ex = ex;
			}
			this.status = ServiceStatus.ACTIVE;
		}

		boolean isFinished() {
			return finished;
		}

		Exception getEx() {
			return ex;
		}

		ServiceStatus getStatus() {
			return status;
		}
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

}
