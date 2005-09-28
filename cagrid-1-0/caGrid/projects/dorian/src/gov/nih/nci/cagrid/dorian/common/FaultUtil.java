package gov.nih.nci.cagrid.gums.common;

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

}
