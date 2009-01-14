package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.StatusChange;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.GridServiceDaoTest;
import gov.nih.nci.cagrid.portal.DBTestBase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.beans.BeansException;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import junit.framework.TestCase;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class AcivateFilterAspectTest extends DBTestBase<GridServiceDao> {

    ApplicationContext applicationContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        applicationContext = new ClassPathXmlApplicationContext(
                new String[]{"classpath:applicationContext-db-aspects.xml"});

        SessionFactory sessionFactory = (SessionFactory) applicationContext
                .getBean("sessionFactory");

        Session session = SessionFactoryUtils.getSession(sessionFactory, true);
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
    }

    public void testEnableFilter(){


        GridService newService3  = new GridService();
        newService3.setUrl("http://new3");
        for(int i=0;i<10000;i++){
            StatusChange sc = new StatusChange();
            sc.setArchived(true);
            sc.setService(newService3);
            sc.setStatus(ServiceStatus.ACTIVE);

            newService3.getStatusHistory().add(sc);
        }

        StatusChange sc = new StatusChange();
        sc.setService(newService3);
        sc.setStatus(ServiceStatus.INACTIVE);
        newService3.getStatusHistory().add(sc);

        getDao().save(newService3);

        try {
            GridServiceDao aspectDao = (GridServiceDao)applicationContext.getBean("gridServiceDao");
            GridService loadedService =  aspectDao.getByUrl("http://new3");
            assertSame("Filter should have been applied",loadedService.getStatusHistory().size(),1);
            assertEquals(ServiceStatus.INACTIVE,loadedService.getStatusHistory().get(0).getStatus());
        } catch (BeansException e) {
            fail("Transaction failed" + e);
        }
    }



    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        SessionFactory sessionFactory = (SessionFactory) applicationContext
                .getBean("sessionFactory");
        TransactionSynchronizationManager.unbindResource(sessionFactory);

        Session session = SessionFactoryUtils.getSession(sessionFactory, true);
        SessionFactoryUtils.closeSession(session);    }

    @Override
    public String getNamingStrategy() {
        // using class so that it can be refactored
        return GridServiceDaoTest.class.getSimpleName();
    }
}