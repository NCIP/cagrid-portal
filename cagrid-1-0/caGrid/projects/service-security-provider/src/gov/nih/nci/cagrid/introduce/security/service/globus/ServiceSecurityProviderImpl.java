package gov.nih.nci.cagrid.introduce.security.service.globus;

import gov.nih.nci.cagrid.introduce.security.service.ServiceSecurityImpl;

import java.rmi.RemoteException;


/**
 * DO NOT EDIT: This class is autogenerated!
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class ServiceSecurityProviderImpl {

	ServiceSecurityImpl impl;


	public ServiceSecurityProviderImpl() throws RemoteException {
		impl = new ServiceSecurityImpl();
	}


	public gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse getServiceSecurityMetadata(
		gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataRequest params) throws RemoteException {
		gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse boxedResult = new gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse();
		boxedResult.setServiceSecurityMetadata(impl.getServiceSecurityMetadata());
		return boxedResult;
	}

}