package gov.nih.nci.cagrid.caarray.cql;

import gov.nih.nci.cagrid.caarray.encoding.MGEDCubeHandler;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.QueryModifier;
import gov.nih.nci.cagrid.cqlresultset.CQLAttributeResult;
import gov.nih.nci.cagrid.cqlresultset.CQLObjectResult;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.cqlresultset.TargetAttribute;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.mapping.Mappings;
import gov.nih.nci.cagrid.data.service.ServiceConfigUtil;
import gov.nih.nci.cagrid.data.utilities.CQLResultsCreationUtil;
import gov.nih.nci.cagrid.data.utilities.ResultsCreationException;
import gov.nih.nci.common.remote.rmi.RMISearchCriteriaHandlerRemoteIF;
import gov.nih.nci.common.search.Directable;
import gov.nih.nci.common.search.SearchCriteria;
import gov.nih.nci.common.search.SearchResult;
import gov.nih.nci.common.search.session.SecureSession;
import gov.nih.nci.common.search.session.SecureSessionFactory;
import gov.nih.nci.mageom.domain.BioAssayData.MeasuredBioAssayData;
import gov.nih.nci.mageom.domain.BioAssayData.impl.BioDataCubeImpl;
import gov.nih.nci.mageom.domain.BioAssayData.impl.MeasuredBioAssayDataImpl;

import java.lang.reflect.Method;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.namespace.QName;

