package gov.nih.nci.cagrid.portal.portlet.diagnostics;

import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@RemoteProxy(name = "idxDiagnostic",
        creator = SpringCreator.class,
        creatorParams = @Param(name = "beanName",
                value = "idxDiagnosticService"))

public class IdxDiagnosticService extends StatusDiagnosticService {


    @Override
    public DiagnosticResult diagnoseInternal(String Url) throws Exception {
        DiagnosticResult _result = super.diagnoseInternal(Url);
        _result.setType(DiagnosticType.INDEX);

        if (_result.getStatus().equals(DiagnosticResultStatus.PASSED))
            _result.setDetail("Service found in INDEX");
        else
            _result.setDetail("Service not found in INDEX");

        return _result;
    }


}
