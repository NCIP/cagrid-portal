package gov.nih.nci.cagrid.cadsr.encoding;

import javax.xml.namespace.QName;

import org.apache.axis.encoding.DeserializationContext;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.DeserializerImpl;
import org.apache.axis.message.MessageElement;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;


public class SDKDeserializer extends DeserializerImpl implements Deserializer {
	public QName xmlType;
	public Class javaType;


	public SDKDeserializer(Class javaType, QName xmlType) {
		this.xmlType = xmlType;
		this.javaType = javaType;
	}


	public void onEndElement(String namespace, String localName, DeserializationContext context) {
		Unmarshaller unmarshall = new Unmarshaller(javaType);
		try {
			unmarshall.setMapping(EncodingUtils.getMapping());
		} catch (MappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		MessageElement msgElem = context.getCurElement();
		if (msgElem != null) {
			try {
				value = unmarshall.unmarshal(msgElem.getAsDOM());
			} catch (MarshalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ValidationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
