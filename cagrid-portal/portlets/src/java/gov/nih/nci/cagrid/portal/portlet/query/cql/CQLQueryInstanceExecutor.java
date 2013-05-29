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
/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.portlet.query.QueryInstanceExecutor;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public interface CQLQueryInstanceExecutor extends QueryInstanceExecutor<CQLQueryInstance> {

    void setCqlQueryInstanceListener(CQLQueryInstanceListener listener);

    CQLQueryInstanceListener getCqlQueryInstanceListener();

}
