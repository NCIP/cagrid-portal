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
package gov.nih.nci.cagrid.portal.domain.metadata.dataservice;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import gov.nih.nci.cagrid.portal.domain.GridDataService;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "domain_models")
@GenericGenerator(name="id-generator", strategy = "native",
    parameters = {
        @Parameter(name="sequence", value="seq_domain_models")
    }
)
public class DomainModel extends AbstractDomainObject {
	
	private List<UMLClass> classes = new ArrayList<UMLClass>();
	private List<XMLSchema> xmlSchemas = new ArrayList<XMLSchema>();
	private GridDataService service;
	
	private String projectDescription;
	private String projectLongName;
	private String projectShortName;
	private String projectVersion;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "model")
	public List<UMLClass> getClasses() {
		return classes;
	}
	public void setClasses(List<UMLClass> classes) {
		this.classes = classes;
	}
	

	@Column(length = 4000)
	public String getProjectDescription() {
		return projectDescription;
	}
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}
	

	public String getProjectLongName() {
		return projectLongName;
	}
	public void setProjectLongName(String projectLongName) {
		this.projectLongName = projectLongName;
	}
	

	public String getProjectShortName() {
		return projectShortName;
	}
	public void setProjectShortName(String projectShortName) {
		this.projectShortName = projectShortName;
	}
	public String getProjectVersion() {
		return projectVersion;
	}
	public void setProjectVersion(String projectVersion) {
		this.projectVersion = projectVersion;
	}
	
	@OneToOne
	@JoinColumn(name = "service_id")
	public GridDataService getService() {
		return service;
	}
	public void setService(GridDataService service) {
		this.service = service;
	}
	
	@ManyToMany
	@JoinTable(
			name = "models_schemas", 
			joinColumns = 
				@JoinColumn(name = "model_id"), 
			inverseJoinColumns = 
				@JoinColumn(name = "schema_id"), 
			uniqueConstraints = 
				@UniqueConstraint(columnNames = 
					{"model_id", "schema_id" }))
	public List<XMLSchema> getXmlSchemas() {
		return xmlSchemas;
	}
	public void setXmlSchemas(List<XMLSchema> xmlSchemas) {
		this.xmlSchemas = xmlSchemas;
	}
	
	
}
