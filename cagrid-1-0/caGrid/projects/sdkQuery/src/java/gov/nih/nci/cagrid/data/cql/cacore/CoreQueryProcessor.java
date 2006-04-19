package gov.nih.nci.cagrid.data.cql.cacore;

import gov.nih.nci.cagrid.cqlquery.CQLQueryType;
import gov.nih.nci.cagrid.cqlquery.Group;
import gov.nih.nci.cagrid.cqlquery.Objects;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResultsType;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.cql.InitializationException;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsUtil;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Subqueries;

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
		// create target criteria
		Class targetClass = Class.forName(query.getTarget().getName());
		DetachedCriteria targetCriteria = DetachedCriteria.forClass(targetClass);
		
		// process the target's object
		if (query.getTarget().getObjects() != null) {
			Objects object = query.getTarget().getObjects();
			DetachedCriteria objectCriteria = ProcessorHelper.processObjects(object);
			Criterion objectCriterion = Subqueries.exists(objectCriteria);
			targetCriteria.add(objectCriterion);
		}

		// process the target's groups
		if (query.getTarget().getGroup() != null) {
			Group[] groups = query.getTarget().getGroup();
			for (int i = 0; i < groups.length; i++) {
				Junction groupJunction = ProcessorHelper.processGroup(groups[i]);
				targetCriteria.add(groupJunction);
			}
		}
		
		// submit the query to the Application Service
		List targetObjects = coreService.query(targetCriteria, query.getTarget().getName());
		CQLQueryResultsType results = CQLQueryResultsUtil.createQueryResults(targetObjects);
		return results;
	}
	
	
	/*
	public CQLQueryResultsType processQuery(CQLQueryType query) throws Exception {
		if (coreService == null) {
			throw new IllegalStateException("The core service is currently null.  Call init() before calling processQuery()!");
		}
		// get the class we're trying to return here
		// TODO: I believe this is a caDSR type name, not a Java
		// class name, although they'll mostly match eachother.
		// Should be using QName here to get the registered
		// (type mapping) class to create.
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
		
		DetachedCriteria criteria = DetachedCriteria.forClass(targetClass);
		
		
		List targetObjects = coreService.search(targetClass, searchMe);
		CQLQueryResultsType results = CQLQueryResultsUtil.createQueryResults(targetObjects);
		return results;
	}
	
	
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
	
	
	private DetachedCriteria getCriteria(CQLQueryType query) throws ClassNotFoundException {
		String targetClassName = query.getTarget().getName();
		Class targetClass = Class.forName(targetClassName);
		DetachedCriteria criteria = DetachedCriteria.forClass(targetClass);
		Session hibernateSession = HibernateUtil.currentSession();
		Criterion cr = Example.create(null);
		Junction j = Restrictions.conjunction().add(cr);
		criteria.add(j);
		return criteria;
	}
	*/
}
