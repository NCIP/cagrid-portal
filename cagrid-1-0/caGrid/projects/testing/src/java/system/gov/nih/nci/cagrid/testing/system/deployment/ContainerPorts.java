package gov.nih.nci.cagrid.testing.system.deployment;

import java.net.ServerSocket;


/**
 * PortPreference
 * 
 * @author oster
 * @author ervin
 * @created Mar 29, 2007 10:58:18 AM
 * @version $Id: multiscaleEclipseCodeTemplates.xml,v 1.1 2007/03/02 14:35:01
 *          dervin Exp $
 */
public class ContainerPorts {

    private Integer port = null;
    private Integer shutdownPort = null;


    public ContainerPorts(Integer port, Integer shutdownPort) {
        this.port = port;
        this.shutdownPort = shutdownPort;
    }


    /**
     * If specificPort is specified, that is returned. Otherwise, will search
     * the port range (inclusive) and find a port that can be bound to, which is
     * not listed in the portExcludes.
     * 
     * @return The port number
     * @throws NoAvailablePortException
     */
    public Integer getPort() throws NoAvailablePortException {
        return port;
    }


    public Integer getShutdownPort() throws NoAvailablePortException {
        return shutdownPort;
    }


    public boolean isPortAvailable() {
        boolean available = false;
        ServerSocket sock = null;
        try {
            sock = new ServerSocket(port.intValue());
            available = true;
        } catch (Throwable e) {
        } finally {
            if (sock != null) {
                try {
                    sock.close();
                } catch (Throwable t) {
                }
            }
        }
        return available;
    }
}
