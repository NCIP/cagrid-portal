package gov.nih.nci.cagrid.introduce.servicetools.security;

import org.globus.wsrf.impl.security.authorization.ResourcePDPConfig;
import org.globus.wsrf.impl.security.authorization.exceptions.InitializeException;
import org.globus.wsrf.impl.security.descriptor.ResourceSecurityDescriptor;
import org.globus.wsrf.impl.security.descriptor.SecurityDescriptorException;
import org.globus.wsrf.security.SecurityException;
import org.globus.wsrf.security.SecurityManager;


/**
 * @author hastings
 */
public class SecurityUtils {

    /**
     * Create a new ResourceSecurityDescriptor using the current peer as the
     * identity to use for acccess. This will only alow this peer to have access
     * to this resource using the Identity PDP from globus.
     * 
     * @return the created ResourceSecurityDescriptor, or null
     * @throws SecurityException
     * @throws SecurityDescriptorException
     */
    public static ResourceSecurityDescriptor createCreatorOnlyResourceSecurityDescriptor() throws SecurityException,
        SecurityDescriptorException {
        ResourceSecurityDescriptor desc = null;

        String peer = SecurityManager.getManager().getCaller();
        // Create a resource security descriptorResourceSecurityDescriptor
        desc = new ResourceSecurityDescriptor();
        // Configure a chain of PDPsString
        String authzChain = "idenAuthz:org.globus.wsrf.impl.security.authorization.IdentityAuthorization";
        // Create configuration object that implements PDPConfig
        ResourcePDPConfig config = new ResourcePDPConfig(authzChain);
        // Set properties that are required by the PDPs on the configuration
        // object.
        // Property used by Identity authorization: scope, property name,
        // property value
        config.setProperty("idenAuthz", "identity", peer);
        try {
            desc.setAuthzChain(authzChain, config, "Caller Only Resource Chain", "caller");
        } catch (InitializeException e) {
            e.printStackTrace();
        }

        return desc;
    }
}
