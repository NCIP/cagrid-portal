package gov.nih.nci.cagrid.portal.web;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 1, 2006
 * Time: 11:44:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class PortalWebUtils {

    public static Object getBean(String beanName) throws EvaluationException {
        FacesContext ctx = FacesContext.getCurrentInstance();
        return ctx.getApplication().getVariableResolver().resolveVariable(ctx, beanName);
    }
}

