/**
 * 
 */
package gov.nih.nci.cagrid.portal2.domain.dataservice;

import gov.nih.nci.cagrid.portal2.domain.GridService;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
		// TODO Auto-generated constructor stub
	}

	@ManyToOne
	@JoinColumn(name = "fqp_service_id")
	public GridService getFqpService() {
		return fqpService;
	}

	public void setFqpService(GridService fqpService) {
		this.fqpService = fqpService;
	}

}
