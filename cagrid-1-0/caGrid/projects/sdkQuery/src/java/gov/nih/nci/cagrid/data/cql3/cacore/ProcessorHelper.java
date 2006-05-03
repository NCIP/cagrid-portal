package gov.nih.nci.cagrid.data.cql3.cacore;

import gov.nih.nci.cagrid.cqlquery3.AssociationType;
import gov.nih.nci.cagrid.cqlquery3.AttributeType;
import gov.nih.nci.cagrid.cqlquery3.GroupType;
import gov.nih.nci.cagrid.cqlquery3.LogicalOperatorType;
import gov.nih.nci.cagrid.cqlquery3.NestedObjectType;
import gov.nih.nci.cagrid.cqlquery3.PredicateType;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/** 
 *  ProcessorHelper
 *  TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 2, 2006 
 * @version $Id$ 
 */
public class ProcessorHelper {
	
	private static Map restrictionFactories = null;

	public static DetachedCriteria createQueryCriteria(NestedObjectType objectType) throws MalformedQueryException, QueryProcessingException {
		// create criteria for the class
		String objectName = objectType.getName();
		Class objectClass = null;
		try {
			objectClass = Class.forName(objectName);
		} catch (Exception ex) {
			throw new QueryProcessingException("Error obtaining nested object class: " + ex.getMessage(), ex);
		}
		DetachedCriteria objectCriteria = DetachedCriteria.forClass(objectClass);
		
		// nested objects can have EITHER an attribute, group, or associated object
		validateObjectChildren(objectType);
		
		// handle association
		if (objectType.getAssociation() != null) {
			Criterion association = handleAssociation(objectClass, objectType.getAssociation());
			objectCriteria.add(association);
		}
		
		// handle attribute
		if (objectType.getAttribute() != null) {
			Criterion attributeCriterion = handleAttribute(objectType.getAttribute());
			objectCriteria.add(attributeCriterion);
		}
		
		// handle group
		if (objectType.getGroup() != null) {
			Junction grouping = handleGroup(objectClass, objectType.getGroup());
			objectCriteria.add(grouping);
		}
		return null;
	}
	
	
	private static Criterion handleAssociation(Class objectClass, AssociationType association) throws MalformedQueryException, QueryProcessingException {
		String role = association.getRoleName();
		String associationType = association.getObject().getName();
		if (role == null) {
			// determine role based on object's type
			Field[] objectFields = objectClass.getFields();
			for (int i = 0; i < objectFields.length; i++) {
				if (objectFields[i].getType().getName().equals(associationType)) {
					if (role == null) {
						role = objectFields[i].getName();
					} else {
						// already found a field of the same type, so association is ambiguous
						throw new MalformedQueryException("Association from " + objectClass.getName() + " to " + associationType + " is ambiguous: Specify a role name");
					}
				}
			}
		}
		if (role == null) {
			// still null?? no association to the object!
			throw new MalformedQueryException("Association from " + objectClass.getName() + " to " + associationType + " does not exist.  Use only direct associations");
		}
		DetachedCriteria associationCriteria = createQueryCriteria(association.getObject());
		Criterion criteria = Property.forName(role).eq(associationCriteria);
		return criteria;
	}
	
	
	private static void validateObjectChildren(NestedObjectType objectType) throws MalformedQueryException {
		int childTypeCount = 0;
		if (objectType.getAssociation() != null) {
			childTypeCount++;
		}
		if (objectType.getAttribute() != null) {
			childTypeCount++;
		}
		if (objectType.getGroup() != null) {
			childTypeCount++;
		}
		if (childTypeCount > 1) {
			throw new MalformedQueryException("NestedObject " + objectType.getName() + " contains more than one of Attribute, Associated Object, or Group");
		}
	}
	
	
	private static Criterion handleAttribute(AttributeType attrib) throws MalformedQueryException, QueryProcessingException {
		String name = attrib.getName();
		String value = attrib.getValue();
		String predicate = attrib.getPredicate().getValue();
		Method factoryMethod = getRestrictionFactory(predicate);
		if (factoryMethod == null) {
			throw new MalformedQueryException("Predicate " + predicate + " is not valid or has no restriction factory");
		}
		try {
			return (Criterion) factoryMethod.invoke(null, new Object[] {name, value});
		} catch (Exception ex) {
			throw new QueryProcessingException("Error generating criterion for attribute " + name + ":" + ex.getMessage(), ex);
		}
	}
	
	
	private static Method getRestrictionFactory(String predicate) throws QueryProcessingException {
		if (restrictionFactories == null) {
			restrictionFactories = new HashMap();
			Class restrictionClass = Restrictions.class;
			try {
				Class[] paramClasses = {String.class, Object.class};
				restrictionFactories.put(PredicateType._EQUAL_TO, restrictionClass.getMethod("eq", paramClasses));
				restrictionFactories.put(PredicateType._GREATER_THAN, restrictionClass.getMethod("gt", paramClasses));
				restrictionFactories.put(PredicateType._GREATER_THAN_EQUAL_TO, restrictionClass.getMethod("ge", paramClasses));
				restrictionFactories.put(PredicateType._ISNOTNULL, restrictionClass.getMethod("isNotNull", paramClasses));
				restrictionFactories.put(PredicateType._ISNULL, restrictionClass.getMethod("isNull", paramClasses));
				restrictionFactories.put(PredicateType._LESS_THAN, restrictionClass.getMethod("lt", paramClasses));
				restrictionFactories.put(PredicateType._LESS_THAN_EQUAL_TO, restrictionClass.getMethod("le", paramClasses));
				restrictionFactories.put(PredicateType._LIKE, restrictionClass.getMethod("like", paramClasses));
				restrictionFactories.put(PredicateType._NOT_EQUAL_TO, restrictionClass.getMethod("ne", paramClasses));
			} catch (NoSuchMethodException ex) {
				throw new QueryProcessingException("Error loading restriction factories: " + ex.getMessage(), ex);
			}
		}
		return (Method) restrictionFactories.get(predicate);
	}
	
	
	private static Junction handleGroup(Class objectClass, GroupType group) throws MalformedQueryException, QueryProcessingException {
		validateGroup(group);
		Junction junction = null;
		if (group.getLogicRelation().getValue().equals(LogicalOperatorType._AND)) {
			junction = Restrictions.conjunction();
		} else if (group.getLogicRelation().getValue().equals(LogicalOperatorType._OR)) {
			junction = Restrictions.disjunction();
		} else {
			throw new MalformedQueryException("Logical operation " + group.getLogicRelation().getValue() + " not recognized.  Use either " + LogicalOperatorType._AND + " or " + LogicalOperatorType._OR);
		}
		
		// attributes
		for (int i = 0; group.getAttribute() != null && i < group.getAttribute().length; i++) {
			Criterion attribCriterion = handleAttribute(group.getAttribute(i));
			junction.add(attribCriterion);
		}
		// associations
		for (int i = 0; group.getAssociation() != null && i < group.getAssociation().length; i++) {
			Criterion association = handleAssociation(objectClass, group.getAssociation(i));
			junction.add(association);
		}
		// groups
		for (int i = 0; group.getGroup() != null && i < group.getGroup().length; i++) {
			Junction subgroup = handleGroup(objectClass, group.getGroup(i));
			junction.add(subgroup);
		}
		return junction;
	}
	
	
	private static void validateGroup(GroupType group) throws MalformedQueryException {
		// ensure there's at least two items in the group
		int itemCount = 0;
		if (group.getAssociation() != null) {
			itemCount += group.getAssociation().length;
		}
		if (group.getAttribute() != null) {
			itemCount += group.getAttribute().length;
		}
		if (group.getGroup() != null) {
			itemCount += group.getGroup().length;
		}
		if (itemCount < 2) {
			throw new MalformedQueryException("Groups must contain at least two items");
		}
	}
}
