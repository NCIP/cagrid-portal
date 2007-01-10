package gov.nih.nci.cagrid.data.cql.cacore.experimental;

import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Group;
import gov.nih.nci.cagrid.cqlquery.LogicalOperator;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;

/** 
 *  CQL2DetachedCriteria
 *  Translates a CQLQuery object into a Hibernate DeatchedCriteria object
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 2, 2006 
 * @version $Id$ 
 */
public class CQL2DetachedCriteria {
	
	private static Map restrictionFactories = null;
	
	/**
	 * Translates a CQLQuery into a Hibernate DetachedCriteria.
	 * his translator assumes the CQLQuery has already passed validation for
	 * at least schema correctness.  Invalid queries may still be processed with
	 * undefined results.
	 * 
	 * @param query
	 * 		A fully valid CQL query.
	 * @return
	 * @throws MalformedQueryException
	 * @throws QueryProcessingException
	 */
	public static DetachedCriteria translate(CQLQuery query) throws MalformedQueryException, QueryProcessingException {
		// create root criteria for the target class
		String objectName = query.getTarget().getName();
		Class objectClass = null;
		try {
			objectClass = Class.forName(objectName);
		} catch (Exception ex) {
			throw new QueryProcessingException("Error obtaining nested object class: " + ex.getMessage(), ex);
		}
		DetachedCriteria targetCriteria = DetachedCriteria.forClass(objectClass);
		populateObjectCritaria(targetCriteria, objectClass, query.getTarget());
		return targetCriteria;
	}
	

