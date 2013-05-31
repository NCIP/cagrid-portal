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