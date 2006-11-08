package gov.nih.nci.cagrid.data.sdk32query;

import gov.nih.nci.cagrid.data.QueryProcessingException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/** 
 *  BooleanAttributeCheckCache
 *  Checks if an attribute of a class is a boolean
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Nov 2, 2006 
 * @version $Id: BooleanAttributeCheckCache.java,v 1.2 2006-11-08 20:20:14 dervin Exp $ 
 */
public class BooleanAttributeCheckCache {

	private static Map booleanFlags = new HashMap();
	
	public static boolean isFieldBoolean(String objClassName, String fieldName) throws QueryProcessingException {
		String key = objClassName + "." + fieldName;
		Boolean isBool = (Boolean) booleanFlags.get(key);
		if (isBool == null) {
			try {
				Class objClass = Class.forName(objClassName);
				Field field = objClass.getDeclaredField(fieldName);
				isBool = Boolean.valueOf(Boolean.class.equals(field.getType()));
				booleanFlags.put(key, isBool);
			} catch (NoSuchFieldException ex) {
				throw new QueryProcessingException("No field " + fieldName + " found for class " + objClassName, ex);
			} catch (ClassNotFoundException ex) {
				throw new QueryProcessingException(ex.getMessage(), ex);
			}
		}
		return isBool.booleanValue();
	}
}
