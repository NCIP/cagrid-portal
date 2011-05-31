package gov.nih.nci.cagrid.portal.domain.catalog;

import java.net.URL;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("tool_portlet")

public class PortletCatalogEntry extends ToolCatalogEntry {
	
	private URL portalUrl;

	public URL getPortalUrl() {
		return portalUrl;
	}

	public void setPortalUrl(URL portalUrl) {
		this.portalUrl = portalUrl;
	}
	
}