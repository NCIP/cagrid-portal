package gov.nih.nci.cagrid.data.cql.validation;

import gov.nih.nci.cadsr.domain.ValueDomain;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Group;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * DomainModelValidator Does the dirty work of validating a CQL query against a
 * domain model
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 26, 2006
 * @version $Id$
 */
public class DomainModelValidator {

	DomainModelValidator() {

	}


	public void validateDomain(CQLQuery query, DomainModel model) throws MalformedQueryException {
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
			&& !(attrib.getPredicate().getValue().equals(Predicate._IS_NOT_NULL) 
				|| attrib.getPredicate().getValue().equals(Predicate._IS_NULL))) {
			String value = attrib.getValue();
			ValueDomain valueDomain = attribMetadata.getValueDomain();
			if (valueDomain != null) {
				String dataType = valueDomain.getDatatypeName();
				try {
					if (dataType.equals(Integer.class.getName())) {
						int i = Integer.valueOf(value).intValue();
						// test min value
						if (valueDomain.getLowValueNumber() != null) {
							int low = Integer.valueOf(valueDomain.getLowValueNumber()).intValue();
							if (i < low) {
								throw new MalformedQueryException("Attribute " + attrib.getName() + " queried with value: "
									+ value + "; minimum accepted value is " + low);
							}
						}
						// test high
						if (valueDomain.getHighValueNumber() != null) {
							int high = Integer.valueOf(valueDomain.getHighValueNumber()).intValue();
							if (i > high) {
								throw new MalformedQueryException("Attribute " + attrib.getName() + " queried with value: "
									+ value + "; maximum accepted value is " + high);
							}
						}
					} else if (dataType.equals(Long.class.getName())) {
						long i = Long.valueOf(value).longValue();
						// test low value
						if (valueDomain.getLowValueNumber() != null) {
							long low = Long.valueOf(valueDomain.getLowValueNumber()).longValue();
							if (i < low) {
								throw new MalformedQueryException("Attribute " + attrib.getName() + " queried with value: "
									+ value + "; minimum accepted value is " + low);
							}
						}
						// test high
						if (valueDomain.getHighValueNumber() != null) {
							long high = Long.valueOf(valueDomain.getHighValueNumber()).longValue();
							if (i > high) {
								throw new MalformedQueryException("Attribute " + attrib.getName() + " queried with value: "
									+ value + "; maximum accepted value is " + high);
							}
						}
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
					throw new MalformedQueryException("Attribute " + attrib.getName() + " queried with value: " 
						+ value + "; but type is " + dataType, ex);
				}
			} else {
				// TODO: warn no value domain found
			}
		}
	}


	private void validateAssociationModel(Object current, Association assoc, DomainModel model)
		throws MalformedQueryException {
		// determine if an association exists between the current
		// and association object
		String roleName = assoc.getRoleName();
		UMLAssociation[] associations = getUmlAssociations(current.getName(), model);
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
					if (roleName != null && assocMd.getTargetUMLAssociationEdge()
							.getUMLAssociationEdge().getRoleName().equals(roleName)) {
						// association found with specidied role name
						associationFound = true;
						break;
					} else {
						// an association of the right type found, but no role name to compare
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
			int shortIndex = fullAttribName.indexOf(":");
			String shortAttribName = fullAttribName.substring(shortIndex + 1);
			if (shortAttribName.equals(attribName)) {
				return attrib;
			}
		}
		return null;
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
}
