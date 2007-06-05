/**
 * 
 */
package gov.nih.nci.cagrid.portal2.domain;

import gov.nih.nci.cagrid.portal2.domain.metadata.dataservice.DomainModel;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@DiscriminatorValue("GridService")
public class GridDataService extends GridService {

	private DomainModel domainModel;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "service")
	public DomainModel getDomainModel() {
		return domainModel;
	}
	public void setDomainModel(DomainModel domainModel) {
		this.domainModel = domainModel;
	}
	
}
