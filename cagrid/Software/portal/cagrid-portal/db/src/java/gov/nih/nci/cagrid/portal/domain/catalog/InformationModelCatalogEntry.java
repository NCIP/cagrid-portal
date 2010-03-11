package gov.nih.nci.cagrid.portal.domain.catalog;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

@Entity
@DiscriminatorValue("information_model")

public class InformationModelCatalogEntry extends DataSetCatalogEntry {
	
	private String projectLongName;

	public String getProjectLongName() {
		return projectLongName;
	}

	public void setProjectLongName(String projectLongName) {
		this.projectLongName = projectLongName;
	}
}