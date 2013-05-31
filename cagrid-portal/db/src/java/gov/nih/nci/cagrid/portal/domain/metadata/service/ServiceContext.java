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
package gov.nih.nci.cagrid.portal.domain.metadata.service;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "svc_ctx")
@GenericGenerator(name="id-generator", strategy = "native",
    parameters = {
        @Parameter(name="sequence", value="seq_svc_ctx")
    }
)
public class ServiceContext extends AbstractDomainObject {
	
	private List<ContextProperty> contextPropertyCollection = new ArrayList<ContextProperty>();
	private String description;
	private String name;
	private List<Operation> operationCollection = new ArrayList<Operation>();
	private Service service;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceContext")
	public List<ContextProperty> getContextPropertyCollection() {
		return contextPropertyCollection;
	}
	public void setContextPropertyCollection(
			List<ContextProperty> contextPropertyCollection) {
		this.contextPropertyCollection = contextPropertyCollection;
	}
	
	@Column(length = 4000)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceContext")
	public List<Operation> getOperationCollection() {
		return operationCollection;
	}
	public void setOperationCollection(List<Operation> operationCollection) {
		this.operationCollection = operationCollection;
	}
	
	@ManyToOne
	public Service getService() {
		return service;
	}
	public void setService(Service service) {
		this.service = service;
	}
}
