package gov.nih.nci.cagrid.portal.domain;

import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "grid_svc_uml_class")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_grid_svc_uml_class") })
public class GridServiceUmlClass extends AbstractDomainObject {
	private UMLClass umlClass;
	private GridService gridService;
	private int objectCount;
	private String caption;
	


	public GridServiceUmlClass() {
		
	}
	
    @ManyToOne
    @JoinColumn(name = "grid_svc_id", nullable = false)
	public GridService getGridService() {
		return gridService;
	}
	public void setGridService(GridService gridService) {
		this.gridService = gridService;
	}
	
    @ManyToOne
    @JoinColumn(name = "uml_class_id", nullable = false)
	public UMLClass getUmlClass() {
		return umlClass;
	}
	public void setUmlClass(UMLClass umlClass) {
		this.umlClass = umlClass;
	}
	
	@Column(name="object_count")
	public int getObjectCount() {
		return objectCount;
	}
	public void setObjectCount(int objectCount) {
		this.objectCount = objectCount;
	}
	
	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	
}
