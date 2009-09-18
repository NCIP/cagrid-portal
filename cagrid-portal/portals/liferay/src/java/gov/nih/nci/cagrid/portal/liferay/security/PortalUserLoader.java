package gov.nih.nci.cagrid.portal.liferay.security;

import com.liferay.portal.model.User;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import org.acegisecurity.AuthenticationCredentialsNotFoundException;
import org.cagrid.websso.client.acegi.WebSSOUser;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PortalUserLoader {

    PortalUserDao portalUserDao;

    public PortalUser getPortalUser(WebSSOUser webSSOUser) {
        PortalUser portalUser = new PortalUser();
        portalUser.setGridIdentity(webSSOUser.getGridId());
        portalUser = getPortalUserDao().getByExample(portalUser);
        return portalUser;
    }


    public PortalUser getPortalUser(User user) throws AuthenticationCredentialsNotFoundException {
        PortalUser portalUser = new PortalUser();
        portalUser.setPortalId(user.getCompanyId() + ":" + user.getUserId());
        portalUser = getPortalUserDao().getByExample(portalUser);
        if (portalUser == null) {
            throw new AuthenticationCredentialsNotFoundException("Portal user not found in Portal DB.");
        }

        return portalUser;

    }

    public PortalUserDao getPortalUserDao() {
        return portalUserDao;
    }

    public void setPortalUserDao(PortalUserDao portalUserDao) {
        this.portalUserDao = portalUserDao;
    }
}
