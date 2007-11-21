/**
 * 
 */
package gov.nih.nci.cagrid.portal.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.NonUniqueResultException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.domain.StatusChange;
import gov.nih.nci.cagrid.portal.domain.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.DomainModel;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class GridServiceDao extends AbstractDao<GridService> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return GridService.class;
	}

	@SuppressWarnings("unchecked")
	public List<GridService> getByIndexServiceUrl(final String indexServiceUrl) {

		List<GridService> services = new ArrayList<GridService>();
		services = (List<GridService>) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {

						return session.createCriteria(GridService.class)
								.createCriteria("indexServices")
								.add(Restrictions.eq("url", indexServiceUrl))
								.list();
					}
				});

		return services;
	}

	public GridService getByUrl(String url) {
		GridService svc = null;

		List svcs = getHibernateTemplate().find(
				"from GridService where url = '" + url + "'");

		if (svcs.size() > 1) {
			throw new NonUniqueResultException("Found " + svcs.size()
					+ " GridService objects for url '" + url + "'");
		} else if (svcs.size() == 1) {
			svc = (GridService) svcs.get(0);
		}
		return svc;
	}

	public void deleteMetadata(GridService service) {
		ServiceMetadata sMeta = service.getServiceMetadata();
		if (sMeta != null) {
			logger.debug("Deleting service metadata " + sMeta.getId());
			service.setServiceMetadata(null);
			getHibernateTemplate().delete(sMeta);
		} else {
			logger.warn("Service " + service.getUrl()
					+ " has no service metadata");
		}
		if (service instanceof GridDataService) {
			GridDataService dataService = (GridDataService) service;
			DomainModel dModel = dataService.getDomainModel();
			if (dModel != null) {
				logger.debug("Deleting domain model " + dModel.getId());
				dataService.setDomainModel(null);
				getHibernateTemplate().delete(dModel);
			} else {
				logger.warn("Data service " + dataService.getUrl()
						+ " has no domain model");
			}
		}
		save(service);
	}

	public void banService(final GridService gridService) {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				StatusChange change = new StatusChange();
				change.setService(gridService);
				change.setTime(new Date());
				change.setStatus(ServiceStatus.BANNED);
				gridService.getStatusHistory().add(change);
				session.save(gridService);
				return null;
			}
		});
	}

	public List<GridService> getAllDataServices() {
		List<GridService> services = new ArrayList<GridService>();
		List l = getHibernateTemplate().find("from GridDataService");
		services.addAll(l);
		return services;
	}
	
	public List<GridService> getAllAnalyticalServices(){
		List<GridService> services = new ArrayList<GridService>();
		List l = getHibernateTemplate().find("from GridService gs where gs.class = GridService");
		services.addAll(l);
		return services;
	}

	public List<GridService> getLatestServices(int latestServicesLimit) {
		List<GridService> latest = new ArrayList<GridService>();
		List<GridService> l = getHibernateTemplate().find(
				"select gs from GridService gs " +
				"join gs.statusHistory status " +
				"group by gs.id " + 
				"order by min(status.time) desc");
		for(int i = 0; i < latestServicesLimit && i < l.size(); i++){
			latest.add(l.get(i));
		}
		return latest;
	}

}