	/**
	 * Populates detached criteria with criterion to restrict the results
	 * of a query
	 * 
	 * @param objectCriteria
	 * 		The criteria object to populate
	 * @param objectClass
	 * 		The class of the object being restricted
	 * @param objectType
	 * 		The Object type restriction
	 * @return
	 * @throws MalformedQueryException
	 * @throws QueryProcessingException
	 */
	private static void populateObjectCritaria(DetachedCriteria objectCriteria, 
		Class objectClass, Object objectType) throws MalformedQueryException, QueryProcessingException {
				
		// handle association
		if (objectType.getAssociation() != null) {
			handleAssociation(objectCriteria, objectClass, objectType.getAssociation());
		}
		
		// handle attribute
		if (objectType.getAttribute() != null) {
			Criterion attributeCriterion = handleAttribute(objectClass, objectType.getAttribute());
			objectCriteria.add(attributeCriterion);
		}
		
		// handle group
		if (objectType.getGroup() != null) {
			Junction grouping = handleGroup(objectClass, objectType.getGroup());
			objectCriteria.add(grouping);
		}
	}
	
	
	/**
	 * Adds criterion to a parent object's criteria to handle an association restriction.
	 * Due to limitations of the Hibernate query engine, the following issues must be 
	 * accounted for when using this method:
	 * 
	 * -- This method can only handle 'and' restrictions if multiple associations are made.
	 * -- Multiple associations to the same object cannot be made.
	 * 
	 * @param parentObjectCriteria
	 * 		The present criteria of the parent object
	 * @param parentObjectClass
	 * 		The parent object's class
	 * @param association
	 * 		The association restriction to perform
	 * 
	 * @throws MalformedQueryException
	 * @throws QueryProcessingException
	 */
	private static void handleAssociation(DetachedCriteria parentObjectCriteria, Class parentObjectClass, 
		Association association) throws MalformedQueryException, QueryProcessingException {
		String roleName = getRoleName(parentObjectClass.getName(), association);
		String associationTypeName = association.getName();
		if (roleName == null) {
			throw new MalformedQueryException("Association from " + parentObjectClass.getName() + " to " + associationTypeName + " does not exist.  Use only direct associations");
		}
		// create an association criteria from parent to child
		DetachedCriteria associationCriteria = parentObjectCriteria.createCriteria(roleName);
		// populate the child criteria
		Class associationClass = null;
		try {
			associationClass = Class.forName(associationTypeName);
		} catch (Exception ex) {
			throw new QueryProcessingException("Error obtaining nested object class: " + ex.getMessage(), ex);
		}
		populateObjectCritaria(associationCriteria, associationClass, association);
	}
	
	
	/**
	 * Produces Criterion to query for an attribute
	 * 
	 * @param objectClass
	 * 		The object class to which the attribute belongs
	 * @param attrib
	 * 		The Attribute restriction
	 * @return
	 * @throws MalformedQueryException
	 * @throws QueryProcessingException
	 */
	private static Criterion handleAttribute(Class objectClass, Attribute attrib) throws MalformedQueryException, QueryProcessingException {
		String name = attrib.getName();
		String value = attrib.getValue();
		String predicate = attrib.getPredicate().getValue();
		Method factoryMethod = getRestrictionFactory(predicate);
		if (factoryMethod == null) {
			throw new MalformedQueryException("Predicate " + predicate + " is not valid or has no restriction factory");
		}
		try {
			if (factoryMethod.getParameterTypes().length == 2) {
				java.lang.Object valueObject = convertToObject(name, value, objectClass);
				return (Criterion) factoryMethod.invoke(null, new java.lang.Object[] {name, valueObject});
			} else {
				return (Criterion) factoryMethod.invoke(null, new java.lang.Object[] {name});
			}
		} catch (Exception ex) {
			throw new QueryProcessingException("Error generating criterion for attribute " + name + ":" + ex.getMessage(), ex);
		}
	}
	
	
	/**
	 * Gets the role name of an association relative to its parent class.
	 * 
	 * @param parentName
	 * 		The class name of the parent of the association
	 * @param assoc
	 * 		The associated object restriction
	 * @return
	 * 		The role name of the associated object
	 * @throws QueryProcessingException
	 */
	private static String getRoleName(String parentName, Association assoc) throws QueryProcessingException {
		String roleName = assoc.getRoleName();
		if (roleName == null) {
			// determine role based on object's type
			Class parentClass = null;
			try {
				parentClass = Class.forName(parentName);
			} catch (Exception ex) {
				throw new QueryProcessingException("Could not load class: " + ex.getMessage(), ex);
			}
			String associationTypeName = assoc.getName();
			// search the fields
			Field[] objectFields = parentClass.getFields();
			for (int i = 0; i < objectFields.length; i++) {
				if (objectFields[i].getType().getName().equals(associationTypeName)) {
					if (roleName == null) {
						roleName = objectFields[i].getName();
					} else {
						// already found a field of the same type, so association is ambiguous
						throw new QueryProcessingException("Association from " + parentClass.getName() + 
							" to " + associationTypeName + " is ambiguous: Specify a role name");
					}
				}
			}
			if (roleName == null) {
				// search for setter method
				Method[] allMethods = parentClass.getMethods();
				for (int i = 0; i < allMethods.length; i++) {
					Method current = allMethods[i];
					if (current.getName().startsWith("set")) {
						Class[] params = current.getParameterTypes();
						if (params.length == 1 && params[0].getName().equals(associationTypeName)) {
							// found a setter for the type
							String potentialRoleName = current.getName().substring(3);
							if (potentialRoleName.length() != 0) {
								if (roleName == null) {
									// lowercase first letter
									if (potentialRoleName.length() == 1) {
										roleName = potentialRoleName.toLowerCase();
									} else {
										roleName = Character.toLowerCase(potentialRoleName.charAt(0))
											+ potentialRoleName.substring(1);
									}
								} else {
									// already found a field of the same type, 
									// so association is ambiguous
									throw new QueryProcessingException("Association from " 
										+ parentClass.getName() + " to " + associationTypeName 
										+ " is ambiguous: Specify a role name");
								}
							}					
						}
					}
				}
			}
		}
		return roleName;
	}
	
	
	/**
	 * Converts a property value to a typed object
	 * 
	 * @param property
	 * @param value
	 * @param objectType
	 * @return
	 * @throws MalformedQueryException
	 * @throws QueryProcessingException
	 */
	private static java.lang.Object convertToObject(String property, String value, Class objectType) throws MalformedQueryException, QueryProcessingException {
		Field field = null;
		try {
			 field = objectType.getDeclaredField(property);
		} catch (NoSuchFieldException ex) {
			throw new MalformedQueryException("No property " + property + " was found on type " + objectType.getName());
		}
		Class propertyType = field.getType();
		if (propertyType == String.class) {
			return value;
		}
		if (propertyType == Integer.class) {
			return Integer.valueOf(value);
		}
		if (propertyType == Long.class) {
			return Long.valueOf(value);
		}
		if (propertyType == Date.class) {
			try {
				return DateFormat.getInstance().parse(value);
			} catch (ParseException ex) {
				throw new QueryProcessingException("Error parsing date: " + ex.getMessage(), ex);
			}
		}
		if (propertyType == Boolean.class) {
			return Boolean.valueOf(value);
		}
		if (propertyType == Character.class) {
			return Character.valueOf(value.charAt(0));
		}
		return null;
	}
	
	
	/**
	 * Gets the restriction factory method for a given predicate
	 * 
	 * @param predicate
	 * @return
	 * @throws QueryProcessingException
	 */
	private static Method getRestrictionFactory(String predicate) throws QueryProcessingException {
		if (restrictionFactories == null) {
			restrictionFactories = new HashMap();
			Class restrictionClass = Restrictions.class;
			try {
				// binary restrictions
				Class[] binaryParams = {String.class, java.lang.Object.class};
				restrictionFactories.put(Predicate._EQUAL_TO, restrictionClass.getMethod("eq", binaryParams));
				restrictionFactories.put(Predicate._GREATER_THAN, restrictionClass.getMethod("gt", binaryParams));
				restrictionFactories.put(Predicate._GREATER_THAN_EQUAL_TO, restrictionClass.getMethod("ge", binaryParams));
				restrictionFactories.put(Predicate._LESS_THAN, restrictionClass.getMethod("lt", binaryParams));
				restrictionFactories.put(Predicate._LESS_THAN_EQUAL_TO, restrictionClass.getMethod("le", binaryParams));
				restrictionFactories.put(Predicate._LIKE, restrictionClass.getMethod("like", binaryParams));
				restrictionFactories.put(Predicate._NOT_EQUAL_TO, restrictionClass.getMethod("ne", binaryParams));
				// unary restrictions
				Class[] unaryParams = {String.class};
				restrictionFactories.put(Predicate._IS_NOT_NULL, restrictionClass.getMethod("isNotNull", unaryParams));
				restrictionFactories.put(Predicate._IS_NULL, restrictionClass.getMethod("isNull", unaryParams));
			} catch (NoSuchMethodException ex) {
				throw new QueryProcessingException("Error loading restriction factories: " + ex.getMessage(), ex);
			}
		}
		return (Method) restrictionFactories.get(predicate);
	}
	
	
	/**
	 * Handles grouping
	 * 
	 * @param objectClass
	 * 		The class of the object the group restriction is being applied to
	 * @param group
	 * 		The group restriction
	 * @return
	 * @throws MalformedQueryException
	 * @throws QueryProcessingException
	 */
	private static Junction handleGroup(Class objectClass, Group group) throws MalformedQueryException, QueryProcessingException {
		Junction junction = null;
		if (group.getLogicRelation().getValue().equals(LogicalOperator._AND)) {
			junction = Restrictions.conjunction();
		} else if (group.getLogicRelation().getValue().equals(LogicalOperator._OR)) {
			junction = Restrictions.disjunction();
		} else {
			throw new MalformedQueryException("Logical operation " + group.getLogicRelation().getValue() 
				+ " not recognized.  Use either " + LogicalOperator._AND + " or " + LogicalOperator._OR);
		}
		
		// attributes
		for (int i = 0; group.getAttribute() != null && i < group.getAttribute().length; i++) {
			Criterion attribCriterion = handleAttribute(objectClass, group.getAttribute(i));
			junction.add(attribCriterion);
		}
		// associations
		for (int i = 0; group.getAssociation() != null && i < group.getAssociation().length; i++) {
			System.err.println("WARNING: associations ARE NOT PROCESSED on groups due to a Hibernate limitation");
			// Criterion association = handleAssociation(objectClass, group.getAssociation(i));
			// junction.add(association);
		}
		// subgroups
		for (int i = 0; group.getGroup() != null && i < group.getGroup().length; i++) {
			Junction subgroup = handleGroup(objectClass, group.getGroup(i));
			junction.add(subgroup);
		}
		return junction;
	}
}
