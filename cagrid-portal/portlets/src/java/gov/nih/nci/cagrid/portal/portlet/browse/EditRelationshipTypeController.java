/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.browse;

import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryRelationshipTypeDao;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRelationshipType;
import gov.nih.nci.cagrid.portal.portlet.UserModel;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * 
 */
public class EditRelationshipTypeController extends AbstractController {

	private UserModel userModel;
	private CatalogEntryRelationshipTypeDao catalogEntryRelationshipTypeDao;

	/**
	 * 
	 */
	public EditRelationshipTypeController() {

	}

	protected void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {

		Integer relTypeId = null;
		try {
			relTypeId = Integer.valueOf(request.getParameter("relTypeId"));
		} catch (Exception ex) {
			throw new RuntimeException("Error getting relTypeId from request: "
					+ ex.getMessage(), ex);
		}
		CatalogEntryRelationshipType relType = getCatalogEntryRelationshipTypeDao()
				.getById(relTypeId);
		if (relType == null) {
			throw new RuntimeException(
					"Couldn't find relationship type for id: " + relTypeId);
		}
		getUserModel().setCurrentRelationshipType(relType);
		response.setRenderParameter("operation", "viewRelationshipType");
		response.setRenderParameter("viewMode", "edit");

	}

	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

	public CatalogEntryRelationshipTypeDao getCatalogEntryRelationshipTypeDao() {
		return catalogEntryRelationshipTypeDao;
	}

	public void setCatalogEntryRelationshipTypeDao(
			CatalogEntryRelationshipTypeDao catalogEntryRelationshipTypeDao) {
		this.catalogEntryRelationshipTypeDao = catalogEntryRelationshipTypeDao;
	}

}
