package gov.nih.nci.cagrid.data.cql.validation;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Group;
import gov.nih.nci.cagrid.cqlquery.LogicalOperator;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.util.HashSet;
import java.util.Set;

/** 
 *  ObjectWalkingCQLValidator
 *  CQLValidator that walks through the CQL object model to perform validation
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 18, 2006 
 * @version $Id$ 
 */
public class ObjectWalkingCQLValidator extends CQLValidator {
	
	private static Set predicateValues = null;
	private static DomainModelValidator domainValidator = null;
	
	public void validateStructure(CQLQuery query) throws MalformedQueryException {
		validateObjectStructure(query.getTarget());
	}
	
	
	public void validateDomain(CQLQuery query, DomainModel model) throws MalformedQueryException {
		if (domainValidator == null) {
			domainValidator = new DomainModelValidator();
		}
		// validate the query against the data service's Domain Model
		domainValidator.validateDomain(query, model);
	}
	
	
	private void validateObjectStructure(Object obj) throws MalformedQueryException {
		// ensure name exists
		if (obj.getName() == null) {
			throw new MalformedQueryException("Object does not have a name!");
		}
		
		// count children
		int childCount = 0;
		if (obj.getAssociation() != null) {
			childCount++;
		}
		if (obj.getAttribute() != null) {
			childCount++;
		}
		if (obj.getGroup() != null) {
			childCount++;
		}
		
		if (childCount > 1) {
			throw new MalformedQueryException("Query for Object " + obj.getName() + " has more than one child");
		}
		
		// validate children AFTER validating count to minimize processing
		if (obj.getAttribute() != null) {
			validateAttributeStructure(obj.getAttribute());
		}
		
		if (obj.getAssociation() != null) {
			// associations ARE objects
			validateObjectStructure(obj.getAssociation());
		}
		
		if (obj.getGroup() != null) {
			validateGroupStructure(obj.getGroup());
		}
	}
	
	
	private void validateAttributeStructure(Attribute attr) throws MalformedQueryException {
		// attrbutes require name and value
		if (attr.getName() == null) {
			throw new MalformedQueryException("Attributes must have a name!");
		}
		if (attr.getValue() == null) {
			throw new MalformedQueryException("Attributes must have a value!");
		}
		// predicate is optional, defaults to EQUAL_TO
		if (attr.getPredicate() != null) {
			// use a static set of predicate values for efficiency when checking multiple attributes
			if (predicateValues == null) {
				predicateValues = new HashSet();
				predicateValues.add(Predicate._EQUAL_TO);
				predicateValues.add(Predicate._GREATER_THAN);
				predicateValues.add(Predicate._GREATER_THAN_EQUAL_TO);
				predicateValues.add(Predicate._IS_NOT_NULL);
				predicateValues.add(Predicate._IS_NULL);
				predicateValues.add(Predicate._LESS_THAN);
				predicateValues.add(Predicate._LESS_THAN_EQUAL_TO);
				predicateValues.add(Predicate._LIKE);
				predicateValues.add(Predicate._NOT_EQUAL_TO);
			}
			String predicate = attr.getPredicate().getValue();
			if (!predicateValues.contains(predicate)) {
				throw new MalformedQueryException("The predicate " + predicate + " is not valid");
			}
		}
	}
	
	
	private void validateGroupStructure(Group group) throws MalformedQueryException {
		// check the logical operator
		if (group.getLogicRelation() == null) {
			throw new MalformedQueryException("Groups must have a logical operator!");
		}
		String logic = group.getLogicRelation().getValue();
		if (!logic.equals(LogicalOperator._AND) && !logic.equals(LogicalOperator._OR)) {
			throw new MalformedQueryException("Logical operator " + logic + " is not valid");
		}
		
		// ensure two or more group members
		int groupMembers = 0;
		if (group.getAssociation() != null) {
			groupMembers += group.getAssociation().length;
		}
		if (group.getAttribute() != null) {
			groupMembers += group.getAttribute().length;
		}
		if (group.getGroup() != null) {
			groupMembers += group.getGroup().length;
		}
		
		if (groupMembers < 2) {
			throw new MalformedQueryException("Groups must have two or more members");
		}
		
		// validate the members of the group
		if (group.getAttribute() != null) {
			for (int i = 0; i < group.getAttribute().length; i++) {
				validateAttributeStructure(group.getAttribute(i));
			}
		}
		
		if (group.getAssociation() != null) {
			for (int i = 0; i < group.getAssociation().length; i++) {
				validateObjectStructure(group.getAssociation(i));
			}
		}
		
		if (group.getGroup() != null) {
			for (int i = 0; i < group.getGroup().length; i++) {
				validateGroupStructure(group.getGroup(i));
			}
		}
	}


	// main method for testing only
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("usage: " + ObjectWalkingCQLValidator.class.getName() + " <cqlDocumentFilename> <domainModelFilename>");
			System.exit(1);
		}
		ObjectWalkingCQLValidator validator = new ObjectWalkingCQLValidator();
		String cqlFilename = args[0];
		String domainModelFilename = args[1];
		try {
			CQLQuery query = (CQLQuery) Utils.deserializeDocument(cqlFilename, CQLQuery.class);
			DomainModel model = (DomainModel) Utils.deserializeDocument(domainModelFilename, DomainModel.class);
			validator.validateCql(query, model);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
