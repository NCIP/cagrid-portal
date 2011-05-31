/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.browse;

import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.portlet.UserModel;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;


import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 *
 */
public class CreateCatalogEntryController extends AbstractController {
	
	
	private UserModel userModel;
	private CatalogEntryFactory catalogEntryFactory;

	
	protected void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {

		String entryType = request.getParameter("entryType");
		CatalogEntry catalogEntry = getCatalogEntryFactory().newCatalogEntry(entryType);
		if(catalogEntry == null){
			throw new RuntimeException("Catalog entry is null for type: " + entryType);
		}
		getUserModel().setCurrentCatalogEntry(catalogEntry);
		response.setRenderParameter("operation", "viewDetails");
		response.setRenderParameter("viewMode", "edit");
		
	}


	public UserModel getUserModel() {
		return userModel;
	}


	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}


	public CatalogEntryFactory getCatalogEntryFactory() {
		return catalogEntryFactory;
	}


	public void setCatalogEntryFactory(CatalogEntryFactory catalogEntryFactory) {
		this.catalogEntryFactory = catalogEntryFactory;
	}

}
