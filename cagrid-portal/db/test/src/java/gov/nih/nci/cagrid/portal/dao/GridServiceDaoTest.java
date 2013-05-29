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
/**
 *
 */
package gov.nih.nci.cagrid.portal.dao;


import gov.nih.nci.cagrid.portal.DBTestBase;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.domain.StatusChange;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.SQLException;
import java.util.*;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author kherm manav.kher@semanticbits.com
 */
public class GridServiceDaoTest extends DBTestBase<GridServiceDao> {


    @Test
    public void testGetGridServiceForIndexService() {


        String idxSvcUrl = "http://some.url";

        IndexService idxSvc = new IndexService();
        idxSvc.setUrl(idxSvcUrl);
        IndexServiceDao indexServiceDao = (IndexServiceDao) getApplicationContext().getBean("indexServiceDao");
        indexServiceDao.save(idxSvc);

        String svcUrl = "http://some.other.url";
        GridService svc = new GridService();
        svc.setUrl(svcUrl);
        svc.getIndexServices().add(idxSvc);
        getDao().save(svc);
        idxSvc.getServices().add(svc);
        indexServiceDao.save(idxSvc);

        interruptSession();
        List<GridService> svcs = getDao().getByIndexServiceUrl(idxSvcUrl);
        assertTrue("found " + svcs.size() + " services for index service", svcs
                .size() == 1);
        GridService svc2 = (GridService) svcs.get(0);
        assertEquals("service urls not equal", svc.getUrl(), svc2.getUrl());


    }


    @Test
    // make sure case insenitive queries will work
    public void caseInSensitive() {
        for (GridService service : getDao().getAll()) {
            assertNotNull(getDao().getByUrl(service.getUrl()));
            assertNotNull(getDao().getByUrl(service.getUrl().toUpperCase()));
            assertNotNull(getDao().getByUrl(service.getUrl().toLowerCase()));

        }
    }

    @Test
    public void testGetLatestServices() {


        Map<String, Date> map = new HashMap<String, Date>();
        Calendar cal = Calendar.getInstance();
        map.put("http://one", cal.getTime());
        cal.add(Calendar.HOUR, 1);
        map.put("http://two", cal.getTime());
        cal.add(Calendar.HOUR, 1);
        map.put("http://three", cal.getTime());


        for (String url : map.keySet()) {
            GridService svc = new GridService();
            svc.setUrl(url);
            StatusChange status = new StatusChange();
            status.setTime(map.get(url));
            status.setStatus(ServiceStatus.ACTIVE);
            status.setService(svc);
            svc.getStatusHistory().add(status);
            getDao().save(svc);
        }
        List<GridService> svcs = getDao().getLatestServices(10);
        assertTrue("got " + svcs.size() + " services", svcs.size() == 3);
        GridService svc = (GridService) svcs.get(0);
        assertEquals("http://three is not first", "http://three", svc.getUrl());


    }

