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

import gov.nih.nci.cagrid.portal.aggr.ServiceUrlProvider;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@RemoteProxy(name = "idxDiagnostic",
        creator = SpringCreator.class,
        creatorParams = @Param(name = "beanName",
                value = "idxDiagnosticService"))

public class IdxDiagnosticService extends AbstractDiagnosticService {

    private String[] indexServiceUrls;
    private ServiceUrlProvider dynamicServiceUrlProvider;

    @Override
    public DiagnosticResult diagnoseInternal(String Url) throws Exception {
        DiagnosticResult _result = new DiagnosticResult(DiagnosticType.STATUS, DiagnosticResultStatus.FAILED, "Service not found in INDEX");
        _result.setType(DiagnosticType.INDEX);

        List<String> _idxs = new ArrayList<String>();
        for (String indexSvcUrl : indexServiceUrls) {
            Set<String> dynamicSvcUrls = null;
            try {
                dynamicSvcUrls = getDynamicServiceUrlProvider().getUrls(
                        indexSvcUrl);
                _idxs.add(indexSvcUrl);
                if (dynamicSvcUrls.contains(Url)) {
                    _result.setStatus(DiagnosticResultStatus.PASSED);
                    _result.setMessage("Service found in the INDEX");
                    break;
                }

            } catch (Exception ex) {
                if (_result.getStatus() != DiagnosticResultStatus.UNDETERMINISTIC) {
                    _result.setDetail("Failed to query the following Index Service(s)");
                    _result.setStatus(DiagnosticResultStatus.UNDETERMINISTIC);
                }
                _result.setDetail(_result.getDetail().concat(getIndexRow(indexSvcUrl)));
            }
        }

        if (_result.getStatus() != DiagnosticResultStatus.PASSED) {
            if (_result.getDetail() != null) _result.setDetail(_result.getDetail() + getFailedMessage(_idxs));
            else _result.setDetail(getFailedMessage(_idxs));
        }

        return _result;
    }

    private String getFailedMessage(List<String> indexes) {
        StringBuilder _detail = new StringBuilder();
        if (indexes.size() > 0) {
            _detail.append("Service not found in Index. Looked in the following Index(s):");
            for (String indexSvcUrl : indexes) {
                _detail.append(getIndexRow(indexSvcUrl));

            }
        }
        return _detail.toString();
    }

    private String getIndexRow(String indexSvcUrl) {
        StringBuilder _detail = new StringBuilder("<div class='row'>");
        _detail.append(indexSvcUrl);
        _detail.append("</div>");
        return _detail.toString();
    }

    public String[] getIndexServiceUrls() {
        return indexServiceUrls;
    }

    public void setIndexServiceUrls(String[] indexServiceUrls) {
        this.indexServiceUrls = indexServiceUrls;
    }

    public ServiceUrlProvider getDynamicServiceUrlProvider() {
        return dynamicServiceUrlProvider;
    }

    public void setDynamicServiceUrlProvider(ServiceUrlProvider dynamicServiceUrlProvider) {
        this.dynamicServiceUrlProvider = dynamicServiceUrlProvider;
    }
}
