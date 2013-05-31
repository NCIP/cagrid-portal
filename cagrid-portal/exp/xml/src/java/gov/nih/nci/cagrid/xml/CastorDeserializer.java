/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.xml;

import gov.nih.nci.cagrid.encoding.EncodingUtils;

import javax.xml.namespace.QName;

import org.apache.axis.encoding.DeserializationContext;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.DeserializerImpl;
import org.apache.axis.message.MessageElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Element;


public class CastorDeserializer extends DeserializerImpl implements Deserializer {
	public QName xmlType;
	public Class javaType;

	protected static Log LOG = LogFactory.getLog(CastorDeserializer.class.getName());


	public CastorDeserializer(Class javaType, QName xmlType) {
		this.xmlType = xmlType;
		this.javaType = javaType;
	}


	public void onEndElement(String namespace, String localName, DeserializationContext context) {
		long startTime=System.currentTimeMillis();
		Unmarshaller unmarshall = new Unmarshaller(javaType);

		try {
			Mapping mapping = EncodingUtils.getMapping(context.getMessageContext());
			if (mapping != null) {
				unmarshall.setMapping(mapping);
			} else {
				LOG.error("Castor mapping was null!  Using default mapping.");
			}
		} catch (MappingException e) {
			LOG.error("Problem establishing castor mapping!  Using default mapping.", e);
		}

		MessageElement msgElem = context.getCurElement();
		Element asDOM = null;
		try {
			asDOM = msgElem.getAsDOM();
		} catch (Exception e) {
			LOG.error("Problem extracting message type! Result will be null!", e);
		}
		if (asDOM != null) {
			try {
				value = unmarshall.unmarshal(asDOM);
			} catch (MarshalException e) {
				LOG.error("Problem with castor marshalling!", e);
			} catch (ValidationException e) {
				LOG.error("XML does not match schema!", e);
			}
		}
		long duration=System.currentTimeMillis()- startTime;
		LOG.debug("Total time to deserialize("+localName+"):"+duration+" ms.");
	}
}
