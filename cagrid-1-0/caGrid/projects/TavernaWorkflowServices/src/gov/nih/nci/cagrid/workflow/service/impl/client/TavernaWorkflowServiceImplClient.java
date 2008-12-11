package gov.nih.nci.cagrid.workflow.service.impl.client;

import java.io.InputStream;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.AxisClient;
import org.apache.axis.client.Stub;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

import org.oasis.wsrf.properties.GetResourcePropertyResponse;

import org.globus.gsi.GlobusCredential;

import gov.nih.nci.cagrid.workflow.service.impl.stubs.TavernaWorkflowServiceImplPortType;
import gov.nih.nci.cagrid.workflow.service.impl.stubs.service.TavernaWorkflowServiceImplServiceAddressingLocator;
import gov.nih.nci.cagrid.workflow.service.impl.common.TavernaWorkflowServiceImplI;
import gov.nih.nci.cagrid.introduce.security.client.ServiceSecurityClient;

/**
 * This class is autogenerated, DO NOT EDIT GENERATED GRID SERVICE ACCESS METHODS.
 *
 * This client is generated automatically by Introduce to provide a clean unwrapped API to the
 * service.
 *
 * On construction the class instance will contact the remote service and retrieve it's security
 * metadata description which it will use to configure the Stub specifically for each method call.
 * 
 * @created by Introduce Toolkit version 1.3
 */
public class TavernaWorkflowServiceImplClient extends TavernaWorkflowServiceImplClientBase implements TavernaWorkflowServiceImplI {	

	public TavernaWorkflowServiceImplClient(String url) throws MalformedURIException, RemoteException {
		this(url,null);	
	}

	public TavernaWorkflowServiceImplClient(String url, GlobusCredential proxy) throws MalformedURIException, RemoteException {
	   	super(url,proxy);
	}
	
	public TavernaWorkflowServiceImplClient(EndpointReferenceType epr) throws MalformedURIException, RemoteException {
	   	this(epr,null);
	}
	
	public TavernaWorkflowServiceImplClient(EndpointReferenceType epr, GlobusCredential proxy) throws MalformedURIException, RemoteException {
	   	super(epr,proxy);
	}

	public static void usage(){
		System.out.println(TavernaWorkflowServiceImplClient.class.getName() + " -url <service url>");
	}
	
