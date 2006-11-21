package gov.nih.nci.cagrid.caarray;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import gov.nih.nci.mageom.domain.Experiment.Experiment;
import gov.nih.nci.mageom.domain.Experiment.impl.ExperimentImpl;

public class TestFromXML {
	
	public static void main(String[] args) throws Exception {
		
		String xmlFile = "etc/sample.xml";
		String mappingFile = "src/gov/nih/nci/cagrid/caarray/common/caarray-xml-mapping.xml";
		Element el = parseXML(xmlFile);
		Mapping mapping = getMapping(mappingFile);
		Unmarshaller u = new Unmarshaller(ExperimentImpl.class);
		u.setMapping(mapping);
		Experiment exp = (Experiment)u.unmarshal(el);
		
		System.out.println(exp.getIdentifier());
		
	}
	
	private static Element parseXML(String xmlFile) throws Exception {
		Element el = null;
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		el = builder.parse(new File(xmlFile)).getDocumentElement();
		return el;
	}

	public static Mapping getMapping(String fileName) throws Exception {
		EntityResolver resolver = new EntityResolver() {
			public InputSource resolveEntity(String publicId, String systemId) {
				if (publicId
						.equals("-//EXOLAB/Castor Object Mapping DTD Version 1.0//EN")) {
					InputStream in = Thread.currentThread()
							.getContextClassLoader().getResourceAsStream(
									"mapping.dtd");
					return new InputSource(in);
				}
				return null;
			}
		};
		org.xml.sax.InputSource mappIS = new org.xml.sax.InputSource(new FileInputStream(fileName));
		Mapping mapping = new Mapping();
		mapping.setEntityResolver(resolver);
		mapping.loadMapping(mappIS);
		return mapping;
	}

}
