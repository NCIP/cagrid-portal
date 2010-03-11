package gov.nih.nci.cagrid.portal.domain.catalog;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("tool_desktop")

public class DesktopToolCatalogEntry extends ToolCatalogEntry {


}