package gov.nih.nci.cagrid.portal.domain.catalog;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("tool_grid_service_interface")

public class GridServiceInterfaceCatalogEntry extends ToolCatalogEntry {
}