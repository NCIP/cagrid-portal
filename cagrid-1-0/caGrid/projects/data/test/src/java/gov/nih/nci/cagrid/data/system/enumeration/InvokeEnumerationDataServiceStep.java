package gov.nih.nci.cagrid.data.system.enumeration;

import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Group;
import gov.nih.nci.cagrid.cqlquery.LogicalOperator;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.data.enumeration.client.EnumerationDataServiceClient;
import gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;

import java.util.NoSuchElementException;

import javax.xml.soap.SOAPElement;

import org.globus.ws.enumeration.ClientEnumIterator;
import org.projectmobius.bookstore.Book;
import org.xmlsoap.schemas.ws._2004._09.enumeration.EnumerateResponse;
import org.xmlsoap.schemas.ws._2004._09.enumeration.Release;

import com.atomicobject.haste.framework.Step;

/** 
 *  InvokeEnumerationDataServiceStep
 *  Testing step to invoke an enumeration data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 23, 2006 
 * @version $Id: InvokeEnumerationDataServiceStep.java,v 1.1 2006-12-18 14:48:47 dervin Exp $ 
 */
public class InvokeEnumerationDataServiceStep extends Step {
	public static final String URL_PART = "/wsrf/services/cagrid/";
	
	private String hostName;
	private int port;
	private String serviceName;
	
	public InvokeEnumerationDataServiceStep(String hostName, int port, String serviceName) {
		this.hostName = hostName;
		this.port = port;
		this.serviceName = serviceName;
	}
	

	public void runStep() throws Throwable {
		System.out.println("Running step " + getClass().getName());
		String serviceUrl = getServiceUrl();
		System.out.println("Invoking service at URL " + serviceUrl);
		// create the generic enumeration client
		EnumerationDataServiceClient client = new EnumerationDataServiceClient(serviceUrl);
		
		// run an enumeration query
		EnumerateResponse response = queryForBooks(client);
		
		// iterate the response
		iterateEnumeration(response, client);
		
		queryForInvalidClass(client);
		
		submitMalformedQuery(client);
	}
	
	
	private void queryForInvalidClass(EnumerationDataServiceClient client) throws Exception {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("non.existant.class");
		query.setTarget(target);
		EnumerateResponse response = null;
		try {
			response = client.enumerationQuery(query);
		} catch (QueryProcessingExceptionType ex) {
			assertTrue("Query Processing Exception Type thrown", true);
		} finally {
			if (response != null) {
				// TODO: release the enumeration
			}
		}
	}
	
	
	private void submitMalformedQuery(EnumerationDataServiceClient client) throws Exception {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
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
		EnumerateResponse response = null;
		try {
			response = client.enumerationQuery(query);
		} catch (MalformedQueryExceptionType ex) {
			assertTrue("Malformed Query Exception Type thrown", true);
		} finally {
			if (response != null && response.getEnumerationContext() != null) {
				Release release = new Release();
				release.setEnumerationContext(response.getEnumerationContext());
				client.releaseOp(release);
			}
		}		
	}
	
	
	private EnumerateResponse queryForBooks(EnumerationDataServiceClient client) throws Exception {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName(Book.class.getName());
		query.setTarget(target);
		EnumerateResponse response = null;
		try {
			response = client.enumerationQuery(query);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return response;
	}
	
	
	private void iterateEnumeration(EnumerateResponse response, EnumerationDataServiceClient dataSource) throws Exception {
		
		/*
		 * This is the preferred way to access an enumeration, but the client enum iterator hides
		 * remote exceptions from the user and throws an empty NoSuchElement exception.
		 */
		ClientEnumIterator iter = new ClientEnumIterator(dataSource, response.getEnumerationContext());
		int resultCount = 0;
		try {
			while (iter.hasNext()) {
				SOAPElement elem = (SOAPElement) iter.next();
				String elemText = elem.toString();
				// make sure it's a book element
				int bookIndex = elemText.indexOf("Book");
				if (bookIndex == -1) {
					throw new NoSuchElementException("Element returned was not of the type Book!");
				}
				resultCount++;
			}
		} catch (NoSuchElementException ex) {
			if (resultCount == 0) {
				throw ex;
			}
		} finally {
			iter.release();
		}
		assertTrue("Results were returned from the enumeration", resultCount != 0);
		
		// this is my own impl to show and handle the exceptions
		/*
		Pull pull = new Pull(); // what dumbass made these names?
		pull.setEnumerationContext(response.getEnumerationContext());
		int resultCount = 0;
		boolean stop = false;
		try {
			while (!stop) {
				PullResponse res = dataSource.pullOp(pull);
				stop = res.getEndOfSequence() != null; // seriously, why not use a boolean?
				ItemListType items = res.getItems();
				if (items != null) {
					SOAPElement[] elems = items.get_any();
					for (int i = 0; i < elems.length; i++) {
						String elemText = elems[i].toString();
						int bookIndex = elemText.indexOf("Book");
						if (bookIndex == -1) {
							throw new NoSuchElementException("Element returned was not of type book!");
						}
						resultCount++;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Release release = new Release();
			release.setEnumerationContext(response.getEnumerationContext());
			try {
				dataSource.releaseOp(release);
			} catch (Exception exx) {
				System.err.println("ERROR IN RELEASE:");
				exx.printStackTrace();
			}
			throw ex;
		}
		*/
	}
	
	
	private String getServiceUrl() {
		return "http://" + hostName + ":" + port + URL_PART + serviceName;
	}
}
