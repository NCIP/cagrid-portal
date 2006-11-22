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
import gov.nih.nci.common.search.SearchCriteria;
import gov.nih.nci.common.search.SearchResult;
import gov.nih.nci.common.search.session.SecureSession;
import gov.nih.nci.common.search.session.SecureSessionFactory;
import gov.nih.nci.mageom.domain.Experiment.Experiment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.namespace.QName;

import org.apache.axis.message.MessageElement;

public class CaArrayCQLQueryProcessor extends CQLQueryProcessor {
	
	public static final String CASE_INSENSITIVE_QUERYING = "queryCaseInsensitive";
	
	public CaArrayCQLQueryProcessor() {

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

	private static QName getQname(String className, Mappings classMappings) {
		for (int i = 0; classMappings.getMapping() != null
				&& i < classMappings.getMapping().length; i++) {
			if (classMappings.getMapping(i).getClassName().equals(className)) {
				return QName.valueOf(classMappings.getMapping(i).getQname());
			}
		}
		return null;
	}
	
	private boolean useCaseInsensitiveQueries() {
		return Boolean.valueOf(getConfiguredParameters().getProperty(
			CASE_INSENSITIVE_QUERYING)).booleanValue();
	}

	private List queryCaArrayService(CQLQuery cqlQuery) throws QueryProcessingException {
		List resultsList = new ArrayList();
		String usr = "PUBLIC";
		String pwd = "";
		SecureSession sess = SecureSessionFactory.defaultSecureSession();
		sess.start(usr, pwd);
		String sessId = sess.getSessionId();
//		System.out.println("sessId=" + sessId);

//		ExperimentSearchCriteria sc = SearchCriteriaFactory
//				.new_EXPERIMENT_EXPERIMENT_SC();
//		sc.setSessionId(sessId);
//		sc
//				.setIdentifier("gov.nih.nci.ncicb.caarray:Experiment:1015897558050098:1");
		
		
		SearchCriteria sc = CQL2SC.translate(cqlQuery, useCaseInsensitiveQueries());
		sc.setSessionId(sessId);
		
		SearchResult sr;
		try {
			sr = sc.search();
		} catch (Exception ex) {
			throw new QueryProcessingException("Error searching: " + ex.getMessage(),
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
	
	public Properties getRequiredParameters(){
		Properties props = super.getRequiredParameters();
		props.setProperty(CASE_INSENSITIVE_QUERYING, "true");
		return props;
	}

}
