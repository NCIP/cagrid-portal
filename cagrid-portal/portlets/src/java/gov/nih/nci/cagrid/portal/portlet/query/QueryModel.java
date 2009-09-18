/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query;

import gov.nih.nci.cagrid.portal.dao.UMLClassDao;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.DCQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryCommand;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryInstanceExecutor;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CriterionBean;
import gov.nih.nci.cagrid.portal.portlet.query.shared.SharedQueryBean;
import gov.nih.nci.cagrid.portal.portlet.query.dcql.DCQLQueryInstanceExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
@Transactional
public class QueryModel implements ApplicationContextAware {

    private GridDataService selectedService;
    private UMLClass selectedUmlClass;
    private CriterionBean selectedCriterion;
    private CQLQueryCommand workingQuery;
    private Map<Integer, QueryInstanceExecutor> executors = new HashMap<Integer, QueryInstanceExecutor>();
    private ApplicationContext applicationContext;
    private QueryInstance selectedQueryInstance;
    private UMLClassDao umlClassDao;
    private PortalUser portalUser;
    private SharedQueryBean workingSharedQuery;
    private List<String> queryResultsColumnNames;

    /**
     *
     */
    public QueryModel() {

    }

    public void selectUmlClassForQuery(Integer umlClassId) {
        if (getSelectedUmlClass() != null
                && getSelectedUmlClass().getId().equals(umlClassId)) {
            // Do nothing
        } else {
            UMLClass umlClass = getUmlClassDao().getById(umlClassId);
            setSelectedUmlClass(umlClass);
            setSelectedService(umlClass.getModel().getService());
        }
    }

    public void submitCqlQuery(CQLQueryInstance instance) {

        if (instance == null) {
            throw new IllegalArgumentException(
                    "CQLQueryInstance must not be null.");
        }
        if (instance.getId() == null) {
            throw new IllegalArgumentException(
                    "CQLQueryInstance must have an id value.");
        }
        if (executors.containsKey(instance.getId())) {
            throw new IllegalStateException("CQLQueryInstance:"
                    + instance.getId() + " has already been submitted.");
        }

        CQLQueryInstanceExecutor executor = (CQLQueryInstanceExecutor) applicationContext
                .getBean("cqlQueryInstanceExecutorPrototype");

        executor.setQueryInstance(instance);
        executor.start();

        executors.put(instance.getId(), executor);
    }


    public void submitDcqlQuery(DCQLQueryInstance instance) {

        if (instance == null) {
            throw new IllegalArgumentException(
                    "DCQLQueryInstance must not be null.");
        }
        if (instance.getId() == null) {
            throw new IllegalArgumentException(
                    "DCQLQueryInstance must have an id value.");
        }
        if (executors.containsKey(instance.getId())) {
            throw new IllegalStateException("DCQLQueryInstance:"
                    + instance.getId() + " has already been submitted.");
        }

        DCQLQueryInstanceExecutor executor = (DCQLQueryInstanceExecutor) applicationContext
                .getBean("dcqlQueryInstanceExecutorPrototype");

        executor.setQueryInstance(instance);
        executor.start();
        executors.put(instance.getId(), executor);
    }

    public List<QueryInstance> getSubmittedQueries() {

        List<QueryInstance> ordered = new ArrayList<QueryInstance>();

        TreeMap<Date, QueryInstance> map = new TreeMap<Date, QueryInstance>();
        synchronized (executors) {
            for (QueryInstanceExecutor executor : executors.values()) {
                QueryInstance instance = executor.getQueryInstance();
                Date startTime = instance.getStartTime();
                if (startTime == null) {
                    startTime = new Date();
                }
                map.put(startTime, instance);
            }
        }
        for (QueryInstance instance : map.values()) {
            ordered.add(instance);
        }
        Collections.reverse(ordered);

        return ordered;

    }

    public List<CQLQueryInstance> getSubmittedCqlQueries() {

        List<CQLQueryInstance> ordered = new ArrayList<CQLQueryInstance>();

        TreeMap<Date, CQLQueryInstance> map = new TreeMap<Date, CQLQueryInstance>();
        synchronized (executors) {
            for (QueryInstanceExecutor executor : executors.values()) {
                if (executor instanceof CQLQueryInstanceExecutor) {
                    CQLQueryInstance instance = ((CQLQueryInstanceExecutor) executor).getQueryInstance();
                    Date startTime = instance.getStartTime();
                    if (startTime == null) {
                        startTime = new Date();
                    }
                    map.put(startTime, instance);
                }
            }
        }
        for (CQLQueryInstance instance : map.values()) {
            ordered.add(instance);
        }
        Collections.reverse(ordered);

        return ordered;

    }

    public GridDataService getSelectedService() {
        return selectedService;
    }

    public void setSelectedService(GridDataService selectedService) {
        this.selectedService = selectedService;
    }

    public UMLClass getSelectedUmlClass() {
        return selectedUmlClass;
    }

    public void setSelectedUmlClass(UMLClass selectedUmlClass) {
        this.selectedUmlClass = selectedUmlClass;
    }

    public CriterionBean getSelectedCriterion() {
        return selectedCriterion;
    }

    public void setSelectedCriterion(CriterionBean selectedCriterion) {
        this.selectedCriterion = selectedCriterion;
    }

    public CQLQueryCommand getWorkingQuery() {
        return workingQuery;
    }

    public void setWorkingQuery(CQLQueryCommand workingQuery) {
        this.workingQuery = workingQuery;
    }

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Map<Integer, QueryInstanceExecutor> getExecutors() {
        return executors;
    }

    public void setExecutors(Map<Integer, QueryInstanceExecutor> executors) {
        this.executors = executors;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void selectQueryInstance(Integer instanceId) {
        setSelectedQueryInstance(getQueryInstance(instanceId));
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

    public void cancelQueryInstance(Integer instanceId) {
        QueryInstanceExecutor executor = executors.get(instanceId);
        executor.cancel();
    }

    public QueryInstance deleteQueryInstance(Integer instanceId) {
        cancelQueryInstance(instanceId);
        QueryInstanceExecutor executor = executors.remove(instanceId);
        return executor.getQueryInstance();
    }

    public QueryInstance getSelectedQueryInstance() {
        return selectedQueryInstance;
    }

    public void setSelectedQueryInstance(QueryInstance selectedQueryInstance) {
        this.selectedQueryInstance = selectedQueryInstance;
    }

    public UMLClassDao getUmlClassDao() {
        return umlClassDao;
    }

    public void setUmlClassDao(UMLClassDao umlClassDao) {
        this.umlClassDao = umlClassDao;
    }

    public PortalUser getPortalUser() {
        return portalUser;
    }

    public void setPortalUser(PortalUser portalUser) {
        this.portalUser = portalUser;
    }

    public SharedQueryBean getWorkingSharedQuery() {
        return workingSharedQuery;
    }

    public void setWorkingSharedQuery(SharedQueryBean workingSharedQuery) {
        this.workingSharedQuery = workingSharedQuery;
    }

	public List<String> getQueryResultsColumnNames() {
		return queryResultsColumnNames;
	}

	public void setQueryResultsColumnNames(List<String> queryResultsColumnNames) {
		this.queryResultsColumnNames = queryResultsColumnNames;
	}

}
