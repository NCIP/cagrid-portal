/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.portlet.diagnostics;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;

import java.util.HashMap;

import gov.nih.nci.cagrid.portal.portlet.AjaxViewGenerator;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public abstract class AbstractDiagnosticService extends AjaxViewGenerator implements DiagnosticServiceI {

    protected final Log logger = LogFactory.getLog(getClass().getName());


    @RemoteMethod
    public String diagnose(String Url) throws Exception {
        WebContext webContext = WebContextFactory.get();

        final DiagnosticResult _result = diagnoseInternal(Url);
        final DiagnosticType _type = _result.getType();

        return super.getView("/WEB-INF/jsp/diagnostics/result_" + _type.toString() + ".jsp" ,
                new HashMap<String,Object>(){{put("result", _result);}});
    }

    /**
     * Subclasses need to implement this method and return a diagnostic result
     *
     * @param Url
     * @return
     */
    protected abstract DiagnosticResult diagnoseInternal(String Url) throws Exception;
}
