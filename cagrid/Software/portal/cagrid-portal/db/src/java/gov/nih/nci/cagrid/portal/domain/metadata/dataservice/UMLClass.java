/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain.metadata.dataservice;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity(name = "DataUMLClass")
@DiscriminatorValue("DataUMLClass")
public class UMLClass extends
		gov.nih.nci.cagrid.portal.domain.metadata.common.UMLClass {

	private DomainModel model;
	private boolean allowableAsTarget;
	private List<UMLAssociationEdge> associations = new ArrayList<UMLAssociationEdge>();
	private UMLClass superClass;
	private List<UMLClass> subClasses = new ArrayList<UMLClass>();
	
	public UMLClass(){
		
	}

	public boolean isAllowableAsTarget() {
		return allowableAsTarget;
	}

	public void setAllowableAsTarget(boolean allowableAsTarget) {
		this.allowableAsTarget = allowableAsTarget;
	}

	@ManyToOne
	@JoinColumn(name = "model_id")
	public DomainModel getModel() {
		return model;
	}

	public void setModel(DomainModel model) {
		this.model = model;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "type")
	public List<UMLAssociationEdge> getAssociations() {
		return associations;
	}

	public void setAssociations(List<UMLAssociationEdge> associations) {
		this.associations = associations;
	}

	@OneToMany(mappedBy = "superClass")
	public List<UMLClass> getSubClasses() {
		return subClasses;
	}

	public void setSubClasses(List<UMLClass> subClasses) {
		this.subClasses = subClasses;
	}

	@ManyToOne
	@JoinColumn(name = "super_id")
	public UMLClass getSuperClass() {
		return superClass;
	}

	public void setSuperClass(UMLClass superClass) {
		this.superClass = superClass;
	}
	
}
