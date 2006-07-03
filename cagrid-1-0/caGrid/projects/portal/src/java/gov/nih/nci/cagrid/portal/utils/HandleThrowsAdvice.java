package gov.nih.nci.cagrid.portal.utils;

import org.springframework.aop.ThrowsAdvice;
import org.apache.log4j.Category;

/**
 *
 * This is a class that provided a Advice
 * that is applied to Portal beans by spring
 * at runtime.
 *
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
     *
     * @param ex
     * @throws Throwable
     */
    public void afterThrowing(Exception ex) throws Throwable{
        Category.getRoot().error(ex);
    }
}
