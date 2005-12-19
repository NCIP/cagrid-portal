import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.dorian.bean.DorianInternalFault;


public class Test {

	
	public static int test() throws Exception{
		if(true){
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Test");	
			throw fault;

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
				DorianInternalFault fault = new DorianInternalFault();
				fault.setFaultString("Problem");
				FaultHelper helper = new FaultHelper(fault);
	            helper.setDescription(Utils.getExceptionMessage(e));
				helper.addFaultCause(e);
				fault = (DorianInternalFault) helper.getFault();
				throw fault;
			}
		
		}catch(Exception e){
			FaultUtil.printFault(e);
		}
	}

}
