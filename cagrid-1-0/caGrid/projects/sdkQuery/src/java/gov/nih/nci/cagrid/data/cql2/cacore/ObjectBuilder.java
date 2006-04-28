package gov.nih.nci.cagrid.data.cql2.cacore;

import gov.nih.nci.cagrid.cqlquery2.NestedObject;
import gov.nih.nci.cagrid.cqlquery2.ObjectGroup;
import gov.nih.nci.cagrid.cqlquery2.ObjectProperty;
import gov.nih.nci.cagrid.cqlquery2.Predicate;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/** 
 *  ObjectBuilder
 *  Builds a list of criteria objects to be passed to an ApplicationServce
 *  instance for querying a data source
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 27, 2006 
 * @version $Id$ 
 */
public class ObjectBuilder {

	public static List createCriteria(NestedObject object) throws MalformedQueryException, QueryProcessingException {
		List criteria = new ArrayList();
		processNestedObject(object, criteria);		
		return criteria;
	}
	
	
	private static void processNestedObject(NestedObject object, List objectList) throws MalformedQueryException, QueryProcessingException {
		// get an instance of the top level object
		Object instance = null;
		String objectName = object.getName();
		try {
			Class objectClass = Class.forName(objectName);
			instance = objectClass.newInstance();
		} catch (Exception ex) {
			throw new QueryProcessingException("Error obtaining an instance of the nested object class", ex);
		}
		
		// apply properties to that object
		if (object.getProperty() != null) {
			for (int i = 0; i < object.getProperty().length; i++) {
				ObjectProperty prop = object.getProperty(i);
				String propertyName = prop.getName();
				String propertyValue = prop.getValue();
				String predicate = prop.getPredicate().getValue();
				setObjectProperty(instance, propertyName, propertyValue, predicate);
			}
		}
		objectList.add(instance);
		
		// start setting up related objects
		for (int i = 0; object.getObject() != null && i < object.getObject().length; i++) {
			NestedObject subobject = object.getObject(i);
			processNestedObject(subobject, objectList);
		}
		
		// process group
		if (object.getGroup() != null) {
			List groupItems = processGroup(object.getGroup());
			objectList.addAll(groupItems);
		}
	}
	
	
	private static List processGroup(ObjectGroup group) throws MalformedQueryException, QueryProcessingException {
		List groupItems = new ArrayList();
		for (int i = 0; i < group.getObject().length; i++) {
			processNestedObject(group.getObject(i), groupItems);
		}
		return groupItems;
	}
	
	
	private static void setObjectProperty(Object o, String name, String value, String predicate) throws MalformedQueryException {
		boolean propertySet = writeToField(o, name, value, predicate);
		if (!propertySet) {
			propertySet = writeToSetter(o, name, value, predicate);
		}
		if (!propertySet) {
			throw new MalformedQueryException("Property " + name + " was not able to be set.  Check your object model");
		}
	}
	
	
	private static boolean writeToField(Object o, String fieldName, String fieldValue, String predicate) throws MalformedQueryException {
		Field[] fields = o.getClass().getFields();
		try {
			for (int i = 0; fields != null && i < fields.length; i++) {
				if (fields[i].getName().equals(fieldName)) {
					String value = applyPredicate(fieldValue, predicate);
					fields[i].set(o, value);
				}
			}
		} catch (IllegalAccessException ex) {
			// fall through to return false
		}
		return false;
	}
	
	
	private static boolean writeToSetter(Object o, String name, String value, String predicate) throws MalformedQueryException {
		String setterName = "set" + capitalizeFirst(name);
		try {
			Method setter = o.getClass().getMethod(setterName, new Class[] {String.class});
			String setMe = applyPredicate(value, predicate);
			setter.invoke(o, new Object[] {setMe});
		} catch (NoSuchMethodException ex) {
			
		} catch (IllegalAccessException ex) {
			
		} catch (InvocationTargetException ex) {
			
		}
		return false;
	}
	
	
	private static String capitalizeFirst(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
	
	
	private static String applyPredicate(String value, String predicate) throws MalformedQueryException {
		if (predicate.equals(Predicate._EQUAL_TO)) {
			return value;
		} else if (predicate.equals(Predicate._LIKE)) {
			return "*" + value + "*";
		} else {
			throw new MalformedQueryException("The predicate " + predicate + " is not supported by this query processor");
		}
	}
}
