package gov.nih.nci.cagrid.caarray.encoding;

import javax.xml.namespace.QName;

import org.apache.axis.MessageContext;
import org.apache.axis.encoding.DeserializationContext;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.message.EnvelopeHandler;
import org.apache.axis.message.SOAPHandler;
import org.globus.wsrf.encoding.DeserializationException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class CaArrayDeserializationContext extends DeserializationContext {
	
	private Class objectClass = null; 
	private Object value = null;
	private QName qName = null;

	public CaArrayDeserializationContext(
		MessageContext context, InputSource source, Class clazz, QName qName) 
		throws DeserializationException {
		super(context, new SOAPHandler());
		this.inputSource = source;
		this.objectClass = clazz;
		this.qName = qName;
	}
	
	
	public Object getValue() throws SAXException {
		if (value == null) {
			popElementHandler();
			Deserializer deserializer = getDeserializer(this.objectClass, this.qName);
			if (deserializer == null) {
				throw new NullPointerException("No deserializer found for class " + objectClass.getName());
			}else{
				
			}
			pushElementHandler(new EnvelopeHandler((SOAPHandler) deserializer));
			parse();
			value = deserializer.getValue();
		}
		return value;
	}
}
