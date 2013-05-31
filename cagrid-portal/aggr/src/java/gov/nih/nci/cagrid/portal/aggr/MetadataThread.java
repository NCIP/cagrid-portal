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
package gov.nih.nci.cagrid.portal.aggr;

import gov.nih.nci.cagrid.metadata.MetadataUtils;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class MetadataThread extends Thread {
	
	private static final Log logger = LogFactory.getLog(MetadataThread.class);
	
	private String url;

	private gov.nih.nci.cagrid.metadata.dataservice.DomainModel domainModel;

	private gov.nih.nci.cagrid.metadata.ServiceMetadata serviceMetadata;

	private Exception ex;

	private boolean finished;

	public gov.nih.nci.cagrid.metadata.dataservice.DomainModel getDomainModel() {
		return domainModel;
	}

	public void setDomainModel(
			gov.nih.nci.cagrid.metadata.dataservice.DomainModel domainModel) {
		this.domainModel = domainModel;
	}


	public Exception getEx() {
		return ex;
	}

	public void setEx(Exception ex) {
		this.ex = ex;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public gov.nih.nci.cagrid.metadata.ServiceMetadata getServiceMetadata() {
		return serviceMetadata;
	}

	public void setServiceMetadata(
			gov.nih.nci.cagrid.metadata.ServiceMetadata serviceMetadata) {
		this.serviceMetadata = serviceMetadata;
	}

	public MetadataThread(String url) {
		this.url = url;
	}

	public void run() {
		try {
			// Get the metadata
			EndpointReferenceType epr = new EndpointReferenceType();
			Address addr = new Address(this.url);
			epr.setAddress(addr);
			this.serviceMetadata = MetadataUtils
					.getServiceMetadata(epr);
			if (this.serviceMetadata == null) {
				throw new RuntimeException("Didn't retrieve metadata for "
						+ epr);
			}

			// Get DomainModel, if it exists
			try {
				this.domainModel = MetadataUtils.getDomainModel(epr);
			} catch (Exception ex) {
//				logger.info("No DomainModel found for " + epr + ": "
//						+ ex.getMessage());
			}
			this.finished = true;
		} catch (Exception ex) {
			this.ex = ex;
		}
	}
}
