package gov.nih.nci.cagrid.data.cql2.cacore;

import gov.nih.nci.cagrid.cqlquery2.LogicalOperator;
import gov.nih.nci.cagrid.cqlquery2.NestedObject;
import gov.nih.nci.cagrid.cqlquery2.ObjectGroup;
import gov.nih.nci.cagrid.cqlquery2.ObjectProperty;
import gov.nih.nci.cagrid.cqlquery2.Predicate;
import gov.nih.nci.cagrid.data.MalformedQueryException;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

/** 
 *  ProcessorHelper
 *  Helper functionality to get a query processed
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 26, 2006 
 * @version $Id$ 
 */
public class ProcessorHelper {

	public static DetachedCriteria processNestedObject(NestedObject object) throws ClassNotFoundException, MalformedQueryException {
		String objectName = object.getName();
		Class objectClass = Class.forName(objectName);
		DetachedCriteria objectCriteria = DetachedCriteria.forClass(objectClass);
		
		// apply properties to the object criteria
		if (object.getProperty() != null) {
			for (int i = 0; i < object.getProperty().length; i++) {
				ObjectProperty property = object.getProperty(i);
				Criterion propertyCriterion = processProperty(property);
				objectCriteria.add(propertyCriterion);
			}
		}
		
		// process group
		if (object.getGroup() != null) {
			Junction groupJunction = processGroup(object.getGroup());
			objectCriteria.add(groupJunction);
		}
		return objectCriteria;
	}
	
	
	public static Criterion processProperty(ObjectProperty property) throws MalformedQueryException {
		Criterion criterion = null;
		String predicateValue = property.getPredicate().getValue();
		if (predicateValue.equals(Predicate._EQUAL_TO)) {
			criterion = Restrictions.eq(property.getName(), property.getValue());
		} else if (predicateValue.equals(Predicate._NOT_EQUAL_TO)) {
			criterion = Restrictions.ne(property.getName(), property.getValue());
		} else if (predicateValue.equals(Predicate._GREATER_THAN)) {
			criterion = Restrictions.gt(property.getName(), property.getValue());
		} else if (predicateValue.equals(Predicate._LESS_THAN)) {
			criterion = Restrictions.lt(property.getName(), property.getValue());
		} else if (predicateValue.equals(Predicate._LIKE)) {
			criterion = Restrictions.like(property.getName(), property.getValue());
		} else {
			throw new MalformedQueryException("The predicate value " + predicateValue + " is not valid!");
		}
		return criterion;
	}
	
	
	public static Junction processGroup(ObjectGroup group) throws ClassNotFoundException, MalformedQueryException {
		Junction junction = null;
		if (group.getRelation().getValue().equals(LogicalOperator._AND)) {
			junction = Restrictions.conjunction();
		} else if (group.getRelation().getValue().equals(LogicalOperator._OR)) {
			junction = Restrictions.disjunction();
		} else {
			throw new MalformedQueryException("Logical operator " + group.getRelation().getValue() + " is not valid!");
		}
		// groups must have at least 2 objects, so no check for null here
		if (group.getObject() != null && group.getObject().length >= 2) {
			for (int i = 0; i < group.getObject().length; i++) {
				DetachedCriteria objectCriteria = processNestedObject(group.getObject(i));
				Criterion criterion = Subqueries.exists(objectCriteria);
				junction.add(criterion);
			}
		} else {
			throw new MalformedQueryException("Groups must contain at least two NestedObjects!");
		}
		return junction;
	}
}
