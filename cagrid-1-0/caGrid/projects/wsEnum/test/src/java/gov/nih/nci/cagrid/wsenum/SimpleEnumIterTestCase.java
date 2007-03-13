package gov.nih.nci.cagrid.wsenum;

import gov.nih.nci.cabio.domain.Gene;
import gov.nih.nci.cagrid.common.ConfigurableObjectDeserializationContext;
import gov.nih.nci.cagrid.wsenum.utils.SimplePersistantSDKObjectIterator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.axis.AxisEngine;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.MessageContext;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.server.AxisServer;
import org.apache.axis.types.Duration;
import org.globus.ws.enumeration.EnumIterator;
import org.globus.ws.enumeration.IterationConstraints;
import org.globus.ws.enumeration.IterationResult;
import org.xml.sax.InputSource;

/** 
 *  SimpleEnumIterTestCase
 *  Test case to test the simple enumeration iterator
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Nov 3, 2006 
 * @version $Id$ 
 */
public class SimpleEnumIterTestCase extends TestCase {
	
	private QName geneQname;
	private MessageContext messageContext;
	private List<Object> objectList;
	private EnumIterator enumIterator;
	
	public SimpleEnumIterTestCase(String name) {
		super(name);
	}
	
	
	public void setUp() {
		String wsddFilename = "test" + File.separator + "resources" + File.separator + "cabio-client-config.wsdd";
		// set up the message context
		try {
			messageContext = createMessageContext(new FileInputStream(wsddFilename));
		} catch (FileNotFoundException ex) {
			fail("Wsdd file not found: " + ex.getMessage());
		}
		// QName for the data type
		geneQname = new QName("gme://caCORE.cabig/3.0/gov.nih.nci.cabio.domain", "Gene");
		// need a list of SDK objects
		objectList = new ArrayList<Object>();
		for (int i = 0; i < 10; i++) {
			Gene g = new Gene();
			g.setSymbol("Symbol" + i);
			g.setFullName("Fake Gene Number " + i);
			objectList.add(g);
		}
		// set up the enum iterator
		try {
			enumIterator = SimplePersistantSDKObjectIterator.createIterator(objectList, geneQname, new FileInputStream(wsddFilename));
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Error initializing the Simple SDK Iterator: " + ex.getMessage());
		}			
	}
	
	
	public void tearDown() {
		enumIterator.release();
		try {
			enumIterator.next(new IterationConstraints());
			fail("Enumeration released, but did not throw exception on next() call");
		} catch (Exception ex) {
			assertTrue("Enumeration released, threw " + NoSuchElementException.class.getName() + " on next()", 
				ex instanceof NoSuchElementException);
		}
	}
	
	
	public void testRetrieveSingleResult() {
		// the simple iterator ignores this anyway
		Duration maxWait = new Duration();
		maxWait.setSeconds(10d);
		IterationConstraints cons = new IterationConstraints(1, -1, maxWait);
		IterationResult result = enumIterator.next(cons);
		SOAPElement[] rawElements = result.getItems();
		assertTrue("Some elements were returned", rawElements != null);
		assertTrue("Only one result was returned", rawElements.length == 1);
		// deserialize the result
		try {
			Gene g = (Gene) deserializeDocumentString(
				rawElements[0].getValue(), Gene.class);
			boolean found = geneInOriginalList(g);
			assertTrue("Returned gene found in original object list", found);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Error deserializing result: " + ex.getMessage());
		}
	}
	
	
	public void testRetrieveMultipleResults() {
		// the simple iterator ignores this anyway
		Duration maxWait = new Duration();
		maxWait.setSeconds(10d);
		IterationConstraints cons = new IterationConstraints(3, -1, maxWait);
		IterationResult result = enumIterator.next(cons);
		SOAPElement[] rawElements = result.getItems();
		assertTrue("Some elements were returned", rawElements != null);
		assertTrue("Three results were returned", rawElements.length == 3);
		for (int i = 0; i < rawElements.length; i++) {
			// deserialize the result
			try {
				Gene g = (Gene) deserializeDocumentString(
					rawElements[i].getValue(), Gene.class);
				boolean found = geneInOriginalList(g);
				assertTrue("Returned gene found in original object list", found);
			} catch (Exception ex) {
				ex.printStackTrace();
				fail("Error deserializing result: " + ex.getMessage());
			}
		}
	}
	
	
	public void testRetrieveAllResults() {
		// the simple iterator ignores this anyway
		Duration maxWait = new Duration();
		maxWait.setSeconds(10d);
		// ask for more results than we actually have
		IterationConstraints cons = new IterationConstraints(objectList.size() + 1, -1, maxWait);
		IterationResult result = enumIterator.next(cons);
		SOAPElement[] rawElements = result.getItems();
		assertTrue("Some elements were returned", rawElements != null);
		assertTrue(String.valueOf(objectList.size()) + " results were returned", 
			rawElements.length == objectList.size());
		assertTrue("End of sequence reached", result.isEndOfSequence());
		for (int i = 0; i < rawElements.length; i++) {
			// deserialize the result
			try {
				Gene g = (Gene) deserializeDocumentString(
					rawElements[i].getValue(), Gene.class);
				boolean found = geneInOriginalList(g);
				assertTrue("Returned gene found in original object list", found);
			} catch (Exception ex) {
				ex.printStackTrace();
				fail("Error deserializing result: " + ex.getMessage());
			}
		}
	}
	
	
	private boolean geneInOriginalList(Gene g) {
		// verify the gene is part of the original object list
		for (int i = 0; i < objectList.size(); i++) {
			Gene current = (Gene) objectList.get(i);
			if (current.getSymbol().equals(g.getSymbol()) 
				&& current.getFullName().equals(g.getFullName())) {
				return true;
			}
		}
		return false;
	}
	
	
	private Object deserializeDocumentString(String xmlDocument, Class objectClass) throws Exception {
		InputSource objectSource = new InputSource(new StringReader(xmlDocument));
		ConfigurableObjectDeserializationContext desContext	= 
			new ConfigurableObjectDeserializationContext(messageContext, objectSource, objectClass);
		return desContext.getValue();
	}
	
	
	private MessageContext createMessageContext(InputStream configStream) {
		EngineConfiguration engineConfig = new FileProvider(configStream);
		AxisEngine engine = new AxisServer(engineConfig);
		
		MessageContext context = new MessageContext(engine);
		context.setEncodingStyle("");
		context.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		// the following two properties prevent xsd types from appearing in
		// every single element in the serialized XML
		context.setProperty(AxisEngine.PROP_EMIT_ALL_TYPES, Boolean.FALSE);
		context.setProperty(AxisEngine.PROP_SEND_XSI, Boolean.FALSE);
		return context;
	}
	

	public static void main(String[] args) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(SimpleEnumIterTestCase.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
