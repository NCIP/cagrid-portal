/**
 * $Id $
 */
package gov.nih.nci.cagrid.browser.search;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.evs.service.EVSDescLogicConceptSearchParams;
import gov.nih.nci.cagrid.evsgridservice.client.EVSGridServiceClient;
import gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType;
import gov.nih.nci.evs.domain.DescLogicConcept;

import java.rmi.RemoteException;
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
public class SemanticKeywordSearch extends AbstractSearch {

	private static final Logger logger = Logger
			.getLogger(SemanticKeywordSearch.class);

	private String category;
	
	private String[] keywords;

	private String vocabulary;

	private int limit;
	
	private String evsGridServiceUrl;

	public String getEvsGridServiceUrl() {
		return evsGridServiceUrl;
	}

	public void setEvsGridServiceUrl(String evsGridServiceUrl) {
		this.evsGridServiceUrl = evsGridServiceUrl;
	}

	public String[] getKeywords() {
		return keywords;
	}

	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}
	
	public EndpointReferenceType[] doRun(DiscoveryClient client) throws Exception {
		
		EVSGridServiceClient evsClient = new EVSGridServiceClient(getEvsGridServiceUrl());
		Set codes = searchEVS(evsClient, getKeywords(), getVocabulary(), getLimit());
		
		return search(client, (String[]) codes.toArray(new String[codes
		                               					.size()]), getCategory());
	}


	public static EndpointReferenceType[] search(DiscoveryClient client,
			String[] codes, String category) throws InterruptedException {
		
		logger.debug("Category: " + category);
		
		EndpointReferenceType[] results = null;

		List searches = new ArrayList();
		List threads = new ArrayList();
		Set allResults = new HashSet();
		for (int i = 0; i < codes.length; i++) {
			ConceptCodeSearch ccs = new ConceptCodeSearch();
			String[] urls = {client.getIndexEPR().getAddress().toString()};
			ccs.setIndexServiceURLs(urls);
			ccs.setCode(codes[i].trim());
			ccs.setType(category);
			searches.add(ccs);
			threads.add(new Thread(ccs));
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
			ConceptCodeSearch ccs = (ConceptCodeSearch) i.next();
			Exception ex = ccs.getException();
			if (ex != null) {
				throw new RuntimeException("thread encountered error: "
						+ ex.getMessage(), ex);
			}
			if (!ccs.isFinished()) {
				throw new RuntimeException("thread timed out");
			}
			allResults.addAll(Arrays.asList(ccs.getEPRs()));
		}
		results = (EndpointReferenceType[]) allResults
				.toArray(new EndpointReferenceType[allResults.size()]);

		return results;
	}

	public static Set searchEVS(EVSGridServiceClient evsClient, String[] terms,
			String vocabulary, int limit) throws InvalidInputExceptionType,
			RemoteException {

		Set conceptCodes = new HashSet();

		for (int i = 0; i < terms.length; i++) {
			String term = terms[i].trim();
			if (term.length() > 0) {
				EVSDescLogicConceptSearchParams evsSearchParams = new EVSDescLogicConceptSearchParams();
				evsSearchParams.setVocabularyName(vocabulary);
				evsSearchParams.setSearchTerm(term);
				evsSearchParams.setLimit(limit);

				DescLogicConcept[] concepts = evsClient
						.searchDescLogicConcept(evsSearchParams);

				if (concepts != null && concepts.length > 0) {
					for (int j = 0; j < concepts.length; j++) {
						DescLogicConcept concept = concepts[j];
						if (concept != null) {
							logger.debug("concept=" + concept.getName());
							String code = concept.getCode();
							if (code != null && code.trim().length() > 0
									&& !code.trim().equals("null")) {
								conceptCodes.add(concept.getCode());
							}
						}
					}
				}
				// ApplicationService appService = ApplicationServiceProvider
				// .getRemoteInstance("http://cabio.nci.nih.gov/cacore31/http/remoteService");
				// EVSQueryImpl query = new EVSQueryImpl();
				// query.searchDescLogicConcepts(vocabulary, term, limit);
				// List results;
				// try {
				// results = appService.evsSearch(query);
				// } catch (Exception ex) {
				// throw new RuntimeException("Error searching evs: " +
				// ex.getMessage(), ex);
				// }
				// for (Iterator j = results.iterator(); j.hasNext();) {
				// DescLogicConcept concept = (DescLogicConcept) j.next();
				// String code = concept.getCode();
				// conceptCodes.add(code);
				// }
			}
		}

		return conceptCodes;
	}


	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getVocabulary() {
		return vocabulary;
	}

	public void setVocabulary(String vocabulary) {
		this.vocabulary = vocabulary;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