import org.apache.axis.message.MessageElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CaArrayCQLQueryProcessor extends CQLQueryProcessor {

	protected static Log LOG = LogFactory
			.getLog(CaArrayCQLQueryProcessor.class);

	public static final String CASE_INSENSITIVE_QUERYING = "queryCaseInsensitive";

	private static final String USERNAME = "usr";

	private static final String PASSWORD = "pwd";

	private static final String RMI_SERVER_URL = "RMIServerURL";

	private static final String SECURE_SESSION_MGR_URL = "SecureSessionManagerURL";

	public CaArrayCQLQueryProcessor() {

	}

	public CQLQueryResults processQuery(CQLQuery cqlQuery)
			throws MalformedQueryException, QueryProcessingException {

		LOG.debug("Processing query...");
		CQLQueryResults results = null;
		try {
			List coreResultsList = queryCaArrayService(cqlQuery);
			String targetName = cqlQuery.getTarget().getName();
			Mappings mappings = null;
			try {
				mappings = getClassToQnameMappings();
			} catch (Exception ex) {
				throw new QueryProcessingException(
						"Error getting class to qname mappings: "
								+ ex.getMessage(), ex);
			}

			// decide on type of results
			QueryModifier mod = cqlQuery.getQueryModifier();
			boolean objectResults = mod == null
					|| (!mod.isCountOnly() && mod.getAttributeNames() == null && mod
							.getDistinctAttribute() == null);

			if (objectResults) {
				try {
					results = createObjectResults(coreResultsList, targetName,
							mappings);
				} catch (Exception ex) {
					throw new QueryProcessingException(
							"Error creating object results: " + ex.getMessage(),
							ex);
				}
			} else {

				if (mod.isCountOnly()) {

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
					try {
						results = createAttributeResults(coreResultsList,
								targetName, names);
					} catch (Exception ex) {
						throw new RuntimeException(
								"Error creating attribute results: "
										+ ex.getMessage(), ex);
					}
				}
			}
		} catch (Exception ex) {
			String msg = "Error processing query: " + ex.getMessage();
			LOG.error(msg, ex);
			throw new QueryProcessingException(msg, ex);
		}
		LOG.debug("...done.");

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
			MessageElement elem = new MessageElement(targetQName, obj);
			objectResults[idx] = new CQLObjectResult(
					new MessageElement[] { elem });

		}
		results.setObjectResult(objectResults);
		return results;

	}

	public CQLQueryResults createAttributeResults(List objects,
			String targetClassname, String[] attNames)
			throws QueryProcessingException {
		CQLQueryResults results = new CQLQueryResults();
		results.setTargetClassname(targetClassname);
		CQLAttributeResult[] attResults = new CQLAttributeResult[objects.size()];
		int idx = 0;
		for (Iterator i = objects.iterator(); i.hasNext(); idx++) {
			Object obj = i.next();
			TargetAttribute[] attValues = new TargetAttribute[attNames.length];
			for (int j = 0; j < attNames.length; j++) {
				Object attValue = getAttributeValue(obj, attNames[j]);
				if (attValue instanceof Object[][][]) {
					attValue = new MGEDCubeHandler()
							.getCubeAsString((Object[][][]) attValue);

				}
				attValues[j] = new TargetAttribute(attNames[j], attValue
						.toString());
			}
			attResults[idx] = new CQLAttributeResult(attValues);
		}
		results.setAttributeResult(attResults);
		return results;
	}

	private Object getAttributeValue(Object obj, String attName)
			throws QueryProcessingException {
		Object value = null;

		// Find accessor
		String accessorName = "get" + attName.substring(0, 1).toUpperCase()
				+ attName.substring(1);
		Method accessor = null;
		Class superClass = obj.getClass();
		search: while (superClass != null) {
			Method[] methods = superClass.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().equals(accessorName)) {
					accessor = methods[i];
					break search;
				}
			}
			superClass = superClass.getSuperclass();
		}

		// Invoke accessor
		try {
			value = accessor.invoke(obj, new Object[0]);
		} catch (Exception ex) {
			throw new QueryProcessingException("Error invoking "
					+ obj.getClass().getName() + "." + accessorName + ": "
					+ ex.getMessage(), ex);
		}

		if (value == null) {
			value = "";
		}
		return value;
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
		return Boolean.valueOf(
				getConfiguredParameters()
						.getProperty(CASE_INSENSITIVE_QUERYING)).booleanValue();
	}

	private List queryCaArrayService(CQLQuery cqlQuery)
			throws QueryProcessingException {
		List resultsList = new ArrayList();
		String usr = getConfiguredParameters().getProperty(USERNAME);
		String pwd = getConfiguredParameters().getProperty(PASSWORD);
		SecureSession sess = SecureSessionFactory.defaultSecureSession();
		((Directable) sess).direct(getConfiguredParameters().getProperty(
				SECURE_SESSION_MGR_URL));
		sess.start(usr, pwd);
		String sessId = sess.getSessionId();

		SearchCriteria sc = CQL2SC.translate(cqlQuery,
				useCaseInsensitiveQueries());
		sc.setSessionId(sessId);

		SearchResult sr;
		try {
			RMISearchCriteriaHandlerRemoteIF rmiServer = (RMISearchCriteriaHandlerRemoteIF) Naming
					.lookup(getConfiguredParameters().getProperty(
							RMI_SERVER_URL));
			sr = rmiServer.search(sc);
		} catch (Exception ex) {
			throw new QueryProcessingException("Error searching: "
					+ ex.getMessage(), ex);
		}
		QueryModifier mod = cqlQuery.getQueryModifier();
		if (mod != null && mod.isCountOnly()) {
			resultsList.add(sr.getCount());
		} else {
			Object[] results = sr.getResultSet();
			LOG.debug("results.length = " + results.length);
			for (int i = 0; i < results.length; i++) {
				resultsList.add(results[i]);
			}
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
		Properties props = super.getRequiredParameters();
		props.setProperty(CASE_INSENSITIVE_QUERYING, "true");
		props.setProperty(USERNAME, "PUBLIC");
		props.setProperty(PASSWORD, "");
		props
				.setProperty(RMI_SERVER_URL,
						"//caarray-mageom-server.nci.nih.gov:8080/SearchCriteriaHandler");
		props
				.setProperty(SECURE_SESSION_MGR_URL,
						"//caarray-mageom-server.nci.nih.gov:8080/SecureSessionManager");
		return props;
	}

}
