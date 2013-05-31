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
import gov.nih.nci.cagrid.portal.domain.metadata.common.SemanticMetadata;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "oper")
@GenericGenerator(name="id-generator", strategy = "native",
    parameters = {
        @Parameter(name="sequence", value="seq_oper")
    }
)
public class Operation extends AbstractDomainObject {

	private String description;
	private List<Fault> faultCollection = new ArrayList<Fault>();
	private List<InputParameter> inputParameterCollection = new ArrayList<InputParameter>();
	private String name;
	private Output output;
	private List<SemanticMetadata> semanticMetadata = new ArrayList<SemanticMetadata>();
	private ServiceContext serviceContext;

	@ManyToOne
	public ServiceContext getServiceContext() {
		return serviceContext;
	}
	public void setServiceContext(ServiceContext serviceContext) {
		this.serviceContext = serviceContext;
	}
	
	@Column(length = 4000)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "operation")
	public List<Fault> getFaultCollection() {
		return faultCollection;
	}
	public void setFaultCollection(List<Fault> faultCollection) {
		this.faultCollection = faultCollection;
	}
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "operation", targetEntity = InputParameter.class)
	public List<InputParameter> getInputParameterCollection() {
		return inputParameterCollection;
	}
	public void setInputParameterCollection(
			List<InputParameter> inputParameterCollection) {
		this.inputParameterCollection = inputParameterCollection;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "operation")
	public Output getOutput() {
		return output;
	}
	public void setOutput(Output output) {
		this.output = output;
	}
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "oper_sem_meta", 
			joinColumns = @JoinColumn(name = "oper_id"), 
			inverseJoinColumns = @JoinColumn(name = "sem_meta_id"), 
			uniqueConstraints =	@UniqueConstraint(columnNames = 
				{"oper_id", "sem_meta_id" })
	)
	public List<SemanticMetadata> getSemanticMetadata() {
		return semanticMetadata;
	}
	public void setSemanticMetadata(List<SemanticMetadata> semanticMetadata) {
		this.semanticMetadata = semanticMetadata;
	}
	
}
