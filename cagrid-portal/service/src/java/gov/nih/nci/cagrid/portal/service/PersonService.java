package gov.nih.nci.cagrid.portal.service;

import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.PersonCatalogEntry;

import org.springframework.transaction.annotation.Transactional;

import com.liferay.portal.service.UserServiceUtil;

@Transactional
public class PersonService extends CatalogEntryService {

	private PortalUserDao portalUserDao;
	
	
	public void deletePerson(PortalUser portalUser, PersonCatalogEntry personCe)
			throws Exception {
		try {
			PortalUser toDelete = personCe.getAbout();
			if (toDelete != null) {
				final long toDeleteId = PortalServiceUtils
						.getPortalUserPortalId(toDelete);
				execute(new DoAsUserThread(portalUser) {
					@Override
					protected Object doRun() throws Exception {
						UserServiceUtil.deleteUser(toDeleteId);
						return null;
					}

				});
			}
			getPortalUserDao().delete(toDelete);
			deleteCatalogEntry(personCe);
			
		} catch (Exception ex) {
			throw new Exception("Error deleting person: " + ex.getMessage(), ex);
		}
	}

	public PortalUserDao getPortalUserDao() {
		return portalUserDao;
	}

	public void setPortalUserDao(PortalUserDao portalUserDao) {
		this.portalUserDao = portalUserDao;
	}

}
