package gov.nih.nci.cagrid.portal.utils;

import org.apache.log4j.Category;
import org.springframework.aop.ThrowsAdvice;

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

    public void afterThrowing(Exception ex) throws Throwable {

        // ToDo remove this line
        System.out.println("ADVICE INterceptor...........................");

        Category.getRoot().error(_prefix + ex);

    }
}
