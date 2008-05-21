/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain.dataservice;

import gov.nih.nci.cagrid.portal.domain.GridDataService;

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
@DiscriminatorValue("CQLQuery")
public class CQLQueryInstance extends QueryInstance {

	private GridDataService dataService;
	
	/**
	 * 
	 */
	public CQLQueryInstance() {
	}
	
	@ManyToOne
	@JoinColumn(name = "data_service_id")
	public GridDataService getDataService() {
		return dataService;
	}

	public void setDataService(GridDataService dataService) {
		this.dataService = dataService;
	}

	@Transient
	public String getType() {
		return "CQL";
	}

	public void setType(String type) {
		//Do nothing. Immutable.
	}


}
