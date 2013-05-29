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
