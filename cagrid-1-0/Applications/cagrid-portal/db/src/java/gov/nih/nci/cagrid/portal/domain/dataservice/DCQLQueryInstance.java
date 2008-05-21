/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain.dataservice;

import gov.nih.nci.cagrid.portal.domain.GridService;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@DiscriminatorValue("DCQLQuery")
public class DCQLQueryInstance extends QueryInstance {
	
	private GridService fqpService;

	/**
	 * 
	 */
	public DCQLQueryInstance() {

	}

	@ManyToOne
	@JoinColumn(name = "fqp_service_id")
	public GridService getFqpService() {
		return fqpService;
	}

	public void setFqpService(GridService fqpService) {
		this.fqpService = fqpService;
	}

	@Transient
	public String getType() {
		return "DCQL";
	}
	public void setType(String type) {
		//Do nothing. Immutable.
	}

}
