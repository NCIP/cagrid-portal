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
