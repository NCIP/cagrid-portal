/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query;

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

import gov.nih.nci.cagrid.portal2.domain.GridDataService;
import gov.nih.nci.cagrid.portal2.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal2.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal2.portlet.query.cql.CQLQueryCommand;
import gov.nih.nci.cagrid.portal2.portlet.query.cql.CQLQueryInstanceExecutor;
import gov.nih.nci.cagrid.portal2.portlet.query.cql.CriterionBean;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class QueryModel implements ApplicationContextAware {

	private GridDataService selectedService;
	private UMLClass selectedUmlClass;
	private CriterionBean selectedCriterion;
	private CQLQueryCommand workingQuery;
	private Map<Integer, CQLQueryInstanceExecutor> executors = new HashMap<Integer, CQLQueryInstanceExecutor>();
	private ApplicationContext applicationContext;
	
	/**
	 * 
	 */
	public QueryModel() {

	}
	
	public void submitCqlQuery(CQLQueryInstance instance) {
		
		if(instance == null){
			throw new IllegalArgumentException("CQLQueryInstance must not be null.");
		}
		if(instance.getId() == null){
			throw new IllegalArgumentException("CQLQueryInstance must have an id value.");
		}
		if(executors.containsKey(instance.getId())){
			throw new IllegalStateException("CQLQueryInstance:" + instance.getId() + " has already been submitted.");
		}
		
		CQLQueryInstanceExecutor executor = (CQLQueryInstanceExecutor)applicationContext.getBean("cqlQueryInstanceExecutorPrototype");
		
		executor.setCqlQueryInstance(instance);
		executor.start();
		
		executors.put(instance.getId(), executor);
	}
	
	public List<CQLQueryInstance> getSubmittedCqlQueries(){
		
		List<CQLQueryInstance> ordered = new ArrayList<CQLQueryInstance>();
		
		TreeMap<Date, CQLQueryInstance> map = new TreeMap<Date,CQLQueryInstance>();
		synchronized(executors){
			for(CQLQueryInstanceExecutor executor : executors.values()){
				CQLQueryInstance instance = executor.getCqlQueryInstance();
				Date startTime = instance.getStartTime();
				if(startTime == null){
					startTime = new Date();
				}
				map.put(startTime, instance);
			}
		}
		for(CQLQueryInstance instance : map.values()){
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

	public Map<Integer, CQLQueryInstanceExecutor> getExecutors() {
		return executors;
	}

	public void setExecutors(Map<Integer, CQLQueryInstanceExecutor> executors) {
		this.executors = executors;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
