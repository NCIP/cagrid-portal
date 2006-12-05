/**
 * $Id $
 */
package gov.nih.nci.cagrid.browser.search;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ServiceKeywordSearch extends AbstractSearch {

	private static final Logger logger = Logger
			.getLogger(ServiceKeywordSearch.class);


	private String category;
	
	private String[] keywords;
	
	public String[] getKeywords() {
		return keywords;
	}

	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}
	
	public EndpointReferenceType[] doRun(DiscoveryClient client) throws Exception {
		
		return search(client, getKeywords(), getCategory());
	}


	public static EndpointReferenceType[] search(DiscoveryClient client,
			String[] terms, String category) throws InterruptedException {
		
		logger.debug("Category: " + category);
		
		EndpointReferenceType[] results = null;

		List searches = new ArrayList();
		List threads = new ArrayList();
		Set allResults = new HashSet();
		for (int i = 0; i < terms.length; i++) {
			ServiceMetadataSearch sms = new ServiceMetadataSearch();
			String[] urls = {client.getIndexEPR().getAddress().toString()};
			sms.setIndexServiceURLs(urls);
			sms.setTerm(terms[i].trim());
			sms.setType(category);
			searches.add(sms);
			threads.add(new Thread(sms));
		}

		for (Iterator i = threads.iterator(); i.hasNext();) {
			Thread t = (Thread) i.next();
			t.start();
		}
		for (Iterator i = threads.iterator(); i.hasNext();) {
			Thread t = (Thread) i.next();
			t.join();
		}
		for (Iterator i = searches.iterator(); i.hasNext();) {
			ServiceMetadataSearch sms = (ServiceMetadataSearch) i.next();
			Exception ex = sms.getException();
			if (ex != null) {
				throw new RuntimeException("thread encountered error: "
						+ ex.getMessage(), ex);
			}
			if (!sms.isFinished()) {
				throw new RuntimeException("thread timed out");
			}
			allResults.addAll(Arrays.asList(sms.getEPRs()));
		}
		results = (EndpointReferenceType[]) allResults
				.toArray(new EndpointReferenceType[allResults.size()]);

		return results;
	}


	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
