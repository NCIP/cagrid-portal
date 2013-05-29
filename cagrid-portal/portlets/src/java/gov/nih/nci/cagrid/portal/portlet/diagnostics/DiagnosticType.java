/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
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
