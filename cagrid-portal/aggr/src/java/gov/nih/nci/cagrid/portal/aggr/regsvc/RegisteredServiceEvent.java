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
package gov.nih.nci.cagrid.portal.aggr.regsvc;

import org.springframework.context.ApplicationEvent;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class RegisteredServiceEvent extends ApplicationEvent {
	
	private String serviceUrl;
	
	private String indexServiceUrl;



	/**
	 * @param event source
	 */
	public RegisteredServiceEvent(Object source) {
		super(source);
	}

	
	public String getIndexServiceUrl() {
		return indexServiceUrl;
	}

	public void setIndexServiceUrl(String indexServiceUrl) {
		this.indexServiceUrl = indexServiceUrl;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
}
