/**
 * 
 */
package gov.nih.nci.cagrid.portal2.domain.dataservice;

import gov.nih.nci.cagrid.portal2.domain.GridDataService;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@DiscriminatorValue("DCQLQuery")
public class DCQLQuery extends Query {

	private List<GridDataService> targetServices = new ArrayList<GridDataService>();
	
	/**
	 * 
	 */
	public DCQLQuery() {

	}

	@ManyToMany
	@JoinTable(
			name = "dcql_svcs", 
			joinColumns = 
				@JoinColumn(name = "query_id"), 
			inverseJoinColumns = 
				@JoinColumn(name = "svc_id"), 
			uniqueConstraints = 
				@UniqueConstraint(columnNames = 
					{"query_id", "svc_id" }))
	public List<GridDataService> getTargetServices() {
		return targetServices;
	}

	public void setTargetServices(List<GridDataService> targetServices) {
		this.targetServices = targetServices;
	}

}
