/**
 * GeoCode_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package us.geocoder.rpc.Geo.Coder.US;

public class GeoCode_ServiceLocator extends org.apache.axis.client.Service implements us.geocoder.rpc.Geo.Coder.US.GeoCode_Service {

    /**
     * WSDL File for Geo Coder - Written by Scott Gunn (scott_gunn*AT*email.com)
     */

    public GeoCode_ServiceLocator() {
    }


    public GeoCode_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public GeoCode_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for GeoCode_Port
    private java.lang.String GeoCode_Port_address = "http://geocoder.us/service/soap";

    public java.lang.String getGeoCode_PortAddress() {
        return GeoCode_Port_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String GeoCode_PortWSDDServiceName = "GeoCode_Port";

    public java.lang.String getGeoCode_PortWSDDServiceName() {
        return GeoCode_PortWSDDServiceName;
    }

    public void setGeoCode_PortWSDDServiceName(java.lang.String name) {
        GeoCode_PortWSDDServiceName = name;
    }

    public us.geocoder.rpc.Geo.Coder.US.GeoCode_PortType getGeoCode_Port() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(GeoCode_Port_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getGeoCode_Port(endpoint);
    }

    public us.geocoder.rpc.Geo.Coder.US.GeoCode_PortType getGeoCode_Port(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            us.geocoder.rpc.Geo.Coder.US.GeoCode_BindingStub _stub = new us.geocoder.rpc.Geo.Coder.US.GeoCode_BindingStub(portAddress, this);
            _stub.setPortName(getGeoCode_PortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setGeoCode_PortEndpointAddress(java.lang.String address) {
        GeoCode_Port_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (us.geocoder.rpc.Geo.Coder.US.GeoCode_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                us.geocoder.rpc.Geo.Coder.US.GeoCode_BindingStub _stub = new us.geocoder.rpc.Geo.Coder.US.GeoCode_BindingStub(new java.net.URL(GeoCode_Port_address), this);
                _stub.setPortName(getGeoCode_PortWSDDServiceName());
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
        if ("GeoCode_Port".equals(inputPortName)) {
            return getGeoCode_Port();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://rpc.geocoder.us/Geo/Coder/US/", "GeoCode_Service");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://rpc.geocoder.us/Geo/Coder/US/", "GeoCode_Port"));
        }
        return ports.iterator();
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("GeoCode_Port".equals(portName)) {
            setGeoCode_PortEndpointAddress(address);
        } else { // Unknown Port Name
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
