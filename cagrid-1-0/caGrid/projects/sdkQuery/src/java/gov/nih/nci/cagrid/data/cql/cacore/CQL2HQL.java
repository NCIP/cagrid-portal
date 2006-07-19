package gov.nih.nci.cagrid.data.cql.cacore;

import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.data.QueryProcessingException;

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
	 * Translates a CQL query into an HQL string.
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
	 * Converts a predicate to its string equivalent.
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
}
