/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.browse;

import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;

import javax.portlet.RenderRequest;
import javax.portlet.PortletRequest;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class BrowseViewProfileController extends BrowseViewDetailsController {

    @Override
     protected CatalogEntry getCatalogEntry(PortletRequest request) throws RuntimeException {
     	CatalogEntry entry = getUserModel().getPortalUser().getCatalog();
		entry = getCatalogEntryDao().getById(entry.getId());
		return entry;
	}
}
