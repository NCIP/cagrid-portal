package gov.nih.nci.cagrid.data.utilities;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlresultset.CQLObjectResult;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.axis.message.MessageElement;

/** 
 *  CQLQueryResultsUtil
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 4, 2006 
 * @version $Id$ 
 */
public class CQLQueryResultsUtil {

	public static CQLObjectResult createObjectResult(Object obj) {
		CQLObjectResult objectResult = new CQLObjectResult();
		objectResult.setType(obj.getClass().getName());
		MessageElement anyElement = new MessageElement(Utils.getRegisteredQName(obj.getClass()), obj);
		objectResult.set_any(new MessageElement[] {anyElement});
		return objectResult;
	}
	
	
	public static CQLQueryResults createQueryResults(List objects) {
		return createQueryResults(objects.iterator());
	}
	
	
	public static CQLQueryResults createQueryResults(Iterator resultIter) {
		CQLQueryResults results = new CQLQueryResults();
		String type = null;
		LinkedList objects = new LinkedList();
		while (resultIter.hasNext()) {
			objects.add(resultIter.next());
			if (type == null) {
				type = objects.getFirst().getClass().getName();
			}
		}
		CQLObjectResult[] objectResults = new CQLObjectResult[objects.size()];
		objects.toArray(objectResults);
		results.setObjectResult(objectResults);
		return results;
	}
}
