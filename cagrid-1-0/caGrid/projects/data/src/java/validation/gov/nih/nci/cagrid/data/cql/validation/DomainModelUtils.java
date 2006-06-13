package gov.nih.nci.cagrid.data.cql.validation;

import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference;


/**
 * @author oster
 * 
 */
public class DomainModelUtils {

	/**
	 * Return the UMLClass from the DomainModel's exposed UML Classes if the
	 * UMLClass with an id exists which is equal to the refid of the
	 * UMLClassReference. In all other cases, null is returned.
	 * 
	 * @param model
	 *            the DomainModel to look in
	 * @param reference
	 *            the UMLClassReference to the UMLClass to look for in the model
	 * @return null or the referenced UMLClass
	 */
	public static UMLClass getReferencedUMLClass(DomainModel model, UMLClassReference reference) {
		if (model == null) {
			return null;
		}
		UMLClass[] classes = model.getExposedUMLClassCollection().getUMLClass();
		for (int i = 0; i < classes.length; i++) {
			UMLClass clazz = classes[i];
			if (clazz.getId().equals(reference.getRefid())) {
				return clazz;
			}
		}
		return null;

	}
}
