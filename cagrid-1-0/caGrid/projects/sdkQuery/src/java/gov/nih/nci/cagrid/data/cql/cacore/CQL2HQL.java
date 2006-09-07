package gov.nih.nci.cagrid.data.cql.cacore;

import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Group;
import gov.nih.nci.cagrid.cqlquery.LogicalOperator;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlquery.QueryModifier;
import gov.nih.nci.cagrid.data.QueryProcessingException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	public static final String TARGET_ALIAS = "xxTargetAliasxx"; 
	
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
		StringBuilder hql = new StringBuilder();
		if (query.getQueryModifier() != null) {
			processModifiedQuery(hql, query.getQueryModifier(), query.getTarget());
		} else {
			processObject(hql, query.getTarget());
		}
		return hql.toString();
	}
	
	
	/**
	 * Processes a query with Query Modifications applied to it
	 * 
	 * @param hql
	 * 		The HQL built thus far
	 * @param mods
	 * 		The modifications to the query
	 * @param target
	 * 		The target object
	 * @throws QueryProcessingException
	 */
	private static void processModifiedQuery(StringBuilder hql, QueryModifier mods, Object target) throws QueryProcessingException {
		if (mods.isCountOnly()) {
			processCountingQuery(hql, mods, target);
		} else {
			processAttributeQuery(hql, mods, target);
		}
	}
	
	
	/**
	 * Processes a query which returns a count
	 * 
	 * @param hql
	 * 		The HQL query fragment
	 * @param mods
	 * 		The modifications to apply to the query
	 * @param target
	 * 		The query's target object
	 * @throws QueryProcessingException
	 */
	private static void processCountingQuery(StringBuilder hql, QueryModifier mods, Object target) throws QueryProcessingException {
		hql.append("select count(");
		if (mods.getDistinctAttribute() != null) {
			// counting distinct attributes
			hql.append("distinct ").append(mods.getDistinctAttribute()).append(") ");
			processObject(hql, target);
		} else if (mods.getAttributeNames() != null) {
			// counting objects where any one of the attribs is not null
			hql.append("*) ");
			// process the target object normally
			processObject(hql, target);
			// only add a where statement if the target has no child restrictions
			boolean addWhereStatement = target.getAssociation() == null 
				&& target.getAttribute() == null && target.getGroup()== null;
			if (addWhereStatement) {
				hql.append(" where ");
			} else {
				hql.append(" and ");
			}
			// build the attribute not null clause
			StringBuilder attribClause = new StringBuilder();
			attribClause.append("(");
			for (int i = 0; i < mods.getAttributeNames().length; i++) {
				attribClause.append(mods.getAttributeNames(i));
				attribClause.append(" is not null");
				if (i + 1 < mods.getAttributeNames().length) {
					attribClause.append(" or ");
				}
			}
			attribClause.append(")");
			// append the attribute not null clause to the target
			hql.append(attribClause.toString());
		} else {
			// counting unique objects
			// need to use an alias in the count clause
			hql.append(TARGET_ALIAS).append(") ");
			int currentIndex = hql.length();
			processObject(hql, target);
			// add the alias to the object
			int targetClassIndex = hql.indexOf(target.getName(), currentIndex);
			targetClassIndex += target.getName().length();
			hql.insert(targetClassIndex, " " + TARGET_ALIAS + " ");
		}
	}
	
	
	/**
	 * Processes a query which returns attributes (distinct or otherwise)
	 * 
	 * @param hql
	 * 		The existing HQL fragment
	 * @param mods
	 * 		The modifications to apply to the query
	 * @param target
	 * 		The target object of the query
	 * @throws QueryProcessingException
	 */
	private static void processAttributeQuery(StringBuilder hql, QueryModifier mods, Object target) throws QueryProcessingException {
		if (mods.getDistinctAttribute() != null) {
			// counting distinct attributes
			hql.append("select distinct ").append(mods.getDistinctAttribute());
		} else {
			String[] names = mods.getAttributeNames();
			if (names != null) {
				hql.append("select ");
				for (int i = 0; i < names.length; i++) {
					hql.append(names[i]);
					if (i + 1 < names.length) {
						hql.append(", ");
					}
				}
			}
		}
		hql.append(" ");
		processObject(hql, target);
	}
	
	
	/**
	 * Processes an Object of a CQL Query.
	 * 
	 * @param hql
	 * 		The existing HQL query fragment
	 * @param obj
	 * 		The object to process into HQL
	 * @throws QueryProcessingException
	 */
	private static void processObject(StringBuilder hql, Object obj) throws QueryProcessingException {
		String objName = obj.getName();
		hql.append("From ").append(objName);
		if (obj.getAttribute() != null) {
			hql.append(" where ");
			processAttribute(hql, obj.getAttribute());
		}
		if (obj.getAssociation() != null) {
			hql.append(" where ");
			processAssociation(hql, objName, obj.getAssociation());
		}
		if (obj.getGroup() != null) {
			hql.append(" where ");
			processGroup(hql, objName, obj.getGroup());
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
	private static void processAttribute(StringBuilder hql, Attribute attrib) throws QueryProcessingException {
		// hql.append(objAlias).append('.').append(attrib.getName());
		hql.append(attrib.getName());
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
	private static void processAssociation(StringBuilder hql, String parentName, Association assoc) throws QueryProcessingException {
		// get the role name of the association
		String roleName = getRoleName(parentName, assoc);
		if (roleName == null) {
			// still null?? no association to the object!
			throw new QueryProcessingException("Association from type " + parentName + 
				" to type " + assoc.getName() + " does not exist.  Use only direct associations");
		}
		// make an HQL subquery for the object
		hql.append(roleName).append(" in (");
		processObject(hql, assoc);
		hql.append(")");
	}
	
	
	/**
	 * Processes a Group of a CQL Query.
	 * 
	 * @param hql
	 * 		The existing HQL query fragment
	 * @param parentName
	 * 		The type name of the parent object
	 * @param group
	 * 		The group to process into HQL
	 * @throws QueryProcessingException
	 */
	private static void processGroup(StringBuilder hql, String parentName, Group group) throws QueryProcessingException {
		String logic = convertLogicalOperator(group.getLogicRelation());
		
		// flag indicating a logic clause is needed before adding further query parts
		boolean logicClauseNeeded = false;
		
		// attributes
		if (group.getAttribute() != null) {
			for (int i = 0; i < group.getAttribute().length; i++) {
				logicClauseNeeded = true;
				processAttribute(hql, group.getAttribute(i));
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
				processAssociation(hql, parentName, group.getAssociation(i));
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
				processGroup(hql, parentName, group.getGroup(i));
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
			
			// search the fields of the right type
			Field[] typedFields = getFieldsOfType(parentClass, associationTypeName);
			if (typedFields.length == 1) {
				// found one and only one field
				roleName = typedFields[0].getName();
			} else if (typedFields.length > 1) {
				// more than one association found
				throw new QueryProcessingException("Association from " + parentClass.getName() + 
					" to " + associationTypeName + " is ambiguous: Specify a role name");
			}
			
			if (roleName == null) {
				// search for a setter method
				Method[] setters = getSettersForType(parentClass, associationTypeName);
				if (setters.length == 1) {
					String temp = setters[0].getName().substring(3);
					if (temp.length() == 1) {
						roleName = String.valueOf(Character.toLowerCase(temp.charAt(0)));
					} else {
						roleName = String.valueOf(Character.toLowerCase(temp.charAt(0))) 
							+ temp.substring(1);
					}
				} else if (setters.length > 1) {
					// more than one association found
					throw new QueryProcessingException("Association from " + parentClass.getName() + 
						" to " + associationTypeName + " is ambiguous: Specify a role name");
				}
			}
		}
		return roleName;
	}
	
	
	/**
	 * Gets all fields from a class and it's superclasses of a given type
	 * 
	 * @param clazz
	 * 		The class to explore for typed fields
	 * @param typeName
	 * 		The name of the type to search for
	 * @return
	 */
	private static Field[] getFieldsOfType(Class clazz, String typeName) {
		Set allFields = new HashSet();
		Class checkClass = clazz;
		while (checkClass != null) {
			Field[] classFields = checkClass.getDeclaredFields();
			Collections.addAll(allFields, classFields);
			checkClass = checkClass.getSuperclass();
		}
		List namedFields = new ArrayList();
		Iterator fieldIter = allFields.iterator();
		while (fieldIter.hasNext()) {
			Field field = (Field) fieldIter.next();
			if (field.getType().getName().equals(typeName)) {
				namedFields.add(field);
			}
		}
		Field[] fieldArray = new Field[namedFields.size()];
		namedFields.toArray(fieldArray);
		return fieldArray;
	}
	
	
	private static Method[] getSettersForType(Class clazz, String typeName) {
		Set allMethods = new HashSet();
		Class checkClass = clazz;
		while (checkClass != null) {
			Method[] classMethods = checkClass.getDeclaredMethods();
			for (int i = 0; i < classMethods.length; i++) {
				Method current = classMethods[i];
				if (current.getName().startsWith("set")) {
					if (Modifier.isPublic(current.getModifiers())) {
						Class[] paramTypes = current.getParameterTypes();
						if (paramTypes.length == 1) {
							if (paramTypes[0].getName().equals(typeName)) {
								allMethods.add(current);
							}
						}
					}
				}
			}
			checkClass = checkClass.getSuperclass();
		}
		Method[] methodArray = new Method[allMethods.size()];
		allMethods.toArray(methodArray);
		return methodArray;
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
