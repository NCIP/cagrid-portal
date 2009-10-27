package gov.nih.nci.cagrid.portal.catalog;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.PersonDao;
import gov.nih.nci.cagrid.portal.dao.catalog.PointOfContactCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.portal.domain.metadata.service.Service;
import gov.nih.nci.cagrid.portal.domain.metadata.service.ServicePointOfContact;
import gov.nih.nci.cagrid.portal.util.PortalAggrIntegrationTestBase;

import java.util.ArrayList;
import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PointOfContactCatalogAspectTest extends PortalAggrIntegrationTestBase {

    private GridServiceDao gridServiceDao;
    private PersonDao personDao;
    private PointOfContactCatalogEntryDao pointOfContactCatalogEntryDao;

    @Override
    protected String[] getConfigLocations() {
        return new String[]{
                "classpath*:applicationContext-aggr-catalog-aspects.xml",
                "applicationContext-service.xml",
                "applicationContext-db.xml"
        };
    }

    public void testCreate() {
        GridService service = new GridService();
        gridServiceDao.save(service);

        assertEquals(pointOfContactCatalogEntryDao.getAll().size(), 0);
    }


    public void testCreateWithPOC() {
        GridService service = new GridService();

        List<ServicePointOfContact> pointOfContactCollection = new ArrayList();
        ServicePointOfContact poc = new ServicePointOfContact();
        pointOfContactCollection.add(poc);

        Service desc = new Service();
        desc.setPointOfContactCollection(pointOfContactCollection);

        ServiceMetadata serviceMeta = new ServiceMetadata();
        serviceMeta.setServiceDescription(desc);
        service.setServiceMetadata(serviceMeta);
        gridServiceDao.save(service);

        assertEquals(pointOfContactCatalogEntryDao.getAll().size(), 0);

        Person p = new Person();
        p.setEmailAddress("person@email.org");
        personDao.save(p);

        ServicePointOfContact poc2 = new ServicePointOfContact();
        poc2.setPerson(p);
        pointOfContactCollection.add(poc2);

        gridServiceDao.save(service);
        assertEquals(pointOfContactCatalogEntryDao.getAll().size(), 1);

    }

    public GridServiceDao getGridServiceDao() {
        return gridServiceDao;
    }

    public void setGridServiceDao(GridServiceDao gridServiceDao) {
        this.gridServiceDao = gridServiceDao;
    }

    public PointOfContactCatalogEntryDao getPointOfContactCatalogEntryDao() {
        return pointOfContactCatalogEntryDao;
    }

    public void setPointOfContactCatalogEntryDao(PointOfContactCatalogEntryDao pointOfContactCatalogEntryDao) {
        this.pointOfContactCatalogEntryDao = pointOfContactCatalogEntryDao;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
