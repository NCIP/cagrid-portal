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

import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.domain.StatusChange;
import gov.nih.nci.cagrid.portal.domain.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import javax.persistence.NonUniqueResultException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
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

    @Override
    public void save(GridService service) {
        super.save(service);    //To change body of overridden methods use File | Settings | File Templates.
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
                "from GridService where lower(url) = ?", new Object[]{url.toLowerCase()});

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
            sMeta.setService(null);
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
                dModel.setService(null);
                for (UMLClass klass : dModel.getClasses()) {
                    for (UMLAssociationEdge edge : klass.getAssociations()) {
                        if (edge instanceof SourceUMLAssociationEdge) {
                            UMLAssociation assoc = ((SourceUMLAssociationEdge) edge)
                                    .getAssociation();
                            getHibernateTemplate().delete(assoc);
                        }
                        getHibernateTemplate().delete(edge);
                    }
                    getHibernateTemplate().delete(klass);
                }
                getHibernateTemplate().delete(dModel);
            } else {
                logger.warn("Data service " + dataService.getUrl()
                        + " has no domain model");
            }
        }
        save(service);
    }

    public void setServiceDormant(final GridService gridService) {
        getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                GridService svc = (GridService) session.get(GridService.class,
                        gridService.getId());
                StatusChange change = new StatusChange();
                change.setService(svc);
                change.setTime(new Date());
                change.setStatus(ServiceStatus.DORMANT);
                session.save(change);
                svc.getStatusHistory().add(change);
                session.save(svc);
                return null;
            }
        });
    }

    public void banService(final GridService gridService) {
        getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                GridService svc = (GridService) session.get(GridService.class,
                        gridService.getId());
                StatusChange change = new StatusChange();
                change.setService(svc);
                change.setTime(new Date());
                change.setStatus(ServiceStatus.BANNED);
                session.save(change);
                svc.getStatusHistory().add(change);
                session.save(svc);
                return null;
            }
        });
    }

    public void unbanService(final GridService gridService) {
        getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                GridService svc = (GridService) session.get(GridService.class,
                        gridService.getId());
                StatusChange change = new StatusChange();
                change.setService(svc);
                change.setTime(new Date());
                change.setStatus(ServiceStatus.DORMANT);
                session.save(change);
                svc.getStatusHistory().add(change);
                session.save(svc);
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

    public List<GridService> getAllAnalyticalServices() {
        List<GridService> services = new ArrayList<GridService>();
        List l = getHibernateTemplate().find(
                "from GridService gs where gs.class = GridService");
        services.addAll(l);
        return services;
    }

    public List<GridService> getLatestServices(int latestServicesLimit) {
        List<GridService> latest = new ArrayList<GridService>();
        // This query doesn't work on HSQLDB or Derby, which makes it difficult
        // for testing.
        // So, changing to just select IDs and then retrieve the objects.
        // List<GridService> l = getHibernateTemplate().find(
        // "select gs from GridService gs " +
        // "join gs.statusHistory status " +
        // "group by gs.id " +
        // "order by min(status.time) desc");
        List<Integer> ids = getHibernateTemplate().find(
                "select gs.id from GridService gs "
                        + "join gs.statusHistory status " + "group by gs.id "
                        + "order by min(status.time) desc");
        List<GridService> l = new ArrayList<GridService>();
        for (Integer id : ids) {
            l.add((GridService) getHibernateTemplate().get(GridService.class,
                    id));
        }
        for (int i = 0; i < latestServicesLimit && i < l.size(); i++) {
            latest.add(l.get(i));
        }
        return latest;
    }

    public List<GridService> getUnindexedServices() {
        return (List<GridService>) getHibernateTemplate().find(
                "from GridService " + "where conceptIndexHash is null or "
                        + "conceptIndexHash != metadataHash");
    }

}
