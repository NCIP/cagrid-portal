package gov.nih.nci.cagrid.portal.domain.catalog;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("tool_workspace")

public class WorkspaceCatalogEntry extends CommunityCatalogEntry {
}