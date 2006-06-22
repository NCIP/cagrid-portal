package gov.nih.nci.cagrid.data.utilities;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlresultset.CQLObjectResult;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

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
		QName objectQname = Utils.getRegisteredQName(obj.getClass());
		if (objectQname == null) {
			throw new NullPointerException("No qname found for class " + obj.getClass().getName() 
				+ ". Check your client or server-config.wsdd");
		}
		MessageElement anyElement = new MessageElement(objectQname, obj);
		objectResult.set_any(new MessageElement[] {anyElement});
		return objectResult;
	}
	
	
	public static CQLQueryResults createQueryResults(List objects) {
		return createQueryResults(objects.iterator());
	}
	
	
	public static CQLQueryResults createQueryResults(Iterator resultIter) {
		CQLQueryResults results = new CQLQueryResults();
		LinkedList objects = new LinkedList();
		while (resultIter.hasNext()) {
			Object obj = resultIter.next();
			objects.add(createObjectResult(obj));
		}
		CQLObjectResult[] objectResults = new CQLObjectResult[objects.size()];
		objects.toArray(objectResults);
		results.setObjectResult(objectResults);
		return results;
	}
}
