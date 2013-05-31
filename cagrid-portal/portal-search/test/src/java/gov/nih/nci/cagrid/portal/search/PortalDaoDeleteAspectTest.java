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
