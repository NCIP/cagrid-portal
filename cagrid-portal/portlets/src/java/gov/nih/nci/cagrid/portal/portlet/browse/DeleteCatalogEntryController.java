/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.browse;

import java.util.ArrayList;

import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.PersonCatalogEntry;
import gov.nih.nci.cagrid.portal.portlet.UserModel;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.web.portlet.mvc.AbstractController;

import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.service.ResourceLocalServiceUtil;
import com.liferay.portal.service.UserServiceUtil;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * 
 */
public class DeleteCatalogEntryController extends AbstractController {

	private UserModel userModel;
	private CatalogEntryDao catalogEntryDao;
	private PortalUserDao portalUserDao;

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

		PortletUtils.deleteResource(getUserModel().getPortalUser(),
				CatalogEntry.class, catalogEntry.getId());

		if (catalogEntry instanceof PersonCatalogEntry) {
			PersonCatalogEntry personCatalogEntry = (PersonCatalogEntry) catalogEntry;

			PortalUser portalUser = personCatalogEntry.getAbout();
			if (portalUser == null) {
				// Then, we must be testing
			} else {

				String portalId = portalUser.getPortalId();
				int idx = portalId.indexOf(":");
				long companyId = Long.parseLong(portalId.substring(0, idx));
				long userId = Long.parseLong(portalId.substring(idx + 1));

				// TODO: remove this try/catch
				try {
					UserServiceUtil.deleteUser(userId);
				} catch (Exception ex) {
					logger.error("Error deleting liferay user: "
							+ ex.getMessage());
				}

				try {
					ResourceLocalServiceUtil.deleteResource(companyId,
							CatalogEntry.class.getName(),
							ResourceConstants.SCOPE_INDIVIDUAL,
							personCatalogEntry.getId());
				} catch (Exception ex) {
					logger.error("Error deleting liferay user: "
							+ ex.getMessage());
				}

				portalUser.setCatalog(null);
				getPortalUserDao().save(portalUser);

				personCatalogEntry.setAbout(null);
				getCatalogEntryDao().save(personCatalogEntry);

				getPortalUserDao().delete(portalUser);
			}
		}

		getCatalogEntryDao().delete(catalogEntry);

		getUserModel().setCurrentCatalogEntry(null);

		response.setRenderParameter("operation", "view");
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
