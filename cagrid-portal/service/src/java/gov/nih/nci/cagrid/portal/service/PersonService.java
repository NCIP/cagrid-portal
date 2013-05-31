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
