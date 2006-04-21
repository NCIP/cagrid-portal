package gov.nih.nci.cagrid.data.cql.cacore;

import gov.nih.nci.cabio.domain.Gene;
import gov.nih.nci.cabio.domain.impl.GeneImpl;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQueryType;
import gov.nih.nci.cagrid.cqlresultset.CQLObjectResult;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResultsType;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.cql.InitializationException;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axis.message.MessageElement;

/** 
 *  IgnorantQueryProcessor
 *  TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 3, 2006 
 * @version $Id$ 
 */
public class IgnorantQueryProcessor implements CQLQueryProcessor {
	
	private ApplicationService coreService = null;

	public void init(String initString) throws InitializationException {
		if (initString.length() == 0) {
			initString = "http://kramer.bmi.ohio-state.edu:8080/cacore31/server/HTTPServer";
		}
		System.out.println("Data Service connecting out to core service:");
		System.out.println("\t" + initString);
		coreService = ApplicationService.getRemoteInstance(initString);
	}


	public CQLQueryResultsType processQuery(CQLQueryType query) throws Exception {
		Gene gene = new GeneImpl();
		gene.setSymbol("brca*"); // searching for all genes whose symbol start with brca
		
		List resultList = coreService.search(Gene.class, gene);
		CQLQueryResultsType results = new CQLQueryResultsType();
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
}
