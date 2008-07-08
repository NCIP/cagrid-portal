package gov.nih.nci.cagrid.workflow.service;

import gov.nih.nci.cagrid.workflow.context.service.globus.resource.WorkflowServiceHome;
import gov.nih.nci.cagrid.workflow.stubs.types.WMSOutputType;

import java.rmi.RemoteException;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.Constants;
import org.globus.wsrf.container.ServiceHost;
import org.globus.wsrf.impl.SimpleResourceKey;
import org.globus.wsrf.utils.AddressingUtils;

/**
 * TODO:DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class WorkflowFactoryServiceImpl extends WorkflowFactoryServiceImplBase {

	public static String BPEL_EXTENSION = ".bpel";

	public static String abAdminRoot = "http://localhost:8080/active-bpel/services/";

	private static ActiveBPELAdapter abAdapter = null;

	public WorkflowFactoryServiceImpl() throws RemoteException {
		super();
	}

  public gov.nih.nci.cagrid.workflow.stubs.types.WMSOutputType createWorkflow(gov.nih.nci.cagrid.workflow.stubs.types.WMSInputType wMSInputElement) throws RemoteException, gov.nih.nci.cagrid.workflow.stubs.types.WorkflowException {
		// TODO: Implement this autogenerated method
		WMSOutputType output = new WMSOutputType();
		WorkflowServiceHome workflowHome = null;
		SimpleResourceKey key = null;
		try {
			Context ctx = new InitialContext();
			String lookupString = Constants.JNDI_SERVICES_BASE_NAME 
			+ "cagrid/WorkflowServiceImpl" + "/home";
			workflowHome = (WorkflowServiceHome) ctx.lookup(lookupString);

			key = workflowHome.create(null, wMSInputElement);
			// Create the EPR here
			EndpointReferenceType epr = new EndpointReferenceType();
			epr = AddressingUtils.createEndpointReference(ServiceHost.getBaseURL() 
					+ "cagrid/WorkflowServiceImpl", key);
			output.setWorkflowEPR(epr);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("Exception deploying workflow:", e);
		}
		return output;
	}

	public static ActiveBPELAdapter getActiveBPELAdapter() {
		return abAdapter;
	}
}
