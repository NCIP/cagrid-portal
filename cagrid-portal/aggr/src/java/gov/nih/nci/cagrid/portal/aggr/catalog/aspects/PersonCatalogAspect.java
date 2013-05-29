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
package gov.nih.nci.cagrid.portal.aggr.catalog.aspects;

import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.dao.catalog.PersonCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
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
public class PersonCatalogAspect {

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
