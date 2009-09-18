package gov.nih.nci.cagrid.portal.aggr.catalog;

import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.dao.catalog.PersonCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@Transactional
public class PersonCatalogCreator extends AbstractCatalogCreator {

    PersonCatalogEntryDao personCatalogEntryDao;
    PortalUserDao portalUserDao;


    public void afterPropertiesSet() throws Exception {

        for (PortalUser user : portalUserDao.getAll()) {

            if (personCatalogEntryDao.isAbout(user) == null) {
                logger.debug("Person catalog not found. WIll create for id " + user.getId());
                personCatalogEntryDao.createCatalogAbout(user);

            }
        }
    }

    public PersonCatalogEntryDao getPersonCatalogEntryDao() {
        return personCatalogEntryDao;
    }

    @Required
    public void setPersonCatalogEntryDao(PersonCatalogEntryDao personCatalogEntryDao) {
        this.personCatalogEntryDao = personCatalogEntryDao;
    }

    public PortalUserDao getPortalUserDao() {
        return portalUserDao;
    }

    @Required
    public void setPortalUserDao(PortalUserDao portalUserDao) {
        this.portalUserDao = portalUserDao;
    }
}
