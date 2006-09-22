package gov.nih.nci.cagrid.data.cql.cacore;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.QueryModifier;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.cql.LazyCQLQueryProcessor;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsUtil;
import gov.nih.nci.common.util.HQLCriteria;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

/** 
 *  HQLCoreQueryProcessor
 *  Implementation of CQL against a caCORE data source using HQL queries
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 2, 2006 
 * @version $Id$ 
 */
public class HQLCoreQueryProcessor extends LazyCQLQueryProcessor {
	public static final String DEFAULT_LOCALHOST_CACORE_URL = "http://localhost:8080/cacore31/server/HTTPServer";
	public static final String APPLICATION_SERVICE_URL = "appserviceUrl";
	
	private static Logger LOG = Logger.getLogger(HQLCoreQueryProcessor.class);
	
	private ApplicationService coreService;
	private StringBuffer wsddContents; 
	
	public HQLCoreQueryProcessor() {
		super();
	}
	
	
	private InputStream getWsdd() throws Exception {
		if (getConfiguredWsddStream() != null) {
			if (wsddContents == null) {
				wsddContents = Utils.inputStreamToStringBuffer(getConfiguredWsddStream());
			}
			return new ByteArrayInputStream(wsddContents.toString().getBytes());
		} else {
			return null;
		}
	}
	

	public CQLQueryResults processQuery(CQLQuery cqlQuery) 
		throws MalformedQueryException, QueryProcessingException {
		InputStream configStream = null;
		try {
			configStream = getWsdd();
		} catch (Exception ex) {
			throw new QueryProcessingException(ex);
		}
		List coreResultsList = queryCoreService(cqlQuery);
		CQLQueryResults results = null;
		// decide on type of results
		boolean objectResults = cqlQuery.getQueryModifier() == null ||
			(!cqlQuery.getQueryModifier().isCountOnly() 
				&& cqlQuery.getQueryModifier().getAttributeNames() == null 
				&& cqlQuery.getQueryModifier().getDistinctAttribute() == null);
		if (objectResults) {
			results = CQLQueryResultsUtil.createQueryResults(
				coreResultsList, cqlQuery.getTarget().getName(), configStream);
		} else {
			QueryModifier mod = cqlQuery.getQueryModifier();
			if (mod.isCountOnly()) {
				// parse the value as a string to long.  This covers returning
				// integers, shorts, and longs
				Long val = Long.valueOf(coreResultsList.get(0).toString());
				results = CQLQueryResultsUtil.createCountQueryResults(
					val.longValue(), cqlQuery.getTarget().getName());
			} else {
				// attributes distinct or otherwise
				String[] names = null;
				if (mod.getDistinctAttribute() != null) {
					names = new String[] {mod.getDistinctAttribute()};
				} else {
					names = mod.getAttributeNames();
				}
				results = CQLQueryResultsUtil.createAttributeQueryResults(
					coreResultsList, cqlQuery.getTarget().getName(), names);
			}
		}
		return results;
	}
	
	
	public Iterator processQueryLazy(CQLQuery cqlQuery) 
		throws MalformedQueryException, QueryProcessingException {
		List coreResultsList = queryCoreService(cqlQuery);
		return coreResultsList.iterator();
	}
	
	
	private List queryCoreService(CQLQuery query) 
		throws MalformedQueryException, QueryProcessingException {
		// get the caCORE application service
		ApplicationService service = getApplicationService();
		
		// see if the target has subclasses
		boolean subclassesDetected = SubclassCheckCache.hasClassProperty(query.getTarget().getName(), service);
		
		// generate the HQL to perform the query
		String hql = null;
		if (subclassesDetected) {
			// simplify the query by removing modifiers
			CQLQuery simpleQuery = new CQLQuery();
			simpleQuery.setTarget(query.getTarget());
			hql = CQL2HQL.translate(simpleQuery,true);
		} else {
			hql = CQL2HQL.translate(query, false);
		}
		System.out.println("Executing HQL: " + hql);
		LOG.debug("Executing HQL:" + hql);
		
		// process the query
		HQLCriteria hqlCriteria = new HQLCriteria(hql);
		List targetObjects = null;
		try {
			targetObjects = coreService.query(hqlCriteria, query.getTarget().getName());
		} catch (Exception ex) {
			throw new QueryProcessingException("Error invoking core query method: " + ex.getMessage(), ex);
		}
		
		// possibly post-process the query
		if (subclassesDetected && query.getQueryModifier() != null) {
			try {
				targetObjects = applyQueryModifiers(targetObjects, query.getQueryModifier());
			} catch (Exception ex) {
				throw new QueryProcessingException("Error applying query modifiers: " + ex.getMessage(), ex);
			}
		}
		return targetObjects;
	}
	
	
	private List applyQueryModifiers(List rawObjects, QueryModifier mods) throws Exception {
		List processed = new LinkedList();
		Iterator rawIter = rawObjects.iterator();
		if (mods.getDistinctAttribute() != null) {
			Set distinctValues = new HashSet();
			while (rawIter.hasNext()) {
				Object o = rawIter.next();
				Object value = accessNamedProperty(o, mods.getDistinctAttribute());
				distinctValues.add(value);
			}
			// convert the single objects to object arrays
			Iterator distinctIter = distinctValues.iterator();
			while (distinctIter.hasNext()) {
				processed.add(new Object[] {distinctIter.next()});
			}
		} else if (mods.getAttributeNames() != null) {
			String[] names = mods.getAttributeNames();
			while (rawIter.hasNext()) {
				Object o = rawIter.next();
				Object[] values = new Object[names.length];
				for (int i = 0; i < names.length; i++) {
					values[i] = accessNamedProperty(o, names[i]);
				}
				processed.add(values);
			}
		} else {
			processed = rawObjects;
		}
		
		if (mods.isCountOnly()) {
			List countList = new ArrayList(1);
			countList.add(new Integer(processed.size()));
			processed = countList;
		}
		return processed;
	}
	
	
	private Object accessNamedProperty(Object o, String name) throws Exception {
		Field[] fields = o.getClass().getFields();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getName().equals(name)
				&& Modifier.isPublic(fields[i].getModifiers())) {
				return fields[i].get(o);
			}
		}
		// no fields?  check methods for getters
		Method[] methods = o.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			String methodName = methods[i].getName();
			if (methodName.startsWith("get") && methods[i].getParameterTypes().length == 0) {
				// strip off the 'get'
				String fieldName = methodName.substring(3);
				if (fieldName.length() == 1) {
					fieldName = String.valueOf(Character.toLowerCase(fieldName.charAt(0)));
				} else {
					fieldName = String.valueOf(Character.toLowerCase(fieldName.charAt(0))) 
						+ fieldName.substring(1);
				}
				if (fieldName.equals(name)) {
					return methods[i].invoke(o, new Object[] {});
				}
			}			
		}
		throw new NoSuchFieldException("No field " + name + " found on " + o.getClass().getName());
	}
	
	
	private ApplicationService getApplicationService() throws QueryProcessingException {
		if (coreService == null) {
			String url = getConfiguredParameters().getProperty(APPLICATION_SERVICE_URL);
			if (url == null || url.length() == 0) {
				throw new QueryProcessingException(
					"Required parameter " + APPLICATION_SERVICE_URL + " was not defined!");
			}
			coreService = ApplicationService.getRemoteInstance(url);
		}
		return coreService;
	}
	
	
	public Properties getRequiredParameters() {
		Properties params = new Properties();
		params.setProperty(APPLICATION_SERVICE_URL, DEFAULT_LOCALHOST_CACORE_URL);
		return params;
	}
}
