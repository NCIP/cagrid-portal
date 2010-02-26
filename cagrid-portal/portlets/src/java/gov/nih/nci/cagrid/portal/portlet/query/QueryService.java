/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.dao.QueryDao;
import gov.nih.nci.cagrid.portal.dao.QueryInstanceDao;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.dataservice.*;
import gov.nih.nci.cagrid.portal.portlet.UserModel;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryInstanceExecutor;
import gov.nih.nci.cagrid.portal.portlet.query.dcql.DCQLQueryInstanceExecutor;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;
import gov.nih.nci.cagrid.portal.service.QueryInstanceService;
import gov.nih.nci.cagrid.portal.util.PortalUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 */
@Transactional
public class QueryService implements ApplicationContextAware, DisposableBean {

    private Map<Integer, QueryInstanceExecutor> executors = new HashMap<Integer, QueryInstanceExecutor>();
    private ApplicationContext applicationContext;
    private String cqlQueryExecutorBeanName = "cqlQueryInstanceExecutorPrototype";
    private String dcqlQueryExecutorBeanName = "dcqlQueryInstanceExecutorPrototype";
    private UserModel userModel;
    private QueryDao queryDao;
    private QueryInstanceDao queryInstanceDao;
    private GridServiceDao gridServiceDao;
    private PortalUserDao portalUserDao;
    private QueryInstanceService queryInstanceService;

    private Log logger = LogFactory.getLog(getClass());


    public QueryInstance submitQuery(String queryXML, String serviceUrl) {
        QueryInstance instance = null;
        if (PortletUtils.isCQL(queryXML)) {
            instance = submitCQLQuery(queryXML, serviceUrl);
        } else {
            instance = submitDCQLQuery(queryXML, serviceUrl);
        }
        return instance;
    }

    public List<QueryInstance> getSubmittedQueries() {
        List<QueryInstance> ordered = new ArrayList<QueryInstance>();

        Set<QueryInstance> set = new HashSet<QueryInstance>();
        TreeMap<Date, QueryInstance> map = new TreeMap<Date, QueryInstance>();
        synchronized (executors) {
            for (QueryInstanceExecutor executor : executors.values()) {
                QueryInstance instance = executor.getQueryInstance();
                set.add(instance);
            }
        }
        PortalUser portalUser = getUserModel().getPortalUser();
        if (portalUser != null) {
            portalUser = getPortalUserDao().getById(portalUser.getId());
            for (QueryInstance instance : portalUser.getQueryInstances()) {
                set.add(instance);
            }
        }
        for (QueryInstance instance : set) {
            map.put(instance.getCreateTime(), instance);
        }
        for (QueryInstance instance : map.values()) {
            ordered.add(instance);
        }
        Collections.reverse(ordered);

        return ordered;
    }

    public QueryInstance removeQueryInstance(Integer instanceId) {
        QueryInstance instance = null;
        QueryInstanceExecutor executor = executors.remove(instanceId);
        if (executor != null) {
            instance = executor.getQueryInstance();
        }
        return instance;
    }

    public void deleteQueryInstance(Integer instanceId) {
        removeQueryInstance(instanceId);
        try {
            getQueryInstanceService().delete(instanceId);
        } catch (Exception ex) {
            String msg = "Error deleting query instance: " + ex.getMessage();
            logger.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }
    }

    public Query loadQuery(String queryXML) {
        return getQueryDao().getQueryByHash(PortalUtils.createHash(queryXML));
    }

    protected DCQLQueryInstance submitDCQLQuery(String queryXML,
                                                String serviceUrl) {

        String hash = PortalUtils.createHash(queryXML);
        DCQLQuery query = (DCQLQuery) getQueryDao().getQueryByHash(hash);
        if (query == null) {
            query = (DCQLQuery) createQueryFromXml(queryXML);
        }

        DCQLQueryInstance instance = new DCQLQueryInstance();
        instance.setCreateTime(new Date());
        instance.setPortalUser(getUserModel().getPortalUser());
        instance.setQuery(query);
        getQueryInstanceDao().save(instance);

        DCQLQueryInstanceExecutor executor = (DCQLQueryInstanceExecutor) getApplicationContext()
                .getBean(getDcqlQueryExecutorBeanName());
        startQueryInstance(instance, executor);

        return instance;
    }

