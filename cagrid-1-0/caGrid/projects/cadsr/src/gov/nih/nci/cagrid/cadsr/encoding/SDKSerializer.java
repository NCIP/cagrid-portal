package gov.nih.nci.cagrid.cadsr.encoding;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.apache.axis.Constants;
import org.apache.axis.encoding.SerializationContext;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.wsdl.fromJava.Types;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;


public class SDKSerializer implements Serializer {
	public void serialize(QName name, Attributes attributes, Object value, SerializationContext context)
		throws IOException {

		AxisContentHandler hand = new AxisContentHandler(context);
		Marshaller marshaller = new Marshaller(hand);
		try {
			marshaller.setMapping(EncodingUtils.getMapping());
		} catch (MappingException e) {
			throw new IOException("Problem establishing castor mapping!" + e.getMessage());
		}
		try {
			marshaller.marshal(value);
		} catch (MarshalException e) {
			throw new IOException("Problem using castor marshalling." + e.getMessage());
		} catch (ValidationException e) {
			throw new IOException("Problem validating castor marshalling." + e.getMessage());
		}
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
