package gov.nih.nci.cagrid.common;

import org.apache.axis.AxisFault;

public class Utils {
	
	public static String getExceptionMessage(Exception e) {
		String mess = e.getMessage();
		if (e instanceof AxisFault) {
			mess = ((AxisFault) e).getFaultString();
			// Handle Special Messages
			AxisFault f = (AxisFault) e;

		}
		return mess;
	}

}
