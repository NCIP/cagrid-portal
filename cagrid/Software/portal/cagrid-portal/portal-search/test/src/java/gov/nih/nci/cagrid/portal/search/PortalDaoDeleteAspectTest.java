package gov.nih.nci.cagrid.portal.search;

import gov.nih.nci.cagrid.portal.dao.catalog.PersonCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.catalog.PersonCatalogEntry;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PortalDaoDeleteAspectTest extends PortalDaoAspectTestBase {


    public void testDelete() {
        PersonCatalogEntry pCE = new PersonCatalogEntry();
        PersonCatalogEntryDao pCEDao = (PersonCatalogEntryDao) getApplicationContext().getBean("personCatalogEntryDao");
        pCEDao.save(pCE);
        MockHttpClient.assertJustRan();

        pCEDao.delete(pCE);
        MockHttpClient.assertJustRan();
    }
}
