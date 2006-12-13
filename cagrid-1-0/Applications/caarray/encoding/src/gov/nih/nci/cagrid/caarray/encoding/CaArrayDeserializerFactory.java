package gov.nih.nci.cagrid.caarray.encoding;

import javax.xml.namespace.QName;

import org.apache.axis.encoding.ser.BaseDeserializerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class CaArrayDeserializerFactory extends BaseDeserializerFactory {

	protected static Log LOG = LogFactory.getLog(CaArrayDeserializerFactory.class.getName());


	public CaArrayDeserializerFactory(Class javaType, QName xmlType) {
		super(CaArrayDeserializer.class, xmlType, javaType);
		LOG.debug("Initializing CaArrayDeserializerFactory for class:" + javaType + " and QName:" + xmlType);
	}
}
