/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.xml;

import gov.nih.nci.cagrid.encoding.AxisContentHandler;
import gov.nih.nci.cagrid.encoding.EncodingUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;

import org.apache.axis.Constants;
import org.apache.axis.encoding.SerializationContext;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.wsdl.fromJava.Types;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;

public class CastorSerializer implements Serializer {

	protected static Log LOG = LogFactory.getLog(CastorSerializer.class
			.getName());
	
	private ChildDocumentHandler childDocumentHandler;

	public void serialize(QName name, Attributes attributes, Object value,
			SerializationContext context) throws IOException {
		
		if(childDocumentHandler == null){
			//Set up with ClassToQname using option from AxisEngine
		}

		AxisContentHandler hand = new AxisContentHandler(context);
		Marshaller marshaller = new Marshaller(hand);

		try {
			Mapping mapping = EncodingUtils.getMapping(context
					.getMessageContext());
			marshaller.setMapping(mapping);
			marshaller.setValidation(true);
		} catch (MappingException e) {
			LOG
					.error(
							"Problem establishing castor mapping!  Using default mapping.",
							e);
		}
		try {
			marshaller.marshal(value);
		} catch (MarshalException e) {
			LOG.error("Problem using castor marshalling.", e);
			throw new IOException("Problem using castor marshalling."
					+ e.getMessage());
		} catch (ValidationException e) {
			LOG
					.error(
							"Problem validating castor marshalling; message doesn't comply with the associated XML schema.",
							e);
			throw new IOException(
					"Problem validating castor marshalling; message doesn't comply with the associated XML schema."
							+ e.getMessage());
		}

		String[] paths = (String[]) context.getMessageContext().getProperty(
				"cagrid.query.paths");
		if (paths != null) {

		}

	}

	public Document marshal(DocumentBuilder builder, Mapping mapping,
			Object object, ObjectPath path) {
		Document doc = builder.newDocument();
		Marshaller m = new Marshaller(doc);
		try {
			m.setMapping(mapping);
			m.marshal(object);
		} catch (Exception ex) {
			String msg = "Error marshalling: " + ex.getMessage();
			LOG.error(msg, ex);
			throw new RuntimeException(msg, ex);
		}
		for(ObjectPath childPath : path.getChildPaths()){
			
			//Invoke getter
			//Determine type of object or collection.
			String className = null;
			
			Collection<Document> childDocuments = new ArrayList<Document>();
			//If it is a collection, recurse on each, putting result in childDocuments
			
			
			childDocumentHandler.insert(className, childPath, doc, childDocuments);
		}
		return doc;
	}

	public String getMechanismType() {
		return Constants.AXIS_SAX;
	}

	/**
	 * Return XML schema for the specified type, suitable for insertion into the
	 * &lt;types&gt; element of a WSDL document, or underneath an
	 * &lt;element&gt; or &lt;attribute&gt; declaration.
	 * 
	 * @param javaType
	 *            the Java Class we're writing out schema for
	 * @param types
	 *            the Java2WSDL Types object which holds the context for the
	 *            WSDL being generated.
	 * @return a type element containing a schema simpleType/complexType
	 * @see org.apache.axis.wsdl.fromJava.Types
	 */
	public Element writeSchema(Class javaType, Types types) throws Exception {
		return null;
	}
}
