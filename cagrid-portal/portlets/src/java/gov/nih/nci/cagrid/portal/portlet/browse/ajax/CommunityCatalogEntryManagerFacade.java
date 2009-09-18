/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.browse.ajax;

import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.CommunityCatalogEntry;

import java.net.URL;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 */
public class CommunityCatalogEntryManagerFacade extends
        CatalogEntryManagerFacade {

	private URL communityUrl;
	
    /**
     *
     */
    public CommunityCatalogEntryManagerFacade() {
        // TODO Auto-generated constructor stub
    }
    
	public String setCommunityUrl(String communityUrlStr){
		String message = null;
		try{
			this.communityUrl = new URL(communityUrlStr);
		}catch(Exception ex){
			message = "Please enter a valid URL.";
		}
		return message;
	}
	
	@Override
	protected Integer saveInternal(CatalogEntry catalogEntry) {
		CommunityCatalogEntry communityCe = (CommunityCatalogEntry)catalogEntry;
		communityCe.setCommunityUrl(this.communityUrl);
		getCatalogEntryDao().save(communityCe);
		return catalogEntry.getId();
	}

}
