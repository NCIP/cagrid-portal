package gov.nih.nci.cagrid.caarray;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.axis.message.MessageElement;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller2;
import org.globus.wsrf.utils.XmlUtils;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import gov.nih.nci.common.search.SearchResult;
import gov.nih.nci.common.search.session.SecureSession;
import gov.nih.nci.common.search.session.SecureSessionFactory;
import gov.nih.nci.mageom.domain.Experiment.Experiment;
import gov.nih.nci.mageom.search.SearchCriteriaFactory;
import gov.nih.nci.mageom.search.Experiment.ExperimentSearchCriteria;

public class TestClient {
	public static void main(String[] args) throws Exception {
		try{
		
		String usr = "PUBLIC";
		String pwd = "";
		SecureSession sess = SecureSessionFactory.defaultSecureSession();
		sess.start(usr, pwd);
		String sessId = sess.getSessionId();
		System.out.println("sessId=" + sessId);

		ExperimentSearchCriteria sc = SearchCriteriaFactory
				.new_EXPERIMENT_EXPERIMENT_SC();
		sc.setSessionId(sessId);
		sc
				.setIdentifier("gov.nih.nci.ncicb.caarray:Experiment:1015897558050098:1");
		SearchResult sr = sc.search();
		Experiment[] results = (Experiment[]) sr.getResultSet();
		System.out.println("results.length=" + results.length);
		
		FileWriter w = new FileWriter("experiment-1015897558050098-1.xml");
		String xml = toXML(results[0]);
		w.write(xml);
		w.flush();
		w.close();
		sess.end();
		}catch(Exception ex){
			throw new RuntimeException("Error: " + ex.getMessage(), ex);
		}
	}

	public static String toXML(Object beanObject) throws Exception {

		StringWriter w = new StringWriter();

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
		org.xml.sax.InputSource mappIS = new org.xml.sax.InputSource(Thread
				.currentThread().getContextClassLoader().getResourceAsStream(
						"caarray-xml-mapping.xml"));
		Mapping mapping = new Mapping();
		mapping.setEntityResolver(resolver);
		mapping.loadMapping(mappIS);

		Marshaller2 marshaller = null;
		Marshaller2.enableDebug = true;
		
		
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		marshaller = new Marshaller2(doc);
		marshaller.getXpaths().add("\\/\\w+\\/\\w+");
		marshaller.getXpaths().add("\\/\\w+\\/\\w+\\/\\w+\\/@.*");
		marshaller.getXpaths().add("\\/\\w+\\/@.*");
		marshaller.getXpaths().add("\\/\\w+\\/descriptions.*");

		marshaller.setMapping(mapping);
		marshaller.setMarshalAsDocument(false);
		marshaller.setValidation(false);
		marshaller.marshal(beanObject);
		
		MessageElement me = new MessageElement(doc.getDocumentElement());

		return XmlUtils.toString(me);
		
	}
}
