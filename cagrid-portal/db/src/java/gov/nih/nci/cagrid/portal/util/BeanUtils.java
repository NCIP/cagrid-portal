package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.portal.domain.DomainObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.util.StringTokenizer;

/**
 * User: kherm
 * <p/>
 * Utility to traverse bean graphs without running
 * into NPE.
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class BeanUtils {

    private static Log logger = LogFactory.getLog(BeanUtils.class);

    /**
     * Will traverse the provided domain object along the path specified
     * and return the value
     * <p/>
     * This method assumes the final property is a String.
     * <p/>
     * <p/>
     * Eg. traverse(new GridService(),"serviceMetadata.");
     *
     * @param obj
     * @param path
     * @return
     */
    public static String traverse(DomainObject obj, String path) {
        Object value = obj;
        try {
            StringTokenizer tokens = new StringTokenizer(path, ".");

            while (tokens.hasMoreTokens()) {
                String token = tokens.nextToken();
                String getterName = "get" + token.substring(0, 1).toUpperCase() + token.substring(1);

                Class c = value.getClass();
                Method getter = c.getMethod(getterName);
                // reassign to what is returned by getter
                value = getter.invoke(value);

            }
        } catch (Exception e) {
            logger.info("Failed to traverse object with path. Will return null  " + path);
            value = null;
        }
        // will assume that final property value is a String
        return (String) value;

    }

    /**
     * Same as traverse(Domain obj, String path)
     * but you can specify the return type
     *
     * @param obj
     * @param path
     * @param T
     * @param <T>
     * @return
     */
    public static <T> T traverse(DomainObject obj, String path, Class T) {
        Object value = obj;
        try {
            StringTokenizer tokens = new StringTokenizer(path, ".");

            while (tokens.hasMoreTokens()) {
                String token = tokens.nextToken();
                String getterName = "get" + token.substring(0, 1).toUpperCase() + token.substring(1);

                Class c = value.getClass();
                Method getter = c.getMethod(getterName);
                // reassign to what is returned by getter
                value = getter.invoke(value);

            }
        } catch (Exception e) {
            logger.info("Failed to traverse object with path. Will return null  " + path);
            value = null;
        }
        return (T) value;

    }
}
