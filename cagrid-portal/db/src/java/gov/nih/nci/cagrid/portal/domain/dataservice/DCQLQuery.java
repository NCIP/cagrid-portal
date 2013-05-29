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
package gov.nih.nci.cagrid.portal.domain.dataservice;

import gov.nih.nci.cagrid.portal.domain.GridDataService;

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
