package gov.nih.nci.cagrid.portal.dao.aspects;

import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.dao.catalog.PersonCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.PersonCatalogEntry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@Aspect
public class PersonCatalogCreator {

    Log logger = LogFactory.getLog(getClass());
    PortalUserDao portalUserDao;
    PersonCatalogEntryDao personCatalogEntryDao;


    /**
     * Aspect will create catalog item when new service is regsitered
     *
     * @param portalUser
     */
    @AfterReturning("execution(* gov.nih.nci.cagrid.portal.dao.PortalUserDao.save*(gov.nih.nci.cagrid.portal.domain.PortalUser))  && args(portalUser)")
    public void onSave(PortalUser portalUser) {

        logger.debug("A new Portal User is being saved. Will make sure catalog exists");
        personCatalogEntryDao.createCatalogAbout(portalUser);
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
}
