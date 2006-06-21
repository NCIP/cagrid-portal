package gov.nih.nci.cagrid.workflow.wms.tests;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.workflow.stubs.Invoke;
import gov.nih.nci.cagrid.workflow.wms.stubs.WorkflowManagementServicePortType;
import gov.nih.nci.cagrid.workflow.wms.stubs.service.WMSInputType;
import gov.nih.nci.cagrid.workflow.wms.stubs.service.WMSOutputType;
import gov.nih.nci.cagrid.workflow.wms.stubs.service.WorkflowInputType;
import gov.nih.nci.cagrid.workflow.wms.stubs.service.WorkflowManagementServiceAddressingLocator;

import java.io.File;
import java.io.InputStream;
import java.rmi.RemoteException;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.AxisClient;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.utils.ClassUtils;
import org.globus.gsi.GlobusCredential;
import org.globus.wsrf.test.GridTestCase;
import org.globus.wsrf.utils.AnyHelper;

public class WMSTest extends GridTestCase {
	
	private GlobusCredential proxy;
	private EndpointReferenceType epr;
	
	public WMSTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void test() throws Exception {
		assertTrue(TEST_CONTAINER != null);
		String serviceUrl = "https://localhost:8443/wsrf/services/cagrid/WorkflowManagementService";
		System.out.println("ServiceURL: " + serviceUrl);
		this.epr = new EndpointReferenceType();
		this.epr.setAddress(new Address(serviceUrl));
		WMSInputType input =createInput("Simple.bpel");
		WMSOutputType output = runWorkflow(input);
		assertTrue(output!=null);
	}

	private WorkflowManagementServicePortType getPortType()
			throws RemoteException {

		WorkflowManagementServiceAddressingLocator locator = new WorkflowManagementServiceAddressingLocator();
		// attempt to load our context sensitive wsdd file
		InputStream resourceAsStream = ClassUtils.getResourceAsStream(
				getClass(), "client-config.wsdd");
		if (resourceAsStream != null) {
			// we found it, so tell axis to configure an engine to use it
			EngineConfiguration engineConfig = new FileProvider(
					resourceAsStream);
			// set the engine of the locator
			locator.setEngine(new AxisClient(engineConfig));
		}
		WorkflowManagementServicePortType port = null;
		try {
			port = locator.getWorkflowManagementServicePortTypePort(this.epr);
		} catch (Exception e) {
			throw new RemoteException("Unable to configured porttype:"
					+ e.getMessage(), e);
		}

		return port;
	}

	public WMSOutputType runWorkflow(WMSInputType wmsInput)
			throws RemoteException {
		WorkflowManagementServicePortType port = this.getPortType();
		org.apache.axis.client.Stub stub = (org.apache.axis.client.Stub) port;

		stub._setProperty(org.globus.wsrf.security.Constants.GSI_TRANSPORT,
				org.globus.wsrf.security.Constants.ENCRYPTION);
		if (proxy != null) {
			try {
				org.ietf.jgss.GSSCredential gss = new org.globus.gsi.gssapi.GlobusGSSCredentialImpl(
						proxy, org.ietf.jgss.GSSCredential.INITIATE_AND_ACCEPT);
				stub._setProperty(
						org.globus.axis.gsi.GSIConstants.GSI_CREDENTIALS, gss);
			} catch (org.ietf.jgss.GSSException ex) {
				throw new RemoteException(ex.getMessage());
			}
		}
		stub._setProperty(org.globus.wsrf.security.Constants.AUTHORIZATION,
				org.globus.wsrf.impl.security.authorization.NoAuthorization
						.getInstance());
		gov.nih.nci.cagrid.workflow.wms.stubs.RunWorkflowRequest params = new gov.nih.nci.cagrid.workflow.wms.stubs.RunWorkflowRequest();
		gov.nih.nci.cagrid.workflow.wms.stubs.RunWorkflowRequestWmsInput wmsInputContainer = new gov.nih.nci.cagrid.workflow.wms.stubs.RunWorkflowRequestWmsInput();
		wmsInputContainer.setWMSInputElement(wmsInput);
		params.setWmsInput(wmsInputContainer);
		gov.nih.nci.cagrid.workflow.wms.stubs.RunWorkflowResponse boxedResult = port
				.runWorkflow(params);
		return boxedResult.getWMSOutputElement();

	}
	
	public static WMSInputType createInput(String bpelFile) throws Exception {
		WMSInputType input = new WMSInputType();
		
		String bpelProcess = Utils.fileToStringBuffer(new File(bpelFile)).toString();
		
		WorkflowInputType inputArgs = new WorkflowInputType();
		Invoke invoke = new Invoke("Hello");
		inputArgs.set_any(AnyHelper.toAnyArray(invoke));
		input.setBpelDoc(bpelProcess);
		input.setInputArgs(inputArgs);
		input.setWorkflowName("Simple");
		return input;
	}

}
