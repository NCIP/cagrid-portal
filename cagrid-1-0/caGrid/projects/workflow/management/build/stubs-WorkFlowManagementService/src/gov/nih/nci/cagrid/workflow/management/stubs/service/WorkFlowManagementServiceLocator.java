/**
 * WorkFlowManagementServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.workflow.management.stubs.service;

public class WorkFlowManagementServiceLocator extends org.apache.axis.client.Service implements gov.nih.nci.cagrid.workflow.management.stubs.service.WorkFlowManagementService {

    public WorkFlowManagementServiceLocator() {
    }


    public WorkFlowManagementServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WorkFlowManagementServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WorkFlowManagementServicePortTypePort
    private java.lang.String WorkFlowManagementServicePortTypePort_address = "http://localhost:8080/wsrf/services/";

    public java.lang.String getWorkFlowManagementServicePortTypePortAddress() {
        return WorkFlowManagementServicePortTypePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WorkFlowManagementServicePortTypePortWSDDServiceName = "WorkFlowManagementServicePortTypePort";

    public java.lang.String getWorkFlowManagementServicePortTypePortWSDDServiceName() {
        return WorkFlowManagementServicePortTypePortWSDDServiceName;
    }

    public void setWorkFlowManagementServicePortTypePortWSDDServiceName(java.lang.String name) {
        WorkFlowManagementServicePortTypePortWSDDServiceName = name;
    }

    public gov.nih.nci.cagrid.workflow.management.stubs.WorkFlowManagementServicePortType getWorkFlowManagementServicePortTypePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WorkFlowManagementServicePortTypePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWorkFlowManagementServicePortTypePort(endpoint);
    }

    public gov.nih.nci.cagrid.workflow.management.stubs.WorkFlowManagementServicePortType getWorkFlowManagementServicePortTypePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            gov.nih.nci.cagrid.workflow.management.stubs.bindings.WorkFlowManagementServicePortTypeSOAPBindingStub _stub = new gov.nih.nci.cagrid.workflow.management.stubs.bindings.WorkFlowManagementServicePortTypeSOAPBindingStub(portAddress, this);
            _stub.setPortName(getWorkFlowManagementServicePortTypePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWorkFlowManagementServicePortTypePortEndpointAddress(java.lang.String address) {
        WorkFlowManagementServicePortTypePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (gov.nih.nci.cagrid.workflow.management.stubs.WorkFlowManagementServicePortType.class.isAssignableFrom(serviceEndpointInterface)) {
                gov.nih.nci.cagrid.workflow.management.stubs.bindings.WorkFlowManagementServicePortTypeSOAPBindingStub _stub = new gov.nih.nci.cagrid.workflow.management.stubs.bindings.WorkFlowManagementServicePortTypeSOAPBindingStub(new java.net.URL(WorkFlowManagementServicePortTypePort_address), this);
                _stub.setPortName(getWorkFlowManagementServicePortTypePortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("WorkFlowManagementServicePortTypePort".equals(inputPortName)) {
            return getWorkFlowManagementServicePortTypePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://workflow.cagrid.nci.nih.gov//WorkFlowManagementService/service", "WorkFlowManagementService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://workflow.cagrid.nci.nih.gov//WorkFlowManagementService/service", "WorkFlowManagementServicePortTypePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("WorkFlowManagementServicePortTypePort".equals(portName)) {
            setWorkFlowManagementServicePortTypePortEndpointAddress(address);
        }
        else { // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
