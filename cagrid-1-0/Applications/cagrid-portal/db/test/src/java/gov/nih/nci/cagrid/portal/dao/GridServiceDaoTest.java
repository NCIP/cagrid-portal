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
import org.springframework.orm.hibernate3.HibernateCallback;

import java.sql.SQLException;
import java.util.*;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author kherm manav.kher@semanticbits.com
 */
public class GridServiceDaoTest extends DBTestBase<GridServiceDao> {


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

}
