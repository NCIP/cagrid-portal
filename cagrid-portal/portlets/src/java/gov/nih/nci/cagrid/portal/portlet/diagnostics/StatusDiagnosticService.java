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

import gov.nih.nci.cagrid.portal.aggr.status.ServiceStatusProvider;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class StatusDiagnosticService extends AbstractDiagnosticService {
    private ServiceStatusProvider serviceStatusProvider;

    public DiagnosticResult diagnoseInternal(String Url) throws Exception {
        DiagnosticResult _result = new DiagnosticResult(DiagnosticType.STATUS, DiagnosticResultStatus.FAILED);

        try {
            ServiceStatus _status = serviceStatusProvider.getStatus(Url);

            if (_status.equals(ServiceStatus.ACTIVE))
                _result.setStatus(DiagnosticResultStatus.PASSED);
            else
                _result.setStatus(DiagnosticResultStatus.FAILED);
        } catch (Exception e) {
            _result.setStatus(DiagnosticResultStatus.UNDETERMINISTIC);
        }

        return _result;
    }


    public ServiceStatusProvider getServiceStatusProvider() {
        return serviceStatusProvider;
    }

    public void setServiceStatusProvider(ServiceStatusProvider serviceStatusProvider) {
        this.serviceStatusProvider = serviceStatusProvider;
    }
}
