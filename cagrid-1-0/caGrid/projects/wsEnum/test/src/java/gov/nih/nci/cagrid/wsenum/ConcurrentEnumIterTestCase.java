package gov.nih.nci.cagrid.wsenum;

import gov.nih.nci.cabio.domain.Gene;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.wsenum.utils.ConcurrenPersistantObjectEnumIterator;

import java.io.FileInputStream;
import java.io.StringWriter;

import javax.xml.soap.SOAPElement;

import org.globus.ws.enumeration.IterationConstraints;
import org.globus.ws.enumeration.IterationResult;

/** 
 *  ConcurrentEnumIterTestCase
 *  Test case to test the persistent (complicated) enumeration iterator
 *  which uses java.util.concurrent
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Nov 3, 2006 
 * @version $Id: ConcurrentEnumIterTestCase.java,v 1.4 2007-04-10 16:34:51 dervin Exp $ 
 */
public class ConcurrentEnumIterTestCase extends CompleteEnumIteratorBaseTest {
	
	public ConcurrentEnumIterTestCase() {
		super(ConcurrenPersistantObjectEnumIterator.class.getName());
	}
	
    
    public void testCharLimitExceded2() {
        // ask for all the results, but only enough chars for the first element
        int charCount = -1;
        StringWriter writer = new StringWriter();
        try {
            Utils.serializeObject(getObjectList().get(0), 
                getGeneQname(), writer, new FileInputStream(getWsddFilename()));
            charCount = writer.getBuffer().length();
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error determining object char count: " + ex.getMessage());
        }
        IterationConstraints cons = new IterationConstraints(
            getObjectList().size(), charCount, null);
        IterationResult result = getEnumIterator().next(cons);
        SOAPElement[] rawResults = result.getItems();
        assertTrue("Enumeration did not return results", rawResults != null);
        assertFalse("Enumeration returned all results", 
            rawResults.length == getObjectList().size());
        assertEquals("Unexpected number of results returned", 1, rawResults.length);
        // verify content
        Gene original = null;
        Gene returned = null;
        try {
            original = (Gene) deserializeDocumentString(
                writer.getBuffer().toString(), Gene.class);
            returned = (Gene) deserializeDocumentString(
                rawResults[0].getValue().toString(), Gene.class);
        } catch (Exception ex) {
            fail("Error deserializing objects: " + ex.getMessage());
        }
        boolean equal = original.getSymbol().equals(returned.getSymbol()) 
            && original.getFullName().equals(returned.getFullName());
        assertTrue("Expected gene and returned gene do not match", equal);
        
        // ask for results again, should get the next object
        writer = new StringWriter();
        try {
            Utils.serializeObject(getObjectList().get(1), 
                getGeneQname(), writer, new FileInputStream(getWsddFilename()));
            charCount = writer.getBuffer().length();
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error determining object char count: " + ex.getMessage());
        }
        cons = new IterationConstraints(getObjectList().size(), charCount, null);
        result = getEnumIterator().next(cons);
        rawResults = result.getItems();
        assertTrue("Enumeration did not return results", rawResults != null);
        assertFalse("Enumeration returned all results", 
            rawResults.length == getObjectList().size());
        assertEquals("Unexpected number of results returned", 1, rawResults.length);
        // verify content
        try {
            original = (Gene) deserializeDocumentString(
                writer.getBuffer().toString(), Gene.class);
            returned = (Gene) deserializeDocumentString(
                rawResults[0].getValue().toString(), Gene.class);
        } catch (Exception ex) {
            fail("Error deserializing objects: " + ex.getMessage());
        }
        equal = original.getSymbol().equals(returned.getSymbol()) 
            && original.getFullName().equals(returned.getFullName());
        assertTrue("Expected gene and returned gene do not match", equal);
    }
}
