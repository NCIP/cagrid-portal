/**
 * TFaultHandlers.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.ws._2003._03.business_process;

public class TFaultHandlers  extends org.xmlsoap.schemas.ws._2003._03.business_process.TExtensibleElements  implements java.io.Serializable {
    private org.xmlsoap.schemas.ws._2003._03.business_process.TCatch[] _catch;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TActivityOrCompensateContainer catchAll;

    public TFaultHandlers() {
    }

    public TFaultHandlers(
           org.xmlsoap.schemas.ws._2003._03.business_process.TCatch[] _catch,
           org.xmlsoap.schemas.ws._2003._03.business_process.TActivityOrCompensateContainer catchAll) {
           this._catch = _catch;
           this.catchAll = catchAll;
    }


    /**
     * Gets the _catch value for this TFaultHandlers.
     * 
     * @return _catch
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TCatch[] get_catch() {
        return _catch;
    }


    /**
     * Sets the _catch value for this TFaultHandlers.
     * 
     * @param _catch
     */
    public void set_catch(org.xmlsoap.schemas.ws._2003._03.business_process.TCatch[] _catch) {
        this._catch = _catch;
    }

    public org.xmlsoap.schemas.ws._2003._03.business_process.TCatch get_catch(int i) {
        return this._catch[i];
    }

    public void set_catch(int i, org.xmlsoap.schemas.ws._2003._03.business_process.TCatch _value) {
        this._catch[i] = _value;
    }


    /**
     * Gets the catchAll value for this TFaultHandlers.
     * 
     * @return catchAll
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TActivityOrCompensateContainer getCatchAll() {
        return catchAll;
    }


    /**
     * Sets the catchAll value for this TFaultHandlers.
     * 
     * @param catchAll
     */
    public void setCatchAll(org.xmlsoap.schemas.ws._2003._03.business_process.TActivityOrCompensateContainer catchAll) {
        this.catchAll = catchAll;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TFaultHandlers)) return false;
        TFaultHandlers other = (TFaultHandlers) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this._catch==null && other.get_catch()==null) || 
             (this._catch!=null &&
              java.util.Arrays.equals(this._catch, other.get_catch()))) &&
            ((this.catchAll==null && other.getCatchAll()==null) || 
             (this.catchAll!=null &&
              this.catchAll.equals(other.getCatchAll())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (get_catch() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(get_catch());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(get_catch(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCatchAll() != null) {
            _hashCode += getCatchAll().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TFaultHandlers.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tFaultHandlers"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_catch");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "catch"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tCatch"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("catchAll");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "catchAll"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tActivityOrCompensateContainer"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
