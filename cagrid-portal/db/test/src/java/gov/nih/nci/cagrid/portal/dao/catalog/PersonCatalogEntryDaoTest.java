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

import gov.nih.nci.cagrid.portal.domain.Address;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.PersonCatalogEntry;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;


/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PersonCatalogEntryDaoTest extends CatalogEntryDaoTestBase<PersonCatalogEntryDao> {


    @Test
    public void createWithNoAbout() {
        PersonCatalogEntry entry = new PersonCatalogEntry();
        getDao().save(entry);
        PersonCatalogEntry loaded = getDao().getById(1);
        assertNotNull(loaded);
    }

    @Test
    public void createWithAddress() {
        try {
            Person person = p.getPerson();
            Address add = new Address();
            add.setCountry("US");
            add.setStateProvince("MD");

            List<Address> addresses = new ArrayList<Address>();
            addresses.add(add);
            person.setAddresses(addresses);
            p.setPerson(person);
            interruptSession();


            getDao().createCatalogAbout(p);
            getDao().isAbout(p);
            PersonCatalogEntry entry = getDao().getById(1);
            assertEquals("email", entry.getEmailAddress());
            assertNotNull(entry.getStateProvince());
            assertNotNull(entry.getCountryCode());
            assertNull(entry.getStreet1());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @Test
    public void create() {
        try {
            getDao().createCatalogAbout(p);
            getDao().isAbout(p);
            PersonCatalogEntry entry = getDao().getById(1);
            assertEquals("email", entry.getEmailAddress());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void about() {

        PersonCatalogEntry catalog = new PersonCatalogEntry();
        catalog.setAbout(p);
        p.setCatalog(catalog);
        getDao().save(catalog);

        assertNotNull(getDao().isAbout(p));

    }


    // test to see if deleting the Participant deletes the catalog as well
    @Test
    public void delete() {


        PersonCatalogEntry catalog = new PersonCatalogEntry();
        catalog.setAbout(p);
        p.setCatalog(catalog);
        getDao().save(catalog);

        assertEquals(1, getDao().getAll().size());

        PortalUser loadedP = pDao.getById(1);
        pDao.delete(loadedP);

        interruptSession();
        assertEquals(0, getDao().getAll().size());

    }

    public void cleanUp() {
        for (PersonCatalogEntry entry : getDao().getAll())
            getDao().delete(entry);
    }

}
