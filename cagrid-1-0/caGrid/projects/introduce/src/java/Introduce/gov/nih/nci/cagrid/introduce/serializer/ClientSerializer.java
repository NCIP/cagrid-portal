package gov.nih.nci.cagrid.introduce.serializer;

import gov.nih.nci.cagrid.encoding.AxisContentHandler;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.apache.axis.Constants;
import org.apache.axis.encoding.SerializationContext;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.wsdl.fromJava.Types;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;


public class ClientSerializer implements Serializer {

	protected static Log LOG = LogFactory.getLog(ClientSerializer.class.getName());


	public void serialize(QName name, Attributes attributes, Object value, SerializationContext context)
		throws IOException {
		long startTime = System.currentTimeMillis();

		AxisContentHandler hand = new AxisContentHandler(context);
		
		long duration = System.currentTimeMillis() - startTime;
		LOG.debug("Total time to serialize(" + name.getLocalPart() + "):" + duration + " ms.");
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
