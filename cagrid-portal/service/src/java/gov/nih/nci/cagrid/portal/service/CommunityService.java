/**
 * 
 */
package gov.nih.nci.cagrid.portal.service;

import gov.nih.nci.cagrid.portal.dao.catalog.CommunityCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.CommunityCatalogEntry;

import org.springframework.transaction.annotation.Transactional;

import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.service.GroupServiceUtil;
import com.liferay.portal.service.ServiceContext;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@Transactional
public class CommunityService extends CatalogEntryService {

	private CommunityCatalogEntryDao communityCatalogEntryDao;

	public void createCommunity(PortalUser user,
			CommunityCatalogEntry communityCe) throws Exception {

		final String communityName = communityCe.getName();
		final String communityDescription = communityCe.getDescription();

		try {
			long portalUserId = PortalServiceUtils.getPortalUserPortalId(user);
			Group group = (Group) execute(new DoAsUserThread(portalUserId) {
				@Override
				protected Object doRun() throws Exception {
					return GroupServiceUtil.addGroup(communityName,
							communityDescription,
							GroupConstants.TYPE_COMMUNITY_OPEN, "", true,
							new ServiceContext());

				}
			});

			communityCe.setCommunityId(group.getGroupId());
			communityCe.setCommunityUrl(group.getFriendlyURL());
			getCommunityCatalogEntryDao().save(communityCe);

		} catch (Exception ex) {
			throw new Exception("Error creating community: " + ex.getMessage(),
					ex);
		}
	}

	public void deleteCommunity(PortalUser portalUser,
			final CommunityCatalogEntry communityCe) throws Exception {
		try {
			
			final long communityId = communityCe.getCommunityId();
			execute(new DoAsUserThread(portalUser) {
				@Override
				protected Object doRun() throws Exception {
					GroupServiceUtil.deleteGroup(communityId);
					return null;
				}

			});
			
			deleteCatalogEntry(communityCe);
			
		} catch (Exception ex) {
			throw new Exception("Error deleting community: " + ex.getMessage(),
					ex);
		}
	}

	public void updateCommunity(PortalUser portalUser,
			final CommunityCatalogEntry communityCe) throws Exception {
		try {
			Group group = (Group) execute(new DoAsUserThread(portalUser) {

				@Override
				protected Object doRun() throws Exception {
					GroupServiceUtil.updateGroup(communityCe.getCommunityId(),
							communityCe.getName(),
							communityCe.getDescription(),
							GroupConstants.TYPE_COMMUNITY_OPEN, communityCe
									.getCommunityUrl(), true,
							new ServiceContext());
					return GroupServiceUtil.getGroup(communityCe
							.getCommunityId());
				}

			});

			communityCe.setCommunityUrl(group.getFriendlyURL());
			getCommunityCatalogEntryDao().save(communityCe);

		} catch (Exception ex) {
			throw new Exception("Error deleting community: " + ex.getMessage(),
					ex);
		}
	}

	public CommunityCatalogEntryDao getCommunityCatalogEntryDao() {
		return communityCatalogEntryDao;
	}

	public void setCommunityCatalogEntryDao(
			CommunityCatalogEntryDao communityCatalogEntryDao) {
		this.communityCatalogEntryDao = communityCatalogEntryDao;
	}
}
