package gov.nih.nci.cagrid.data.cql.cacore;

import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Group;
import gov.nih.nci.cagrid.cqlquery.LogicalOperator;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.data.QueryProcessingException;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/** 
 *  CQL2HQL
 *  Translates a CQL query to Hibernate v3 HQL
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Jul 19, 2006 
 * @version $Id$ 
 */
public class CQL2HQL {
	private static Map predicateValues;

	/**
	 * Translates a CQL query into an HQL string.  This translation process assumes the
	 * CQL Query has passed validation.  Processing of invalid CQL may or may not procede
	 * with undefined results.
	 * 
	 * @param query
	 * @return
	 * @throws QueryProcessingException
	 */
	public static String translate(CQLQuery query) throws QueryProcessingException {
		Map aliases = new HashMap();
		StringBuilder hql = new StringBuilder();
		processObject(hql, aliases, query.getTarget());
		return hql.toString();
	}
	
	
	/**
	 * Processes an Object of a CQL Query.
	 * 
	 * @param hql
	 * 		The existing HQL query fragment
	 * @param aliases
	 * 		The aliases used in the query
	 * @param obj
	 * 		The object to process into HQL
	 * @throws QueryProcessingException
	 */
	private static void processObject(StringBuilder hql, Map aliases, Object obj) throws QueryProcessingException {
		String objName = obj.getName();
		String objAlias = alias(aliases, objName);
		hql.append("From ").append(objName).append(" as ").append(objAlias);
		
		if (obj.getAttribute() != null) {
			hql.append(" where ");
			processAttribute(hql, objAlias, obj.getAttribute());
		}
		if (obj.getAssociation() != null) {
			hql.append(" where ");
			processAssociation(hql, aliases, objAlias, objName, obj.getAssociation());
		}
		if (obj.getGroup() != null) {
			hql.append(" where ");
			processGroup(hql, aliases, objName, obj.getGroup());
		}
	}
	
	
	/**
	 * Proceses an Attribute of a CQL Query.
	 * 
	 * @param hql
	 * 		The existing HQL query fragment
	 * @param objAlias
	 * 		The alias of the object to which this attribute belongs
	 * @param attrib
	 * 		The attribute to process into HQL
	 * @throws QueryProcessingException
	 */
	private static void processAttribute(StringBuilder hql, String objAlias, Attribute attrib) throws QueryProcessingException {
		hql.append(objAlias).append('.').append(attrib.getName());
		Predicate predicate = attrib.getPredicate();
		// unary predicates
		if (predicate.equals(Predicate.IS_NULL)) {
			hql.append(" is null");
		} else if (predicate.equals(Predicate.IS_NOT_NULL)) {
			hql.append(" is not null");
		} else {
			// binary predicates
			String predValue = convertPredicate(predicate);
			hql.append(" ").append(predValue).append(" '").append(attrib.getValue()).append("'");
		}
	}
	
	
	/**
	 * Processes an Association of a CQL Query.
	 * 
	 * @param hql
	 * 		The existing HQL query fragment
	 * @param parentAlias
	 * 		The alias of the parent object
	 * @param parentName
	 * 		The class name of the parent object
	 * @param assoc
	 * 		The association to process into HQL
	 * @throws QueryProcessingException
	 */
	private static void processAssociation(StringBuilder hql, Map aliases, String parentAlias, 
		String parentName, Association assoc) throws QueryProcessingException {
		// get the role name of the association
		String roleName = getRoleName(parentName, assoc);
		if (roleName == null) {
			// still null?? no association to the object!
			throw new QueryProcessingException("Association from type " + parentName + 
				" to type " + assoc.getName() + " does not exist.  Use only direct associations");
		}
		// make an HQL subquery for the object
		hql.append(" ").append(parentAlias).append('.').append(roleName).append(" in (");
		processObject(hql, aliases, assoc);
		hql.append(")");
	}
	
	
	/**
	 * Processes a Group of a CQL Query.
	 * 
	 * @param hql
	 * 		The existing HQL query fragment
	 * @param aliases
	 * 		The map of aliases for objects
	 * @param parentName
	 * 		The type name of the parent object
	 * @param group
	 * 		The group to process into HQL
	 * @throws QueryProcessingException
	 */
	private static void processGroup(StringBuilder hql, Map aliases, 
		String parentName, Group group) throws QueryProcessingException {
		String logic = convertLogicalOperator(group.getLogicRelation());
		String parentAlias = alias(aliases, parentName);
		
		// flag indicating a logic clause is needed before adding further query parts
		boolean logicClauseNeeded = false;
		
		// attributes
		if (group.getAttribute() != null) {
			for (int i = 0; i < group.getAttribute().length; i++) {
				logicClauseNeeded = true;
				processAttribute(hql, parentAlias, group.getAttribute(i));
				if (i + 1 < group.getAttribute().length) {
					hql.append(" ").append(logic).append(" ");
				}
			}
		}
		
		// associations
		if (group.getAssociation() != null) {
			if (logicClauseNeeded) {
				hql.append(" ").append(logic).append(" ");
			}
			for (int i = 0; i < group.getAssociation().length; i++) {
				logicClauseNeeded = true;
				processAssociation(hql, aliases, parentAlias, parentName, group.getAssociation(i));
				if (i + 1 < group.getAssociation().length) {
					hql.append(" ").append(logic).append(" ");
				}
			}
		}
		
		// subgroups
		if (group.getGroup() != null) {
			if (logicClauseNeeded) {
				hql.append(" ").append(logic).append(" ");
			}
			for (int i = 0; i < group.getGroup().length; i++) {
				hql.append("( ");
				processGroup(hql, aliases, parentName, group);
				hql.append(" )");
				if (i + 1 < group.getGroup().length) {
					hql.append(" ").append(logic).append(" ");
				}
			}
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
		}
		return roleName;
	}
	
	
	/**
	 * Gets or creates a globally (within the current CQL Query) unique alias for an object
	 * 
	 * @param aliases
	 * @param fullName
	 * @return
	 */
	private static String alias(Map aliases, String fullName) {
		String alias = (String) aliases.get(fullName);
		if (alias == null) {
			// new alias
			alias = createShortName(aliases.values(), fullName);
			aliases.put(fullName, alias);
		}
		return alias;
	}
	
	
	/**
	 * Creates a unique short name for the given full name, avoiding names
	 * already present in the taken names collection.
	 * 
	 * @param takenNames
	 * @param fullName
	 * @return
	 */
	private static String createShortName(Collection takenNames, String fullName) {
		int suffix = 0;
		int dotIndex = fullName.lastIndexOf('.');
		String alias = fullName.substring(dotIndex + 1);
		if (alias.length() > 1) {
			alias = alias.substring(0, 1).toLowerCase() + alias.substring(1);
		} else {
			alias = alias.toLowerCase();
		}
		while (takenNames.contains(alias + suffix)) {
			suffix++;
		}
		return alias + suffix;
	}
	
	
	/**
	 * Converts a predicate to its HQL string equivalent.
	 * 
	 * @param p
	 * @return
	 */
	private static String convertPredicate(Predicate p) {
		if (predicateValues == null) {
			predicateValues = new HashMap();
			predicateValues.put(Predicate.EQUAL_TO, "=");
			predicateValues.put(Predicate.GREATER_THAN, ">");
			predicateValues.put(Predicate.GREATER_THAN_EQUAL_TO, ">=");
			predicateValues.put(Predicate.LESS_THAN, "<");
			predicateValues.put(Predicate.LESS_THAN_EQUAL_TO, "<=");
			predicateValues.put(Predicate.LIKE, "LIKE");
			predicateValues.put(Predicate.NOT_EQUAL_TO, "!=");
		}
		return (String) predicateValues.get(p);
	}
	
	
	/**
	 * Converts a logical operator to its HQL string equiavalent.
	 * 
	 * @param op
	 * @return
	 */
	private static String convertLogicalOperator(LogicalOperator op) throws QueryProcessingException {
		if (op.getValue().equals(LogicalOperator._AND)) {
			return "AND";
		} else if (op.getValue().equals(LogicalOperator._OR)) {
			return "OR";
		}
		throw new QueryProcessingException("Logical operator '" + op.getValue() + "' is not recognized.");
	}
}
