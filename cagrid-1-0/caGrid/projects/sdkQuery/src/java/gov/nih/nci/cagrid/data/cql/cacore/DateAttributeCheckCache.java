package gov.nih.nci.cagrid.data.cql.cacore;

import gov.nih.nci.cagrid.data.QueryProcessingException;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/** 
 *  DateAttributeCheckCache
 *  Checks for attribute data types as dates and caches that information for later use
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 29, 2006 
 * @version $Id: DateAttributeCheckCache.java,v 1.1 2006-11-29 17:08:25 dervin Exp $ 
 */
public class DateAttributeCheckCache {

	private static Map dateFlags = new HashMap();
	
	public static boolean isFieldDate(String objClassName, String fieldName) throws QueryProcessingException {
		String key = objClassName + "." + fieldName;
		Boolean isDate = (Boolean) dateFlags.get(key);
		if (isDate == null) {
			try {
				Class objClass = Class.forName(objClassName);
				Field field = ClassAccessUtilities.getNamedField(objClass, fieldName);
				if (field == null) {
					throw new QueryProcessingException("No field " + fieldName + " found for class " + objClassName);
				}
				isDate = Boolean.valueOf(Date.class.equals(field.getType()));
				dateFlags.put(key, isDate);
			} catch (ClassNotFoundException ex) {
				throw new QueryProcessingException(ex.getMessage(), ex);
			}
		}
		return isDate.booleanValue();
	}
}
