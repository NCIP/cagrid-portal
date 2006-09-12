/**
 * GeoCode_Service.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package us.geocoder.rpc.Geo.Coder.US;

public interface GeoCode_Service extends javax.xml.rpc.Service {

    /**
     * WSDL File for Geo Coder - Written by Scott Gunn (scott_gunn*AT*email.com)
     */
    public java.lang.String getGeoCode_PortAddress();

    public us.geocoder.rpc.Geo.Coder.US.GeoCode_PortType getGeoCode_Port() throws javax.xml.rpc.ServiceException;

    public us.geocoder.rpc.Geo.Coder.US.GeoCode_PortType getGeoCode_Port(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
