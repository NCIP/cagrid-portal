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
package gov.nih.nci.cagrid.portal.domain.metadata.common;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "uml_class")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_uml_class") })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "uml_class_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("UMLClass")
public class UMLClass extends AbstractDomainObject {
	
	private String cadsrId;
	private String className;
	private String description;
	private String packageName;
	private String projectName;
	private String projectVersion;
	private List<SemanticMetadata> semanticMetadata = new ArrayList<SemanticMetadata>();
	
	private List<UMLAttribute> umlAttributeCollection = new ArrayList<UMLAttribute>();
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	@Column(length = 4000)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectVersion() {
		return projectVersion;
	}
	public void setProjectVersion(String projectVersion) {
		this.projectVersion = projectVersion;
	}
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "uml_class_sem_meta", 
			joinColumns = @JoinColumn(name = "uml_class_id"), 
			inverseJoinColumns = @JoinColumn(name = "sem_meta_id"), 
			uniqueConstraints =	@UniqueConstraint(columnNames = 
				{"uml_class_id", "sem_meta_id" })
	)
	public List<SemanticMetadata> getSemanticMetadata() {
		return semanticMetadata;
	}
	public void setSemanticMetadata(List<SemanticMetadata> semanticMetadata) {
		this.semanticMetadata = semanticMetadata;
	}
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "umlClass")
	public List<UMLAttribute> getUmlAttributeCollection() {
		return umlAttributeCollection;
	}
	public void setUmlAttributeCollection(List<UMLAttribute> umlAttributeCollection) {
		this.umlAttributeCollection = umlAttributeCollection;
	}
	
	public String getCadsrId() {
		return cadsrId;
	}
	public void setCadsrId(String cadsrId) {
		this.cadsrId = cadsrId;
	}
}
