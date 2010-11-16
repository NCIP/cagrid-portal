package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.DaoTestBase;
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.catalog.GridDataServiceEndPointCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.GridServiceEndPointCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.util.List;

import static org.junit.Assert.*;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class GridServiceEndPointCatalogEntryDaoTest extends
        DaoTestBase<GridServiceEndPointCatalogEntryDao> {

    GridServiceDao pDao;
    GridService p;

    @Before
    public void setup() {
        pDao = (GridServiceDao) getApplicationContext().getBean(
                "gridServiceDao");
        p = new GridService();

    }

    @Test
    public void hide() {
        GridServiceEndPointCatalogEntry entry = new GridServiceEndPointCatalogEntry();
        getDao().save(entry);
        assertEquals(false, getDao().getById(1).isHidden());
        getDao().hide(entry);
        assertEquals(true, getDao().getById(1).isHidden());

    }

    @Test
    public void createWithNoAbout() {
        GridServiceEndPointCatalogEntry entry = new GridServiceEndPointCatalogEntry();
        getDao().save(entry);
        GridServiceEndPointCatalogEntry loaded = getDao().getById(1);
        assertNotNull(loaded);
        assertNull(loaded.getAbout());
    }

    @Test
    public void createAbout() {
        pDao.save(p);

        GridServiceEndPointCatalogEntry catalog = new GridServiceEndPointCatalogEntry();
        catalog.setAbout(p);
        p.setCatalog(catalog);
        pDao.save(p);
        getDao().save(catalog);

        assertNotNull(getDao().isAbout(p));
    }

    @Test
    public void dataType() {
        pDao.save(p);

        GridDataServiceEndPointCatalogEntry catalog = new GridDataServiceEndPointCatalogEntry();
        catalog.setAbout(p);
        p.setCatalog(catalog);
        pDao.save(p);
        getDao().save(catalog);

        assertNotNull(getDao().isAbout(p));
        assertNotNull(getDao().getAll());
    }

    // test to see if deleting the Participant deletes the catalog as well

    @Test
    public void delete() {

        pDao.save(p);

        GridServiceEndPointCatalogEntry catalog = new GridServiceEndPointCatalogEntry();
        catalog.setAbout(p);
        p.setCatalog(catalog);
        pDao.save(p);
        getDao().save(catalog);

        assertEquals(1, getDao().getAll().size());

        GridService loadedP = pDao.getById(1);
        pDao.delete(loadedP);

        interruptSession();
        assertEquals(0, getDao().getAll().size());

    }

    @Test
    public void getByPartialUrl() {
        p.setUrl("http://complete.url");
        pDao.save(p);

        GridServiceEndPointCatalogEntry catalog = new GridServiceEndPointCatalogEntry();
        catalog.setAbout(p);
        p.setCatalog(catalog);
        getDao().save(catalog);

        assertEquals(1, getDao().getByPartialUrl("http://").size());
        assertEquals(1, getDao().getByPartialUrl("url").size());
        assertEquals(1, getDao().getByPartialUrl("complete").size());
        assertEquals(0, getDao().getByPartialUrl("someother.url").size());


    }

    /**
     * Test to see tha that hidden catalogs
     * are not returned
     * 
     */
    @Test
    public void getByPartialUrlHiddenCatalogs() {
        p.setUrl("http://complete.url");
        pDao.save(p);

        GridServiceEndPointCatalogEntry catalog = new GridServiceEndPointCatalogEntry();
        catalog.setAbout(p);
        p.setCatalog(catalog);
        catalog.setHidden(true);
        getDao().save(catalog);

        assertEquals(0, getDao().getByPartialUrl("http://").size());
        assertEquals(0, getDao().getByPartialUrl("url").size());

//        un-hide the catalog
        catalog.setHidden(false);
        getDao().save(catalog);
        interruptSession();
        assertEquals(1, getDao().getByPartialUrl("http://").size());
        assertEquals(1, getDao().getByPartialUrl("url").size());
    }

    @Test
    public void getByUmlClassNameAndPartialUrl() {

        HibernateTemplate templ = getDao().getHibernateTemplate();

        for (int i = 0; i < 2; i++) {
            GridDataService dSvc1 = new GridDataService();
            dSvc1.setUrl("http://dSvc" + i);
            templ.save(dSvc1);
            DomainModel model1 = new DomainModel();
            model1.setService(dSvc1);
            templ.save(model1);
            dSvc1.setDomainModel(model1);
            templ.save(dSvc1);
            UMLClass umlClass1 = new UMLClass();
            umlClass1.setPackageName("pkg" + i);
            umlClass1.setClassName("Class" + i);
            umlClass1.setModel(model1);
            templ.save(umlClass1);
            model1.getClasses().add(umlClass1);
            templ.save(model1);
            GridServiceEndPointCatalogEntry ce = new GridServiceEndPointCatalogEntry();
            ce.setName("CE" + i);
            ce.setAbout(dSvc1);
            templ.save(ce);
            dSvc1.setCatalog(ce);
            templ.save(dSvc1);
        }

        GridService svc1 = new GridService();
        svc1.setUrl("http://svc1");
        templ.save(svc1);
        GridServiceEndPointCatalogEntry ce = new GridServiceEndPointCatalogEntry();
        ce.setAbout(svc1);
        templ.save(ce);
        svc1.setCatalog(ce);
        templ.save(svc1);

        assertEquals(2, getDao().getByPartialUrl("dSvc").size());

        List<GridServiceEndPointCatalogEntry> ces = getDao()
                .getByUmlClassNameAndPartialUrl("pkg1", "Class1", "dSvc");
        assertEquals(1, ces.size());

    }

}
