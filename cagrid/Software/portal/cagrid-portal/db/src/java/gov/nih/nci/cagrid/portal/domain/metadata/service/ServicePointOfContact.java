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
