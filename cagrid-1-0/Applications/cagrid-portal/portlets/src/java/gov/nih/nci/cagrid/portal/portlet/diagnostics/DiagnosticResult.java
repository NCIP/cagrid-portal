package gov.nih.nci.cagrid.portal.portlet.diagnostics;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@DataTransferObject
public class DiagnosticResult {


    private DiagnosticType type;
    private DiagnosticResultStatus status;
    private String detail;
    private String errorDetail;


    public DiagnosticResult(DiagnosticType type) {
        this.type = type;
    }

    public DiagnosticResult(DiagnosticType type, DiagnosticResultStatus status) {
        this.type = type;
        this.status = status;
    }

    public DiagnosticResult(DiagnosticType type, DiagnosticResultStatus status, String detail) {
        this.type = type;
        this.status = status;
        this.detail = detail;
    }

    @RemoteProperty
    public DiagnosticResultStatus getStatus() {
        return status;
    }

    public void setStatus(DiagnosticResultStatus status) {
        this.status = status;
    }

    @RemoteProperty
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @RemoteProperty
    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }

    public DiagnosticType getType() {
        return type;
    }

    public void setType(DiagnosticType type) {
        this.type = type;
    }
}
