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
package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.dao.PointOfContactDao;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.catalog.PointOfContactCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.portal.domain.metadata.common.ResearchCenterPointOfContact;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PointOfContactCatalogEntryDaoTest extends CatalogEntryDaoTestBase<PointOfContactCatalogEntryDao> {
    PointOfContactDao pocDao;

    @Before
    public void init() {
        pocDao = (PointOfContactDao) getApplicationContext().getBean("pointOfContactDao");
    }

    @Test
    public void createWithNoAbout() {
        PointOfContactCatalogEntry entry = new PointOfContactCatalogEntry();
        getDao().save(entry);
        PointOfContactCatalogEntry loaded = getDao().getAll().get(0);
        assertNotNull(loaded);
    }

    @Test
    public void createWithEmail() {
        PointOfContact poc = new ResearchCenterPointOfContact();
        poc.setPerson(p.getPerson());
        pocDao.save(poc);

        getDao().createCatalogAbout(poc);

        assertNotNull(getDao().isAbout(poc));
        assertNotNull(getDao().getAll().get(0));
    }

    @Test
    public void createWithDuplicateEmail() {
        PointOfContact poc = new ResearchCenterPointOfContact();
        poc.setPerson(p.getPerson());
        pocDao.save(poc);

        getDao().createCatalogAbout(poc);
        assertNotNull(getDao().isAbout(poc));
        assertNotNull(getDao().getAll().get(0));
        assertEquals(getDao().getAll().size(), 1);

        PointOfContact poc2 = new ResearchCenterPointOfContact();
        Person p2 = new Person();
        p2.setEmailAddress("email2");
        poc2.setPerson(p2);
        personDao.save(p2);
        pocDao.save(poc2);

        getDao().createCatalogAbout(poc2);
        assertNotNull(getDao().isAbout(poc2));
        assertNotNull(getDao().getAll().get(1));
        assertEquals(getDao().getAll().size(), 2);

        //this should not create a new CE
        PointOfContact poc3 = new ResearchCenterPointOfContact();
        Person p3 = new Person();
        p3.setEmailAddress("email2");
        poc3.setPerson(p3);
        personDao.save(p3);
        pocDao.save(poc3);

        getDao().createCatalogAbout(poc3);
        assertNotNull(getDao().isAbout(poc3));
        assertNotNull(getDao().getAll().get(1));
        assertEquals(getDao().getAll().size(), 2);

    }

}