    @Test
    public void testBannedServices() {
        int serviceCount = getDao().getAll().size();

        GridService svc1 = new GridService();
        svc1.setUrl("http://one");
        StatusChange status = new StatusChange();
        status.setTime(new Date());
        status.setStatus(ServiceStatus.ACTIVE);
        status.setService(svc1);
        svc1.getStatusHistory().add(status);
        getDao().save(svc1);

        GridService svc2 = new GridService();
        svc2.setUrl("http://two");
        status = new StatusChange();
        status.setTime(new Date());
        status.setStatus(ServiceStatus.ACTIVE);
        status.setService(svc2);
        svc2.getStatusHistory().add(status);
        getDao().save(svc2);

        List<GridService> svcs = getDao().getAll();
        assertTrue("found " + svcs.size() + " services; expecting" + serviceCount + 2, svcs
                .size() == serviceCount + 2);

        status = new StatusChange();
        status.setService(svc2);
        status.setTime(new Date());
        status.setStatus(ServiceStatus.BANNED);
        svc2.getStatusHistory().add(status);
        getDao().save(svc2);

        final List<GridService> nonBanned = new ArrayList<GridService>();
        getDao().getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Query query = session.createQuery("from GridService");
                List services = query.list();
                for (Iterator i = services.iterator(); i.hasNext();) {
                    GridService svc = (GridService) i.next();
                    if (!ServiceStatus.BANNED.equals(svc.getCurrentStatus())) {
                        nonBanned.add(svc);
                    }
                }
                return null;
            }
        });

        assertTrue("found " + nonBanned.size()
                + " non-banned services; expecting" + serviceCount + 1, nonBanned.size() == serviceCount + 1);


    }

    @Test
    public void testCachePerformance() {
        GridService newService = new GridService();

        for (int i = 0; i < 10000; i++) {
            StatusChange sc = new StatusChange();
            sc.setService(newService);
            sc.setStatus(ServiceStatus.ACTIVE);
            newService.getStatusHistory().add(sc);
        }
        getDao().save(newService);

        for (int i = 0; i < 10; i++) {
            interruptSession();
            long init = System.currentTimeMillis();
            GridService service = getDao().getById(1);
            assertEquals(ServiceStatus.ACTIVE, service.getCurrentStatus());
            System.out.println("Time taken for 10000 Status changes " + (System.currentTimeMillis() - init) + " miliseconds");
        }

    }


    @Test
    public void testPerformance() {
        long init = System.currentTimeMillis();
        GridService service = (GridService) getDao().getById(-1);
        service.getCurrentStatus();
        System.out.println("Time taken for 0 status changes " + (System.currentTimeMillis() - init) + " miliseconds");

        GridService newService = new GridService();
        newService.setUrl("http://new1");
        for (int i = 0; i < 100; i++) {
            StatusChange sc = new StatusChange();
            sc.setService(newService);
            sc.setStatus(ServiceStatus.DORMANT);
            newService.getStatusHistory().add(sc);
        }

        getDao().save(newService);
        interruptSession();
        init = System.currentTimeMillis();
        service = (GridService) getDao().getByUrl("http://new1");
        assertEquals(ServiceStatus.DORMANT, service.getCurrentStatus());
        System.out.println("Time taken for 100 status changes " + (System.currentTimeMillis() - init) + " miliseconds");

        GridService newService2 = new GridService();
        newService2.setUrl("http://new2");
        for (int i = 0; i < 10000; i++) {
            StatusChange sc = new StatusChange();
            sc.setService(newService2);
            sc.setStatus(ServiceStatus.ACTIVE);
            newService2.getStatusHistory().add(sc);
        }
        getDao().save(newService2);
        interruptSession();
        init = System.currentTimeMillis();
        service = (GridService) getDao().getByUrl("http://new2");
        assertEquals(ServiceStatus.ACTIVE, service.getCurrentStatus());
        System.out.println("Time taken for 10000 Status changes " + (System.currentTimeMillis() - init) + " miliseconds");


    }

    @Test
    public void testFilterPerformance() {

        GridService newService3 = new GridService();
        newService3.setUrl("http://new3");
        for (int i = 0; i < 10000; i++) {
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
        interruptSession();

        GridService loadedService = getDao().getByUrl("http://new3");
        assertNotSame("Filter should not have applied", loadedService.getStatusHistory().size(), 1);

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                new String[]{"classpath:applicationContext-db-aspects.xml"});

        SessionFactory sessionFactory = (SessionFactory) applicationContext
                .getBean("sessionFactory");

        Session session = SessionFactoryUtils.getSession(sessionFactory, true);
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));


        try {
            GridServiceDao aspectDao = (GridServiceDao) applicationContext.getBean("gridServiceDao");
            long init = System.currentTimeMillis();
            loadedService = aspectDao.getByUrl("http://new3");
            assertSame("Filter should have been applied", loadedService.getStatusHistory().size(), 1);
            assertEquals(ServiceStatus.INACTIVE, loadedService.getStatusHistory().get(0).getStatus());
            System.out.println("Time taken for 10000 Status changes with filter " + (System.currentTimeMillis() - init) + " miliseconds");
        } catch (BeansException e) {
            fail("Transaction failed" + e);
        }
        finally {
            TransactionSynchronizationManager.unbindResource(sessionFactory);
            SessionFactoryUtils.closeSession(session);
        }
    }


}
