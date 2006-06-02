package gov.nih.nci.cagrid.introduce.serializer;

import gov.nih.nci.cagrid.encoding.EncodingUtils;

import javax.xml.namespace.QName;

import org.apache.axis.encoding.DeserializationContext;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.DeserializerImpl;
import org.apache.axis.message.MessageElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Element;

public class ClientDeserializer extends DeserializerImpl implements
		Deserializer {
	public QName xmlType;

	public Class javaType;

	protected static Log LOG = LogFactory.getLog(ClientDeserializer.class
			.getName());

	public ClientDeserializer(Class javaType, QName xmlType) {
		this.xmlType = xmlType;
		this.javaType = javaType;
	}

	public void onEndElement(String namespace, String localName,
			DeserializationContext context) {
		long startTime = System.currentTimeMillis();

		MessageElement msgElem = context.getCurElement();
		Element asDOM = null;
		try {
			asDOM = msgElem.getAsDOM();
		} catch (Exception e) {
			LOG.error("Problem extracting message type! Result will be null!",
					e);
		}
		if (asDOM != null) {
			value = null;

		}
		long duration = System.currentTimeMillis() - startTime;
		LOG.debug("Total time to deserialize(" + localName + "):" + duration
				+ " ms.");
	}
}
