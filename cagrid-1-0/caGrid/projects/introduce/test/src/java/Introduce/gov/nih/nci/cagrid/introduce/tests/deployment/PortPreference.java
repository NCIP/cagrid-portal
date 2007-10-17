package gov.nih.nci.cagrid.introduce.tests.deployment;

import java.net.ServerSocket;
import java.util.Arrays;


/**
 * PortPreference
 * 
 * @author oster
 * @author ervin
 * @created Mar 29, 2007 10:58:18 AM
 * @version $Id: multiscaleEclipseCodeTemplates.xml,v 1.1 2007/03/02 14:35:01
 *          dervin Exp $
 */
public class PortPreference {
    private Integer portRangeMinimum;
    private Integer portRangeMaximum;
    private Integer specificPort;
    private Integer portExcludes[];
    
    private Integer availablePort = null;


    /**
     * @return Returns the portRangeMinimum.
     */
    public Integer getPortRangeMinimum() {
        return this.portRangeMinimum;
    }


    /**
     * @return Returns the portRangeMaximum.
     */
    public Integer getPortRangeMaximum() {
        return this.portRangeMaximum;
    }


    /**
     * @return Returns the specificPort.
     */
    public Integer getSpecificPort() {
        return this.specificPort;
    }


    /**
     * @return Returns the portExcludes.
     */
    public Integer[] getPortExcludes() {
        return this.portExcludes;
    }


    /**
     * Specifies the identified port should be used.
     * 
     * @param specificPort
     * @throws IllegalArgumentException
     *             if port is null or less than 0
     */
    public PortPreference(Integer specificPort) throws IllegalArgumentException {
        if (specificPort == null || specificPort.intValue() < 0) {
            throw new IllegalArgumentException("Cannot specify null or negative port!");
        }
        this.specificPort = specificPort;
    }


    /**
     * Specifies to search the specified port range (inclusive) and find a port
     * that can be bound to, which is not listed in the portExcludes.
     * 
     * @param portRangeMinimum
     * @param portRangeMaximum
     * @param portExcludes
     * @throws IllegalArgumentException
     *             if portRangeMinimum or portRangeMaximum are either null or
     *             less than 0
     */
    public PortPreference(Integer portRangeMinimum, Integer portRangeMaximum, Integer[] portExcludes)
        throws IllegalArgumentException {
        if (portRangeMinimum == null || portRangeMaximum == null || portRangeMinimum.intValue() < 0
            || portRangeMaximum.intValue() < 0) {
            throw new IllegalArgumentException("Cannot specify null or negative range constraints (min,max)=("
                + portRangeMinimum + "," + portRangeMaximum + ")!");
        }

        this.portRangeMinimum = portRangeMinimum;
        this.portRangeMaximum = portRangeMaximum;
        if (portExcludes != null) {
            Arrays.sort(portExcludes);
            this.portExcludes = portExcludes;
        }
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
        if (availablePort == null) {
            if (this.specificPort != null && isPortAvailable(this.specificPort.intValue())) {
                availablePort = this.specificPort;
            } else {
                for (int i = this.portRangeMinimum.intValue(); i <= this.portRangeMaximum.intValue(); i++) {
                    if (this.portExcludes != null && 
                        Arrays.binarySearch(this.portExcludes, Integer.valueOf(i)) >= 0) {
                        continue;
                    }
                    if (isPortAvailable(i)) {
                        availablePort = Integer.valueOf(i);
                        break;
                    }
                }
            }
        }
        
        // if still null, could not obtain a port
        if (availablePort == null) {
            throw new NoAvailablePortException("Could not find an available port.");
        }
        return availablePort;
    }
    
    
    private boolean isPortAvailable(int port) {
        boolean available = false;
        ServerSocket sock = null;
        try {
            sock = new ServerSocket(port);
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