    protected CQLQueryInstance submitCQLQuery(String queryXML, String serviceUrl) {

        String hash = PortalUtils.createHash(queryXML);
        CQLQuery query = (CQLQuery) getQueryDao().getQueryByHash(hash);
        if (query == null) {
            query = (CQLQuery) createQueryFromXml(queryXML);
        }

        CQLQueryInstance instance = new CQLQueryInstance();
        instance.setCreateTime(new Date());
        GridDataService dataService = (GridDataService) getGridServiceDao()
                .getByUrl(serviceUrl);
        instance.setDataService(dataService);
        instance.setPortalUser(getUserModel().getPortalUser());
        instance.setQuery(query);
        getQueryInstanceDao().save(instance);

        CQLQueryInstanceExecutor executor = (CQLQueryInstanceExecutor) getApplicationContext()
                .getBean(getCqlQueryExecutorBeanName());
        startQueryInstance(instance, executor);

        return instance;
    }

    protected void startQueryInstance(QueryInstance instance,
                                      QueryInstanceExecutor executor) {
        executor.setQueryInstance(instance);
        executor.start();
        executors.put(instance.getId(), executor);
    }

    public void cancelQueryInstance(Integer instanceId) {
        QueryInstanceExecutor executor = executors.get(instanceId);
        executor.cancel();
    }


    public QueryInstance getQueryInstance(Integer instanceId) {
        QueryInstance selected = null;
        for (QueryInstance instance : getSubmittedQueries()) {
            if (instance.getId().equals(instanceId)) {
                selected = instance;
                break;
            }
        }
        return selected;
    }

    public Query createQueryFromXml(String xml) {
        Query query = null;
        String hash = PortalUtils.createHash(xml);
        if (PortletUtils.isCQL(xml)) {
            query = new CQLQuery();
            query.setXml(xml);
            query.setHash(hash);
            getQueryDao().save(query);
        } else {
            query = new DCQLQuery();
            query.setXml(xml);
            query.setHash(hash);
            getQueryDao().save(query);

            List<GridDataService> targetServices = new ArrayList<GridDataService>();
            Set<String> svcUrls = null;
            try {
                svcUrls = PortletUtils.getTargetServiceUrls(xml);
            } catch (Exception ex) {
                throw new RuntimeException(
                        "Error getting target service URLs from DCQL query: "
                                + ex.getMessage(), ex);
            }
            for (String svcUrl : svcUrls) {
                GridDataService svc = (GridDataService) getGridServiceDao()
                        .getByUrl(svcUrl);
                targetServices.add(svc);
            }
            ((DCQLQuery) query).setTargetServices(targetServices);
            getQueryDao().save(query);
        }

        return query;
    }

    /**
     * Clean all the queries when session ends
     *
     * @throws Exception
     */
    public void destroy() throws Exception {
        for (QueryInstance instance : getSubmittedQueries()) {
            deleteQueryInstance(instance.getId());
        }
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public String getCqlQueryExecutorBeanName() {
        return cqlQueryExecutorBeanName;
    }

    public void setCqlQueryExecutorBeanName(String cqlQueryExecutorBeanName) {
        this.cqlQueryExecutorBeanName = cqlQueryExecutorBeanName;
    }

    public String getDcqlQueryExecutorBeanName() {
        return dcqlQueryExecutorBeanName;
    }

    public void setDcqlQueryExecutorBeanName(String dcqlQueryExecutorBeanName) {
        this.dcqlQueryExecutorBeanName = dcqlQueryExecutorBeanName;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public QueryDao getQueryDao() {
        return queryDao;
    }

    public void setQueryDao(QueryDao queryDao) {
        this.queryDao = queryDao;
    }

    public GridServiceDao getGridServiceDao() {
        return gridServiceDao;
    }

    public void setGridServiceDao(GridServiceDao gridServiceDao) {
        this.gridServiceDao = gridServiceDao;
    }

    public QueryInstanceDao getQueryInstanceDao() {
        return queryInstanceDao;
    }

    public void setQueryInstanceDao(QueryInstanceDao queryInstanceDao) {
        this.queryInstanceDao = queryInstanceDao;
    }

    public PortalUserDao getPortalUserDao() {
        return portalUserDao;
    }

    public void setPortalUserDao(PortalUserDao portalUserDao) {
        this.portalUserDao = portalUserDao;
    }

    public QueryInstanceService getQueryInstanceService() {
        return queryInstanceService;
    }

    public void setQueryInstanceService(QueryInstanceService queryInstanceService) {
        this.queryInstanceService = queryInstanceService;
    }
}
