package gov.nih.nci.cagrid.portal.portlet.browse.ajax;

import gov.nih.nci.cagrid.portal.dao.PersonDao;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.portlet.PortalPortletIntegrationTestBase;
import gov.nih.nci.cagrid.portal.portlet.UserModel;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class AbstractCatalogManagerFacadeTest<T extends CatalogEntryManagerFacade> extends PortalPortletIntegrationTestBase {

    protected T facade;
    private CatalogEntryDao catalogEntryDao;
    private PortalUserDao portalUserDao;
    private PersonDao personDao;

    CatalogEntry ce;


    @Override
    protected void onSetUp() throws Exception {
        super.onSetUp();
        ce = new CatalogEntry();
        getCatalogEntryDao().save(ce);


        Person p = new Person();
        personDao.save(p);

        PortalUser user = new PortalUser();
        user.setPerson(p);
        portalUserDao.save(user);

        facade = loadFacade();
        UserModel model = mock(UserModel.class);
        when(model.getPortalUser()).thenReturn(user);
        when(model.getCurrentCatalogEntry()).thenReturn(ce);

        facade.setUserModel(model);
    }

    protected T loadFacade() {
        return (T) getApplicationContext().getBean(getNamingStrategy().substring(0, 1).toLowerCase() + getNamingStrategy().substring(1, getNamingStrategy().indexOf("Test")));

    }

    protected String getNamingStrategy() {
        return getClass().getSimpleName();
    }


    public CatalogEntryDao getCatalogEntryDao() {
        return catalogEntryDao;
    }

    public void setCatalogEntryDao(CatalogEntryDao catalogEntryDao) {
        this.catalogEntryDao = catalogEntryDao;
    }

    public PortalUserDao getPortalUserDao() {
        return portalUserDao;
    }

    public void setPortalUserDao(PortalUserDao portalUserDao) {
        this.portalUserDao = portalUserDao;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
