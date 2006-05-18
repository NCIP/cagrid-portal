package gov.nih.nci.cagrid.data.cql.validation;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.globus.wsrf.encoding.ObjectSerializer;
import org.globus.wsrf.encoding.SerializationException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/** 
 *  CQLValidator
 *  Validates a CQL query document against the CQL schema
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 16, 2006 
 * @version $Id$ 
 */
public class JaxPValidator {
	
	public static final String SCHEMA_SOUCE_PROPERTY = "http://java.sun.com/xml/jaxp/properties/schemaSource";
	public static final String SCHEMA_LANGUAGE_PROPERTY = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	public static final String DOCUMENT_BUILDER_FACTORY_PARAM = "javax.xml.parsers.DocumentBuilderFactory";
	public static final String DEFAULT_DOCUMENT_BUILDER = "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl";
	
	public static void main(String[] args) {
		CQLQuery query = new CQLQuery();
		Object target = new Object();
		target.setName("foo.bar");
		query.setTarget(target);
		try {
			validateCql(query, "schema/Data/1_gov.nih.nci.cagrid.CQLQuery.xsd");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	

	public static void validateCql(CQLQuery query, String cqlSchemaFilename) throws MalformedQueryException, QueryProcessingException {
		// set a jaxp document builder factory if none is specified
		if (System.getProperty(DOCUMENT_BUILDER_FACTORY_PARAM) == null) {
			System.setProperty(DOCUMENT_BUILDER_FACTORY_PARAM, DEFAULT_DOCUMENT_BUILDER);
		}
		
		// create an instance of the document builder factory specified in the system parameter
		// DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		factory.setNamespaceAware(true);
		
		URL schemaUrl = null;
		try {
			schemaUrl = new File(cqlSchemaFilename).toURL();
		} catch (MalformedURLException ex) {
			throw new QueryProcessingException("Error locating cql schema: " + cqlSchemaFilename, ex);
		}
		factory.setAttribute(SCHEMA_LANGUAGE_PROPERTY, IntroduceConstants.W3CNAMESPACE);
		factory.setAttribute(SCHEMA_SOUCE_PROPERTY, schemaUrl.toString());
		
		/*
		// get an instance of the CQL Schema
		FileReader schemaReader = null;
		try {
			schemaReader = new FileReader(cqlSchemaFilename);
		} catch (FileNotFoundException ex) {
			throw new QueryProcessingException("Error opening the CQL schema: " + cqlSchemaFilename + ": "  + ex.getMessage(), ex);
		}
		SchemaFactory w3cSchemaFactory = SchemaFactory.newInstance(IntroduceConstants.W3CNAMESPACE);
		Source schemaSource = new StreamSource(schemaReader);
		Schema cqlSchema = null;
		try {
			cqlSchema = w3cSchemaFactory.newSchema(schemaSource);
		} catch (SAXException ex) {
			throw new QueryProcessingException("Error parsing the CQL schema: " + ex.getMessage(), ex);
		}
		
		// set the schema on the document builder factory
		factory.setSchema(cqlSchema);
		*/
		
		// serialize the query to XML for validation
		StringWriter xmlWriter = new StringWriter();
		QName cqlQname = Utils.getRegisteredQName(query.getClass());
		try {
			ObjectSerializer.serialize(xmlWriter, query, cqlQname);
		} catch (SerializationException ex) {
			throw new QueryProcessingException("Error serializing the query: " + ex.getMessage(), ex);
		}
		String xmlString = xmlWriter.getBuffer().toString();
		
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
			throw new QueryProcessingException("Error in configuration of Document Builder Factory: " + ex.getMessage(), ex);
		}
		builder.setErrorHandler(new CqlSchemaErrorHandler());
		InputStream xmlStream = new ByteArrayInputStream(xmlString.getBytes());
		try {
			builder.parse(xmlStream);
		} catch (IOException ex) {
			throw new QueryProcessingException("Error reading the serialized CQL query: " + ex.getMessage(), ex);
		} catch (SAXException ex) {
			throw new MalformedQueryException(ex.getMessage(), ex);
		}
	}
	
	
	private static class CqlSchemaErrorHandler implements ErrorHandler {
		
		public CqlSchemaErrorHandler() {
			
		}
		
		
		 /**
	     * Receive notification of a warning.
	     *
	     * <p>SAX parsers will use this method to report conditions that
	     * are not errors or fatal errors as defined by the XML
	     * recommendation.  The default behaviour is to take no
	     * action.</p>
	     *
	     * <p>The SAX parser must continue to provide normal parsing events
	     * after invoking this method: it should still be possible for the
	     * application to process the document through to the end.</p>
	     *
	     * <p>Filters may use this method to report other, non-XML warnings
	     * as well.</p>
	     *
	     * @param exception The warning information encapsulated in a
	     *                  SAX parse exception.
	     * @exception org.xml.sax.SAXException Any SAX exception, possibly
	     *            wrapping another exception.
	     * @see org.xml.sax.SAXParseException 
	     */
	    public void warning (SAXParseException exception) throws SAXException {
	    	 System.out.println("SAX PARSER WARNING: " + exception.getMessage());
	    }
	    
	    
	    /**
	     * Receive notification of a recoverable error.
	     *
	     * <p>This corresponds to the definition of "error" in section 1.2
	     * of the W3C XML 1.0 Recommendation.  For example, a validating
	     * parser would use this callback to report the violation of a
	     * validity constraint.  The default behaviour is to take no
	     * action.</p>
	     *
	     * <p>The SAX parser must continue to provide normal parsing
	     * events after invoking this method: it should still be possible
	     * for the application to process the document through to the end.
	     * If the application cannot do so, then the parser should report
	     * a fatal error even if the XML recommendation does not require
	     * it to do so.</p>
	     *
	     * <p>Filters may use this method to report other, non-XML errors
	     * as well.</p>
	     *
	     * @param exception The error information encapsulated in a
	     *                  SAX parse exception.
	     * @exception org.xml.sax.SAXException Any SAX exception, possibly
	     *            wrapping another exception.
	     * @see org.xml.sax.SAXParseException 
	     */
	    public void error (SAXParseException exception) throws SAXException {
	    	throw new SAXException("Error in CQL Validity: " + exception.getMessage(), exception);
	    }
	    
	    
	    /**
	     * Receive notification of a non-recoverable error.
	     *
	     * <p><strong>There is an apparent contradiction between the
	     * documentation for this method and the documentation for {@link
	     * org.xml.sax.ContentHandler#endDocument}.  Until this ambiguity
	     * is resolved in a future major release, clients should make no
	     * assumptions about whether endDocument() will or will not be
	     * invoked when the parser has reported a fatalError() or thrown
	     * an exception.</strong></p>
	     *
	     * <p>This corresponds to the definition of "fatal error" in
	     * section 1.2 of the W3C XML 1.0 Recommendation.  For example, a
	     * parser would use this callback to report the violation of a
	     * well-formedness constraint.</p>
	     *
	     * <p>The application must assume that the document is unusable
	     * after the parser has invoked this method, and should continue
	     * (if at all) only for the sake of collecting additional error
	     * messages: in fact, SAX parsers are free to stop reporting any
	     * other events once this method has been invoked.</p>
	     *
	     * @param exception The error information encapsulated in a
	     *                  SAX parse exception.  
	     * @exception org.xml.sax.SAXException Any SAX exception, possibly
	     *            wrapping another exception.
	     * @see org.xml.sax.SAXParseException
	     */
	    public void fatalError (SAXParseException exception) throws SAXException {
	    	throw new SAXException("Error in CQL Validity: " + exception.getMessage(), exception);
	    }
	}
}
