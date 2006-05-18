package gov.nih.nci.cagrid.data.cql.validation;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLAttributeMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Group;
import gov.nih.nci.cagrid.cqlquery.LogicalOperator;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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
public class ObjectWalkingCQLValidator implements CQLValidator {
	
	private static Set predicateValues = null;

	public void validateCql(CQLQuery query, DomainModel model, Project project) throws MalformedQueryException {
		// start by validating the structure of the query
		validateStructure(query);
		
		// validate the query against the data service's Domain Model
		validateQueryTarget(query, model);
		validateObjectModel(query.getTarget(), model, project);
	}
	
	
	private void validateStructure(CQLQuery query) throws MalformedQueryException {
		validateObjectStructure(query.getTarget());
	}
	
	
	private void validateObjectStructure(Object obj) throws MalformedQueryException {
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
	
	
	private void validateQueryTarget(CQLQuery query, DomainModel model) throws MalformedQueryException {
		UMLClass targetClass = getUmlClass(query.getTarget().getName(), model);
		if (targetClass == null) {
			throw new MalformedQueryException("Query target " + query.getTarget().getName() + " is not a valid target in the domain model");
		}
	}
	
	
	private void validateObjectModel(Object obj, DomainModel model, Project proj) throws MalformedQueryException {
		// verify the object exists in the project
		UMLClassMetadata classMd = getUmlClassMetadata(obj.getName(), proj);
		if (classMd == null) {
			throw new MalformedQueryException("No object " + obj.getName() + " found in the project");
		}
		
		if (obj.getAttribute() != null) {
			validateAttributeModel(obj.getAttribute(), classMd);
		}
	}
	
	
	private void validateAttributeModel(Attribute attrib, UMLClassMetadata classMd) throws MalformedQueryException {
		// verify the attribute exists
		UMLAttributeMetadata attribMd = getUmlAttributeMetadata(attrib.getName(), classMd);
		if (attribMd == null) {
			throw new MalformedQueryException("Attribute " + attrib.getName() + " is not defined for the class " + classMd.getFullyQualifiedName());
		}
		// if the predicate is a binary operator, verify the value is of the correct type
		if (attrib.getPredicate() != null && 
			!(attrib.getPredicate().getValue().equals(Predicate._IS_NOT_NULL) ||
			attrib.getPredicate().getValue().equals(Predicate._IS_NULL))) {
			String value = attrib.getValue();
			String dataType = attribMd.getDataElement().getValueDomain().getLongName();
			try {
				if (dataType.equals(Integer.class.getName())) {
					Integer.valueOf(value);
					// TODO: max / min permissable values
				} else if (dataType.equals(Long.class.getName())) {
					Long.valueOf(value);
				} else if (dataType.equals(Date.class.getName())) {
					DateFormat.getInstance().parse(value);
				} else if (dataType.equals(Boolean.class.getName())) {
					Boolean.valueOf(value);
				} else if (dataType.equals(Character.class.getName())) {
					if (value.length() != 1) {
						throw new MalformedQueryException("Characters can only be of length 1, not " + value.length());
					}
					Character.valueOf(value.charAt(0));
				}
			} catch (Exception ex) {
				throw new MalformedQueryException("Attribute " + attrib.getName() + " queried with value " + value + ", but type is " + dataType, ex);
			}
		}
	}
	
	
	private UMLClass getUmlClass(String className, DomainModel model) {
		UMLClass[] allClasses = model.getExposedUMLClassCollection().getUMLClass();
		for (int i = 0; allClasses != null && i < allClasses.length; i++) {
			if (allClasses[i].getClassname().equals(className));
			return allClasses[i];
		}
		return null;
	}
	
	
	private UMLClassMetadata getUmlClassMetadata(String className, Project proj) {
		Iterator classMdIter = proj.getUMLClassMetadataCollection().iterator();
		while (classMdIter.hasNext()) {
			UMLClassMetadata md = (UMLClassMetadata) classMdIter.next();
			if (md.getFullyQualifiedName().equals(className)) {
				return md;
			}
		}
		return null;
	}
	
	
	private UMLAttributeMetadata getUmlAttributeMetadata(String attribName, UMLClassMetadata classMd) {
		Iterator attribMdIter = classMd.getUMLAttributeMetadataCollection().iterator();
		while (attribMdIter.hasNext()) {
			UMLAttributeMetadata attribMd = (UMLAttributeMetadata) attribMdIter.next();
			if (attribMd.getName().equals(attribName)) {
				return attribMd;
			}
		}
		return null;
	}


	// main method for testing only
	public static void main(String[] args) {
		if (args.length != 3) {
			System.err.println("usage: " + ObjectWalkingCQLValidator.class.getName() + " <cqlDocumentFilename> <domainModelFilename> <projectFilename>");
			System.exit(1);
		}
		ObjectWalkingCQLValidator validator = new ObjectWalkingCQLValidator();
		String cqlFilename = args[0];
		String domainModelFilename = args[1];
		String projectFilename = args[2];
		try {
			CQLQuery query = (CQLQuery) Utils.deserializeDocument(cqlFilename, CQLQuery.class);
			DomainModel model = (DomainModel) Utils.deserializeDocument(domainModelFilename, DomainModel.class);
			Project project = (Project) Utils.deserializeDocument(projectFilename, Project.class);
			validator.validateCql(query, model, project);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
