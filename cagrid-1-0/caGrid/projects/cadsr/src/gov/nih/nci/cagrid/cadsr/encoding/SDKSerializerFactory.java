package gov.nih.nci.cagrid.cadsr.encoding;

import javax.xml.namespace.QName;

import org.apache.axis.encoding.ser.BaseSerializerFactory;

public class SDKSerializerFactory extends BaseSerializerFactory {
	public SDKSerializerFactory(Class javaType, QName xmlType) {
		super(SDKSerializer.class, xmlType, javaType);
		System.out.println("Initializing SDKSerializerFactory");
	}
}
