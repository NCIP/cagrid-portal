package gov.nih.nci.cagrid.data.cql.cacore;

import gov.nih.nci.cagrid.cqlquery.Group;
import gov.nih.nci.cagrid.cqlquery.LogicalOperator;
import gov.nih.nci.cagrid.cqlquery.Objects;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlquery.Property;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

/** 
 *  ProcessorHelper
 *  Helper class to process the CQL Query objects class into Hibernate criterion
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 19, 2006 
 * @version $Id$ 
 */
public class ProcessorHelper {
	
	public static DetachedCriteria processObjects(Objects objects) throws ClassNotFoundException {
		// create the object's criteria
		String objectName = objects.getName();
		Class objectClass = Class.forName(objectName);
		DetachedCriteria objectCriteria = DetachedCriteria.forClass(objectClass);		
		
		// apply properties to the object
		Property[] properties = objects.getProperty();
		for (int i = 0; i < properties.length; i++) {
			Criterion propertyCriterion = processProperty(properties[i]);
			objectCriteria.add(propertyCriterion);
		}
		
		// add criteria for objects further down the line
		Objects subObject = objects.getObjects();
		if (subObject != null) {
			DetachedCriteria subObjectCriteria = processObjects(subObject);
			Criterion subObjectCriterion = Subqueries.exists(subObjectCriteria);
			objectCriteria.add(subObjectCriterion);
		}
		
		// process the groups
		if (objects.getGroup() != null) {
			Junction groupJunction = processGroup(objects.getGroup());
			objectCriteria.add(groupJunction);
		}
		
		return objectCriteria;
	}
	
	
	public static Criterion processProperty(Property prop) {
		Criterion expression = null;
		if (prop.getPredicate().getValue().equals(Predicate._equal)) {
			expression = Restrictions.eq(prop.getName(), prop.getValue());
		} else if (prop.getPredicate().getValue().equals(Predicate._like)) {
			expression = Restrictions.like(prop.getName(), prop.getValue());
		}
		return expression;
	}
	
	
	public static Junction processGroup(Group group) throws ClassNotFoundException {
		Junction groupJunction = null;
		if (group.getLogicRelation().getValue().equals(LogicalOperator._AND)) {
			groupJunction = Restrictions.conjunction();
		} else if (group.getLogicRelation().getValue().equals(LogicalOperator._OR)) {
			groupJunction = Restrictions.disjunction();
		}
		
		Objects[] objects = group.getObjects();
		for (int i = 0; i < objects.length; i++) {
			DetachedCriteria objectCriteria = processObjects(objects[i]);
			Criterion subObjectCriterion = Subqueries.exists(objectCriteria);
			objectCriteria.add(subObjectCriterion);
		}
		return groupJunction;
	}
}
