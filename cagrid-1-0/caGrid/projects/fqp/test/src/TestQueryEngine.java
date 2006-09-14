import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.fqp.processor.FederatedQueryEngine;


/**
 * @author oster
 * 
 */
public class TestQueryEngine {

	public static void main(String[] args) {
		try {
			FederatedQueryEngine fqp = new FederatedQueryEngine();
			DCQLQuery dcql = (DCQLQuery) Utils.deserializeDocument(args[0], DCQLQuery.class);
			CQLQueryResults results = fqp.execute(dcql);
			CQLQueryResultsIterator iterator = new CQLQueryResultsIterator(results, true);
			int resultCount = 0;
			while (iterator.hasNext()) {
				System.out.println("=====RESULT [" + resultCount++ + "] =====");
				System.out.println(iterator.next());
				System.out.println("=====END RESULT=====\n\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
