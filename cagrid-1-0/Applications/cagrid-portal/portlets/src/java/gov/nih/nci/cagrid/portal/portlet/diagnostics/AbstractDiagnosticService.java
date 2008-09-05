package gov.nih.nci.cagrid.portal.portlet.diagnostics;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public abstract class AbstractDiagnosticService implements DiagnosticServiceI {

    protected final Log logger = LogFactory.getLog(getClass().getName());


    @RemoteMethod
    public String diagnose(String Url) throws Exception {
        WebContext webContext = WebContextFactory.get();

        DiagnosticResult _result = diagnoseInternal(Url);
        DiagnosticType _type = _result.getType();

        webContext.getHttpServletRequest().setAttribute("result", _result);

        return webContext.forwardToString("/WEB-INF/jsp/diagnostics/result_" + _type.toString() + ".jsp");
    }

    /**
     * Subclasses need to implement this method and return a diagnostic result
     *
     * @param Url
     * @return
     */
    protected abstract DiagnosticResult diagnoseInternal(String Url) throws Exception;
}
