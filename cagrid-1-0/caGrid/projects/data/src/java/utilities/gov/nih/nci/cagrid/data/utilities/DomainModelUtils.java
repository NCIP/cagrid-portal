package gov.nih.nci.cagrid.data.utilities;

import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference;
import gov.nih.nci.cagrid.metadata.dataservice.UMLGeneralization;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


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
	
	
	/**
	 * Gets all superclasses of the specified UMLCLass
	 * 
	 * @param model
	 * 		The domain model to search for superclasses
	 * @param umlClass
	 * 		The class to retrieve superclasses of
	 * @return
	 */	
	public static UMLClass[] getAllSuperclasses(DomainModel model, UMLClass umlClass) {
		return getAllSuperclasses(model, umlClass.getPackageName() + "." + umlClass.getClassName());
	}
	
	
	/**
	 * Gets all superclasses of the specified UMLCLass
	 * 
	 * @param model
	 * 		The domain model to search for superclasses
	 * @param className
	 * 		The name of the class to retrieve superclasses of
	 * @return
	 */
	public static UMLClass[] getAllSuperclasses(DomainModel model, String className) {
		Set supers = getSuperclasses(model, className);
		UMLClass[] classes = new UMLClass[supers.size()];
		supers.toArray(classes);
		return classes;
	}
	
	
	private static Set getSuperclasses(DomainModel model, String className) {
		UMLGeneralization[] generalization = model.getUmlGeneralizationCollection().getUMLGeneralization();
		Set superclasses = new HashSet();
		// find all generalizations where subclass is the class in question,
		// then get the superclasses from each
		for (int i = 0; i < generalization.length; i++) {
			UMLClassReference subClassRef = generalization[i].getSubClassReference();
			UMLClassReference superClassRef = generalization[i].getSuperClassReference();
			if (subClassRef != null && superClassRef != null) {
				UMLClass subclass = getReferencedUMLClass(model, subClassRef);
				if (className.equals(subclass.getPackageName() + "." + subclass.getClassName())) {
					UMLClass superclass = getReferencedUMLClass(model, superClassRef);
					superclasses.add(superclass);
					// get superclasses of the superclass
					superclasses.addAll(getSuperclasses(model, superclass.getPackageName() + "." + superclass.getClassName()));
				}
			}
		}
		return superclasses;
	}
}
