package gov.nih.nci.cagrid.data.utilities;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlresultset.CQLObjectResult;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResultsType;

import java.util.Iterator;
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
	
	
	public static CQLQueryResultsType createQueryResults(List objects) {
		CQLQueryResultsType results = new CQLQueryResultsType();
		CQLObjectResult[] objectResults = new CQLObjectResult[objects.size()];
		Iterator objectIter = objects.iterator();
		int index = 0;
		while (objectIter.hasNext()) {
			objectResults[index] = createObjectResult(objectIter.next());
		}
		results.setObjectResult(objectResults);
		return results;
	}
}
