/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet;

import gov.nih.nci.cagrid.portal2.domain.PortalUser;
import gov.nih.nci.cagrid.portal2.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal2.portlet.query.cql.CQLQueryInstanceExecutor;

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

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SessionBasedSharedApplicationModel implements
		SharedApplicationModel, ApplicationContextAware {


	private Map<Integer, CQLQueryInstanceExecutor> executors = new HashMap<Integer, CQLQueryInstanceExecutor>();
	private ApplicationContext applicationContext;
	private PortalUser portalUser;
	private Integer selectedCqlQueryInstanceId;

	/**
	 * 
	 */
	public SessionBasedSharedApplicationModel() {

	}

	public PortalUser getPortalUser() {
		return portalUser;
	}

	public void setPortalUser(PortalUser portalUser) {
		this.portalUser = portalUser;
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
	


	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public Integer getSelectedCqlQueryInstanceId() {
		return selectedCqlQueryInstanceId;
	}

	public void setSelectedCqlQueryInstanceId(Integer id) {
		this.selectedCqlQueryInstanceId = id;
	}

}
