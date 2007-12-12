/**
 * 
 */
package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.TestDB;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.domain.StatusChange;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class GridServiceDaoTest extends TestCase {

	/**
	 * 
	 */
	public GridServiceDaoTest() {

	}

	/**
	 * @param name
	 */
	public GridServiceDaoTest(String name) {
		super(name);

	}

	public void testGetGridServiceForIndexService() {
		TestDB.create();

		String idxSvcUrl = "http://some.url";

		IndexService idxSvc = new IndexService();
		idxSvc.setUrl(idxSvcUrl);
		IndexServiceDao indexServiceDao = (IndexServiceDao) TestDB
				.getApplicationContext().getBean("indexServiceDao");
		indexServiceDao.save(idxSvc);

		String svcUrl = "http://some.other.url";
		GridService svc = new GridService();
		svc.setUrl(svcUrl);
		svc.getIndexServices().add(idxSvc);
		GridServiceDao gridServiceDao = (GridServiceDao) TestDB
				.getApplicationContext().getBean("gridServiceDao");
		gridServiceDao.save(svc);
		idxSvc.getServices().add(svc);
		indexServiceDao.save(idxSvc);

		List<GridService> svcs = gridServiceDao.getByIndexServiceUrl(idxSvcUrl);
		assertTrue("found " + svcs.size() + " services for index service", svcs
				.size() == 1);
		GridService svc2 = (GridService) svcs.get(0);
		assertEquals("service urls not equal", svc.getUrl(), svc2.getUrl());

		TestDB.drop();
	}

	public void testGetLatestServices() {
		TestDB.create();

		Map<String, Date> map = new HashMap<String, Date>();
		Calendar cal = Calendar.getInstance();
		map.put("http://one", cal.getTime());
		cal.add(Calendar.HOUR, 1);
		map.put("http://two", cal.getTime());
		cal.add(Calendar.HOUR, 1);
		map.put("http://three", cal.getTime());

		GridServiceDao gridServiceDao = (GridServiceDao) TestDB
				.getApplicationContext().getBean("gridServiceDao");
		for (String url : map.keySet()) {
			GridService svc = new GridService();
			svc.setUrl(url);
			StatusChange status = new StatusChange();
			status.setTime(map.get(url));
			status.setStatus(ServiceStatus.ACTIVE);
			status.setService(svc);
			svc.getStatusHistory().add(status);
			gridServiceDao.save(svc);
		}
		List<GridService> svcs = gridServiceDao.getLatestServices(10);
		assertTrue("got " + svcs.size() + " services", svcs.size() == 3);
		GridService svc = (GridService) svcs.get(0);
		assertEquals("http://three is not first", "http://three", svc.getUrl());

		TestDB.drop();
	}

	public void testBannedServices() {
		TestDB.create();

		GridServiceDao gridServiceDao = (GridServiceDao) TestDB
				.getApplicationContext().getBean("gridServiceDao");

		GridService svc1 = new GridService();
		svc1.setUrl("http://one");
		StatusChange status = new StatusChange();
		status.setTime(new Date());
		status.setStatus(ServiceStatus.ACTIVE);
		status.setService(svc1);
		svc1.getStatusHistory().add(status);
		gridServiceDao.save(svc1);

		GridService svc2 = new GridService();
		svc2.setUrl("http://two");
		status = new StatusChange();
		status.setTime(new Date());
		status.setStatus(ServiceStatus.ACTIVE);
		status.setService(svc2);
		svc2.getStatusHistory().add(status);
		gridServiceDao.save(svc2);

		List<GridService> svcs = gridServiceDao.getAll();
		assertTrue("found " + svcs.size() + " services; expecting 2", svcs
				.size() == 2);

		status = new StatusChange();
		status.setService(svc2);
		status.setTime(new Date());
		status.setStatus(ServiceStatus.BANNED);
		svc2.getStatusHistory().add(status);
		gridServiceDao.save(svc2);

		final List<GridService> nonBanned = new ArrayList<GridService>();
		gridServiceDao.getHibernateTemplate().execute(new HibernateCallback() {
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
				+ " non-banned services; expecting 1", nonBanned.size() == 1);

		TestDB.drop();
	}

}
