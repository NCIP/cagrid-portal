/**
 * 
 */
package gov.nih.nci.cagrid.portal.service;

import gov.nih.nci.cagrid.portal.dao.IdPAuthenticationDao;
import gov.nih.nci.cagrid.portal.dao.IdentityProviderDao;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.domain.IdPAuthentication;
import gov.nih.nci.cagrid.portal.domain.IdentityProvider;
import gov.nih.nci.cagrid.portal.domain.PortalUser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class UserService {
	
	private PortalUserDao portalUserDao;
	private IdentityProviderDao identityProviderDao;
	private IdPAuthenticationDao idpAuthenticationDao;
	
	public void addCredential(PortalUser user, String idpUrl, String identity){
		
		IdentityProvider idp = getIdentityProviderDao().getByUrl(idpUrl);
		if(idp == null){
			throw new RuntimeException("Unknown identity provider: " + idpUrl);
		}
		
		IdPAuthentication idpAuthn = null;
		for (IdPAuthentication ia : user.getAuthentications()) {
			if (ia.getIdentity().equals(identity)) {
				idpAuthn = ia;
				break;
			}			
		}
		if (idpAuthn == null) {
			idpAuthn = new IdPAuthentication();
			idpAuthn.setIdentity(identity);
			idpAuthn.setIdentityProvider(idp);
			idpAuthn.setPortalUser(user);
			getIdpAuthenticationDao().save(idpAuthn);
			user.getAuthentications().add(idpAuthn);
			getPortalUserDao().save(user);
		}
	}
	
	public void setDefaultCredential(PortalUser user, String identity){
		List<IdPAuthentication> idpAuthns = new ArrayList<IdPAuthentication>();
		IdPAuthentication idpAuthn = null;
		for (IdPAuthentication ia : user.getAuthentications()) {
			if (ia.getIdentity().equals(identity)) {
				idpAuthn = ia;
				ia.setDefault(true);
			} else {
				ia.setDefault(false);
			}
			idpAuthns.add(ia);
			getIdpAuthenticationDao().save(ia);
		}
		if (idpAuthn == null) {
			throw new RuntimeException(
					"No authentication found for identity: " + identity);
		}
		user.setGridCredential(idpAuthn.getGridCredential());
		user.setAuthentications(idpAuthns);
		user.setGridIdentity(identity);
		getPortalUserDao().save(user);
	}
	
	public void setDefaultCredential(Integer userId, String identity){
		setDefaultCredential(getPortalUserDao().getById(userId), identity);
	}
	
	
	public PortalUserDao getPortalUserDao() {
		return portalUserDao;
	}
	public void setPortalUserDao(PortalUserDao portalUserDao) {
		this.portalUserDao = portalUserDao;
	}
	public IdentityProviderDao getIdentityProviderDao() {
		return identityProviderDao;
	}
	public void setIdentityProviderDao(IdentityProviderDao identityProviderDao) {
		this.identityProviderDao = identityProviderDao;
	}
	public IdPAuthenticationDao getIdpAuthenticationDao() {
		return idpAuthenticationDao;
	}
	public void setIdpAuthenticationDao(IdPAuthenticationDao idpAuthenticationDao) {
		this.idpAuthenticationDao = idpAuthenticationDao;
	}
	
	

}
