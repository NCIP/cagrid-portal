package gov.nih.nci.cagrid.introduce.serializer;

import javax.xml.namespace.QName;

import org.apache.axis.encoding.ser.BaseDeserializerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ClientDeserializerFactory extends BaseDeserializerFactory {

	protected static Log LOG = LogFactory.getLog(ClientDeserializerFactory.class.getName());


	public ClientDeserializerFactory(Class javaType, QName xmlType) {
		super(ClientDeserializer.class, xmlType, javaType);
		LOG.debug("Initializing ClientDeserializerFactory for class:" + javaType + " and QName:" + xmlType);
	}
}
