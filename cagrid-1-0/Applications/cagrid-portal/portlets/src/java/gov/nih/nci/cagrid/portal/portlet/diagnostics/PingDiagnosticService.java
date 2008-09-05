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
            _result.setDetail("Sucessfully pinged service");
        else
            _result.setDetail("Failed to PING service");

        return _result;
    }
}
