package gov.nih.nci.cagrid.portal.web;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 5, 2006
 * Time: 2:38:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class PortalExceptionUtils {

    public static List getExceptions(Throwable cause) {
        List exceptions = new ArrayList(10);
        exceptions.add(cause);

        do {
            Throwable nextCause;
            try {
                Method rootCause = cause.getClass().getMethod("getRootCause", new Class[]{});
                nextCause = (Throwable) rootCause.invoke(cause, new Object[]{});
            }
            catch (Exception e) {
                nextCause = cause.getCause();
            }
            if (cause == nextCause) {
                break;
            }

            if (nextCause != null) {
                exceptions.add(nextCause);
            }

            cause = nextCause;
        }
        while (cause != null);

        return exceptions;
    }

    /**
     * Find a throwable message starting with the last element.<br />
     * Returns the first throwable message where <code>throwable.getMessage() != null</code>
     */
    public static String getExceptionMessage(List throwables) {
        if (throwables == null) {
            return null;
        }

        for (int i = throwables.size() - 1; i > 0; i--) {
            Throwable t = (Throwable) throwables.get(i);
            if (t.getMessage() != null) {
                return t.getMessage();
            }
        }

        return null;
    }
}
