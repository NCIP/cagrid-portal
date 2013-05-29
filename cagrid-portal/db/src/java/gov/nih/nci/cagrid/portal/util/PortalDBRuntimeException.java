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
package gov.nih.nci.cagrid.portal.util;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PortalDBRuntimeException extends RuntimeException{

    public PortalDBRuntimeException() {
    }

    public PortalDBRuntimeException(String s) {
        super(s);
    }

    public PortalDBRuntimeException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PortalDBRuntimeException(Throwable throwable) {
        super(throwable);
    }
}
