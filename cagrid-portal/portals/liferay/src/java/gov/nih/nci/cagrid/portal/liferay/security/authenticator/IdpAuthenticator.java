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
package gov.nih.nci.cagrid.portal.liferay.security.authenticator;

import com.liferay.portal.UserPasswordException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.AuthException;
import com.liferay.portal.security.auth.Authenticator;
import com.liferay.portal.service.UserLocalServiceUtil;
import gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault;
import gov.nih.nci.cagrid.portal.authn.AuthnServiceException;
import gov.nih.nci.cagrid.portal.authn.EncryptionService;
import gov.nih.nci.cagrid.portal.authn.ProxyUtil;
import gov.nih.nci.cagrid.portal.authn.domain.IdPAuthnInfo;
import gov.nih.nci.cagrid.portal.dao.IdentityProviderDao;
import gov.nih.nci.cagrid.portal.dao.PersonDao;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.dao.catalog.PersonCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.IdentityProvider;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.liferay.security.util.LiferayLoginHelper;
import gov.nih.nci.cagrid.portal.security.AuthnService;

import gov.nih.nci.cagrid.portal.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.gsi.GlobusCredential;

import java.util.Map;

/**
 * caGrid Idp authentication
 * <p/>
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class IdpAuthenticator implements Authenticator {

    private PersonDao personDao;
    private PortalUserDao portalUserDao;
    private PersonCatalogEntryDao personCatalogEntryDao;
    private AuthnService authnService;
    private UserService userService;
    private IdentityProviderDao identityProviderDao;
    private String ifsUrl;
    private String companyWebId;
    private String omniUserEmail;
    private EncryptionService encryptionService;

    private LiferayLoginHelper liferayLoginHelper;
    private Log log = LogFactory.getLog(getClass());

    public int authenticateByEmailAddress(long companyId, String emailAddress, String password, Map<String, String[]> headerMap, Map<String, String[]> parameterMap) throws AuthException {
        return authenticate(companyId, emailAddress, password, headerMap, parameterMap);
    }

    public int authenticateByUserId(
            long companyId, long userId, String password,
            Map<String, String[]> headerMap, Map<String, String[]> parameterMap)
            throws AuthException {
        return authenticate(companyId, String.valueOf(userId), password, headerMap, parameterMap);
    }

    // we will support this with Dorian
    public int authenticateByScreenName(long companyId, String login, String password,
                                        Map<String, String[]> headerMap, Map<String, String[]> parameterMap) throws AuthException {
        return authenticate(companyId, login, password, headerMap, parameterMap);
    }


    public int authenticate(long companyId, String login, String password,
                            Map<String, String[]> headerMap, Map<String, String[]> parameterMap) throws AuthException {

        log.debug("Will authenticate user against the caGrid Idp");
        if (Validator.isNull(password)) {
            throw new AuthException("" +
                    UserPasswordException.PASSWORD_INVALID);
        }

        int authResult = Authenticator.FAILURE;

        // Do authentication
        String idpUrl = parameterMap.get("identityProvider")[0];
        String idpLabel = parameterMap.get("identityProviderLabel")[0];

        IdPAuthnInfo authnInfo = null;
        GlobusCredential globusCred = null;
        try {
            authnInfo = getAuthnService().authenticateToIdP(login,
                    password, getIfsUrl());
            globusCred = getAuthnService().authenticateToIFS(idpUrl,
                    authnInfo.getSaml());
        } catch (InvalidCredentialFault ex) {
            throw new AuthException(ex.getFaultString());
        } catch (Exception ex) {
            log.info("Could not authenticate user " + ex.getMessage());
            throw new AuthException(ex.getMessage());
        }
        authResult = Authenticator.SUCCESS;


        // Set credential to default
        IdentityProvider idp = getIdentityProviderDao().getByUrl(idpUrl);
        if (idp == null) {
            idp = new IdentityProvider();
            idp.setLabel(idpLabel);
            idp.setUrl(idpUrl);
            getIdentityProviderDao().save(idp);
        }
        User user = null;


        PortalUser sampleUser = new PortalUser();
        sampleUser.setGridIdentity(globusCred.getIdentity());
        PortalUser pUser = getPortalUserDao().getByExample(sampleUser);

        if (pUser != null) {
            String[] portalId = pUser.getPortalId().split(":");
            try {
                user = UserLocalServiceUtil.getUserById(Integer
                        .parseInt(portalId[0]), Integer
                        .parseInt(portalId[1]));
            } catch (Exception e) {
                throw new AuthException("Portal user not found in Liferay DB", e);
            }
        }


        if (user == null) {
            try {
                log.debug("User is new, will create in Liferay");
                user = liferayLoginHelper.addLiferayUser(authnInfo);
            } catch (Exception e1) {
                String msg = "Could not create user in the Portal DB";
                log.warn(msg);
                throw new AuthException(msg);
            }
        }

        String portalUserId = companyId + ":" + user.getUserId();
        PortalUser portalUser = getPortalUserDao().getByPortalId(portalUserId);
        if (portalUser == null) {
            String msg = "Couldn't find user for " + portalUserId;
            log.warn(msg);
            throw new AuthException(msg);
        }

        getUserService().addCredential(portalUser, idpUrl, globusCred
                .getIdentity());
        try {
            String cred = getEncryptionService().encrypt(ProxyUtil.getProxyString(globusCred));
            getUserService().setDefaultCredential(portalUser, globusCred
                    .getIdentity(), cred);

        } catch (AuthnServiceException e) {
            throw new AuthException("Could not encrypt proxy ", e);
        }
        return authResult;
    }


    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public PortalUserDao getPortalUserDao() {
        return portalUserDao;
    }

    public void setPortalUserDao(PortalUserDao portalUserDao) {
        this.portalUserDao = portalUserDao;
    }

    public PersonCatalogEntryDao getPersonCatalogEntryDao() {
        return personCatalogEntryDao;
    }

    public void setPersonCatalogEntryDao(PersonCatalogEntryDao personCatalogEntryDao) {
        this.personCatalogEntryDao = personCatalogEntryDao;
    }

    public AuthnService getAuthnService() {
        return authnService;
    }

    public void setAuthnService(AuthnService authnService) {
        this.authnService = authnService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public IdentityProviderDao getIdentityProviderDao() {
        return identityProviderDao;
    }

    public void setIdentityProviderDao(IdentityProviderDao identityProviderDao) {
        this.identityProviderDao = identityProviderDao;
    }

    public String getIfsUrl() {
        return ifsUrl;
    }

    public void setIfsUrl(String ifsUrl) {
        this.ifsUrl = ifsUrl;
    }

    public String getCompanyWebId() {
        return companyWebId;
    }

    public void setCompanyWebId(String companyWebId) {
        this.companyWebId = companyWebId;
    }

    public String getOmniUserEmail() {
        return omniUserEmail;
    }

    public void setOmniUserEmail(String omniUserEmail) {
        this.omniUserEmail = omniUserEmail;
    }

    public LiferayLoginHelper getLiferayLoginHelper() {
        return liferayLoginHelper;
    }

    public void setLiferayLoginHelper(LiferayLoginHelper liferayLoginHelper) {
        this.liferayLoginHelper = liferayLoginHelper;
    }

    public EncryptionService getEncryptionService() {
        return encryptionService;
    }

    public void setEncryptionService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }
}

