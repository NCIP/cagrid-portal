package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.DaoTestBase;
import gov.nih.nci.cagrid.portal.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal.domain.Address;
import gov.nih.nci.cagrid.portal.domain.Participant;
import gov.nih.nci.cagrid.portal.domain.catalog.InstitutionCatalogEntry;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class InstitutionCatalogEntryDaoTest extends DaoTestBase<InstitutionCatalogEntryDao> {


    ParticipantDao pDao;
    Participant p;


    @Before
    public void setup() {
        pDao = (ParticipantDao) getApplicationContext().getBean("participantDao");
        p = new Participant();
    }

    @Test
    public void createWithNoAbout() {
        InstitutionCatalogEntry entry = new InstitutionCatalogEntry();
        getDao().save(entry);
        InstitutionCatalogEntry loaded = getDao().getById(1);
        assertNotNull(loaded);
        assertNull(loaded.getAbout());
    }

    @Test
    public void createAbout() {

        pDao.save(p);

        InstitutionCatalogEntry catalog = new InstitutionCatalogEntry();
        catalog.setAbout(p);
        // has to be set on both sides
        p.setCatalog(catalog);
        getDao().save(catalog);

        assertNotNull(getDao().isAbout(p));

    }

    @Test
    public void createWithAddress() {
        try {
            Address mockAddress = mock(Address.class);
            when(mockAddress.getCountry()).thenReturn("US");
            when(mockAddress.getStateProvince()).thenReturn("MD");
            p.setAddress(mockAddress);

            getDao().createCatalogAbout(p);
            getDao().isAbout(p);
            InstitutionCatalogEntry entry = getDao().getById(1);

            assertNotNull(entry.getStateProvince());
            assertNotNull(entry.getCountryCode());
            assertNull(entry.getStreet1());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    // test to see if deleting the Participant deletes the catalog as well
    @Test
    public void delete() {
        pDao.save(p);

        InstitutionCatalogEntry catalog = new InstitutionCatalogEntry();
        catalog.setAbout(p);
        p.setCatalog(catalog);
        getDao().save(catalog);

        assertEquals(1, getDao().getAll().size());

        Participant loadedP = pDao.getById(1);
        pDao.delete(loadedP);

        interruptSession();
        assertEquals(0, getDao().getAll().size());

    }

}
