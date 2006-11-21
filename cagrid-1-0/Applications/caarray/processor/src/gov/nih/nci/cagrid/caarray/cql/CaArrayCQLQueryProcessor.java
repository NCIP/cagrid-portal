package gov.nih.nci.cagrid.caarray.cql;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.QueryModifier;
import gov.nih.nci.cagrid.cqlresultset.CQLObjectResult;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.mapping.Mappings;
import gov.nih.nci.cagrid.data.service.ServiceConfigUtil;
import gov.nih.nci.cagrid.data.utilities.CQLResultsCreationUtil;
import gov.nih.nci.cagrid.data.utilities.ResultsCreationException;
import gov.nih.nci.common.search.SearchException;
import gov.nih.nci.common.search.SearchResult;
import gov.nih.nci.common.search.session.SecureSession;
import gov.nih.nci.common.search.session.SecureSessionFactory;
import gov.nih.nci.mageom.domain.Experiment.Experiment;
import gov.nih.nci.mageom.search.SearchCriteriaFactory;
import gov.nih.nci.mageom.search.Experiment.ExperimentSearchCriteria;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.message.MessageElement;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.SDKMarshaller;
import org.globus.wsrf.config.ContainerConfig;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.globus.wsrf.utils.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class CaArrayCQLQueryProcessor extends CQLQueryProcessor {

	private static final String CASTOR_MAPPING_FILE = "castorMappingFile";

	private Mapping mapping;

	private DocumentBuilder builder;

	public CaArrayCQLQueryProcessor() {
		try {
			this.builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
			throw new RuntimeException("Error creating document builder: "
					+ ex.getMessage(), ex);
		}
	}

	public CQLQueryResults processQuery(CQLQuery cqlQuery)
			throws MalformedQueryException, QueryProcessingException {

		System.out.println("Processing query...");
		
		List coreResultsList = queryCaArrayService(cqlQuery);
		String targetName = cqlQuery.getTarget().getName();
		Mappings mappings = null;
		try {
			mappings = getClassToQnameMappings();
		} catch (Exception ex) {
			throw new QueryProcessingException(
					"Error getting class to qname mappings: " + ex.getMessage(),
					ex);
		}
		CQLQueryResults results = null;
		// decide on type of results
		boolean objectResults = cqlQuery.getQueryModifier() == null
				|| (!cqlQuery.getQueryModifier().isCountOnly()
						&& cqlQuery.getQueryModifier().getAttributeNames() == null && cqlQuery
						.getQueryModifier().getDistinctAttribute() == null);
		if (objectResults) {
			try {
				results = createObjectResults(coreResultsList, targetName,
						mappings);
			} catch (ResultsCreationException ex) {
				throw new QueryProcessingException(ex.getMessage(), ex);
			}
		} else {
			QueryModifier mod = cqlQuery.getQueryModifier();
			if (mod.isCountOnly()) {
				// parse the value as a string to long. This covers returning
				// integers, shorts, and longs
				Long val = Long.valueOf(coreResultsList.get(0).toString());
				results = CQLResultsCreationUtil.createCountResults(val
						.longValue(), targetName);
			} else {
				// attributes distinct or otherwise
				String[] names = null;
				if (mod.getDistinctAttribute() != null) {
					names = new String[] { mod.getDistinctAttribute() };
				} else {
					names = mod.getAttributeNames();
				}
				results = CQLResultsCreationUtil.createAttributeResults(
						coreResultsList, targetName, names);
			}
		}
		
		System.out.println("...done.");
		
		return results;
	}

	private CQLQueryResults createObjectResults(List objects,
			String targetName, Mappings classToQname)
			throws ResultsCreationException {
		CQLQueryResults results = new CQLQueryResults();
		results.setTargetClassname(targetName);
		QName targetQName = getQname(targetName, classToQname);
		CQLObjectResult[] objectResults = new CQLObjectResult[objects.size()];
		int idx = 0;
		for (Iterator i = objects.iterator(); i.hasNext(); idx++) {
			Object obj = i.next();
//			Element el = serializeObject(obj);
			// String nsURI = el.getNamespaceURI();
			// System.out.println("nsURI: " + nsURI);
			// Object obj2 = ObjectDeserializer.toObject(el, Experiment.class);
//			MessageElement elem = new MessageElement(el);
			// MessageElement elem = new MessageElement(nsURI, obj2);
			// System.out.println(XmlUtils.toString(elem));

//			String fileName = "out" + idx + ".xml";
//			try {
//				FileWriter w = new FileWriter(fileName);
//				w.write(XmlUtils.toString(elem));
//				w.flush();
//				w.close();
//			} catch (Exception ex) {
//				throw new RuntimeException("Error writing " + fileName + ": "
//						+ ex.getMessage(), ex);
//			}
//
//			objectResults[idx] = new CQLObjectResult(
//					new MessageElement[] { elem });
			
			
			MessageElement elem = new MessageElement(targetQName, obj);
			objectResults[idx] = new CQLObjectResult(new MessageElement[]{elem});
			
		}
		results.setObjectResult(objectResults);
		return results;

	}

	private Element serializeObject(Object obj) {

		String sampleFile = ContainerConfig.getBaseDirectory() + File.separator
				+ "etc/cagrid_CaArraySvc/sample.xml";
		Document doc = null;
		try {
			doc = this.builder.parse(new File(sampleFile));
		} catch (Exception ex) {
			throw new RuntimeException("Error serializing object: "
					+ ex.getMessage(), ex);
		}

		// Document doc = this.builder.newDocument();
		// SDKMarshaller marshaller = getMarshaller(doc);
		//
		// try {
		// marshaller.marshal(obj);
		// } catch (Exception ex) {
		// throw new RuntimeException("Error error marshalling object: "
		// + ex.getMessage(), ex);
		// }
		//
		return doc.getDocumentElement();
	}

	private SDKMarshaller getMarshaller(Document doc) {

		if (this.mapping == null) {
			String castorMappingFile = ContainerConfig.getBaseDirectory()
					+ File.separator
					+ getConfiguredParameters()
							.getProperty(CASTOR_MAPPING_FILE);
			try {
				EntityResolver resolver = new EntityResolver() {
					public InputSource resolveEntity(String publicId,
							String systemId) {
						if (publicId
								.equals("-//EXOLAB/Castor Object Mapping DTD Version 1.0//EN")) {
							InputStream in = Thread.currentThread()
									.getContextClassLoader()
									.getResourceAsStream("mapping.dtd");
							return new InputSource(in);
						}
						return null;
					}
				};
				InputSource mappIS = new InputSource(new FileInputStream(
						castorMappingFile));
				this.mapping = new Mapping();
				this.mapping.setEntityResolver(resolver);
				this.mapping.loadMapping(mappIS);

			} catch (Exception ex) {
				throw new RuntimeException("Error creating mapping from file '"
						+ castorMappingFile + "': " + ex.getMessage(), ex);
			}
		}

		SDKMarshaller marshaller = null;
		try {
			marshaller = new SDKMarshaller(doc);
			marshaller.getXpaths().add("\\/\\w+\\/\\w+");
			marshaller.getXpaths().add("\\/\\w+\\/\\w+\\/\\w+\\/@.*");
			marshaller.getXpaths().add("\\/\\w+\\/@.*");
			marshaller.getXpaths().add("\\/\\w+\\/descriptions.*");
			marshaller.setMapping(this.mapping);
			marshaller.setMarshalAsDocument(false);
			marshaller.setValidation(false);
		} catch (Exception ex) {
			throw new RuntimeException("Error creating marshaller: "
					+ ex.getMessage(), ex);
		}
		return marshaller;
	}

	private static QName getQname(String className, Mappings classMappings) {
		for (int i = 0; classMappings.getMapping() != null
				&& i < classMappings.getMapping().length; i++) {
			if (classMappings.getMapping(i).getClassName().equals(className)) {
				return QName.valueOf(classMappings.getMapping(i).getQname());
			}
		}
		return null;
	}

	private List queryCaArrayService(CQLQuery cqlQuery) {
		List resultsList = new ArrayList();
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
		SearchResult sr;
		try {
			sr = sc.search();
		} catch (SearchException ex) {
			throw new RuntimeException("Error searching: " + ex.getMessage(),
					ex);
		}

		Experiment[] results = (Experiment[]) sr.getResultSet();
		System.out.println("results.length=" + results.length);
		for (int i = 0; i < results.length; i++) {
			resultsList.add(results[i]);
		}
		return resultsList;
	}

	private Mappings getClassToQnameMappings() throws Exception {
		// get the mapping file name
		String filename = ServiceConfigUtil.getClassToQnameMappingsFile();
		Mappings mappings = (Mappings) Utils.deserializeDocument(filename,
				Mappings.class);
		return mappings;
	}

	public Properties getRequiredParameters() {
		Properties props = new Properties();
		props.setProperty(CASTOR_MAPPING_FILE, "");
		return props;
	}
}
