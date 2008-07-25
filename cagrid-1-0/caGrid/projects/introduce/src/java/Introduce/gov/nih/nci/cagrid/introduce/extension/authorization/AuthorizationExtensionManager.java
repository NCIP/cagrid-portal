package gov.nih.nci.cagrid.introduce.extension.authorization;

import gov.nih.nci.cagrid.introduce.beans.extension.AuthorizationExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.common.SpecificServiceInformation;

/**
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created 
 */
public interface AuthorizationExtensionManager {
	
    /**
     * Should create or configure the class that should be used for this particular auth extensions callback.
     * Will be called each time the introduce service is saved.
     * 
     * @param desc
     * @param info
     * @return
     * @throws AuthorizationExtensionException
     */
	public String generateAuthorizationExtension(AuthorizationExtensionDescriptionType desc, SpecificServiceInformation info) throws AuthorizationExtensionException;

	/**
	 * Will be called when introduce detects the extension is no longer being used in the specfic service
	 * 
	 * @param desc
	 * @param info
	 * @return
	 * @throws AuthorizationExtensionException
	 */
	public String removeAuthorizationExtension(AuthorizationExtensionDescriptionType desc, SpecificServiceInformation info) throws AuthorizationExtensionException;

}
