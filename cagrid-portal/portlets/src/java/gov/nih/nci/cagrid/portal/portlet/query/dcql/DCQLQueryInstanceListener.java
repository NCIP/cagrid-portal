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
package gov.nih.nci.cagrid.portal.portlet.query.dcql;

import gov.nih.nci.cagrid.portal.domain.dataservice.DCQLQueryInstance;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public interface DCQLQueryInstanceListener {

    void onSheduled(DCQLQueryInstance instance);

    void onRunning(DCQLQueryInstance instance);

    void onComplete(DCQLQueryInstance instance, String results);

    void onCancelled(DCQLQueryInstance instance, boolean cancelled);

    void onError(DCQLQueryInstance instance, Exception error);

    void onTimeout(DCQLQueryInstance instance, boolean cancelled);
}
