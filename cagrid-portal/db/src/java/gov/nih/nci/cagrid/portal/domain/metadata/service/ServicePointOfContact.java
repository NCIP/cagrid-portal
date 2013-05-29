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
package gov.nih.nci.cagrid.portal.domain.metadata.service;

import gov.nih.nci.cagrid.portal.domain.metadata.common.PointOfContact;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@DiscriminatorValue("ServicePointOfContact")
public class ServicePointOfContact extends PointOfContact {

	private Service serviceDescription;
	
	/**
	 * 
	 */
	public ServicePointOfContact() {

	}

	@ManyToOne
	@JoinColumn(name = "service_desc_id")
	public Service getServiceDescription() {
		return serviceDescription;
	}

	public void setServiceDescription(Service serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

}
