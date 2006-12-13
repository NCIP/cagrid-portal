package gov.nih.nci.cagrid.caarray.encoding;

import gov.nih.nci.cagrid.encoding.AxisContentHandler;
import gov.nih.nci.cagrid.encoding.EncodingUtils;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.apache.axis.Constants;
import org.apache.axis.encoding.SerializationContext;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.wsdl.fromJava.Types;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller2;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;

public class CaArraySerializer implements Serializer {


	private static final String DEFAULT_MARSHALLER_XPATH_REGEX = "\\/\\w+\\/@.*";
	private static final String MARSHALLER_XPATH_REGEX = "marshallerXpathRegex";
	protected static Log LOG = LogFactory.getLog(CaArraySerializer.class.getName());


	public void serialize(QName name, Attributes attributes, Object value, SerializationContext context)
		throws IOException {
		
		
		AxisContentHandler hand = new AxisContentHandler(context);
		Marshaller2 marshaller = null;
		try{
			marshaller = new Marshaller2(hand);
			String regex = (String) context.getMessageContext().getProperty(MARSHALLER_XPATH_REGEX);
			if(regex == null){
				regex = DEFAULT_MARSHALLER_XPATH_REGEX;
				
			}
			marshaller.getXpaths().add(regex);
		}catch(Exception ex){
			String msg = "Error creating marshaller: " + ex.getMessage();
			LOG.error(msg, ex);
			throw new IOException(msg);
		}
		
		try {
			Mapping mapping = EncodingUtils.getMapping(context.getMessageContext());
			marshaller.setMapping(mapping);
			marshaller.setMarshalAsDocument(true);
			marshaller.setValidation(false);
		} catch (MappingException ex) {
			String msg = "Problem establishing castor mapping:" + ex.getMessage();
			LOG.error(msg, ex);
			throw new IOException(msg);
		}
		try {
			marshaller.marshal(value);
		}catch(Exception ex){
			String msg = "Error marshalling: " + ex.getMessage(); 
			LOG.error(msg, ex);
			ex.printStackTrace();
			throw new IOException(msg);
		}

	}


	public String getMechanismType() {
		return Constants.AXIS_SAX;
	}

	public Element writeSchema(Class arg0, Types arg1) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
