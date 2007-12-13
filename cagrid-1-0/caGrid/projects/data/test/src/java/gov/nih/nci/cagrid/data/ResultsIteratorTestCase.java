package gov.nih.nci.cagrid.data;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.XMLUtilities;
import gov.nih.nci.cagrid.cqlresultset.CQLCountResult;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.NoSuchElementException;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;

/** 
 *  ResultsIteratorTestCase
 *  Test case to iterate CQL Query results
 * 
 * @author David Ervin
 * 
 * @created Dec 12, 2007 1:02:25 PM
 * @version $Id: ResultsIteratorTestCase.java,v 1.1 2007-12-13 16:28:14 dervin Exp $ 
 */
public class ResultsIteratorTestCase extends TestCase {
    // system property indicating where the result documents live
    public static final String RESULTS_XML_DIR = "results.xml.dir";
    
    private static Logger LOG = Logger.getLogger(ResultsIteratorTestCase.class);
    
    public ResultsIteratorTestCase(String name) {
        super(name);
    }
    
    
    /* Enable these as tests get implemented
    public void testObjectIteration() {
        
    }
    
    
    public void testObjectXmlIteration() {
        
    }
    
    
    public void testAttributeIteration() {
        
    }
    
    
    public void testAttributeXmlIteration() {
        
    }
    */
    
    
    public void testCountIteration() {
        CQLQueryResults results = loadResults("countResults.xml");
        CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results, false);
        assertTrue("Count iterator had no results", iter.hasNext());
        Object o = iter.next();
        assertNotNull("Count iteration result was null", o);
        assertEquals("Count iteration result was of unexpected type", Long.class, o.getClass());
        assertEquals("Count iteration result was of unexpected value", ((Long) o).longValue(), 200);
        assertFalse("Count iterator claims to have more results", iter.hasNext());
        try {
            iter.next();
            fail("Count iterator did not throw NoSuchElementException with no results to return");
        } catch (NoSuchElementException ex) {
            // expected
        }
    }
    
    
    public void testCountXmlIteration() {
        CQLQueryResults results = loadResults("countResults.xml");
        CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results, true);
        assertTrue("Count XML iterator had no results", iter.hasNext());
        Object o = iter.next();
        assertNotNull("Count XML iteratation result was null", o);
        assertEquals("Count XML iteration result was of unexpected type", String.class, o.getClass());
        // deserialize the result and compare to the deserialized doc from the original XML
        Element resultsRoot = loadElement("countResults.xml");
        Element countResultElement = resultsRoot.getChild("CountResult", resultsRoot.getNamespace());
        String originalString = XMLUtilities.elementToString(countResultElement);
        String resultString = (String) o;
        
        CQLCountResult originalCount = null;
        CQLCountResult resultCount = null;
        try {
            originalCount = (CQLCountResult) Utils.deserializeObject(new StringReader(originalString), CQLCountResult.class);
            resultCount = (CQLCountResult) Utils.deserializeObject(new StringReader(resultString), CQLCountResult.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error deserializing XML: " + ex.getMessage());
        }
        assertEquals("Original count and result count objects did not match", originalCount, resultCount);
        assertFalse("Count XML iterator claims to have more results", iter.hasNext());
        try {
            iter.next();
            fail("Count XML iterator did not throw NoSuchElementException with no results to return");
        } catch (NoSuchElementException ex) {
            // expected
        }
    }
    
    
    /*
    public void testIdentifierIteration() {
        // TODO: uncomment and use when identifiers are implemented
    }
    
    
    public void testIdentifierXmlIteration() {
        // TODO: uncomment and use when identifiers are implemented
    }
    */
    
    
    private CQLQueryResults loadResults(String filename) {
        CQLQueryResults results = null;
        try {
            String dir = System.getProperty(RESULTS_XML_DIR);
            LOG.debug("Results dir: " + dir);
            File dirFile = new File(dir);
            FileReader reader = new FileReader(new File(dirFile, filename));
            results = (CQLQueryResults) Utils.deserializeObject(reader, CQLQueryResults.class);
            reader.close();
        } catch (Exception ex) {
            LOG.error("Error loading results: " + ex.getMessage(), ex);
            fail("Error loading results: " + ex.getMessage());
        }
        
        return results;
    }
    
    
    private Element loadElement(String filename) {
        Element element = null;
        try {
            String dir = System.getProperty(RESULTS_XML_DIR);
            File dirFile = new File(dir);
            Document doc = XMLUtilities.fileNameToDocument(new File(dirFile, filename).getAbsolutePath());
            element = doc.detachRootElement();
        } catch (Exception ex) {
            LOG.error("Error loading results as Element: " + ex.getMessage(), ex);
            fail("Error loading results as Element: " + ex.getMessage());
        }
        return element;
    }
    
    
    public static void main(String[] args) {
        TestRunner runner = new TestRunner();
        TestResult result = runner.doRun(new TestSuite(ResultsIteratorTestCase.class));
        System.exit(result.errorCount() + result.failureCount());
    }
}