	public static void main(String [] args){
	    System.out.println("Running the Grid Service Client");
		try{
		if(!(args.length < 2)){
			if(args[0].equals("-url")){
			  TavernaWorkflowServiceImplClient client = new TavernaWorkflowServiceImplClient(args[1]);
			  // place client calls here if you want to use this main as a
			  // test....
			} else {
				usage();
				System.exit(1);
			}
		} else {
			usage();
			System.exit(1);
		}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

  public org.oasis.wsrf.lifetime.DestroyResponse destroy(org.oasis.wsrf.lifetime.Destroy params) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"destroy");
    return portType.destroy(params);
    }
  }

  public org.oasis.wsrf.lifetime.SetTerminationTimeResponse setTerminationTime(org.oasis.wsrf.lifetime.SetTerminationTime params) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"setTerminationTime");
    return portType.setTerminationTime(params);
    }
  }

  public void cancel() throws RemoteException, gov.nih.nci.cagrid.workflow.service.impl.stubs.types.CannotCancelWorkflowFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"cancel");
    gov.nih.nci.cagrid.workflow.service.impl.stubs.CancelRequest params = new gov.nih.nci.cagrid.workflow.service.impl.stubs.CancelRequest();
    gov.nih.nci.cagrid.workflow.service.impl.stubs.CancelResponse boxedResult = portType.cancel(params);
    }
  }

  public workflowmanagementfactoryservice.WorkflowStatusEventType[] getDetailedStatus() throws RemoteException, gov.nih.nci.cagrid.workflow.service.impl.stubs.types.WorkflowException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"getDetailedStatus");
    gov.nih.nci.cagrid.workflow.service.impl.stubs.GetDetailedStatusRequest params = new gov.nih.nci.cagrid.workflow.service.impl.stubs.GetDetailedStatusRequest();
    gov.nih.nci.cagrid.workflow.service.impl.stubs.GetDetailedStatusResponse boxedResult = portType.getDetailedStatus(params);
    return boxedResult.getDetailedStatusOutputElement();
    }
  }

  public workflowmanagementfactoryservice.WorkflowStatusType getStatus() throws RemoteException, gov.nih.nci.cagrid.workflow.service.impl.stubs.types.WorkflowException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"getStatus");
    gov.nih.nci.cagrid.workflow.service.impl.stubs.GetStatusRequest params = new gov.nih.nci.cagrid.workflow.service.impl.stubs.GetStatusRequest();
    gov.nih.nci.cagrid.workflow.service.impl.stubs.GetStatusResponse boxedResult = portType.getStatus(params);
    return boxedResult.getWorkflowStatusElement();
    }
  }

  public workflowmanagementfactoryservice.WorkflowOutputType getWorkflowOutput() throws RemoteException, gov.nih.nci.cagrid.workflow.service.impl.stubs.types.WorkflowException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"getWorkflowOutput");
    gov.nih.nci.cagrid.workflow.service.impl.stubs.GetWorkflowOutputRequest params = new gov.nih.nci.cagrid.workflow.service.impl.stubs.GetWorkflowOutputRequest();
    gov.nih.nci.cagrid.workflow.service.impl.stubs.GetWorkflowOutputResponse boxedResult = portType.getWorkflowOutput(params);
    return boxedResult.getWorkflowOutputElement();
    }
  }

  public workflowmanagementfactoryservice.WorkflowStatusType pause() throws RemoteException, gov.nih.nci.cagrid.workflow.service.impl.stubs.types.WorkflowException, gov.nih.nci.cagrid.workflow.service.impl.stubs.types.CannotPauseWorkflowFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"pause");
    gov.nih.nci.cagrid.workflow.service.impl.stubs.PauseRequest params = new gov.nih.nci.cagrid.workflow.service.impl.stubs.PauseRequest();
    gov.nih.nci.cagrid.workflow.service.impl.stubs.PauseResponse boxedResult = portType.pause(params);
    return boxedResult.getWorkflowStatusElement();
    }
  }

  public workflowmanagementfactoryservice.WorkflowStatusType resume() throws RemoteException, gov.nih.nci.cagrid.workflow.service.impl.stubs.types.WorkflowException, gov.nih.nci.cagrid.workflow.service.impl.stubs.types.CannotResumeWorkflowFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"resume");
    gov.nih.nci.cagrid.workflow.service.impl.stubs.ResumeRequest params = new gov.nih.nci.cagrid.workflow.service.impl.stubs.ResumeRequest();
    gov.nih.nci.cagrid.workflow.service.impl.stubs.ResumeResponse boxedResult = portType.resume(params);
    return boxedResult.getWorkflowStatusElement();
    }
  }

  public workflowmanagementfactoryservice.WorkflowStatusType start(workflowmanagementfactoryservice.StartInputType startInputElement) throws RemoteException, gov.nih.nci.cagrid.workflow.service.impl.stubs.types.CannotStartWorkflowFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"start");
    gov.nih.nci.cagrid.workflow.service.impl.stubs.StartRequest params = new gov.nih.nci.cagrid.workflow.service.impl.stubs.StartRequest();
    gov.nih.nci.cagrid.workflow.service.impl.stubs.StartRequestStartInputElement startInputElementContainer = new gov.nih.nci.cagrid.workflow.service.impl.stubs.StartRequestStartInputElement();
    startInputElementContainer.setStartInputElement(startInputElement);
    params.setStartInputElement(startInputElementContainer);
    gov.nih.nci.cagrid.workflow.service.impl.stubs.StartResponse boxedResult = portType.start(params);
    return boxedResult.getWorkflowStatusElement();
    }
  }

  public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(org.oasis.wsrf.properties.GetMultipleResourceProperties_Element params) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"getMultipleResourceProperties");
    return portType.getMultipleResourceProperties(params);
    }
  }

  public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName params) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"getResourceProperty");
    return portType.getResourceProperty(params);
    }
  }

  public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element params) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"queryResourceProperties");
    return portType.queryResourceProperties(params);
    }
  }

}
