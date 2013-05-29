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
package gov.nih.nci.cagrid.portal.portlet.query.dcql;

import gov.nih.nci.cagrid.portal.domain.dataservice.DCQLQueryInstance;
import gov.nih.nci.cagrid.portal.portlet.query.QueryInstanceExecutor;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public interface DCQLQueryInstanceExecutor extends QueryInstanceExecutor<DCQLQueryInstance> {

    void setDcqlQueryInstanceListener(DCQLQueryInstanceListener listener);

    DCQLQueryInstanceListener getDcqlQueryInstanceListener();


}
