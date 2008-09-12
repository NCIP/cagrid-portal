package gov.nih.nci.cagrid.portal.portlet.diagnostics;

import gov.nih.nci.cagrid.portal.aggr.ServiceUrlProvider;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;

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
        DiagnosticResult _result = new DiagnosticResult(DiagnosticType.STATUS, DiagnosticResultStatus.FAILED);
        _result.setType(DiagnosticType.INDEX);

        for (String indexSvcUrl : indexServiceUrls) {
            Set<String> dynamicSvcUrls = null;
            try {
                dynamicSvcUrls = getDynamicServiceUrlProvider().getUrls(
                        indexSvcUrl);
                if (dynamicSvcUrls.contains(Url)) {
                    _result.setStatus(DiagnosticResultStatus.PASSED);
                    _result.setMessage("Service found in the INDEX");
                    logger.debug("Found service in Index");
                    break;
                }

            } catch (Exception ex) {
                //Will happen during high server load
                //catch and log exception. Status returned will be ACTIVE
                logger.error("Index service query failed.");
                _result.setStatus(DiagnosticResultStatus.UNDETERMINISTIC);
                _result.setMessage("Failed to query the Index");
                _result.setDetail("Failed to query the index service at: " + indexSvcUrl + ".Please try again");
            }
        }

        if (_result.getStatus() == DiagnosticResultStatus.FAILED) {
            _result.setMessage("Service not found in INDEX");
            _result.setStatus(DiagnosticResultStatus.FAILED);
            StringBuilder _detailMessage = new StringBuilder("Service was not found in the index(s). Looked in the following index(s) ");
            for (String _idx : indexServiceUrls) {
                _detailMessage.append("<br/>");
                _detailMessage.append(_idx);
            }
            _result.setDetail(_detailMessage.substring(0, _detailMessage.length()));
        }

        return _result;
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
