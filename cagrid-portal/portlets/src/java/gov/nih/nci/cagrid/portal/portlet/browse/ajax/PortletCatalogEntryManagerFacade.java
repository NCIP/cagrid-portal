/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.browse.ajax;

import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.PortletCatalogEntry;

import java.net.URL;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class PortletCatalogEntryManagerFacade extends
        ToolCatalogEntryManagerFacade {

	private URL portalUrl;
	
	/**
	 * 
	 */
	public PortletCatalogEntryManagerFacade() {

	}
	
	public String setPortalUrl(String portalUrlStr){
		String message = null;
		try{
			this.portalUrl = new URL(portalUrlStr);
		}catch(Exception ex){
			message = "Please enter a valid URL.";
		}
		return message;
	}
	
	@Override
	protected Integer saveInternal(CatalogEntry catalogEntry) {
		PortletCatalogEntry portletCe = (PortletCatalogEntry)catalogEntry;
		portletCe.setPortalUrl(this.portalUrl);
		getCatalogEntryDao().save(portletCe);
		return catalogEntry.getId();
	}

}
