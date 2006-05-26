package gov.nih.nci.cagrid.data.cql.validation;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Group;
import gov.nih.nci.cagrid.cqlquery.LogicalOperator;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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

	
	public void validateStructure(CQLQuery query) throws MalformedQueryException {
		validateObjectStructure(query.getTarget());
	}
	
	
	public void validateDomain(CQLQuery query, DomainModel model) throws MalformedQueryException {
		// validate the query against the data service's Domain Model
		validateQueryTarget(query, model);
		validateObjectModel(query.getTarget(), model);
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
	
	
	public void validateQueryTarget(CQLQuery query, DomainModel model) throws MalformedQueryException {
		UMLClass targetClass = getUmlClass(query.getTarget().getName(), model);
		if (targetClass == null) {
			throw new MalformedQueryException("Query target " + query.getTarget().getName() + " is not a valid target in the domain model");
		}
	}
	
	
	public void validateObjectModel(Object obj, DomainModel model) throws MalformedQueryException {
		// verify the object exists in the project
		UMLClass classMd = getUmlClass(obj.getName(), model);
		if (classMd == null) {
			throw new MalformedQueryException("No object " + obj.getName() + " found in the project");
		}
		
		if (obj.getAttribute() != null) {
			validateAttributeModel(obj.getAttribute(), classMd);
		}
		
		if (obj.getAssociation() != null) {
			// ensure the association is valid
			validateAssociationModel(obj, obj.getAssociation(), model);
			// step through the association's submodel
			validateObjectModel(obj.getAssociation(), model);
		}
		
		if (obj.getGroup() != null) {
			validateGroupModel(obj, obj.getGroup(), model);
		}
	}
	
	
	private void validateAttributeModel(Attribute attrib, UMLClass classMd) throws MalformedQueryException {
		// verify the attribute exists
		UMLAttribute attribMd = getUmlAttribute(attrib.getName(), classMd);
		if (attribMd == null) {
			throw new MalformedQueryException("Attribute " + attrib.getName() + " is not defined for the class " + classMd.getClassName());
		}
		// if the predicate is a binary operator, verify the value is of the correct type
		if (attrib.getPredicate() != null && 
			!(attrib.getPredicate().getValue().equals(Predicate._IS_NOT_NULL) ||
			attrib.getPredicate().getValue().equals(Predicate._IS_NULL))) {
			String value = attrib.getValue();
			// TODO: evaluate this somehow
			String dataType = attribMd.getSemanticMetadataCollection().getSemanticMetadata(0).getConcept().getLongName();
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
	
	
	private void validateAssociationModel(Object current, Association assoc, DomainModel model) throws MalformedQueryException {
		// determine if an association exists between the current and association object
		String roleName = assoc.getRoleName();
		UMLAssociation[] associations = getUmlAssociations(current.getName(), model);
		boolean associationFound = false;
		for (int i = 0; i < associations.length; i++) {
			UMLAssociation assocMd = associations[i];
			UMLClass targetClassMd = assocMd.getTargetUMLAssociationEdge().getUMLAssociationEdge().getUmlClass().getUMLClass();
			if (targetClassMd.getClassName().equals(assoc.getName())) {
				if (associationFound) {
					// no role name, and already found an association of the same type
					throw new MalformedQueryException("The association from " + current.getName() + " to " + assoc.getName() + " is ambiguous without a role name");
				}
				if (roleName != null) {
					if (assocMd.getTargetUMLAssociationEdge().getUMLAssociationEdge().getRoleName().equals(roleName)) {
						associationFound = true;
						break;
					}
				}
			}
		}
		if (!associationFound) {
			throw new MalformedQueryException("No association from " + current.getName() + " to " + assoc.getName() + " with role name " + assoc.getRoleName());
		}
	}
	
	
	private void validateGroupModel(Object current, Group group, DomainModel model) throws MalformedQueryException {
		if (group.getAttribute() != null) {
			UMLClass classMd = getUmlClass(current.getName(), model);
			for (int i = 0; i < group.getAttribute().length; i++) {
				validateAttributeModel(group.getAttribute(i), classMd);
			}
		}
		
		if (group.getAssociation() != null) {
			for (int i = 0; i < group.getAssociation().length; i++) {
				validateAssociationModel(current, group.getAssociation(i), model);
			}
		}
		
		if (group.getGroup() != null) {
			for (int i = 0; i < group.getGroup().length; i++) {
				validateGroupModel(current, group.getGroup(i), model);
			}
		}
	}
	
	
	private UMLClass getUmlClass(String className, DomainModel model) {
		UMLClass[] allClasses = model.getExposedUMLClassCollection().getUMLClass();
		for (int i = 0; allClasses != null && i < allClasses.length; i++) {
			if (allClasses[i].getClassName().equals(className));
			return allClasses[i];
		}
		return null;
	}
	
	
	private UMLAttribute getUmlAttribute(String attribName, UMLClass classMd) {
		UMLAttribute[] attribs = classMd.getUmlAttributeCollection().getUMLAttribute();
		for (int i = 0; attribs != null && i < attribs.length; i++) {
			UMLAttribute attrib = attribs[i];
			if (attrib.getName().equals(attribName)) {
				return attrib;
			}
		}
		return null;
	}
	
	
	private UMLAssociation[] getUmlAssociations(String sourceClass, DomainModel model) {
		List associations = new ArrayList();
		if (model.getExposedUMLAssociationCollection() != null &&
			model.getExposedUMLAssociationCollection().getUMLAssociation() != null) {
			for (int i = 0; i < model.getExposedUMLAssociationCollection().getUMLAssociation().length; i++) {
				UMLAssociation assoc = model.getExposedUMLAssociationCollection().getUMLAssociation(i);
				if (assoc.getSourceUMLAssociationEdge().getUMLAssociationEdge()
					.getUmlClass().getUMLClass().getClassName().equals(sourceClass)) {
					associations.add(assoc);
				}
			}
		}
		UMLAssociation[] array = new UMLAssociation[associations.size()];
		associations.toArray(array);
		return array;
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
