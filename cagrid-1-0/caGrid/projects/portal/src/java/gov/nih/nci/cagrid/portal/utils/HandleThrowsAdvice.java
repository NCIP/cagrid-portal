package gov.nih.nci.cagrid.portal.utils;

import org.apache.log4j.Category;
import org.springframework.aop.ThrowsAdvice;

import java.lang.reflect.Method;

/**
 * This is a class that provided a Advice
 * that is applied to Portal beans by spring
 * at runtime.
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jul 3, 2006
 * Time: 3:32:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class HandleThrowsAdvice implements ThrowsAdvice {

    /**
     * Takes appropriate action on
     * an exception being thrown.
     */

    public final String _prefix = " Portal Exception: ";

    /**
     * Capture the most generic Exception
     * being thrown
     *
     * @param m
     * @param target
     * @param ex
     * @throws Throwable
     */
    void afterThrowing(Method m, Object target, Exception ex) throws Throwable {
        Category cat = Category.getInstance(m.getClass());
        // throw custom message
        cat.error(_prefix + "## Class:" + target + " ::Method:" + m + " ## " + ex);
    }

}
