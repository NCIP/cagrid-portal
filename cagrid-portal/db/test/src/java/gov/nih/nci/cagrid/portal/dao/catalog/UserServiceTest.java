/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.dao.catalog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import gov.nih.nci.cagrid.portal.AbstractDBTestBase;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRelationshipInstance;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRoleInstance;
import gov.nih.nci.cagrid.portal.domain.catalog.PersonCatalogEntry;

import org.junit.Before;
import org.junit.Test;

/**
 * @author joshua
 * 
 */
public class UserServiceTest extends AbstractDBTestBase {

	private PortalUserDao portalUserDao;
	private PersonCatalogEntryDao personCatalogEntryDao;
	private CatalogEntryRelationshipInstanceDao catalogEntryRelationshipInstanceDao;
	private CatalogEntryRoleInstanceDao catalogEntryRoleInstanceDao;

	@Before
	public void setup() {
		portalUserDao = (PortalUserDao) getApplicationContext().getBean(
				"portalUserDao");
		personCatalogEntryDao = (PersonCatalogEntryDao) getApplicationContext()
				.getBean("personCatalogEntryDao");
		catalogEntryRelationshipInstanceDao = (CatalogEntryRelationshipInstanceDao) getApplicationContext()
				.getBean("catalogEntryRelationshipInstanceDao");
		catalogEntryRoleInstanceDao = (CatalogEntryRoleInstanceDao) getApplicationContext()
				.getBean("catalogEntryRoleInstanceDao");
	}

	@Test
	public void deletePersonCatalogEntry() {

		try {

			PortalUser portalUser1 = createUserAndEntry();
			PortalUser portalUser2 = createUserAndEntry();

			CatalogEntryRoleInstance role1 = new CatalogEntryRoleInstance();
			role1.setCatalogEntry(portalUser1.getCatalog());
			catalogEntryRoleInstanceDao.save(role1);
			portalUser1.getCatalog().getRoles().add(role1);
			portalUserDao.save(portalUser1);

			CatalogEntryRoleInstance role2 = new CatalogEntryRoleInstance();
			role2.setCatalogEntry(portalUser2.getCatalog());
			catalogEntryRoleInstanceDao.save(role2);
			portalUser2.getCatalog().getRoles().add(role2);
			portalUserDao.save(portalUser2);

			CatalogEntryRelationshipInstance rel = new CatalogEntryRelationshipInstance();
			rel.setRoleA(role1);
			rel.setRoleB(role2);
			catalogEntryRelationshipInstanceDao.save(rel);

			role1.setRelationship(rel);
			catalogEntryRoleInstanceDao.save(role1);
			role2.setRelationship(rel);
			catalogEntryRoleInstanceDao.save(role2);

			portalUserDao.getHibernateTemplate().flush();

			interruptSession();

			assertEquals(2, catalogEntryRoleInstanceDao.getAll().size());
			assertEquals(1, catalogEntryRelationshipInstanceDao.getAll().size());
			assertEquals(2, portalUserDao.getAll().size());
			assertEquals(2, personCatalogEntryDao.getAll().size());

			portalUser1 = portalUserDao.getById(portalUser1.getId());
			for (CatalogEntryRoleInstance roleInst : portalUser1.getCatalog()
					.getRoles()) {
				CatalogEntryRelationshipInstance relInst = roleInst
						.getRelationship();
				catalogEntryRelationshipInstanceDao.delete(relInst);
			}
			portalUserDao.delete(portalUser1);
			portalUserDao.getHibernateTemplate().flush();

			interruptSession();

			assertEquals(1, portalUserDao.getAll().size());
			assertEquals(1, personCatalogEntryDao.getAll().size());
			assertEquals(0, catalogEntryRoleInstanceDao.getAll().size());
			assertEquals(0, catalogEntryRelationshipInstanceDao.getAll().size());

		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Error: " + ex.getMessage());
		}

	}

	private PortalUser createUserAndEntry() {
		PortalUser portalUser = new PortalUser();
		portalUserDao.save(portalUser);
		PersonCatalogEntry personCatalogEntry = new PersonCatalogEntry();
		personCatalogEntryDao.save(personCatalogEntry);
		portalUser.setCatalog(personCatalogEntry);
		portalUserDao.save(portalUser);
		personCatalogEntry.setAbout(portalUser);
		personCatalogEntry.setAuthor(portalUser);
		personCatalogEntryDao.save(personCatalogEntry);
		return portalUser;
	}

}
