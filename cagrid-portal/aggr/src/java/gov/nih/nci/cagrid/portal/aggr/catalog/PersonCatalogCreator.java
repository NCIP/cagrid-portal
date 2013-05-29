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
                logger.info("Person catalog not found. WIll create for id " + user.getId());
                try {
                    personCatalogEntryDao.createCatalogAbout(user);
                } catch (Exception e) {
                    logger.warn("Error cresting Person catalog for ID " + user.getId() + ". Will skip");
                }

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
