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
package gov.nih.nci.cagrid.portal.domain.metadata.common;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "uml_attr")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_uml_attr") })
public class UMLAttribute extends AbstractDomainObject {
	
	private String description;
	private String name;
	private List<SemanticMetadata> semanticMetadata = new ArrayList<SemanticMetadata>();
	private ValueDomain valueDomain;
    private Long publicID;
    private float version;
	private String dataTypeName;
	private UMLClass umlClass;
	
	public String getDataTypeName() {
		return dataTypeName;
	}
	public void setDataTypeName(String dataTypeName) {
		this.dataTypeName = dataTypeName;
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
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "uml_attr_sem_meta", 
			joinColumns = @JoinColumn(name = "uml_attr_id"), 
			inverseJoinColumns = @JoinColumn(name = "sem_meta_id"), 
			uniqueConstraints =	@UniqueConstraint(columnNames = 
				{"uml_attr_id", "sem_meta_id" })
	)
	public List<SemanticMetadata> getSemanticMetadata() {
		return semanticMetadata;
	}
	public void setSemanticMetadata(List<SemanticMetadata> semanticMetadata) {
		this.semanticMetadata = semanticMetadata;
	}
	
	@ManyToOne
	public UMLClass getUmlClass() {
		return umlClass;
	}
	public void setUmlClass(UMLClass umlClass) {
		this.umlClass = umlClass;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "val_dom_id")
	public ValueDomain getValueDomain() {
		return valueDomain;
	}
	public void setValueDomain(ValueDomain valueDomain) {
		this.valueDomain = valueDomain;
	}
	public float getVersion() {
		return version;
	}
	public void setVersion(float version) {
		this.version = version;
	}

    public Long getPublicID() {
        return publicID;
    }

    public void setPublicID(Long publicID) {
        this.publicID = publicID;
    }
}
