package gov.nih.nci.cagrid.xml;

import javax.xml.namespace.QName;

import org.apache.axis.encoding.ser.BaseDeserializerFactory;

public class CastorDeserializerFactory extends BaseDeserializerFactory {

	public CastorDeserializerFactory(Class javaType, QName xmlType) {
		super(CastorDeserializer.class, xmlType, javaType);
	}
}
