package gov.nih.nci.cagrid.data.cql.cacore;

import gov.nih.nci.cagrid.cqlquery.CQLQueryType;
import gov.nih.nci.cagrid.cqlquery.Group;
import gov.nih.nci.cagrid.cqlquery.Objects;
import gov.nih.nci.cagrid.cqlquery.Property;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResultsType;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.cql.InitializationException;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsUtil;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/** 
 *  CoreQueryProcessor
 *  Implementation of CQL Query Processor that talks to a caCORE data source 
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Mar 31, 2006 
 * @version $Id$ 
 */
public class CoreQueryProcessor implements CQLQueryProcessor {
	private ApplicationService coreService;
	
	public CoreQueryProcessor() {
		this.coreService = null;
	}
	
	
	public void init(String initString) throws InitializationException {
		if (initString == null || initString.length() == 0) {
			coreService = ApplicationService.getLocalInstance();
		} else {
			coreService = ApplicationService.getRemoteInstance(initString);
		}
	}
	
	
	public CQLQueryResultsType processQuery(CQLQueryType query) throws Exception {
		if (coreService == null) {
			throw new IllegalStateException("The core service is currently null.  Call init() before calling processQuery()!");
		}
		// get the class we're trying to return here
		String targetClassName = query.getTarget().getName();
		Class targetClass = Class.forName(targetClassName);
		
		List searchMe = new ArrayList();
		Group[] groups = query.getTarget().getGroup();
		for (int i = 0; i < groups.length; i++) {
			String logic = groups[i].getLogicRelation().getValue();
			Objects[] objects = groups[i].getObjects();
			for (int j = 0; j < objects.length; j++) {
				Object queryObject = generateQueryObject(objects[i]);
				searchMe.add(queryObject);
			}
		}
		
		List targetObjects = coreService.search(targetClass, searchMe);
		// CQLQueryResultsType results = buildResults(targetObjects);
		CQLQueryResultsType results = CQLQueryResultsUtil.createQueryResults(targetObjects);
		return results;
	}
	
	
	/*
	private CQLQueryResultsType buildResults(List objects) {
		CQLQueryResultsType results = new CQLQueryResultsType();
		// TODO: At the moment, this only deals with object results, 
		// not identifiers or attributes
		CQLObjectResult[] objectResults = new CQLObjectResult[objects.size()];
		for (int i = 0; i < objects.size(); i++) {
			Object object = objects.get(i);
			CQLObjectResult result = new CQLObjectResult();
			result.setType(object.getClass().getName());
			QName objectQname = Utils.getRegisteredQName(object.getClass());
			MessageElement anyElement = new MessageElement(objectQname, object);
			result.set_any(new MessageElement[] {anyElement});
		}
		results.setObjectResult(objectResults);
		return results;
	}
	*/
	
	
	private Object generateQueryObject(Objects input) throws Exception {
		Class inputClass = Class.forName(input.getName());
		Object instance = inputClass.newInstance();
		Property[] properties = input.getProperty();
		for (int i = 0; i < properties.length; i++) {
			Property prop = properties[i];
			setValueOnObject(instance, prop.getName(), prop.getPredicate().getValue(), prop.getValue());
		}
		return instance;
	}
	
	
	private void setValueOnObject(Object obj, String name, String predicate, String value)
		throws InvocationTargetException, IllegalAccessException {
		Method[] methods = obj.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method currentMethod = methods[i];
			// is this method a setter??
			if (isSetter(currentMethod)) {
				String fieldSet = getSetterField(currentMethod);
				if (fieldSet.equals(name)) {
					Object[] args = {predicate + value};
					currentMethod.invoke(obj, args);
					break;
				}
			}
		}
	}
	
	
	private boolean isSetter(Method method) {
		return (method.getName().startsWith("set") && method.getParameterTypes().length == 1 
			&& method.getParameterTypes()[0].equals(String.class));
	}
	
	
	private String getSetterField(Method setter) {
		String name = setter.getName();
		name = name.substring("set".length());
		if (name.length() > 1) {
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
		} else {
			name = name.toLowerCase();
		}
		return name;
	}
}
