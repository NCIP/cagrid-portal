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
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.browse;

import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.portlet.UserModel;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * 
 */
public class EditCatalogEntryController extends AbstractController {

	private UserModel userModel;
	private CatalogEntryDao catalogEntryDao;

	/**
	 * 
	 */
	public EditCatalogEntryController() {

	}

	protected void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {

		Integer entryId = null;
		try {
			entryId = Integer.valueOf(request.getParameter("entryId"));
		} catch (Exception ex) {
			throw new RuntimeException("Error getting entryId from request: "
					+ ex.getMessage(), ex);
		}
		CatalogEntry catalogEntry = getCatalogEntryDao().getById(entryId);
		if (catalogEntry == null) {
			throw new RuntimeException("Couldn't find catalog entry for id: "
					+ entryId);
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

	public CatalogEntryDao getCatalogEntryDao() {
		return catalogEntryDao;
	}

	public void setCatalogEntryDao(CatalogEntryDao catalogEntryDao) {
		this.catalogEntryDao = catalogEntryDao;
	}

}
