package gov.nih.nci.cagrid.caarray.encoding;

import javax.xml.namespace.QName;

import org.apache.axis.encoding.ser.BaseSerializerFactory;

public class CaArraySerializerFactory extends BaseSerializerFactory {

	public CaArraySerializerFactory(Class javaType, QName xmlType){
		super(CaArraySerializer.class, xmlType, javaType);
	}
	
}
