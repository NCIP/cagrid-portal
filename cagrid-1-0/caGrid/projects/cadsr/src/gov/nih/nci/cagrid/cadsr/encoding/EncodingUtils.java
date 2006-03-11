package gov.nih.nci.cagrid.cadsr.encoding;

import java.io.IOException;
import java.io.InputStream;

import org.apache.axis.utils.ClassUtils;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;


public class EncodingUtils {

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
		InputStream mappingStream = ClassUtils.getResourceAsStream(EncodingUtils.class, "/xml-mapping.xml");
		if (mappingStream == null) {
			System.err.println("Mapping file was null!");
		}
		InputSource mappIS = new org.xml.sax.InputSource(mappingStream);
		Mapping mapping = new Mapping();
		mapping.setEntityResolver(resolver);
		try {
			mapping.loadMapping(mappIS);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MappingException e) {
			e.printStackTrace();
		}

		return mapping;
	}
}
