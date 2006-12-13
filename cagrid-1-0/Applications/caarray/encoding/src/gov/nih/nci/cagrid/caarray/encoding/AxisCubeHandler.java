package gov.nih.nci.cagrid.caarray.encoding;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.namespace.QName;

import gov.nih.nci.mageom.domain.BioAssayData.impl.BioDataCubeImpl;

import org.apache.axis.AxisEngine;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.MessageContext;
import org.apache.axis.client.AxisClient;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.encoding.SerializationContext;
import org.apache.axis.message.MessageElement;
import org.apache.axis.server.AxisServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.ValidityException;
import org.globus.wsrf.encoding.ObjectSerializer;
import org.xml.sax.InputSource;

public class AxisCubeHandler implements FieldHandler {
	
	private static Log LOG = LogFactory.getLog(AxisCubeHandler.class);
	
	public static final String CONFIG_PROP = "gov.nih.nci.cagrid.caarray.encoding.config";
	public static final String NS_PROP = "gov.nih.nci.cagrid.caarray.encoding.cube.namespace";
	public static final String LN_PROP = "gov.nih.nci.cagrid.caarray.encoding.cube.localname";
	
	public static final String DEFAULT_CONFIG = "gov/nih/nci/cagrid/caarray/client/client-config.wsdd";
	public static final String DEFAULT_NS = "gme://caArray.caBIG/1.1/gov.nih.nci.mageom.domain.BioAssayData";
	public static final String DEFAULT_LN = "cube";

	public void checkValidity(Object arg0) throws ValidityException,
			IllegalStateException {
		// TODO Auto-generated method stub

	}

	public Object getValue(Object obj) throws IllegalStateException {
		Object value = null;
		if (obj != null) {
			if (obj instanceof BioDataCubeImpl) {

				BioDataCubeImpl bdc = (BioDataCubeImpl) obj;
				Object[][][] cube = bdc.getCube();
				if (cube != null) {

					try {
						
						String config = System.getProperty(CONFIG_PROP, DEFAULT_CONFIG);
						String ns = System.getProperty(NS_PROP, DEFAULT_NS);
						String ln = System.getProperty(LN_PROP, DEFAULT_LN);
						
						QName qName = new QName(ns, ln);
						StringWriter sw = new StringWriter();
						InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(config);
						
						LOG.debug("Beginning encoding...");						
						serializeObject(cube, qName, sw, in);
						LOG.debug("...done encoding.");
						
						value = sw.getBuffer().toString();

					} catch (Exception ex) {
						LOG.error("Error enconding cube: " + ex.getMessage(), ex); 
						throw new RuntimeException("Error encoding cube: "
								+ ex.getMessage(), ex);
					}
				}

			} else {
				throw new IllegalStateException("Object not instanceof: "
						+ BioDataCubeImpl.class.getName()
						+ obj.getClass().getName());
			}
		}

		return value;

	}

	public Object newInstance(Object arg0) throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	public void resetValue(Object arg0) throws IllegalStateException,
			IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	public void setValue(Object obj, Object value)
			throws IllegalStateException, IllegalArgumentException {
		// TODO Auto-generated method stub
		if (obj != null) {

			BioDataCubeImpl bdc = null;
			if (!(obj instanceof BioDataCubeImpl)) {
				throw new IllegalArgumentException("Object not instance of "
						+ BioDataCubeImpl.class.getName() + ": "
						+ obj.getClass().getName());
			}
			bdc = (BioDataCubeImpl) obj;

			if (value == null) {
				bdc.setCube(null);
			} else {

				if (!(value instanceof String)) {
					throw new IllegalArgumentException(
							"Value not instanceof String: "
									+ value.getClass().getName());
				}
				Object[][][] cube = null;
				try {
					
					String config = System.getProperty(CONFIG_PROP, DEFAULT_CONFIG);
					String ns = System.getProperty(NS_PROP, DEFAULT_NS);
					String ln = System.getProperty(LN_PROP, DEFAULT_LN);
					
					InputSource source = new InputSource(new StringReader((String)value));
					QName qName = new QName(ns, ln);
					StringWriter sw = new StringWriter();
					InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(config);
					
					cube = (Object[][][]) deserializeObject(source, Object[][][].class, qName, in);
					bdc.setCube(cube);
					
				} catch (Exception ex) {
					throw new RuntimeException("Error decoding cube: "
							+ ex.getMessage(), ex);
				}

			}

		}
	}
	
	public static void serializeObject(Object obj, QName qname, Writer writer,
			InputStream wsdd) throws Exception {
		MessageElement element = (MessageElement) ObjectSerializer
				.toSOAPElement(obj, qname);
		EngineConfiguration engineConfig = new FileProvider(wsdd);
		AxisEngine axisClient = new AxisServer(engineConfig);
		MessageContext messageContext = new MessageContext(axisClient);
		messageContext.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.TRUE);
		SerializationContext serializationContext = new SerializationContext(
				writer, messageContext);
		serializationContext.setPretty(true);
		element.output(serializationContext);
		writer.write("\n");
		writer.flush();
	}

	public static Object deserializeObject(InputSource source, Class klass,
			QName qName, InputStream wsdd) throws Exception {

		EngineConfiguration engineConfig = new FileProvider(wsdd);
		AxisClient axisClient = new AxisClient(engineConfig);
		MessageContext messageContext = new MessageContext(axisClient);
		messageContext.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.TRUE);
		Object obj = null;

		CaArrayDeserializationContext ctx = new CaArrayDeserializationContext(
				messageContext, source, klass, qName);

		obj = ctx.getValue();

		return obj;
	}

}
