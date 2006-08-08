/**
 * GeoCode_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package us.geocoder.rpc.Geo.Coder.US;

public interface GeoCode_PortType extends java.rmi.Remote {
    public us.geocoder.rpc.Geo.Coder.US.GeocoderResult[] geocode(java.lang.String location) throws java.rmi.RemoteException;
    public us.geocoder.rpc.Geo.Coder.US.GeocoderAddressResult[] geocode_address(java.lang.String address) throws java.rmi.RemoteException;
    public us.geocoder.rpc.Geo.Coder.US.GeocoderIntersectionResult[] geocode_intersection(java.lang.String intersection) throws java.rmi.RemoteException;
}
