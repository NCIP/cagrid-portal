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
