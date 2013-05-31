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
package gov.nih.nci.cagrid.portal.search;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PortalSearchRuntimeException extends RuntimeException {
    public PortalSearchRuntimeException() {
    }

    public PortalSearchRuntimeException(String s) {
        super(s);
    }

    public PortalSearchRuntimeException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PortalSearchRuntimeException(Throwable throwable) {
        super(throwable);
    }
}
