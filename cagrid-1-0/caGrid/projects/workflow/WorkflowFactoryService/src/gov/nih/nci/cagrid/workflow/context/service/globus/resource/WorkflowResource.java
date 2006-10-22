package gov.nih.nci.cagrid.workflow.context.service.globus.resource;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.workflow.service.ActiveBPELAdapter;
import gov.nih.nci.cagrid.workflow.stubs.types.WMSInputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WSDLReferences;

import java.io.File;
import java.util.Calendar;

import org.globus.wsrf.Resource;

public class WorkflowResource implements Resource {

	public String workflowName = null;

	public String bpelFileName = null;

	public String bpelProcess = null;

	public static String BPEL_EXTENSION = ".bpel";

	protected String abAdminRoot = "http://localhost:8080/active-bpel/services/";

	public WorkflowResource(WMSInputType input, Calendar terminationTime)
			throws Exception {
		File bpelFile = null;
		this.workflowName = input.getWorkflowName();
		this.bpelProcess = input.getBpelDoc();
		// TODO Auto-generated constructor stub
		String bpelFileName = System.getProperty("java.io.tmpdir")
				+ File.separator + workflowName + BPEL_EXTENSION;
		bpelFile = new File(bpelFileName);
		bpelFile.deleteOnExit();
		Utils.stringBufferToFile(new StringBuffer(bpelProcess), bpelFileName);
		WSDLReferences[] wsdlRefArray = input.getWsdlReferences();
		String returnString = deploy(bpelFileName, workflowName, wsdlRefArray);
	}

	private String deploy(String bpelFileName, String workflowName,
			WSDLReferences[] wsdlRefArray) throws Exception {
		String abAdminUrl = this.abAdminRoot + "BpelEngineAdmin";
		return ActiveBPELAdapter.deployBpr(abAdminUrl, bpelFileName,
				workflowName, wsdlRefArray);
	}

}
