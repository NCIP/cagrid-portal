/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.browse;

import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryDao;
import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryRelationshipInstanceDao;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRelationshipInstance;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRoleInstance;
import gov.nih.nci.cagrid.portal.domain.catalog.PersonCatalogEntry;
import gov.nih.nci.cagrid.portal.portlet.UserModel;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.web.portlet.mvc.AbstractController;

import com.liferay.portal.service.UserServiceUtil;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * 
 */
public class DeleteCatalogEntryController extends AbstractController {

	private UserModel userModel;
	private CatalogEntryDao catalogEntryDao;
	private PortalUserDao portalUserDao;
	private CatalogEntryRelationshipInstanceDao catalogEntryRelationshipInstanceDao;

	public PortalUserDao getPortalUserDao() {
		return portalUserDao;
	}

	public void setPortalUserDao(PortalUserDao portalUserDao) {
		this.portalUserDao = portalUserDao;
	}

	/**
	 * 
	 */
	public DeleteCatalogEntryController() {

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

		deleteRelationships(catalogEntry);
		if (catalogEntry instanceof PersonCatalogEntry) {
			PersonCatalogEntry personCatalogEntry = (PersonCatalogEntry) catalogEntry;
			PortalUser portalUser = personCatalogEntry.getAbout();
			String portalId = portalUser.getPortalId();
			int idx = portalId.indexOf(":");
			long liferayUserId = Long.parseLong(portalId.substring(idx + 1));
			getPortalUserDao().delete(portalUser);
			UserServiceUtil.deleteUser(liferayUserId);
		}
		getCatalogEntryDao().delete(catalogEntry);

		getUserModel().setCurrentCatalogEntry(null);

		response.setRenderParameter("operation", "view");
	}

	private void deleteRelationships(CatalogEntry catalogEntry) {
		for (CatalogEntryRoleInstance roleInst : catalogEntry.getRoles()) {
			CatalogEntryRelationshipInstance relInst = roleInst
					.getRelationship();
			getCatalogEntryRelationshipInstanceDao().delete(relInst);
		}
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

	public CatalogEntryRelationshipInstanceDao getCatalogEntryRelationshipInstanceDao() {
		return catalogEntryRelationshipInstanceDao;
	}

	public void setCatalogEntryRelationshipInstanceDao(
			CatalogEntryRelationshipInstanceDao catalogEntryRelationshipInstanceDao) {
		this.catalogEntryRelationshipInstanceDao = catalogEntryRelationshipInstanceDao;
	}

}
