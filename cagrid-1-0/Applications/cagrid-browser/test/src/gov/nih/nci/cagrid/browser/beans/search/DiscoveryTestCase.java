/**
 * $Id $
 */
package gov.nih.nci.cagrid.browser.beans.search;

import gov.nih.nci.cagrid.browser.search.ConceptCodeSearch;
import gov.nih.nci.cagrid.browser.search.SemanticKeywordSearch;
import gov.nih.nci.cagrid.browser.search.ServiceDiscoveryThread;
import gov.nih.nci.cagrid.browser.search.ServiceKeywordSearch;
import gov.nih.nci.cagrid.browser.search.ServiceTypeSearch;
import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.evs.service.EVSDescLogicConceptSearchParams;
import gov.nih.nci.cagrid.evsgridservice.client.EVSGridServiceClient;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClass;
import gov.nih.nci.evs.domain.DescLogicConcept;
import gov.nih.nci.evs.query.EVSQueryImpl;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;

import java.io.StringWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.utils.XmlUtils;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DiscoveryTestCase extends TestCase {

	
	private static final String[] INDEX_SVC_URLS = {"http://cbiovdev5017.nci.nih.gov:28080/wsrf/services/DefaultIndexService"};
	private static final String EVS_SVC_URL = "http://cbiovdev5017.nci.nih.gov:48080/wsrf/services/cagrid/EVSGridService";
	
	/**
	 * 
	 */
	public DiscoveryTestCase() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public DiscoveryTestCase(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();
//		suite.addTest(new DiscoveryTestCase("testServiceTypeSearch"));
//		suite.addTest(new DiscoveryTestCase("testConceptCodeSearch"));
//		suite.addTest(new DiscoveryTestCase("testSemanticKeywordSearch"));
//		suite.addTest(new DiscoveryTestCase("testServiceKeywordSearch"));
//		suite.addTest(new DiscoveryTestCase("testRetrieveStandardServiceMetadata"));
		suite.addTest(new DiscoveryTestCase("testRetrieveDomainModel"));
		return suite;
	}
	
	public void setUp(){
		try {


		} catch (Exception ex) {
			throw new RuntimeException("Error setting up client: " + ex.getMessage(), ex);
		}
	}
	
	public void testServiceTypeSearch(){
		try{
			ServiceTypeSearch sts1 = new ServiceTypeSearch();
			sts1.setType(ServiceTypeSearch.DATA);
			sts1.setIndexServiceURLs(INDEX_SVC_URLS);
			Set dataEprs = makeSet(runSearch(sts1, 5000));
			assertNotNull("dataEprs is null", dataEprs);
			
			ServiceTypeSearch sts2 = new ServiceTypeSearch();
			sts2.setType(ServiceTypeSearch.ANALYTICAL);
			sts2.setIndexServiceURLs(INDEX_SVC_URLS);
			Set analyticalEprs = makeSet(runSearch(sts2, 5000));
			assertNotNull("analyticalEprs is null", analyticalEprs);
			
			ServiceTypeSearch sts3 = new ServiceTypeSearch();
			sts3.setIndexServiceURLs(INDEX_SVC_URLS);
			Set anyEprs = makeSet(runSearch(sts3, 5000));
			assertNotNull("anyEprs is null", anyEprs);
			
			Set bigger = dataEprs;
			Set smaller = analyticalEprs;
			if(analyticalEprs.size() > dataEprs.size()){
				bigger = analyticalEprs;
				smaller = dataEprs;
			}
			for(Iterator i = bigger.iterator(); i.hasNext();){
				EndpointReferenceType epr = (EndpointReferenceType)i.next();
				if(smaller.contains(epr)){
					fail("data and analytical sets ore not disjoint");
				}
			}
			
			//This fails if there are any bogus registrations.
//			if(anyEprs.size() != dataEprs.size() + analyticalEprs.size()){
//				fail("data(" + dataEprs.size() + ") + analytical(" + analyticalEprs.size() + ") != any(" + anyEprs.size() + ")");
//			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			fail("Error searching: " + ex.getMessage());
		}
	}
	
	public void testConceptCodeSearch(){
		String gridService = "C63541";
		
		try{
			ConceptCodeSearch ccs1 = new ConceptCodeSearch();
			ccs1.setType(ConceptCodeSearch.SERVICE);
			ccs1.setCode(gridService);
			ccs1.setIndexServiceURLs(INDEX_SVC_URLS);
			Set serviceEprs = makeSet(runSearch(ccs1, 5000));
			assertNotNull("serviceEprs is null", serviceEprs);
			assertTrue("serviceEprs.size() == 0", serviceEprs.size() > 0);
			
			
		}catch(Exception ex){
			ex.printStackTrace();
			fail("Error searching: " + ex.getMessage());
		}
	}
	
	public void testSemanticKeywordSearch(){
		
		String evsAppSvcUrl = "http://cabio.nci.nih.gov/cacore31/http/remoteService";
		String keywords = "gene*, target";
//		String keywords = "blood*";
		String vocabulary = "NCI_Thesaurus";
		int limit = 10;
		
		try{
			
			EVSGridServiceClient evsClient = new EVSGridServiceClient(EVS_SVC_URL);
			
			Set codes = new HashSet();
			ApplicationService appService = ApplicationServiceProvider.getRemoteInstance(evsAppSvcUrl);
			EVSQueryImpl query = new EVSQueryImpl();
			query.searchDescLogicConcepts(vocabulary, keywords, limit);
			List results = appService.evsSearch(query);
			for(Iterator i = results.iterator(); i.hasNext();){
				DescLogicConcept concept = (DescLogicConcept)i.next();
				String code = concept.getCode();
				System.out.println("From appService: " + concept.getCode());
				codes.add(code);
			}
			assertTrue("no codes from appService", codes.size() > 0);
			
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
						System.out.println("From gridService: " + concept.getName());
						String code = concept.getCode();
						if (code != null && code.trim().length() > 0
								&& !code.trim().equals("null")) {
							codes.remove(code);
						}
					}
				}
			}
			assertTrue("sets not equal", codes.size() == 0);
			
			
			SemanticKeywordSearch sks = new SemanticKeywordSearch();
			sks.setKeywords(keywords.split(","));
			sks.setIndexServiceURLs(INDEX_SVC_URLS);
			sks.setEvsGridServiceUrl(EVS_SVC_URL);
			sks.setLimit(limit);
			sks.setVocabulary(vocabulary);
			Set serviceEprs = makeSet(runSearch(sks, 60000));
			assertNotNull("serviceEprs is null", serviceEprs);
			assertTrue("serviceEprs.size() == 0", serviceEprs.size() > 0);
			
		}catch(Exception ex){
			ex.printStackTrace();
			fail("Error searching: " + ex.getMessage());
		}
	}
	
	public void testRetrieveStandardServiceMetadata(){
		String serviceUrl = "http://cbiovdev5017.nci.nih.gov:8680/wsrf/services/cagrid/SDK31NoSecExampleSvc";
//		String serviceUrl = "http://cbiovdev5017.nci.nih.gov:48080/wsrf/services/cagrid/CaBIOSvc";
		try{
			
			EndpointReferenceType epr = new EndpointReferenceType();
			epr.setAddress(new Address(serviceUrl));
			gov.nih.nci.cagrid.metadata.ServiceMetadata metadata = MetadataUtils.getServiceMetadata(epr);
			
			StringWriter writer = new StringWriter();
			MetadataUtils.serializeServiceMetadata(metadata, writer);
			System.out.println(writer.getBuffer().toString());
			
//            String description = metadata.getServiceDescription().getService().getDescription();
//            String name = metadata.getServiceDescription().getService().getName();
//            String version = metadata.getServiceDescription().getService().getVersion();
//            
//            System.out.println("description: " + description);
//            System.out.println("name: " + name);
//            System.out.println("version: " + version);
//            
		}catch(Exception ex){
			ex.printStackTrace();
			fail("Error retrieving metadata: " + ex.getMessage());
		}
	}
	
	public void testRetrieveDomainModel(){
		String serviceUrl = "http://cbiovdev5017.nci.nih.gov:8680/wsrf/services/cagrid/SDK31NoSecExampleSvc";
//		String serviceUrl = "http://cbiovdev5017.nci.nih.gov:48080/wsrf/services/cagrid/CaBIOSvc";
		try{
			EndpointReferenceType epr = new EndpointReferenceType();
			epr.setAddress(new Address(serviceUrl));
			DomainModel metadata = MetadataUtils.getDomainModel(epr);
			
			StringWriter writer = new StringWriter();
			MetadataUtils.serializeDomainModel(metadata, writer);
			System.out.println(writer.getBuffer().toString());
			
			UMLClass[] classes = metadata.getExposedUMLClassCollection().getUMLClass();
			for(int i = 0; i < classes.length; i++){
				System.out.println(classes[i].getClassName());
			}

		}catch(Exception ex){
			ex.printStackTrace();
			fail("Error retrieving metadata: " + ex.getMessage());
		}
	}

	
	public void testServiceKeywordSearch(){
		String[] keywords = {"Example", "Dorian"};
		
		try{
			ServiceKeywordSearch sks = new ServiceKeywordSearch();
			sks.setKeywords(keywords);
			sks.setIndexServiceURLs(INDEX_SVC_URLS);
			Set serviceEprs = makeSet(runSearch(sks, 5000));
			assertNotNull("serviceEprs is null", serviceEprs);
			assertTrue("serviceEprs.size() == 0", serviceEprs.size() > 0);
			
			
		}catch(Exception ex){
			ex.printStackTrace();
			fail("Error searching: " + ex.getMessage());
		}
	}

	private Set makeSet(EndpointReferenceType[] eprs) {
		Set eprsSet = null;
		if(eprs != null){
			eprsSet = new HashSet();
			for(int i = 0; i < eprs.length; i++){
				eprsSet.add(eprs[i]);
			}
		}
		return eprsSet;
	}

	private EndpointReferenceType[] runSearch(ServiceDiscoveryThread sdt, int timeout) {
		Thread t = new Thread(sdt);
		t.start();
		try{
			t.join(timeout);
		}catch(Exception ex){
			fail("Error joining thread: " + ex.getMessage());
		}
		Exception ex = sdt.getException();
		if(ex != null){
			fail("Thread encountered error: " + ex.getMessage());
		}
		if(!sdt.isFinished()){
			fail("Thread timed out.");
		}
		return sdt.getEPRs();
	}

}
