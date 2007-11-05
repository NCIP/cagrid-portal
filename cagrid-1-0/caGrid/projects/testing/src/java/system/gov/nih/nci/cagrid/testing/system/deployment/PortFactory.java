package gov.nih.nci.cagrid.testing.system.deployment;

import gov.nih.nci.cagrid.testing.core.TestingConstants;

import java.util.LinkedList;
import java.util.List;

/** 
 *  PortFactory
 *  Utility to hand out PortPreference instances in a static way.
 *  This should help in the static initialization which Haste
 *  uses when setting up story steps.
 * 
 * @author David Ervin
 * 
 * @created Nov 5, 2007 10:13:07 AM
 * @version $Id: PortFactory.java,v 1.1 2007-11-05 15:30:10 dervin Exp $ 
 */
public class PortFactory {

    private static List<Integer> assignedPortNumbers = null;
    
    public static PortPreference getPort() throws NoAvailablePortException {
        if (assignedPortNumbers == null) {
            assignedPortNumbers = new LinkedList<Integer>();
        }
        Integer[] usedPorts = new Integer[assignedPortNumbers.size()];
        assignedPortNumbers.toArray(usedPorts);
        PortPreference port = new PortPreference(
            TestingConstants.TEST_PORT_LOWER_BOUND, TestingConstants.TEST_PORT_UPPER_BOUND,
            usedPorts);
        Integer assignment = port.getPort();
        assignedPortNumbers.add(assignment);
        return port;
    }
}
