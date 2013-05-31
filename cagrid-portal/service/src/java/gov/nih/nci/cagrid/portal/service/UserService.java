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
/**
 *
 */
package gov.nih.nci.cagrid.portal.service;

import gov.nih.nci.cagrid.portal.dao.IdPAuthenticationDao;
import gov.nih.nci.cagrid.portal.dao.IdentityProviderDao;
import gov.nih.nci.cagrid.portal.dao.PersonDao;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.dao.catalog.PersonCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.IdPAuthentication;
import gov.nih.nci.cagrid.portal.domain.IdentityProvider;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.PersonCatalogEntry;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 */
@Transactional
public class UserService {

    private PortalUserDao portalUserDao;
    private IdentityProviderDao identityProviderDao;
    private IdPAuthenticationDao idpAuthenticationDao;
    private PersonCatalogEntryDao personCatalogEntryDao;
    private PersonDao personDao;

    public PersonCatalogEntryDao getPersonCatalogEntryDao() {
        return personCatalogEntryDao;
    }

    public void setPersonCatalogEntryDao(PersonCatalogEntryDao personCatalogEntryDao) {
        this.personCatalogEntryDao = personCatalogEntryDao;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public void addCredential(PortalUser user, String idpUrl, String identity) {
        user = getPortalUserDao().getByExample(user);

        IdentityProvider idp = getIdentityProviderDao().getByUrl(idpUrl);

        if (idp == null) {
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

    public void setDefaultCredential(PortalUser user, String identity, String gridCredential) {
        user = getPortalUserDao().getByExample(user);
        List<IdPAuthentication> idpAuthns = new ArrayList<IdPAuthentication>();
        IdPAuthentication idpAuthn = null;
        for (IdPAuthentication idpa : user.getAuthentications()) {
            idpAuthn = idpa;
            if (idpAuthn.getIdentity().equals(identity)) {
                idpAuthn.setGridCredential(gridCredential);
                idpAuthn.setDefault(true);
            } else {
                idpAuthn.setDefault(false);
            }
            idpAuthns.add(idpAuthn);
            getIdpAuthenticationDao().save(idpAuthn);
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


    public PortalUser getOrCreatePortalUser(String gridIdentity, String email,
                                            String firstName, String lastName, String encryptedProxyStr) {
        PortalUser portalUser = new PortalUser();
        portalUser.setGridIdentity(gridIdentity);
        portalUser = getPortalUserDao().getByExample(portalUser);
        if (portalUser == null) {
            portalUser = new PortalUser();
            portalUser.setGridIdentity(gridIdentity);
            Person person = new Person();
            person.setEmailAddress(email);
            person.setFirstName(firstName);
            person.setLastName(lastName);
            getPersonDao().save(person);
            portalUser.setPerson(person);
        }
        portalUser.setGridCredential(encryptedProxyStr);
        getPortalUserDao().save(portalUser);
        PersonCatalogEntry personCe = portalUser.getCatalog();
        personCe.setAuthor(portalUser);
        getPersonCatalogEntryDao().save(personCe);
        return portalUser;
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
