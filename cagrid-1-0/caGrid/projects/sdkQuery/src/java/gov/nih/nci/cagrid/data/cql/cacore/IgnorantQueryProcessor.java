package gov.nih.nci.cagrid.data.cql.cacore;

import gov.nih.nci.cabio.domain.Gene;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLObjectResult;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.InitializationException;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis.message.MessageElement;

/** 
 *  IgnorantQueryProcessor
 *  IQP Takes a cql query, ignores it, and returns some results from a
 *  caBIO / caCORE SDK data source
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 3, 2006 
 * @version $Id$ 
 */
public class IgnorantQueryProcessor extends CQLQueryProcessor {
	public static final String APPLICATION_SERVICE_URL = "AppserviceUrl";
	
	private ApplicationService coreService = null;
	
	public IgnorantQueryProcessor() {
		super();
	}
	
	
	public void initialize(Map properties) throws InitializationException {
		String serviceUrl = (String) properties.get(APPLICATION_SERVICE_URL);
		
		if (serviceUrl == null || serviceUrl.length() == 0) {
			serviceUrl = "http://kramer.bmi.ohio-state.edu:8080/cacore31/server/HTTPServer";
		}
		System.out.println("Data Service connecting out to core service:");
		System.out.println("\t" + serviceUrl);
		coreService = ApplicationService.getRemoteInstance(serviceUrl);
	}


	public CQLQueryResults processQuery(CQLQuery query) throws MalformedQueryException, QueryProcessingException {
		Gene gene = new Gene();
		gene.setSymbol("brca*"); // searching for all genes whose symbol start with brca
		List resultList = null;
		try {
			resultList = coreService.search(Gene.class, gene);
		} catch (ApplicationException ex) {
			throw new QueryProcessingException("Error in SDK application service: " + ex.getMessage(), ex);
		}
		CQLQueryResults results = new CQLQueryResults();
		CQLObjectResult[] objectResults = new CQLObjectResult[resultList.size()];
		for (int i = 0; i < resultList.size(); i++) {
			Gene returnedGene = (Gene) resultList.get(i);
			System.out.println("Symbol: " + returnedGene.getSymbol() +
				"\tTaxon:" +
				returnedGene.getTaxon().getScientificName() +
				"\tName " + returnedGene.getFullName());
			CQLObjectResult result = new CQLObjectResult();
			result.setType(Gene.class.getName());
			QName geneQname = Utils.getRegisteredQName(Gene.class);
			MessageElement anyElement = new MessageElement(geneQname, gene);
			result.set_any(new MessageElement[] {anyElement});
		}
		results.setObjectResult(objectResults);
		return results;
	}
	
	
	public Map getRequiredParameters() {
		Map params = new HashMap();
		params.put(APPLICATION_SERVICE_URL, "http://kramer.bmi.ohio-state.edu:8080/cacore31/server/HTTPServer");
		return params;
	}
}
