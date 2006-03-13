package gov.nih.nci.cagrid.cadsr.encoding;

import java.io.IOException;
import java.io.InputStream;

import org.apache.axis.utils.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;


public class EncodingUtils {

	protected static Log LOG = LogFactory.getLog(EncodingUtils.class.getName());


	public static Mapping getMapping() {

		EntityResolver resolver = new EntityResolver() {
			public InputSource resolveEntity(String publicId, String systemId) {
				if (publicId.equals("-//EXOLAB/Castor Object Mapping DTD Version 1.0//EN")) {
					InputStream in = ClassUtils.getResourceAsStream(EncodingUtils.class, "mapping.dtd");
					return new InputSource(in);
				}
				return null;
			}
		};
		
		//TODO: what to use here such that multiple services can use?
		String mappingLocation = "/xml-mapping.xml";
		InputStream mappingStream = ClassUtils.getResourceAsStream(EncodingUtils.class, mappingLocation);
		if (mappingStream == null) {
			LOG.error("Mapping file was null!");
		}
		InputSource mappIS = new org.xml.sax.InputSource(mappingStream);
		
		Mapping mapping = new Mapping();
		mapping.setEntityResolver(resolver);
		try {
			mapping.loadMapping(mappIS);
		} catch (IOException e) {
			LOG.error("Unable to load mapping file:" + mappingLocation, e);
		} catch (MappingException e) {
			LOG.error("Problem with mapping!", e);
		}

		return mapping;
	}
}
