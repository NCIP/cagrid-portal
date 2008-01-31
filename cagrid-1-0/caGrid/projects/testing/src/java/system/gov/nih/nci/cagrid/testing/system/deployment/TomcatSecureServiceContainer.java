package gov.nih.nci.cagrid.testing.system.deployment;

import java.io.File;


/**
 * TomcatServiceContainer Service container implementation for tomcat
 * 
 * @author David Ervin
 * @created Oct 19, 2007 12:01:22 PM
 * @version $Id: TomcatServiceContainer.java,v 1.4 2007/11/05 16:19:58 dervin
 *          Exp $
 */
public class TomcatSecureServiceContainer extends TomcatServiceContainer implements SecureContainer {

    public TomcatSecureServiceContainer(ContainerProperties properties) {
        super(properties);

    }


    public File getCertificatesDirectory() throws Exception {
        return new File(this.getProperties().getContainerDirectory() + File.separator + "certificates");
    }

}
