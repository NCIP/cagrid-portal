package gov.nih.nci.cagrid.sdkquery4.processor;

import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Group;
import gov.nih.nci.cagrid.cqlquery.LogicalOperator;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlquery.QueryModifier;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.sdkquery4.beans.domaininfo.DomainTypesInformation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/** 
 *  CQL2ParameterizedHQL
 *  Converter utility to turn CQL into HQL using positional parameters 
 *  compatible with Hibernate 3.2.0ga for use with caCORE SDK 4
 * 
 * @author David Ervin
 * 
 * @created Mar 2, 2007 10:26:47 AM
 * @version $Id: CQL2ParameterizedHQL.java,v 1.6 2008-02-06 15:44:04 dervin Exp $ 
 */
public class CQL2ParameterizedHQL {
    public static final String TARGET_ALIAS = "__TargetAlias__";
    
    private static Logger LOG = Logger.getLogger(CQL2ParameterizedHQL.class);
	
    // maps a CQL predicate to its HQL string representation 
	private Map<Predicate, String> predicateValues = null;
    
    private DomainTypesInformationUtil typesInfoUtil = null;
    private RoleNameResolver roleNameResolver = null;
    private boolean caseInsensitive;
    
    
    public CQL2ParameterizedHQL(DomainTypesInformation typesInfo, 
        RoleNameResolver roleNameResolver, boolean caseInsensitive) throws IOException, ClassNotFoundException {
        this.typesInfoUtil = new DomainTypesInformationUtil(typesInfo);
        this.roleNameResolver = roleNameResolver;
        this.caseInsensitive = caseInsensitive;
        initPredicateValues();
    }
    
    
    private void initPredicateValues() {
        predicateValues = new HashMap<Predicate, String>();
        predicateValues.put(Predicate.EQUAL_TO, "=");
        predicateValues.put(Predicate.GREATER_THAN, ">");
        predicateValues.put(Predicate.GREATER_THAN_EQUAL_TO, ">=");
        predicateValues.put(Predicate.LESS_THAN, "<");
        predicateValues.put(Predicate.LESS_THAN_EQUAL_TO, "<=");
        predicateValues.put(Predicate.LIKE, "LIKE");
        predicateValues.put(Predicate.NOT_EQUAL_TO, "!=");
        predicateValues.put(Predicate.IS_NOT_NULL, "is not null");
        predicateValues.put(Predicate.IS_NULL, "is null");
    }
    
	
	/**
	 * Converts CQL to parameterized HQL suitable for use with 
     * Hibernate v3.2.0ga and the caCORE SDK version 4.0
	 * 
	 * @param query
	 * 		The query to convert
	 * @return
	 * 		A parameterized HQL Query representing the CQL query
	 * @throws QueryProcessingException
	 */
	public ParameterizedHqlQuery convertToHql(CQLQuery query) throws QueryProcessingException {
		// create a string builder to build up the HQL
		StringBuilder rawHql = new StringBuilder();
        // create the list in which parameters will be placed
        List<java.lang.Object> parameters = new LinkedList<java.lang.Object>();
        // determine if the target has subclasses
        List<String> subclasses = typesInfoUtil.getSubclasses(query.getTarget().getName());
        boolean hasSubclasses = !(subclasses == null || subclasses.size() == 0);
        LOG.debug(query.getTarget().getName() 
            + (hasSubclasses ? " has " + subclasses.size() + " subclasses" : " has no subclasse"));
		processTarget(query.getTarget(), rawHql, parameters, hasSubclasses);
		if (query.getQueryModifier() != null) {
			handleQueryModifier(query.getQueryModifier(), rawHql);
		}
        ParameterizedHqlQuery hqlQuery = new ParameterizedHqlQuery(rawHql.toString(), parameters);
		return hqlQuery;
	}
	
	
	/**
	 * Applies query modifiers to the HQL query
	 * 
	 * @param mods
	 * 		The modifiers to apply
	 * @param hql
	 * 		The HQL to apply the modifications to
	 */
	private void handleQueryModifier(QueryModifier mods, StringBuilder hql) {
		StringBuilder prepend = new StringBuilder();
		if (mods.isCountOnly()) {
			prepend.append("select count(");
			if (mods.getDistinctAttribute() != null) {
				prepend.append("distinct ").append(mods.getDistinctAttribute());
			} else {
				prepend.append('*');
			}
			prepend.append(')');
		} else {
			prepend.append("select ");
			if (mods.getDistinctAttribute() != null) {
				prepend.append("distinct ").append(mods.getDistinctAttribute());
			} else {
				for (int i = 0; i < mods.getAttributeNames().length; i++) {
					prepend.append(mods.getAttributeNames(i));
					if (i + 1 < mods.getAttributeNames().length) {
						prepend.append(", ");
					}
				}
			}
		}
		
		prepend.append(' ');
		
		hql.insert(0, prepend.toString());
	}
	
	
	/**
	 * Processes the target object of a CQL query
	 * 
	 * @param target
	 * 		The target of a CQL query
	 * @param hql
	 * 		The hql string builder to append to
     * @param parameters
     *      The list of positional parameter values
	 * @param avoidSubclasses
	 * 		A flag to indicate the target has subclasses, which we should not return
	 * @throws QueryProcessingException
	 */
	private void processTarget(Object target, StringBuilder hql, List<java.lang.Object> parameters,
		boolean avoidSubclasses) throws QueryProcessingException {
		LOG.debug("Processing target " + target.getName());
        
        // the stack of associations processed at the current depth of the query
		List<String> associationTrace = new LinkedList<String>();
        associationTrace.add(TARGET_ALIAS);
		
        // start the query
		hql.append("From ").append(target.getName()).append(" as ").append(TARGET_ALIAS).append(' ');
		
		if (target.getAssociation() != null) {
			hql.append("where ");
			processAssociation(target.getAssociation(), hql, parameters, associationTrace, target.getName());
		}
		if (target.getAttribute() != null) {
			hql.append("where ");
			processAttribute(target.getAttribute(), hql, parameters, associationTrace, target.getName());
		}
		if (target.getGroup() != null) {
			hql.append("where ");
			processGroup(target.getGroup(), hql, parameters, associationTrace, target.getName());
		}
		
		if (avoidSubclasses) {
			boolean mustAddWhereClause = 
				target.getAssociation() == null
				&& target.getAttribute() == null
				&& target.getGroup() == null;
			if (mustAddWhereClause) {
				hql.append(" where ");
			} else {
				hql.append(" and ");
			}
			hql.append(TARGET_ALIAS).append(".class = ?");
            // 0 is the targeted class, 1 is the first subclass, 2 is the next...
            parameters.add(Integer.valueOf(0));
		}
	}
	
	
	/**
	 * Processes a CQL query attribute into HQL
	 * 
	 * @param attribute
	 * 		The CQL attribute
	 * @param hql
	 * 		The HQL statement fragment
     * @param parameters
     *      The positional parameters list
	 * @param associationTrace
	 * 		The trace of associations
	 * @param objectClassName
	 * 		The class name of the object to which this association belongs
	 * @throws QueryProcessingException
	 */
	private void processAttribute(Attribute attribute, StringBuilder hql, List<java.lang.Object> parameters, 
		List<String> associationTrace, String objectClassName) throws QueryProcessingException {
        LOG.debug("Processing attribute " + objectClassName + "." + attribute.getName());
        
        // determine the Java type of the field being processed
        String attributeFieldType = typesInfoUtil.getAttributeJavaType(objectClassName, attribute.getName());
        if (attributeFieldType == null) {
            throw new QueryProcessingException("Field type of " 
                + objectClassName + "." + attribute.getName() + " could not be determined");
        }
        LOG.debug("Attribute found to be of type " + attributeFieldType);
        
        // determine some flags needed for proper query construction
		boolean isBoolAttribute = attributeFieldType.equals(Boolean.class.getName()); 
		boolean unaryPredicate = attribute.getPredicate().equals(Predicate.IS_NOT_NULL)
			|| attribute.getPredicate().equals(Predicate.IS_NULL);
		
        // build the association trace, if applicable
		String trace = associationTrace.size() != 0 ? buildAssociationTrace(associationTrace) : null;
		
        // construct the query fragment
        // TODO: does Hibernate 3.2.0ga not allow lower() for booleans?  
        // If it allows it, can get rid of the isBoolAttribute checking
		if (caseInsensitive && !isBoolAttribute) {
			hql.append("lower(");
		}
		if (trace != null) {
			hql.append(trace).append('.');
		}
		hql.append(attribute.getName());
		if (caseInsensitive && !isBoolAttribute) {
			hql.append(')');
		}
		hql.append(' ');
		String predicateAsString = predicateValues.get(attribute.getPredicate());
		if (!unaryPredicate) {
			hql.append(predicateAsString).append(' ');
            // TODO: will lower() work with positional parameters?
			if (caseInsensitive) {
				hql.append("lower(");
			}
            
            // add a placeholder parameter to the HQL query
			hql.append('?');
            // include a positional parameter value
            parameters.add(valueToObject(attributeFieldType, 
                caseInsensitive ? attribute.getValue().toLowerCase() : attribute.getValue()));
			
			if (caseInsensitive) {
				hql.append(')');
			}
		} else {
			hql.append(predicateAsString);
		}
	}
	
	
	/**
	 * Processes CQL associations into HQL
	 * 
	 * @param association
	 * 		The CQL association
	 * @param hql
	 * 		The HQL fragment which will be edited
     * @param parameters
     *      The positional HQL query parameters
	 * @param associationTrace
	 * 		The trace of associations
	 * @param sourceClassName
	 * 		The class name of the type to which this association belongs
	 * @throws QueryProcessingException
	 */
	private void processAssociation(Association association, StringBuilder hql, List<java.lang.Object> parameters, 
        List<String> associationTrace, String sourceClassName) throws QueryProcessingException {
        LOG.debug("Processing association " + sourceClassName + " to " + association.getName());
        
		String roleName = roleNameResolver.getRoleName(sourceClassName, association);
		if (roleName == null) {
			// still null?? no association to the object!
            // TODO: should probably be malformed query exception
			throw new QueryProcessingException("Association from type " + sourceClassName + 
				" to type " + association.getName() + " does not exist.  Use only direct associations");
		}
        
		// add the role name to the association trace
		associationTrace.add(roleName);
        LOG.debug("Role name determined to be " + roleName);
		
        boolean simpleNullCheck = true;
		if (association.getAssociation() != null) {
            simpleNullCheck = false;
			processAssociation(association.getAssociation(), hql, parameters, associationTrace, association.getName());
		}
		if (association.getAttribute() != null) {
            simpleNullCheck = false;
			processAttribute(association.getAttribute(), hql, parameters, associationTrace, association.getName());
		}
		if (association.getGroup() != null) {
            simpleNullCheck = false;
			processGroup(association.getGroup(), hql, parameters, associationTrace, association.getName());
		}
		
        if (simpleNullCheck) {
            // query is checking for the association to exist and be non-null
            hql.append(buildAssociationTrace(associationTrace)).append(".id is not null ");
        }
        
		// remove this association from the trace
		associationTrace.remove(associationTrace.size() - 1);
	}
	
	
	/**
	 * Processes a CQL group into HQL
	 * 
	 * @param group
	 * 		The CQL Group
	 * @param hql
	 * 		The HQL fragment which will be edited
     * @param parameters
     *      The positional HQL query parameters
	 * @param associationTrace
	 * 		The trace of associations
	 * @param sourceClassName
	 * 		The class to which this group belongs
	 * @throws QueryProcessingException
	 */
	private void processGroup(Group group, StringBuilder hql, List<java.lang.Object> parameters,
        List<String> associationTrace, String sourceClassName) throws QueryProcessingException {
        LOG.debug("Processing group on " + sourceClassName);
        
		String logic = convertLogicalOperator(group.getLogicRelation());
		boolean mustAddLogic = false;
		
		// open the group
		hql.append('(');
		
		if (group.getAssociation() != null) {
			for (int i = 0; i < group.getAssociation().length; i++) {
				mustAddLogic = true;
				processAssociation(group.getAssociation(i), hql, parameters, associationTrace, sourceClassName);
				if (i + 1 < group.getAssociation().length) {
					hql.append(' ').append(logic).append(' ');
				}
			}
		}
		if (group.getAttribute() != null) {
			if (mustAddLogic) {
				hql.append(' ').append(logic).append(' ');
			}
			for (int i = 0; i < group.getAttribute().length; i++) {
				mustAddLogic = true;
				processAttribute(group.getAttribute(i), hql, parameters, associationTrace, sourceClassName);
				if (i + 1 < group.getAttribute().length) {
					hql.append(' ').append(logic).append(' ');
				}
			}
		}
		if (group.getGroup() != null) {
			if (mustAddLogic) {
				hql.append(' ').append(logic).append(' ');
			}
			for (int i = 0; i < group.getGroup().length; i++) {
				processGroup(group.getGroup(i), hql, parameters, associationTrace, sourceClassName);
				if (i + 1 < group.getGroup().length) {
					hql.append(' ').append(logic).append(' ');
				}
			}
		}
		
		// close the group
		hql.append(')');
	}
	
	
	/**
	 * Converts a logical operator to its HQL string equiavalent.
	 * 
	 * @param op
	 * 		The logical operator to convert
	 * @return
	 * 		The CQL logical operator as HQL
	 */
	private String convertLogicalOperator(LogicalOperator op) throws QueryProcessingException {
		if (op.getValue().equals(LogicalOperator._AND)) {
			return "AND";
		} else if (op.getValue().equals(LogicalOperator._OR)) {
			return "OR";
		}
		throw new QueryProcessingException("Logical operator '" + op.getValue() + "' is not recognized.");
	}
	
	
	/**
	 * Builds a trace of association names from a list of those names
	 * 
	 * @param associationTrace
	 * @return
	 * 		An HQL fragment
	 */
	private String buildAssociationTrace(List<String> associationTrace) {
		// build up what the trace to this association looks like
		StringBuilder trace = new StringBuilder();
		Iterator traceIter = associationTrace.iterator();
		while (traceIter.hasNext()) {
			trace.append(traceIter.next());
			if (traceIter.hasNext()) {
				trace.append('.');
			}
		}
		return trace.toString();
	}
    
    
    // uses the class type to convert the value to a typed object
    private java.lang.Object valueToObject(String className, String value) throws QueryProcessingException {
        LOG.debug("Converting \"" + value + "\" to object of type " + className);
        if (className.equals(String.class.getName())) {
            return value;
        }
        // TODO: Integer, Long, and Double have the same valueOf(String s) method.  Could use reflection
        if (className.equals(Integer.class.getName())) {
            return Integer.valueOf(value);
        }
        if (className.equals(Long.class.getName())) {
            return Long.valueOf(value);
        }
        if (className.equals(Double.class.getName())) {
            return Double.valueOf(value);
        }
        if (className.equals(Character.class.getName())) {
            if (value.length() == 1) {
                return Character.valueOf(value.charAt(0));
            } else {
                throw new QueryProcessingException("The value \"" + value + "\" of length " 
                    + value.length() + " is not a valid character");
            }
        }
        // TODO: Date
        
        throw new QueryProcessingException("No conversion for type " + className);
    }
}
