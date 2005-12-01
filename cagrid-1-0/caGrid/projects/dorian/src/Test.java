import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.common.FaultUtil;

import org.globus.wsrf.utils.FaultHelper;

public class Test {

	
	public static int test() throws Exception{
		if(true){
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Test");	
			throw fault;
//			helper.setDescription(gov.nih.nci.cagrid.gums.common.IOUtils.getExceptionMessage(e));
		}
		return 1;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			try{
				test();
			}catch(Exception e){
				GUMSInternalFault fault = new GUMSInternalFault();
				fault.setFaultString("Problem");
				FaultHelper helper = new FaultHelper(fault);
	            helper.setDescription(gov.nih.nci.cagrid.gums.common.IOUtils.getExceptionMessage(e));
				helper.addFaultCause(e);
				fault = (GUMSInternalFault) helper.getFault();
				throw fault;
			}
		
		}catch(Exception e){
			FaultUtil.printFault(e);
		}
	}

}
