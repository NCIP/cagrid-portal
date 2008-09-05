package gov.nih.nci.cagrid.portal.portlet.diagnostics;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public enum DiagnosticType {
    METADATA("metadata"), INDEX("idx"), PING("ping"), STATUS("status");


    private String strValue;

    DiagnosticType(String type) {
        this.strValue = type;
    }

    @Override
    public String toString() {
        return this.strValue;
    }

}
