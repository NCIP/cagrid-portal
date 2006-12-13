package gov.nih.nci.cagrid.caarray.encoding;

import java.io.StringReader;
import java.util.Iterator;

import gov.nih.nci.cagrid.encoding.EncodingUtils;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;

import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.encoding.DeserializationContext;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.DeserializerImpl;
import org.apache.axis.message.MessageElement;
import org.apache.axis.message.PrefixedQName;
import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/**
 * This is ripped from SDKDeserializer. I just need to ensure that
 * Castor preserves whitespace.
 * 
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class CaArrayDeserializer extends DeserializerImpl implements
		Deserializer {
	public QName xmlType;

	public Class javaType;

	protected static Log LOG = LogFactory.getLog(CaArrayDeserializer.class
			.getName());

	public CaArrayDeserializer(Class javaType, QName xmlType) {
		this.xmlType = xmlType;
		this.javaType = javaType;
	}

	public void onEndElement(String namespace, String localName,
			DeserializationContext context) {
		
		LOG.debug("deserializing...");
		
		long startTime = System.currentTimeMillis();
		Unmarshaller unmarshall = new Unmarshaller(javaType);
		
		try {
			Mapping mapping = EncodingUtils.getMapping(context
					.getMessageContext());
			if (mapping != null) {
				unmarshall.setMapping(mapping);
			} else {
				LOG.error("Castor mapping was null!  Using default mapping.");
			}
		} catch (MappingException e) {
			LOG
					.error(
							"Problem establishing castor mapping!  Using default mapping.",
							e);
		}
		
		
		MessageElement msgElem = context.getCurElement();
		
//		String str = CaArrayDOM2Writer.nodeToString(msgElem, true);
//		LOG.debug("msgElem:\n" + str);
		
		Element asDOM = null;
		try {
			asDOM = msgElem.getAsDOM();
			
		} catch (Exception e) {
			LOG.error("Problem extracting message type! Result will be null!",
					e);
		}
		if (asDOM != null) {
			try {
				unmarshall.setWhitespacePreserve(true);
				value = unmarshall.unmarshal(asDOM);
			} catch (Exception e) {
				LOG.error("Problem with castor marshalling!", e);
			}
		}
		long duration = System.currentTimeMillis() - startTime;
		LOG.debug("Total time to deserialize(" + localName + "):" + duration
				+ " ms.");
		LOG.debug("deserializing...");
	}
}
