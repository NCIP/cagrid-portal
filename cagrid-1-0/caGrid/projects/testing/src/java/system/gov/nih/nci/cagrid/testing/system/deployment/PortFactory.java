package gov.nih.nci.cagrid.testing.system.deployment;

import gov.nih.nci.cagrid.testing.core.TestingConstants;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/** 
 *  PortFactory
 *  Utility to hand out PortPreference instances in a static way.
 *  This should help in the static initialization which Haste
 *  uses when setting up story steps.
 * 
 * @author David Ervin
 * 
 * @created Nov 5, 2007 10:13:07 AM
 * @version $Id: PortFactory.java,v 1.3 2007-12-17 17:38:02 dervin Exp $ 
 */
public class PortFactory {
    private static List<Integer> assignedPortNumbers = null;
    
    public static PortPreference getPort() throws NoAvailablePortException {
        if (assignedPortNumbers == null) {
            assignedPortNumbers = new LinkedList<Integer>();
        }
        Integer[] usedPorts = new Integer[assignedPortNumbers.size()];
        assignedPortNumbers.toArray(usedPorts);
        PortPreference port = null;
        // determine if testing port assignment is in effect
        if (usingTestingPortAssignmentMode()) {
            Integer testPort = getTestingPort();
            port = new PortPreference(testPort);
        } else {
            port = new PortPreference(
                TestingConstants.TEST_PORT_LOWER_BOUND, 
                TestingConstants.TEST_PORT_UPPER_BOUND,
                usedPorts);
        }
        // grab the port
        Integer assignment = port.getPort();
        // record its usage
        assignedPortNumbers.add(assignment);
        return port;
    }
    
    
    public static List<Integer> getAssignedPortNumbers() {
        // clone it so modifications to the returned list won't
        // affect the inner workings of this utility
        List<Integer> clone = new ArrayList<Integer>(assignedPortNumbers.size());
        clone.addAll(assignedPortNumbers);
        return clone;
    }
    
    
    /**
     * Iterates through all assigned ports and checks if they are
     * currently bound.  All unbound ports will be removed from the assignment
     * list, and made available for future assignment.
     */
    public static synchronized void reclaimPorts() {
        Iterator<Integer> portIter = assignedPortNumbers.iterator();
        while (portIter.hasNext()) {
            Integer port = portIter.next();
            if (isPortAvailable(port.intValue())) {
                portIter.remove();
            }
        }
    }
    
    
    private static boolean isPortAvailable(int port) {
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
    
    
    public static boolean usingTestingPortAssignmentMode() {
        Properties props = System.getProperties();
        if (props.containsKey(TestingConstants.USE_TESTING_ASSIGNMENT_MODE)) {
            String val = props.getProperty(TestingConstants.USE_TESTING_ASSIGNMENT_MODE);
            boolean b = Boolean.parseBoolean(val);
            if (b) {
                System.out.println("USING TEST PORT ASSIGNMENT MODE");
                System.out.println("USING TEST PORT ASSIGNMENT MODE");
                System.out.println("USING TEST PORT ASSIGNMENT MODE");
            }
            return b;
        }
        return false;
    }
    
    
    private static Integer getTestingPort() {
        int range = TestingConstants.TEST_PORT_UPPER_BOUND.intValue() -
            TestingConstants.TEST_PORT_LOWER_BOUND.intValue();
        boolean used = true;
        int count = 0;
        Integer testPort = null;
        while (used && count < range) { 
            int offset = (int) (System.currentTimeMillis() % range);
            testPort = Integer.valueOf(TestingConstants.TEST_PORT_LOWER_BOUND.intValue() + offset);
            used = assignedPortNumbers.contains(testPort) && isPortAvailable(testPort.intValue());
            count++;
        }
        return testPort;
    }
}
