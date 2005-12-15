package gov.nih.nci.cagrid.dorian.common;

import org.oasis.wsrf.faults.BaseFaultType;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class FaultUtil {
	public static void printFault(Exception e){
	        if (e instanceof BaseFaultType) {        	
	        	BaseFaultType fault = (BaseFaultType)e;
	        	System.err.println(fault.getFaultString());
	        	FaultHelper helper = new FaultHelper(fault);
	        	helper.printStackTrace();    
	        }else{
	        	e.printStackTrace();
	        }       
	}
	/*
	public static String faultToString(Exception e){
		StringBuffer sb = new StringBuffer();
        if (e instanceof BaseFaultType) {        	
        	BaseFaultType fault = (BaseFaultType)e;
        	sb.append(fault.getFaultString());
        	StringWriter io = new StringWriter();
        	FaultHelper helper = new FaultHelper(fault);
        	helper.printStackTrace(io);    
        }else{
        	e.printStackTrace();
        }    
        return sb.toString();
}
*/

}
