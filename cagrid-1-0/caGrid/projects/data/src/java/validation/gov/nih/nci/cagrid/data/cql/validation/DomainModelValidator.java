package gov.nih.nci.cagrid.data.cql.validation;

import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Group;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.utilities.DomainModelUtils;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.common.Enumeration;
import gov.nih.nci.cagrid.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.common.ValueDomain;
import gov.nih.nci.cagrid.metadata.common.ValueDomainEnumerationCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * DomainModelValidator Does the dirty work of validating a CQL query against a
 * domain model
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 26, 2006
 * @version $Id$
 */
public class DomainModelValidator implements CqlDomainValidator {

	public DomainModelValidator() {

	}


	public void validateDomainModel(CQLQuery query, DomainModel model) throws MalformedQueryException {
		validateQueryTarget(query, model);
		validateObjectModel(query.getTarget(), model);
	}


	private void validateQueryTarget(CQLQuery query, DomainModel model) throws MalformedQueryException {
		UMLClass targetClass = getUmlClass(query.getTarget().getName(), model);
		if (targetClass == null) {
			throw new MalformedQueryException("Query target " + query.getTarget().getName()
				+ " is not a valid target in the domain model");
		}
	}


	private void validateObjectModel(Object obj, DomainModel model) throws MalformedQueryException {
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
			throw new MalformedQueryException("Attribute '" + attrib.getName() + "' is not defined for the class "
				+ classMd.getClassName());
		}
		// verify the data type being used is compatible
		validateAttributeDataType(attrib, attribMd);
	}


	private void validateAttributeDataType(Attribute attrib, UMLAttribute attribMetadata)
		throws MalformedQueryException {
		// if the predicate is a binary operator, verify the value is of the correct type
		if (attrib.getPredicate() != null
			&& !(attrib.getPredicate().getValue().equals(Predicate._IS_NOT_NULL) || attrib.getPredicate().getValue()
				.equals(Predicate._IS_NULL))) {
			String valueAsString = attrib.getValue().toString();
			//check datatype name
			String datatype = attribMetadata.getDataTypeName();
			DataTypeValidator.validate(valueAsString, datatype);
			//check enumeration values
			ValueDomain valueDomain = attribMetadata.getValueDomain();
			if (valueDomain != null) {
				ValueDomainEnumerationCollection enumerationCollection = valueDomain.getEnumerationCollection();
				if (enumerationCollection != null && enumerationCollection.getEnumeration() != null
					&& enumerationCollection.getEnumeration().length > 0) {
					Enumeration[] enumeration = enumerationCollection.getEnumeration();
					Set permValues = new HashSet();
					for (int i = 0; i < enumeration.length; i++) {
						Enumeration e = enumeration[i];
						permValues.add(e.getPermissibleValue());
					}
					if (!permValues.contains(valueAsString)) {
						throw new MalformedQueryException("Attribute '" + attrib.getName()
							+ "' defines a permissible value enumeration, and the value'" + valueAsString
							+ "' is not permissible.");
					}
				}
			}
		}
	}


	private void validateAssociationModel(Object current, Association assoc, DomainModel model)
		throws MalformedQueryException {
		// determine if an association exists between the current
		// and association object
		String roleName = assoc.getRoleName();
		// UMLAssociation[] associations = getUmlAssociations(current.getName(), model);
		UMLAssociation[] associations = getAllAssociationsFromSource(current.getName(), model);
		boolean associationFound = false;
		for (int i = 0; i < associations.length; i++) {
			UMLAssociation assocMd = associations[i];
			UMLClass targetClassMd = DomainModelUtils.getReferencedUMLClass(model, assocMd
				.getTargetUMLAssociationEdge().getUMLAssociationEdge().getUMLClassReference());
			if (targetClassMd != null) {
				String targetClassName = targetClassMd.getPackageName() + "." + targetClassMd.getClassName();
				if (targetClassName.equals(assoc.getName())) {
					if (associationFound) {
						// no role name, and already found an association
						// of the same type
						throw new MalformedQueryException("The association from " + current.getName() + " to "
							+ assoc.getName() + " is ambiguous without a role name");
					}
					if (roleName != null
						&& assocMd.getTargetUMLAssociationEdge().getUMLAssociationEdge().getRoleName().equals(roleName)) {
						// association found with specidied role name
						associationFound = true;
						break;
					} else if (roleName == null) {
						// an association of the right type found, but no role
						// name to compare
						// so association is found, but must check for ambiguity
						associationFound = true;
					}
				}
			}
		}
		if (!associationFound) {
			throw new MalformedQueryException("No association from " + current.getName() + " to " + assoc.getName()
				+ " with role name " + assoc.getRoleName());
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
			String fqn = allClasses[i].getPackageName().trim();
			if (!fqn.equals("")) {
				fqn += "." + allClasses[i].getClassName();
			}

			if (fqn.equals(className)) {
				return allClasses[i];
			}
		}
		return null;
	}


	private UMLAttribute getUmlAttribute(String attribName, UMLClass classMd) {
		UMLAttribute[] attribs = classMd.getUmlAttributeCollection().getUMLAttribute();
		for (int i = 0; attribs != null && i < attribs.length; i++) {
			UMLAttribute attrib = attribs[i];
			String fullAttribName = attrib.getName();
			int shortIndex = fullAttribName.indexOf(':');
			String shortAttribName = fullAttribName.substring(shortIndex + 1);
			if (shortAttribName.equals(attribName)) {
				return attrib;
			}
		}
		return null;
	}


	private UMLAssociation[] getAllAssociationsFromSource(String sourceClass, DomainModel model) {
		String[] classNames = getClassHierarchy(sourceClass, model);
		Set associations = new HashSet();
		for (int i = 0; i < classNames.length; i++) {
			Collections.addAll(associations, (java.lang.Object[]) getUmlAssociations(classNames[i], model));
		}
		UMLAssociation[] assocArray = new UMLAssociation[associations.size()];
		associations.toArray(assocArray);
		return assocArray;
	}


	private String[] getClassHierarchy(String className, DomainModel model) {
		UMLClass[] superclasses = DomainModelUtils.getAllSuperclasses(model, className);
		String[] names = new String[superclasses.length + 1];
		for (int i = 0; i < superclasses.length; i++) {
			names[i] = superclasses[i].getPackageName() + "." + superclasses[i].getClassName();
		}
		names[names.length - 1] = className;
		return names;
	}


	private UMLAssociation[] getUmlAssociations(String sourceClass, DomainModel model) {
		List associations = new ArrayList();
		if (model.getExposedUMLAssociationCollection() != null
			&& model.getExposedUMLAssociationCollection().getUMLAssociation() != null) {
			for (int i = 0; i < model.getExposedUMLAssociationCollection().getUMLAssociation().length; i++) {
				UMLAssociation assoc = model.getExposedUMLAssociationCollection().getUMLAssociation(i);

				UMLClass referencedUMLClass = DomainModelUtils.getReferencedUMLClass(model, assoc
					.getSourceUMLAssociationEdge().getUMLAssociationEdge().getUMLClassReference());
				if (referencedUMLClass != null) {
					String refClassName = referencedUMLClass.getPackageName() + "." + referencedUMLClass.getClassName();
					if (refClassName.equals(sourceClass)) {
						associations.add(assoc);
					}
				}
			}
		}
		UMLAssociation[] array = new UMLAssociation[associations.size()];
		associations.toArray(array);
		return array;
	}
	
	
	public static void main(String[] args) {
		DomainModelValidator validator = new DomainModelValidator();
		if (args.length < 2) {
			System.err.println("USAGE: domainModel.xml cqlQuery1.xml [cqlQuery2.xml .. cqlQueryN.xml]");
			System.exit(1);
		}
		DomainModel model = null;
		try {
			model = MetadataUtils.deserializeDomainModel(new FileReader(args[0]));
		} catch (Exception ex) {
			System.err.println("Error deserializing domain model file: " + args[0]);
			System.exit(1);
		}
		for (int i = 1; i < args.length; i++) {
			CQLQuery query = null;
			try {
				
			} catch (Exception ex) {
				System.err.println("Error deserializign CQL query file: " + args[i]);
				ex.printStackTrace();
				System.exit(1);
			}
			try {
				validator.validateDomainModel(query, model);
			} catch (MalformedQueryException ex) {
				System.err.println("Query " + args[i] + " is not valid");
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}
}
