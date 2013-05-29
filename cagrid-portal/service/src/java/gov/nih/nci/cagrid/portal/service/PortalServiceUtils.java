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
package gov.nih.nci.cagrid.portal.service;

import gov.nih.nci.cagrid.portal.domain.PortalUser;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class PortalServiceUtils {

	public static long getPortalUserPortalId(PortalUser portalUser){
		return Long.valueOf(portalUser.getPortalId().split(":")[1]);
	}
	
}
