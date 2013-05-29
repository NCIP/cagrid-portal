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

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface CQLQueryInstanceListener {
	void onSheduled(CQLQueryInstance instance);
	void onRunning(CQLQueryInstance instance);
	void onComplete(CQLQueryInstance instance, String results);
	void onCancelled(CQLQueryInstance instance, boolean cancelled);
	void onError(CQLQueryInstance instance, Exception error);
	void onTimeout(CQLQueryInstance instance, boolean cancelled);
}
