/**
 * 
 */
package gov.nih.nci.cagrid.portal2.dao;

import gov.nih.nci.cagrid.portal2.domain.GridPortalUser;
import gov.nih.nci.cagrid.portal2.domain.PortalUser;
import gov.nih.nci.cagrid.portal2.domain.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dbunit.operation.DatabaseOperation;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class PortalUserDaoTest extends AbstractDaoTest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal2.dao.AbstractDaoTest#getDataSetFileName()
	 */
	@Override
	protected String getDataSetFileName() {
		// TODO Auto-generated method stub
		return getDataSetFileName("test/data/PortalUserDaoTest.xml");
	}

	protected DatabaseOperation getTearDownOperation() throws Exception {
		return DatabaseOperation.DELETE_ALL;
	}

	public void testPortalUser() {
		RoleDao roleDao = (RoleDao) getApplicationContext().getBean("roleDao");
		PortalUserDao portalUserDao = (PortalUserDao) getApplicationContext()
				.getBean("portalUserDao");
		GridPortalUserDao gridPortalUserDao = (GridPortalUserDao) getApplicationContext()
				.getBean("gridPortalUserDao");
		List<PortalUser> allUsers = portalUserDao.getAll();
		assertTrue("Should have retrieved 2 users. Got " + allUsers.size(),
				allUsers.size() == 2);
		PortalUser portalUser = null;
		GridPortalUser gridPortalUser = null;
		for (PortalUser user : allUsers) {
			if (user instanceof GridPortalUser) {
				gridPortalUser = (GridPortalUser) user;
			} else {
				portalUser = user;
			}
		}
		assertNotNull("PortalUser not found in collection", portalUser);
		assertNotNull("GridPortalUser not found in collection", gridPortalUser);
		List<GridPortalUser> gridUsers = gridPortalUserDao.getAll();
		assertTrue("Should have retrieved 1 GridPortalUser. Got "
				+ gridUsers.size(), gridUsers.size() == 1);
		GridPortalUser gridPortalUser2 = gridUsers.get(0);
		assertEquals("GridPortalUser objects are not the same", gridPortalUser
				.getId(), gridPortalUser2.getId());
		Set<String> expectedRoles = new HashSet<String>();
		expectedRoles.add("one");
		expectedRoles.add("two");
		for (Role role : portalUser.getRoles()) {
			expectedRoles.remove(role.getName());
		}
		assertTrue("PortalUser missing roles", expectedRoles.size() == 0);
		expectedRoles.add("three");
		expectedRoles.add("four");
		for (Role role : gridPortalUser.getRoles()) {
			expectedRoles.remove(role.getName());
		}
		assertTrue("GridPortalUser missing roles", expectedRoles.size() == 0);
		Role newRole = new Role();
		newRole.setName("five");
		roleDao.save(newRole);
		gridPortalUser.getRoles().add(newRole);
		gridPortalUserDao.save(gridPortalUser);
		gridPortalUser2 = gridPortalUserDao.getById(gridPortalUser.getId());
		expectedRoles.add("three");
		expectedRoles.add("four");
		expectedRoles.add("five");
		for (Role role : gridPortalUser2.getRoles()) {
			expectedRoles.remove(role.getName());
		}
		assertTrue("GridPortalUser missing roles (2)",
				expectedRoles.size() == 0);
		GridPortalUser newGridUser = new GridPortalUser();
		newGridUser.setUsername("yadda");
		newGridUser.setPassword("dadda");
		portalUserDao.save(newGridUser);
		gridUsers = gridPortalUserDao.getAll();
		assertTrue("Should have retrieved 2 GridPortalUser objects. Got "
				+ gridUsers.size(), gridUsers.size() == 2);

	}

}
