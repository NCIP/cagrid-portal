package gov.nih.nci.cagrid.data.utilities;

import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference;

import java.util.HashMap;
import java.util.Map;


/** 
  *  DomainModelUtils
  *  Utilities for walking and manipulating a DomainModel
  * 
  * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
  * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
  * 
  * @created Jun 14, 2006 
  * @version $Id$
 */
public class DomainModelUtils {

	private static Map domainRefedClasses = new HashMap();
	
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
		Map refedClasses = (Map) domainRefedClasses.get(model);
		if (refedClasses == null) {
			// populate class references for this model
			UMLClass[] classes = model.getExposedUMLClassCollection().getUMLClass();
			refedClasses = new HashMap(classes.length);
			for (int i = 0; i < classes.length; i++) {
				UMLClass clazz = classes[i];
				refedClasses.put(clazz.getId(), clazz);
			}
		}
		UMLClass refedClass = (UMLClass) refedClasses.get(reference.getRefid());
		return refedClass;
	}
}
