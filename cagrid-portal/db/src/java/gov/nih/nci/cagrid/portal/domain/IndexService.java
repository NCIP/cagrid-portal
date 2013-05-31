/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "index_services")
@GenericGenerator(name="id-generator", strategy = "native",
    parameters = {
        @Parameter(name="sequence", value="seq_index_services")
    }
)
public class IndexService extends AbstractDomainObject {

	private String url;
	private List<GridService> services = new ArrayList<GridService>();
	
	/**
	 * 
	 */
	public IndexService() {
	}

	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	@ManyToMany
	@JoinTable(
			name = "index_svc_grid_svc", 
			joinColumns = 
				@JoinColumn(name = "index_svc_id"), 
			inverseJoinColumns = 
				@JoinColumn(name = "grid_svc_id"), 
			uniqueConstraints = 
				@UniqueConstraint(columnNames = 
					{"index_svc_id", "grid_svc_id" }))
	public List<GridService> getServices() {
		return services;
	}


	public void setServices(List<GridService> services) {
		this.services = services;
	}
}
