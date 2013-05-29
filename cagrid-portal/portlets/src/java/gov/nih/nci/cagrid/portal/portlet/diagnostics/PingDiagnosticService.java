/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.portlet.diagnostics;

import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@RemoteProxy(name = "pingDiagnostic",
        creator = SpringCreator.class,
        creatorParams = @Param(name = "beanName",
                value = "pingDiagnosticService"))
public class PingDiagnosticService extends StatusDiagnosticService {

    
    @Override
    public DiagnosticResult diagnoseInternal(String Url) throws Exception {
        DiagnosticResult _result = super.diagnoseInternal(Url);
        _result.setType(DiagnosticType.PING);

        if (_result.getStatus().equals(DiagnosticResultStatus.PASSED))
            _result.setMessage("Successfully pinged service");
        else {
            _result.setMessage("Failed to PING service");
            _result.setDetail("Could not reach <br><i><b>" + Url + "</i></b><br/> or its not a valid caGrid Service. Please make sure that the URL is correct and is reachable from the Internet.");
        }

        return _result;
    }


}
