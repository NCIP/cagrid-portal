/**
 * WorkFlowManagementServiceAddressingLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.workflow.management.stubs.service;

public class WorkFlowManagementServiceAddressingLocator extends gov.nih.nci.cagrid.workflow.management.stubs.service.WorkFlowManagementServiceLocator implements gov.nih.nci.cagrid.workflow.management.stubs.service.WorkFlowManagementServiceAddressing {
    public gov.nih.nci.cagrid.workflow.management.stubs.WorkFlowManagementServicePortType getWorkFlowManagementServicePortTypePort(org.apache.axis.message.addressing.EndpointReferenceType reference) throws javax.xml.rpc.ServiceException {
	org.apache.axis.message.addressing.AttributedURI address = reference.getAddress();
	if (address == null) {
		throw new javax.xml.rpc.ServiceException("No address in EndpointReference");
	}
	java.net.URL endpoint;
	try {
		endpoint = new java.net.URL(address.toString());
	} catch (java.net.MalformedURLException e) {
		throw new javax.xml.rpc.ServiceException(e);
	}
	gov.nih.nci.cagrid.workflow.management.stubs.WorkFlowManagementServicePortType _stub = getWorkFlowManagementServicePortTypePort(endpoint);
	if (_stub != null) {
		org.apache.axis.message.addressing.AddressingHeaders headers =
			new org.apache.axis.message.addressing.AddressingHeaders();
		headers.setTo(address);
		headers.setReferenceProperties(reference.getProperties());
		((javax.xml.rpc.Stub)_stub)._setProperty(org.apache.axis.message.addressing.Constants.ENV_ADDRESSING_SHARED_HEADERS, headers);
	}
	return _stub;
    }


}
