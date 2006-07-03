package gov.nih.nci.cagrid.portal.utils;

import org.springframework.aop.MethodBeforeAdvice;
import org.apache.log4j.Category;

import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jul 3, 2006
 * Time: 3:06:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class DebugBeforeAdvice implements MethodBeforeAdvice {

    public void before(Method method, Object[] objects, Object object) throws Throwable {
        String className = method.getClass().getName();
        Category logCat = Category.getInstance(className);

        logCat.debug("DEBUG: Class " + className  + "  Before metod " + method );
    }
}
