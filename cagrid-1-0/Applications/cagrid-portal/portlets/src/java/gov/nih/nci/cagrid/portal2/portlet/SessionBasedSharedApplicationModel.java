/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet;

import gov.nih.nci.cagrid.portal2.domain.PortalUser;
import gov.nih.nci.cagrid.portal2.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal2.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal2.portlet.directory.DirectoryBean;
import gov.nih.nci.cagrid.portal2.portlet.discovery.DiscoveryResultsBean;
import gov.nih.nci.cagrid.portal2.portlet.query.cql.CQLQueryCommand;
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
	private List<DiscoveryResultsBean> discoveryResults = new ArrayList<DiscoveryResultsBean>();
	private DirectoryBean selectedDirectoryBean;
	private Integer selectedGridDataServiceId;
	private UMLClass selectedUMLClass;
	private CQLQueryCommand workingCqlQuery;

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

	public void addDiscoveryResults(DiscoveryResultsBean res) {
		getDiscoveryResults().add(res);
	}

	public List<DiscoveryResultsBean> getDiscoveryResults() {
		return discoveryResults;
	}

	public void setDiscoveryResults(List<DiscoveryResultsBean> discoveryResults) {
		this.discoveryResults = discoveryResults;
	}

	public DirectoryBean getSelectedDirectoryBean() {
		return selectedDirectoryBean;
	}

	public void setSelectedDirectoryBean(DirectoryBean selectedDirectoryBean) {
		this.selectedDirectoryBean = selectedDirectoryBean;
	}

	public DiscoveryResultsBean getDiscoveryResultsBean(String resultsId) {
		DiscoveryResultsBean bean = null;
		for(DiscoveryResultsBean b : getDiscoveryResults()){
			if(b.getId().equals(resultsId)){
				bean = b;
				break;
			}
		}
		return bean;
	}

	public Integer getSelectedGridDataServiceId() {
		return selectedGridDataServiceId;
	}

	public void setSelectedGridDataServiceId(Integer id) {
		this.selectedGridDataServiceId = id;
	}

	public UMLClass getSelectedUMLClass() {
		return selectedUMLClass;
	}

	public void setSelectedUMLClass(UMLClass selectedUMLClass) {
		this.selectedUMLClass = selectedUMLClass;
	}

	public CQLQueryCommand getWorkingCqlQuery() {
		return workingCqlQuery;
	}

	public void setWorkingCqlQuery(CQLQueryCommand workingCqlQuery) {
		this.workingCqlQuery = workingCqlQuery;
	}

}
