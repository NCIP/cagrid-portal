package gov.nih.nci.cagrid.introduce.serializer;

import javax.xml.namespace.QName;

import org.apache.axis.encoding.ser.BaseSerializerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ClientSerializerFactory extends BaseSerializerFactory {

	protected static Log LOG = LogFactory.getLog(ClientSerializerFactory.class.getName());


	public ClientSerializerFactory(Class javaType, QName xmlType) {
		super(ClientSerializer.class, xmlType, javaType);
		LOG.debug("Initializing ClientSerializerFactory for class:" + javaType + " and QName:" + xmlType);
	}
}
