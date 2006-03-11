package gov.nih.nci.cagrid.cadsr.encoding;

import javax.xml.namespace.QName;

import org.apache.axis.encoding.ser.BaseDeserializerFactory;


public class SDKDeserializerFactory extends BaseDeserializerFactory {
	public SDKDeserializerFactory(Class javaType, QName xmlType) {
		super(SDKDeserializer.class, xmlType, javaType);
		System.out.println("Initializing SDKDeserializerFactory");
	}
}
