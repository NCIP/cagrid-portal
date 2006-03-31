package gov.nih.nci.cagrid.data.cql.cacore;

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
 *  CoreQueryProcessor
 *  Implementation of CQL Query Processor that talks to a caCORE data source 
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Mar 31, 2006 
 * @version $Id$ 
 */
public class CoreQueryProcessor implements CQLQueryProcessor {
	private ApplicationService coreService;
	
	public CoreQueryProcessor() {
		this.coreService = null;
	}
	
	
	public void init(String initString) throws InitializationException {
		if (initString == null || initString.length() == 0) {
			coreService = ApplicationService.getLocalInstance();
		} else {
			coreService = ApplicationService.getRemoteInstance(initString);
		}
	}
	
	
	public CQLQueryResultsType processQuery(CQLQueryType query) throws Exception {
		if (coreService == null) {
			throw new IllegalStateException("The core service is currently null.  Call init() before calling processQuery()!");
		}
		// get the class we're trying to return here
		String targetClassName = query.getTarget().getName();
		Class targetClass = Class.forName(targetClassName);
		
		// TODO: implement CQL here
		
		List targetObjects = coreService.search(targetClass, null);
		CQLQueryResultsType results = buildResults(targetObjects);
		return results;
	}
	
	
	private CQLQueryResultsType buildResults(List objects) {
		CQLQueryResultsType results = new CQLQueryResultsType();
		// TODO: At the moment, this only deals with object results, 
		// not identifiers or attributes
		CQLObjectResult[] objectResults = new CQLObjectResult[objects.size()];
		for (int i = 0; i < objects.size(); i++) {
			Object object = objects.get(i);
			CQLObjectResult result = new CQLObjectResult();
			result.setType(object.getClass().getName());
			QName objectQname = Utils.getRegisteredQName(object.getClass());
			MessageElement anyElement = new MessageElement(objectQname, object);
			result.set_any(new MessageElement[] {anyElement});
		}
		results.setObjectResult(objectResults);
		return results;
	}
}
