/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.browse;

import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;

import javax.portlet.RenderRequest;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class BrowseViewProfileController extends BrowseViewDetailsController {
	protected CatalogEntry getCatalogEntry(RenderRequest request){
		CatalogEntry entry = getUserModel().getPortalUser().getCatalog();
		entry = getCatalogEntryDao().getById(entry.getId());
		return entry;
	}
}
