/**
 * 
 */
package gov.nih.nci.cagrid.portal2.webauthn.common;

import gov.nih.nci.cagrid.portal2.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal2.dao.RoleDao;
import gov.nih.nci.cagrid.portal2.domain.PortalUser;
import gov.nih.nci.cagrid.portal2.domain.Role;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class AppInit implements InitializingBean {

	private RoleDao roleDao;

	private PortalUserDao portalUserDao;

	private List<Role> defaultRoles;

	private PortalUser adminUser;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Transactional
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.defaultRoles,
				"The defaultRoles property is required.");
		Assert.notNull(this.adminUser, "The adminUser property is required.");
		Assert.notNull(this.roleDao, "The roleDao property is required.");
		Assert.notNull(this.portalUserDao,
				"The portalUserDao property is required.");

		for (Role role : getDefaultRoles()) {
			Role r = getRoleDao().getByExample(role);
			if (r == null) {
				getRoleDao().save(role);
			}
		}

		PortalUser adminEg = getAdminUser();
		PortalUser admin = getPortalUserDao().getByExample(adminEg);
		if (admin == null) {
			for (Role role : adminEg.getRoles()) {
				List<Role> roles = new ArrayList<Role>();
				Role r = getRoleDao().getByExample(role);
				if (r == null) {
					getRoleDao().save(role);
					roles.add(role);
				}else{
					roles.add(r);
				}
				adminEg.setRoles(roles);
			}
			getPortalUserDao().save(adminEg);
		}


	}

	public PortalUser getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(PortalUser adminUser) {
		this.adminUser = adminUser;
	}

	public List<Role> getDefaultRoles() {
		return defaultRoles;
	}

	public void setDefaultRoles(List<Role> defaultRoles) {
		this.defaultRoles = defaultRoles;
	}

	public PortalUserDao getPortalUserDao() {
		return portalUserDao;
	}

	public void setPortalUserDao(PortalUserDao portalUserDao) {
		this.portalUserDao = portalUserDao;
	}

	public RoleDao getRoleDao() {
		return roleDao;
	}

	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

}
