package gov.nih.nci.cagrid.data.cql.cacore;

import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.data.QueryProcessingException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/** 
 *  ClassAccessUtilities
 *  A package of utilities for accessing classes in the context
 *  of CQL query processing
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Oct 3, 2006 
 * @version $Id$ 
 */
public class ClassAccessUtilities {	
	/**
	 * Gets the role name of an association relative to its parent class.
	 * 
	 * @param parentName
	 * 		The class name of the parent of the association
	 * @param assoc
	 * 		The associated object restriction
	 * @return
	 * 		The role name of the associated object
	 * @throws QueryProcessingException
	 */
	public static String getRoleName(String parentName, Association assoc) throws QueryProcessingException {
		String roleName = assoc.getRoleName();
		if (roleName == null) {
			// determine role based on object's type
			Class parentClass = null;
			try {
				parentClass = Class.forName(parentName);
			} catch (Exception ex) {
				throw new QueryProcessingException("Could not load class: " + ex.getMessage(), ex);
			}
			String associationTypeName = assoc.getName();
			
			// search the fields of the right type
			Field[] typedFields = getFieldsOfType(parentClass, associationTypeName);
			if (typedFields.length == 1) {
				// found one and only one field
				roleName = typedFields[0].getName();
			} else if (typedFields.length > 1) {
				// more than one association found
				throw new QueryProcessingException("Association from " + parentClass.getName() + 
					" to " + associationTypeName + " is ambiguous: Specify a role name");
			}
			
			if (roleName == null) {
				// search for a setter method
				Method[] setters = getSettersForType(parentClass, associationTypeName);
				if (setters.length == 1) {
					String temp = setters[0].getName().substring(3);
					if (temp.length() == 1) {
						roleName = String.valueOf(Character.toLowerCase(temp.charAt(0)));
					} else {
						roleName = String.valueOf(Character.toLowerCase(temp.charAt(0))) 
							+ temp.substring(1);
					}
				} else if (setters.length > 1) {
					// more than one association found
					throw new QueryProcessingException("Association from " + parentClass.getName() + 
						" to " + associationTypeName + " is ambiguous: Specify a role name");
				}
			}
		}
		return roleName;
	}
	
	
	/**
	 * Gets all fields from a class and it's superclasses of a given type
	 * 
	 * @param clazz
	 * 		The class to explore for typed fields
	 * @param typeName
	 * 		The name of the type to search for
	 * @return
	 * 		All Fields from the class of the specified type
	 */
	public static Field[] getFieldsOfType(Class clazz, String typeName) {
		Set allFields = new HashSet();
		Class checkClass = clazz;
		while (checkClass != null) {
			Field[] classFields = checkClass.getDeclaredFields();
			Collections.addAll(allFields, classFields);
			checkClass = checkClass.getSuperclass();
		}
		List namedFields = new ArrayList();
		Iterator fieldIter = allFields.iterator();
		while (fieldIter.hasNext()) {
			Field field = (Field) fieldIter.next();
			if (field.getType().getName().equals(typeName)) {
				namedFields.add(field);
			}
		}
		Field[] fieldArray = new Field[namedFields.size()];
		namedFields.toArray(fieldArray);
		return fieldArray;
	}
	
	
	public static Method[] getSettersForType(Class clazz, String typeName) {
		Set allMethods = new HashSet();
		Class checkClass = clazz;
		while (checkClass != null) {
			Method[] classMethods = checkClass.getDeclaredMethods();
			for (int i = 0; i < classMethods.length; i++) {
				Method current = classMethods[i];
				if (current.getName().startsWith("set")) {
					if (Modifier.isPublic(current.getModifiers())) {
						Class[] paramTypes = current.getParameterTypes();
						if (paramTypes.length == 1) {
							if (paramTypes[0].getName().equals(typeName)) {
								allMethods.add(current);
							}
						}
					}
				}
			}
			checkClass = checkClass.getSuperclass();
		}
		Method[] methodArray = new Method[allMethods.size()];
		allMethods.toArray(methodArray);
		return methodArray;
	}
}
