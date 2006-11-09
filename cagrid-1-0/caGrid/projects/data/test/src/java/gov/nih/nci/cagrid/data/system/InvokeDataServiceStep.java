package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Group;
import gov.nih.nci.cagrid.cqlquery.LogicalOperator;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLObjectResult;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.client.DataServiceClient;
import gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;

import java.util.Iterator;

import org.apache.axis.message.MessageElement;
import org.projectmobius.bookstore.Book;

import com.atomicobject.haste.framework.Step;

/** 
 *  InvokeDataServiceStep
 * 	Step to invoke the deployed data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 8, 2006 
 * @version $Id: InvokeDataServiceStep.java,v 1.2 2006-11-09 15:27:52 dervin Exp $ 
 */
public class InvokeDataServiceStep extends Step {
	public static final String URL_PART = "/wsrf/services/cagrid/";
	
	private String hostName;
	private int port;
	private String serviceName;
	
	public InvokeDataServiceStep(String hostName, int port, String serviceName) {
		this.hostName = hostName;
		this.port = port;
		this.serviceName = serviceName;
	}
	

	public void runStep() throws Throwable {
		System.out.println("Running step: " + getClass().getName());
		// create the data service client
		DataServiceClient client = new DataServiceClient(getServiceUrl());
		// run a query
		CQLQueryResults results = queryForBooks(client);
		// verify the query has object results
		checkForObjectResults(results);
		// iterate the results
		iterateBookResults(results);
		// run a query for an invalid class
		queryForInvalidClass(client);
		// run an invalid syntax query
		submitMalformedQuery(client);
	}
	
	
	private CQLQueryResults queryForBooks(DataServiceClient client) throws Exception {
		CQLQuery query = new CQLQuery();
		Object target = new Object();
		target.setName(Book.class.getName());
		query.setTarget(target);
		CQLQueryResults results = null;
		try {
			results = client.query(query);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return results;
	}
	
	
	private void checkForObjectResults(CQLQueryResults results) throws Exception {
		CQLObjectResult[] objResults = results.getObjectResult();
		if (objResults == null || objResults.length == 0) {
			fail("No object results returned");
		}
		for (int i = 0; i < objResults.length; i++) {
			MessageElement[] elements = objResults[i].get_any();
			if (elements == null) {
				fail("Object result returned with null object contents");
			}
			if (elements.length != 1) {
				fail("Object result returned with number of object contents != 1 (" 
					+ elements.length + " found!)");
			}
		}
	}
	
	
	private void iterateBookResults(CQLQueryResults results) throws Exception {
		Iterator iter = new CQLQueryResultsIterator(results);
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			if (!(obj instanceof Book)) {
				fail("Iterator returned other than book (" + obj.getClass().getName() + ")");
			}
		}
	}
	
	
	private void queryForInvalidClass(DataServiceClient client) throws Exception {
		CQLQuery query = new CQLQuery();
		Object target = new Object();
		target.setName("non.existant.class");
		query.setTarget(target);
		
		try {
			client.query(query);
		} catch (QueryProcessingExceptionType ex) {
			assertTrue("Query Processing Exception Type thrown", true);
		}
	}
	
	
	private void submitMalformedQuery(DataServiceClient client) throws Exception {
		CQLQuery query = new CQLQuery();
		Object target = new Object();
		target.setName(Book.class.getName());
		Attribute attrib1 = new Attribute("name", Predicate.LIKE, "E%");
		target.setAttribute(attrib1);
		Group group = new Group();
		group.setLogicRelation(LogicalOperator.AND);
		group.setAttribute(new Attribute[] {
			new Attribute("author", Predicate.IS_NOT_NULL, ""),
			new Attribute("ISBN", Predicate.IS_NULL, "")
		});
		target.setGroup(group);
		query.setTarget(target);
		try {
			client.query(query);
		} catch (MalformedQueryExceptionType ex) {
			assertTrue("Malformed Query Exception Type thrown", true);
		}
		
	}
	
	
	private String getServiceUrl() {
		return "http://" + hostName + ":" + port + URL_PART + serviceName; 
	}
}
