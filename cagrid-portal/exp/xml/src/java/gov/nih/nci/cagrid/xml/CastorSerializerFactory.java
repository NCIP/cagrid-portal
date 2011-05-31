package gov.nih.nci.cagrid.xml;

import javax.xml.namespace.QName;

import org.apache.axis.encoding.ser.BaseSerializerFactory;

public class CastorSerializerFactory extends BaseSerializerFactory {

	public CastorSerializerFactory(Class javaType, QName xmlType) {
		super(CastorSerializer.class, xmlType, javaType);
	}
}
