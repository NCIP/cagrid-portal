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
import org.exolab.castor.xml.SDKMarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;

public class CaArraySerializer implements Serializer {


	protected static Log LOG = LogFactory.getLog(CaArraySerializer.class.getName());


	public void serialize(QName name, Attributes attributes, Object value, SerializationContext context)
		throws IOException {
		long startTime = System.currentTimeMillis();

		AxisContentHandler hand = new AxisContentHandler(context);
		SDKMarshaller marshaller = new SDKMarshaller(hand);
		marshaller.getXpaths().add("\\/\\w+\\/@.*");
		try {
			Mapping mapping = EncodingUtils.getMapping(context.getMessageContext());
			marshaller.setMapping(mapping);
			marshaller.setValidation(true);
		} catch (MappingException e) {
			LOG.error("Problem establishing castor mapping!  Using default mapping.", e);
		}
		try {
			marshaller.marshal(value);
		} catch (MarshalException e) {
			LOG.error("Problem using castor marshalling.", e);
			throw new IOException("Problem using castor marshalling." + e.getMessage());
		} catch (ValidationException e) {
			LOG.error("Problem validating castor marshalling; message doesn't comply with the associated XML schema.",
				e);
			throw new IOException(
				"Problem validating castor marshalling; message doesn't comply with the associated XML schema."
					+ e.getMessage());
		}
		long duration = System.currentTimeMillis() - startTime;
		LOG.debug("Total time to serialize(" + name.getLocalPart() + "):" + duration + " ms.");
	}


	public String getMechanismType() {
		return Constants.AXIS_SAX;
	}

	public Element writeSchema(Class arg0, Types arg1) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
