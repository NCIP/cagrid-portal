package test;

import gov.nih.nci.cagrid.evs.service.EVSDescLogicConceptSearchParams;
import gov.nih.nci.cagrid.evsgridservice.client.EVSGridServiceClient;
import gov.nih.nci.evs.domain.DescLogicConcept;
import gov.nih.nci.evs.query.EVSQueryImpl;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class EVSUnmarshallingProblem {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		String evsGridSvcUrl = "http://localhost:8081/wsrf/services/cagrid/EVSGridService";
		String evsAppSvcUrl = "http://cabio.nci.nih.gov/cacore31/http/remoteService";
		String keywords = "blood*";
		String vocabulary = "NCI_Thesaurus";
		int limit = 10;
		
		//Search app service
		Set codes = new HashSet();
		ApplicationService appService = ApplicationServiceProvider
				.getRemoteInstance(evsAppSvcUrl);
		EVSQueryImpl query = new EVSQueryImpl();
		query.searchDescLogicConcepts(vocabulary, keywords, limit);
		List results = appService.evsSearch(query);
		for (Iterator i = results.iterator(); i.hasNext();) {
			DescLogicConcept concept = (DescLogicConcept) i.next();
			String code = concept.getCode();
			System.out.println("From appService: " + concept.getCode());
			codes.add(code);
		}
		if(codes.size() == 0){
			throw new RuntimeException("no codes from appService");
		}
		
		//Search grid service
		EVSGridServiceClient evsClient = new EVSGridServiceClient(evsGridSvcUrl);
		EVSDescLogicConceptSearchParams evsSearchParams = new EVSDescLogicConceptSearchParams();
		evsSearchParams.setVocabularyName(vocabulary);
		evsSearchParams.setSearchTerm(keywords);
		evsSearchParams.setLimit(limit);
		DescLogicConcept[] concepts = evsClient
				.searchDescLogicConcept(evsSearchParams);
		if (concepts != null && concepts.length > 0) {
			for (int j = 0; j < concepts.length; j++) {
				DescLogicConcept concept = concepts[j];
				if (concept != null) {
					System.out
							.println("From gridService: " + concept.getName());
					String code = concept.getCode();
					if (code != null && code.trim().length() > 0
							&& !code.trim().equals("null")) {
						codes.remove(code);
					}
				}
			}
		}
		if(codes.size() != 0){
			System.out.println("FAILURE");
		}else{
			System.out.println("SUCCESS");
		}
	}

}
