/**
 * 
 */
package gov.nih.nci.cagrid.portal2.domain.dataservice;

import gov.nih.nci.cagrid.portal2.domain.GridDataService;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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


}
