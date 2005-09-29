package gov.nih.nci.cagrid.gums.common;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.globus.wsrf.utils.FaultHelper;
import org.oasis.wsrf.faults.BaseFaultType;

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
