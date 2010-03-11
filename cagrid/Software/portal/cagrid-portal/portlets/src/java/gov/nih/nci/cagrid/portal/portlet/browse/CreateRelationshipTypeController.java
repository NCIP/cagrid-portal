/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.browse;

import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRelationshipType;
import gov.nih.nci.cagrid.portal.portlet.UserModel;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * 
 */
public class CreateRelationshipTypeController extends AbstractController {

	private UserModel userModel;

	protected void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {

		getUserModel().setCurrentRelationshipType(
				new CatalogEntryRelationshipType());
		response.setRenderParameter("operation", "viewRelationshipType");
		response.setRenderParameter("viewMode", "edit");

	}

	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

}
