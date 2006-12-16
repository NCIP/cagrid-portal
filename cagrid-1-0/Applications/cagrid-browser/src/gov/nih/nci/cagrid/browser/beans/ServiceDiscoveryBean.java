/**
 * $Id $
 */
package gov.nih.nci.cagrid.browser.beans;

import gov.nih.nci.cagrid.browser.search.SemanticKeywordSearch;
import gov.nih.nci.cagrid.browser.search.ServiceDiscoveryThread;
import gov.nih.nci.cagrid.browser.search.ServiceKeywordSearch;
import gov.nih.nci.cagrid.browser.search.ServiceTypeSearch;
import gov.nih.nci.cagrid.browser.util.AppUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ServiceDiscoveryBean {

	private static Logger logger = Logger.getLogger(ServiceDiscoveryBean.class);

	private static final String DISCOVERY_FAILURE = "discoveryFailed.error";

	private static final String DISCOVERY_FAILURE_TIMEOUT = "discoveryFailed.timeout";

	private static final String CHANGE_INDEX_SERVICE_URLS_FAILED_ERROR = "changeIndexServiceUrlsFailed.error";

	private static final String CHANGE_INDEX_SERVICE_URLS_FAILED_NO_URLS = "changeIndexServiceUrlsFailed.noUrls";

	private static final Object ALL = "all";

	private int timeout;

	private String keywords;

	private String discoveryFailureMessage;
	
	private Map semanticMetadataCategoriesMap;
	
	private Map serviceMetadataCategoriesMap;

	private List semanticMetadataCategories;

	private List semanticMetadataCategoriesSelected;
	
	private List serviceMetadataCategories;

	private List serviceMetadataCategoriesSelected;

	private List indexServiceUrls;
	
	private String[] indexServiceUrlsSelected;
	
	private List serviceTypeSelected;

	private DiscoveredServices discoveryResult;
	
	private String changeIndexServiceUrlsFailureMessage;

	public String getChangeIndexServiceUrlsFailureMessage() {
		return changeIndexServiceUrlsFailureMessage;
	}

	public void setChangeIndexServiceUrlsFailureMessage(
			String changeIndexServiceUrlsFailureMessage) {
		this.changeIndexServiceUrlsFailureMessage = changeIndexServiceUrlsFailureMessage;
	}

	public DiscoveredServices getDiscoveryResult() {
		return discoveryResult;
	}

	public void setDiscoveryResult(DiscoveredServices discoveryResult) {
		this.discoveryResult = discoveryResult;
	}

	public List getIndexServiceUrls() {
		return indexServiceUrls;
	}

	public void setIndexServiceUrls(List indexServiceUrls) {
		this.indexServiceUrls = indexServiceUrls;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public List getSemanticMetadataCategories() {
		return semanticMetadataCategories;
	}

	public void setSemanticMetadataCategories(List semanticMetadataCategories) {
		this.semanticMetadataCategories = semanticMetadataCategories;
	}

	public List getSemanticMetadataCategoriesSelected() {
		return semanticMetadataCategoriesSelected;
	}

	public void setSemanticMetadataCategoriesSelected(
			List semanticMetadataCategoriesSelected) {
		this.semanticMetadataCategoriesSelected = semanticMetadataCategoriesSelected;
	}
	
	public String changeIndexServiceUrls(){
		String result = AppUtils.FAILED_METHOD;
		try{
			String[] urls = getIndexServiceUrlsSelected();
			if(urls == null || urls.length == 0){
				setChangeIndexServiceUrlsFailureMessage(AppUtils.getMessage(CHANGE_INDEX_SERVICE_URLS_FAILED_NO_URLS));
			}else{
				result = AppUtils.SUCCESS_METHOD;
				setChangeIndexServiceUrlsFailureMessage(null);
			}
		}catch(Exception ex){
			setChangeIndexServiceUrlsFailureMessage(AppUtils.getMessage(CHANGE_INDEX_SERVICE_URLS_FAILED_ERROR));
		}
		return result;
	}

	public String doDiscovery() {
		String result = AppUtils.FAILED_METHOD;

		logger.debug("Doing discovery.");

		try {
			DiscoveredServices allResults = getDiscoveryResult();
			allResults.clear();

			List threads = new ArrayList();
			List searches = new ArrayList();
			
			String[] terms = new String[0];
			String keywordsStr = getKeywords();
			if(keywordsStr != null){
				terms = getKeywords().split(AppUtils.KEYWORD_DELIMITER);
			}
			
			/**
			 * Set up the semantic searches.
			 */
			Map semanticCategoriesMap = getSemanticMetadataCategoriesMap();
			List semanticCategories = getSemanticMetadataCategoriesSelected();
			if(semanticCategories.contains(ALL)){
				semanticCategories = new ArrayList(semanticCategoriesMap.keySet());
			}
			logger.debug("Selected " + semanticCategories.size() + " semantic metadata categories.");
			for (Iterator i = semanticCategories.iterator(); i.hasNext();) {
				String category = (String) i.next();
				SemanticKeywordSearch search = (SemanticKeywordSearch) semanticCategoriesMap.get(category);
				if(search == null){
					logger.debug("No search found for category '" + category + "'");
					throw new RuntimeException("No search found for category '" + category + "'");
				}
				search.setIndexServiceURLs(getIndexServiceUrlsSelected());
				search.setKeywords(terms);
				searches.add(search);
				threads.add(new Thread(search));
			}

			
			/**
			 * Set up the service type searches.
			 */
			List serviceTypeCategories = getServiceTypeSelected();
			setUpTypeSearch(serviceTypeCategories, searches, threads);
			
			
			
			/**
			 * Set up the service metadata searches.
			 */
			Map serviceCategoriesMap = getServiceMetadataCategoriesMap();
			List serviceCategories = getServiceMetadataCategoriesSelected();
			if(serviceCategories.size() == 0 && serviceTypeCategories.size() == 0 && semanticCategories.size() == 0 || serviceCategories.contains(ALL)){
				/**
				 * This means the user hasn't specified any categories.
				 * So, just search all the service metadata.
				 */
				serviceCategories = new ArrayList(serviceCategoriesMap.keySet());
			}
			logger.debug("Selected " + serviceCategories.size() + " service metadata categories.");
			for (Iterator i = serviceCategories.iterator(); i.hasNext();) {
				String category = (String) i.next();
				ServiceKeywordSearch search = (ServiceKeywordSearch) serviceCategoriesMap.get(category);
				if(search == null){
					logger.debug("No search found for category '" + category + "'");
					throw new RuntimeException("No search found for category '" + category + "'");
				}
				search.setIndexServiceURLs(getIndexServiceUrlsSelected());
				search.setKeywords(terms);
				searches.add(search);
				threads.add(new Thread(search));
			}
			
			if(searches.size() == 0){
				/**
				 * This means the user hasn't selected any of the categories AND
				 * hasn't specified any keywords. So, just return all services.
				 */
				serviceTypeCategories.add(ALL);
				setUpTypeSearch(serviceTypeCategories, searches, threads);
			}

			boolean errorEncountered = false;
			runThreads(threads, getTimeout());
			for (Iterator i = searches.iterator(); i.hasNext();) {
				ServiceDiscoveryThread search = (ServiceDiscoveryThread) i
						.next();

				Exception ex = search.getException();
				if (ex != null) {
					errorEncountered = true;
					logger.error("Thread error: " + ex.getMessage(), ex);
					setDiscoveryFailureMessage(AppUtils
							.getMessage(DISCOVERY_FAILURE));
					break;
				}
				if (!search.isFinished()) {
					errorEncountered = true;
					logger.error("Search timed out.");
					setDiscoveryFailureMessage(AppUtils
							.getMessage(DISCOVERY_FAILURE_TIMEOUT));
					break;
				}

				EndpointReferenceType[] eprs = search.getEPRs();
				if (eprs != null && eprs.length > 0) {
					logger.debug("Got " + eprs.length + " eprs.");
					allResults.addDiscoveryResult(eprs);
				}else{
					logger.debug("Got no eprs.");
				}

			}
			if (!errorEncountered) {
				result = AppUtils.SUCCESS_METHOD;
				setDiscoveryFailureMessage(null);
			}
		} catch (Exception ex) {
			setDiscoveryFailureMessage(AppUtils.getMessage(DISCOVERY_FAILURE));
		}

		return result;
	}

	private void setUpTypeSearch(List serviceTypeCategories, List searches, List threads) {
		if(serviceTypeCategories.contains(ALL)){
			serviceTypeCategories.clear();
			serviceTypeCategories.add(ServiceTypeSearch.DATA);
			serviceTypeCategories.add(ServiceTypeSearch.ANALYTICAL);
		}
		for(Iterator i = serviceTypeCategories.iterator(); i.hasNext();){
			String category = (String)i.next();
			ServiceTypeSearch sts = new ServiceTypeSearch();
			sts.setType(category);
			sts.setIndexServiceURLs(getIndexServiceUrlsSelected());
			searches.add(sts);
			threads.add(new Thread(sts));
		}

	}

	private void runThreads(List threads, int timeout) {
		logger.debug("Running " + threads.size() + " threads.");
		for (Iterator i = threads.iterator(); i.hasNext();) {
			Thread t = (Thread) i.next();
			t.start();
		}
		for (Iterator i = threads.iterator(); i.hasNext();) {
			Thread t = (Thread) i.next();
			try {
				t.join(timeout);
			} catch (Exception ex) {
				throw new RuntimeException("Error joining thread: "
						+ ex.getMessage(), ex);
			}
		}
	}

	public String getDiscoveryFailureMessage() {
		return discoveryFailureMessage;
	}

	public void setDiscoveryFailureMessage(String discoveryFailureMessage) {
		this.discoveryFailureMessage = discoveryFailureMessage;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public Map getSemanticMetadataCategoriesMap() {
		return semanticMetadataCategoriesMap;
	}

	public void setSemanticMetadataCategoriesMap(Map semanticMetadataCategoriesMap) {
		this.semanticMetadataCategoriesMap = semanticMetadataCategoriesMap;
	}

	public String[] getIndexServiceUrlsSelected() {
		return indexServiceUrlsSelected;
	}

	public void setIndexServiceUrlsSelected(String[] indexServiceUrlsSelected) {
		this.indexServiceUrlsSelected = indexServiceUrlsSelected;
	}

	public List getServiceMetadataCategories() {
		return serviceMetadataCategories;
	}

	public void setServiceMetadataCategories(List serviceMetadataCategories) {
		this.serviceMetadataCategories = serviceMetadataCategories;
	}

	public List getServiceMetadataCategoriesSelected() {
		return serviceMetadataCategoriesSelected;
	}

	public void setServiceMetadataCategoriesSelected(
			List serviceMetadataCategoriesSelected) {
		this.serviceMetadataCategoriesSelected = serviceMetadataCategoriesSelected;
	}

	public Map getServiceMetadataCategoriesMap() {
		return serviceMetadataCategoriesMap;
	}

	public void setServiceMetadataCategoriesMap(Map serviceMetadataCategoriesMap) {
		this.serviceMetadataCategoriesMap = serviceMetadataCategoriesMap;
	}

	public List getServiceTypeSelected() {
		return serviceTypeSelected;
	}

	public void setServiceTypeSelected(List serviceTypeSelected) {
		this.serviceTypeSelected = serviceTypeSelected;
	}

}
