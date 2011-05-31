package gov.nih.nci.cagrid.portal.domain.catalog;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("tools")

public class ToolCatalogEntry extends CatalogEntry {
	
	private String version;

    public List<SharedQueryToolsRelationship> sharedQueryRelationships = new ArrayList<SharedQueryToolsRelationship>();

    public List<ToolApplications> applications = new ArrayList<ToolApplications>();


    @OneToMany(mappedBy = "tool")
    public List<SharedQueryToolsRelationship> getSharedQueryRelationships() {
        return sharedQueryRelationships;
    }

    public void setSharedQueryRelationships(List<SharedQueryToolsRelationship> sharedQueryRelationships) {
        this.sharedQueryRelationships = sharedQueryRelationships;
    }

    @OneToMany(mappedBy="tool")
    public List<ToolApplications> getApplications() {
        return applications;
    }

    public void setApplications(List<ToolApplications> applications) {
        this.applications = applications;
    }

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}