package gov.nih.nci.cagrid.cadsr.encoding;

import org.apache.axis.encoding.SerializationContext;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.namespace.QName;
import java.io.IOException;


/**
 * This ContentHandler delegates all serialization to an axis
 * SerializationContext
 * 
 * @author <a href="mailto:fabien.nisol@advalvas.be">Fabien Nisol</a>
 */
public class AxisContentHandler extends DefaultHandler {
	/**
	 * serialization context to delegate to
	 */
	private SerializationContext context;


	/**
	 * Creates a contentHandler delegate
	 * 
	 * @param context :
	 *            axis context to delegate to
	 */
	public AxisContentHandler(SerializationContext context) {
		super();
		setContext(context);
	}


	/**
	 * Getter for property context.
	 * 
	 * @return Value of property context.
	 */
	public SerializationContext getContext() {
		return context;
	}


	/**
	 * Setter for property context.
	 * 
	 * @param context
	 *            New value of property context.
	 */
	public void setContext(SerializationContext context) {
		this.context = context;
	}


	/**
	 * delegates to the serialization context
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		try {
			context.startElement(new QName(uri, localName), attributes);
		} catch (IOException ioe) {
			throw new SAXException(ioe);
		}
	}


	/**
	 * delegates to the serialization context
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		try {
			context.endElement();
		} catch (IOException ioe) {
			throw new SAXException(ioe);
		}
	}


	/**
	 * delegates to the serialization context
	 */
	public void characters(char[] ch, int start, int length) throws org.xml.sax.SAXException {
		try {
			context.writeChars(ch, start, length);
		} catch (IOException ioe) {
			throw new SAXException(ioe);
		}
	}
}