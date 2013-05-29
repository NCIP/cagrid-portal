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

import gov.nih.nci.cagrid.portal.domain.ServiceStatus;

import org.springframework.context.ApplicationEvent;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ServiceStatusChangeEvent extends ApplicationEvent {

	private String serviceUrl;
	private ServiceStatus oldStatus;
	private ServiceStatus newStatus;
	
	/**
	 * @param source
	 */
	public ServiceStatusChangeEvent(Object source) {
		super(source);
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public ServiceStatus getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(ServiceStatus oldStatus) {
		this.oldStatus = oldStatus;
	}
	
	public ServiceStatus getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(ServiceStatus newStatus) {
		this.newStatus = newStatus;
	}

}
