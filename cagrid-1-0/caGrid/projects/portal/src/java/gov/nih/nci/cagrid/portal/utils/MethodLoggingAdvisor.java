package gov.nih.nci.cagrid.portal.utils;

import org.apache.log4j.Category;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
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
public class MethodLoggingAdvisor implements MethodBeforeAdvice, AfterReturningAdvice, ThrowsAdvice {
    /**
     * Takes appropriate action on
     * an exception being thrown.
     */

    public final String _errPrefix = " Portal Exception: ";
    public final String _debugPrefix = " Portal Debug: ";


    public MethodLoggingAdvisor() {
    }

    public void before(Method method, Object[] objects, Object target) throws Throwable {
        Category cat = Category.getInstance(target.getClass());
        cat.debug(_debugPrefix + "Begin Method " + method.getName());
    }

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
        cat.error(_errPrefix + "## Class:" + target + " ::Method:" + m + " ## " + ex);
    }

    public void afterReturning(Object object, Method method, Object[] objects, Object target) throws Throwable {
        Category cat = Category.getInstance(target.getClass());
        cat.debug(_debugPrefix + "End Method " + method.getName());
    }

}
