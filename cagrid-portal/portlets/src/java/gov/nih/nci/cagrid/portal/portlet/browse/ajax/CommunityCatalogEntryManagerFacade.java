/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.browse.ajax;

import gov.nih.nci.cagrid.portal.dao.catalog.CommunityCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.CommunityCatalogEntry;
import gov.nih.nci.cagrid.portal.service.CommunityService;

import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * @author kherm manav.kher@semanticbits.com
 */
public class CommunityCatalogEntryManagerFacade extends
		CatalogEntryManagerFacade {

	private CommunityCatalogEntryDao communityCatalogEntryDao;
	private CommunityService communityService;

	/**
     *
     */
	public CommunityCatalogEntryManagerFacade() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String setName(String name) {
		if (name.length() < 2) {
			return "Name is two short";
		}

		if (name.endsWith(StringPool.SLASH)) {
			return "Invalid name. Ends with a slash";
		}

		if (name.indexOf(StringPool.DOUBLE_SLASH) != -1) {
			return "Invalid name. Contains two slashes";
		}

		if (name.indexOf(StringPool.COMMA) != -1) {
			return "Invalid name. Contains comma";
		}

		if (name.indexOf(StringPool.STAR) != -1) {
			return "Invalid name. Contains asterick";
		}

		if(Validator.isNumber(name)){
			return "Invalid name. Contains only digits";
		}

		for (char c : name.toCharArray()) {
			if (c == CharPool.SPACE)
				c = CharPool.DASH;
			if ((c != CharPool.SPACE) && (!Validator.isChar(c))
					&& (!Validator.isDigit(c)) && (c == CharPool.SPACE)
					&& (c != CharPool.DASH) && (c != CharPool.PERCENT)
					&& (c != CharPool.PERIOD) && (c != CharPool.SLASH)
					&& (c != CharPool.UNDERLINE)) {
				return "Name has invalid characters";
			}
		}
		if (!getCommunityCatalogEntryDao().isUnique(name))
			return "Community with the same name exists";

		return super.setName(name);
	}

	@Override
	public Integer save() {
		Integer id = null;
		try {
			PortalUser portalUser = getUserModel().getPortalUser();
			CommunityCatalogEntry ce = (CommunityCatalogEntry) getUserModel()
					.getCurrentCatalogEntry();
			if (ce.getId() == null) {
				// create
				getCommunityService().createCommunity(portalUser, ce);
				getUserModel().setCurrentCatalogEntry(ce);
			}
			ce.setAuthor(portalUser);
			getCatalogEntryDao().save(ce);
			saveAreasOfFocus();

			id = saveInternal(ce);
		} catch (Exception ex) {
			String msg = "Error saving catalog entry. Name has invalid characters!";
			logger.debug(msg, ex);
			throw new RuntimeException(msg, ex);
		}
		return id;
	}

	@Override
	protected Integer saveInternal(CatalogEntry catalogEntry) throws Exception {

		getCommunityService().updateCommunity(catalogEntry.getAuthor(),
				(CommunityCatalogEntry) catalogEntry);
		return catalogEntry.getId();
	}

	public CommunityCatalogEntryDao getCommunityCatalogEntryDao() {
		return communityCatalogEntryDao;
	}

	public void setCommunityCatalogEntryDao(
			CommunityCatalogEntryDao communityCatalogEntryDao) {
		this.communityCatalogEntryDao = communityCatalogEntryDao;
	}

	public CommunityService getCommunityService() {
		return communityService;
	}

	public void setCommunityService(CommunityService communityService) {
		this.communityService = communityService;
	}
}
