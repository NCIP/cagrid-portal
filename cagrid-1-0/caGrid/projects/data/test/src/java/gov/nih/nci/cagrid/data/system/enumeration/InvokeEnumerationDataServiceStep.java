package gov.nih.nci.cagrid.data.system.enumeration;

import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Group;
import gov.nih.nci.cagrid.cqlquery.LogicalOperator;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.data.enumeration.client.EnumerationDataServiceClient;
import gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;
import gov.nih.nci.cagrid.enumeration.stubs.response.EnumerationResponseContainer;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.NoSuchElementException;

import javax.xml.soap.SOAPElement;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.AxisClient;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.utils.ClassUtils;
import org.globus.ws.enumeration.ClientEnumIterator;
import org.projectmobius.bookstore.Book;
import org.xmlsoap.schemas.ws._2004._09.enumeration.DataSource;
import org.xmlsoap.schemas.ws._2004._09.enumeration.Release;
import org.xmlsoap.schemas.ws._2004._09.enumeration.service.EnumerationServiceAddressingLocator;

import com.atomicobject.haste.framework.Step;

/** 
 *  InvokeEnumerationDataServiceStep
 *  Testing step to invoke an enumeration data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 23, 2006 
 * @version $Id: InvokeEnumerationDataServiceStep.java,v 1.8 2007-05-09 15:49:12 dervin Exp $ 
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
	
    
    private static DataSource createDataSource(EndpointReferenceType epr) throws RemoteException {

        EnumerationServiceAddressingLocator locator = new EnumerationServiceAddressingLocator();
        
        // attempt to load our context sensitive wsdd file
        InputStream resourceAsStream = ClassUtils.getResourceAsStream(
            InvokeEnumerationDataServiceStep.class, "client-config.wsdd");
        if (resourceAsStream != null) {
            // we found it, so tell axis to configure an engine to use it
            EngineConfiguration engineConfig = new FileProvider(resourceAsStream);
            // set the engine of the locator
            locator.setEngine(new AxisClient(engineConfig));
        }
        DataSource port = null;
        try {
            port = locator.getDataSourcePort(epr);
        } catch (Exception e) {
            throw new RemoteException("Unable to locate portType:" + e.getMessage(), e);
        }

        return port;
    }
    

	public void runStep() throws Throwable {
		System.out.println("Running step " + getClass().getName());
		String serviceUrl = getServiceUrl();
		System.out.println("Invoking service at URL " + serviceUrl);
		// create the generic enumeration client
		EnumerationDataServiceClient client = new EnumerationDataServiceClient(serviceUrl);
		
		// iterate over an enumeration response
		iterateEnumeration(client);
		
        // make sure invalid classes still throw exceptions
		queryForInvalidClass(client);
		
        // make sure malformed queries behave as expected
		submitMalformedQuery(client);
    }
	
	
	private void queryForInvalidClass(EnumerationDataServiceClient client) throws Exception {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("non.existant.class");
		query.setTarget(target);
		EnumerationResponseContainer container = null;
		try {
			container = client.enumerationQuery(query);
		} catch (QueryProcessingExceptionType ex) {
			assertTrue("Query Processing Exception Type thrown", true);
		} finally {
			if (container != null && container.getContext() != null) {
				Release release = new Release();
				release.setEnumerationContext(container.getContext());
                createDataSource(container.getEPR()).releaseOp(release);
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
		EnumerationResponseContainer container = null;
		try {
			container = client.enumerationQuery(query);
		} catch (MalformedQueryExceptionType ex) {
			assertTrue("Malformed Query Exception Type thrown", true);
		} finally {
			if (container != null && container.getContext() != null) {
				Release release = new Release();
				release.setEnumerationContext(container.getContext());
				createDataSource(container.getEPR()).releaseOp(release);
			}
		}		
	}
	
	
	private EnumerationResponseContainer queryForBooks(EnumerationDataServiceClient client) throws Exception {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName(Book.class.getName());
		query.setTarget(target);
		EnumerationResponseContainer container = null;
		try {
			container = client.enumerationQuery(query);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return container;
	}
	
	
	private void iterateEnumeration(EnumerationDataServiceClient client) throws Exception {
		EnumerationResponseContainer container = queryForBooks(client);
        
        DataSource dataSource = createDataSource(container.getEPR());
        
		/*
		 * This is the preferred way to access an enumeration, but the client enum iterator hides
		 * remote exceptions from the user and throws an empty NoSuchElement exception.
		 */
		ClientEnumIterator iter = new ClientEnumIterator(dataSource, container.getContext());
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
            try {
                iter.next();
                fail("Call to next() after release should have failed!");
            } catch (NoSuchElementException ex) {
                // expected
            } catch (Exception ex) {
                ex.printStackTrace();
                fail("Exception other than NoSuchElementException thrown: " + ex.getClass().getName());
            }
		}
		assertTrue("No results were returned from the enumeration", resultCount != 0);
		
		// this is my own impl to show and handle the exceptions
		/*
		Pull pull = new Pull(); // who made these names?
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
