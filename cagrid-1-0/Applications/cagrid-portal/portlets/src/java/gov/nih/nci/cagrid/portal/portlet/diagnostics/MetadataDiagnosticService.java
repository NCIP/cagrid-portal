package gov.nih.nci.cagrid.portal.portlet.diagnostics;

import gov.nih.nci.cagrid.portal.util.PortalUtils;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@RemoteProxy(name = "metadataDiagnostic",
        creator = SpringCreator.class,
        creatorParams = @Param(name = "beanName",
                value = "metadataDiagnosticService"))
public class MetadataDiagnosticService extends AbstractDiagnosticService {

    private long defaultTimeout;


    public DiagnosticResult diagnoseInternal(String Url) {
        DiagnosticResult _result = new DiagnosticResult(DiagnosticType.METADATA, DiagnosticResultStatus.FAILED);
        try {
            PortalUtils.getMetadata(Url, defaultTimeout);
            _result.setStatus(DiagnosticResultStatus.PASSED);
            logger.debug("Retreived metadata sucessfully for " + Url);
            _result.setDetail("Metadata retreived sucessfully");
        } catch (Exception e) {
            _result.setDetail("Could not retreive Metadata");
            logger.debug("Failed to retreive METADATA for " + Url);
        }

        return _result;
    }

    protected DiagnosticType getDiangosticType() {
        return DiagnosticType.METADATA;
    }

    public long getDefaultTimeout() {
        return defaultTimeout;
    }

    public void setDefaultTimeout(long defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }
}
