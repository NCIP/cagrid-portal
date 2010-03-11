package gov.nih.nci.cagrid.portal.portlet.diagnostics;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.convert.EnumConverter;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@DataTransferObject(converter = EnumConverter.class)
public enum DiagnosticResultStatus {

    PASSED("Passed"), FAILED("Failed"), PROBLEM("Problem"), UNDETERMINISTIC("Underterministic");

    private String strValue;

    DiagnosticResultStatus(String status) {
        this.strValue = status;
    }

    @Override
    public String toString() {
        return this.strValue;
    }


}
