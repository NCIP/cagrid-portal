/**
 * GeoCode_BindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package us.geocoder.rpc.Geo.Coder.US;

public class GeoCode_BindingStub extends org.apache.axis.client.Stub implements us.geocoder.rpc.Geo.Coder.US.GeoCode_PortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[3];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1() {
        org.apache.axis.description.OperationDesc oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("geocode");
        oper.addParameter(new javax.xml.namespace.QName("", "location"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://rpc.geocoder.us/Geo/Coder/US/", "ArrayOfGeocoderResult"));
        oper.setReturnClass(us.geocoder.rpc.Geo.Coder.US.GeocoderResult[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "results"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("geocode_address");
        oper.addParameter(new javax.xml.namespace.QName("", "address"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://rpc.geocoder.us/Geo/Coder/US/", "ArrayOfGeocoderAddressResult"));
        oper.setReturnClass(us.geocoder.rpc.Geo.Coder.US.GeocoderAddressResult[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "results"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("geocode_intersection");
        oper.addParameter(new javax.xml.namespace.QName("", "intersection"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://rpc.geocoder.us/Geo/Coder/US/", "ArrayOfGeocoderIntersectionResult"));
        oper.setReturnClass(us.geocoder.rpc.Geo.Coder.US.GeocoderIntersectionResult[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "results"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[2] = oper;

    }

    public GeoCode_BindingStub() throws org.apache.axis.AxisFault {
        this(null);
    }

    public GeoCode_BindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public GeoCode_BindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        java.lang.Class cls;
        javax.xml.namespace.QName qName;
        java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
        java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
        java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
        java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
        java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
        java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
        java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
        java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
        java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
        java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
        qName = new javax.xml.namespace.QName("http://rpc.geocoder.us/Geo/Coder/US/", "GeocoderIntersectionResult");
        cachedSerQNames.add(qName);
        cls = us.geocoder.rpc.Geo.Coder.US.GeocoderIntersectionResult.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://rpc.geocoder.us/Geo/Coder/US/", "GeocoderAddressResult");
        cachedSerQNames.add(qName);
        cls = us.geocoder.rpc.Geo.Coder.US.GeocoderAddressResult.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://rpc.geocoder.us/Geo/Coder/US/", "ArrayOfGeocoderResult");
        cachedSerQNames.add(qName);
        cls = us.geocoder.rpc.Geo.Coder.US.GeocoderResult[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://rpc.geocoder.us/Geo/Coder/US/", "GeocoderResult");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://rpc.geocoder.us/Geo/Coder/US/", "ArrayOfGeocoderAddressResult");
        cachedSerQNames.add(qName);
        cls = us.geocoder.rpc.Geo.Coder.US.GeocoderAddressResult[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://rpc.geocoder.us/Geo/Coder/US/", "GeocoderAddressResult");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://rpc.geocoder.us/Geo/Coder/US/", "ArrayOfGeocoderIntersectionResult");
        cachedSerQNames.add(qName);
        cls = us.geocoder.rpc.Geo.Coder.US.GeocoderIntersectionResult[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://rpc.geocoder.us/Geo/Coder/US/", "GeocoderIntersectionResult");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://rpc.geocoder.us/Geo/Coder/US/", "GeocoderResult");
        cachedSerQNames.add(qName);
        cls = us.geocoder.rpc.Geo.Coder.US.GeocoderResult.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call =
                    (org.apache.axis.client.Call) super.service.createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
                    _call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                    cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                    cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        } else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                    cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                    cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public us.geocoder.rpc.Geo.Coder.US.GeocoderResult[] geocode(java.lang.String location) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://rpc.geocoder.us/Geo/Coder/US#geocode");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://rpc.geocoder.us/Geo/Coder/US/", "geocode"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[]{location});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException) _resp;
        } else {
            extractAttachments(_call);
            try {
                return (us.geocoder.rpc.Geo.Coder.US.GeocoderResult[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (us.geocoder.rpc.Geo.Coder.US.GeocoderResult[]) org.apache.axis.utils.JavaUtils.convert(_resp, us.geocoder.rpc.Geo.Coder.US.GeocoderResult[].class);
            }
        }
    }

    public us.geocoder.rpc.Geo.Coder.US.GeocoderAddressResult[] geocode_address(java.lang.String address) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://rpc.geocoder.us/Geo/Coder/US#geocode_address");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://rpc.geocoder.us/Geo/Coder/US/", "geocode_address"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[]{address});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException) _resp;
        } else {
            extractAttachments(_call);
            try {
                return (us.geocoder.rpc.Geo.Coder.US.GeocoderAddressResult[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (us.geocoder.rpc.Geo.Coder.US.GeocoderAddressResult[]) org.apache.axis.utils.JavaUtils.convert(_resp, us.geocoder.rpc.Geo.Coder.US.GeocoderAddressResult[].class);
            }
        }
    }

    public us.geocoder.rpc.Geo.Coder.US.GeocoderIntersectionResult[] geocode_intersection(java.lang.String intersection) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://rpc.geocoder.us/Geo/Coder/US#geocode_intersection");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://rpc.geocoder.us/Geo/Coder/US/", "geocode_intersection"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[]{intersection});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException) _resp;
        } else {
            extractAttachments(_call);
            try {
                return (us.geocoder.rpc.Geo.Coder.US.GeocoderIntersectionResult[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (us.geocoder.rpc.Geo.Coder.US.GeocoderIntersectionResult[]) org.apache.axis.utils.JavaUtils.convert(_resp, us.geocoder.rpc.Geo.Coder.US.GeocoderIntersectionResult[].class);
            }
        }
    }

}
