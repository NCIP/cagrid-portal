/**
 * TSwitch.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.ws._2003._03.business_process;

public class TSwitch  extends org.xmlsoap.schemas.ws._2003._03.business_process.TActivity  implements java.io.Serializable {
    private org.xmlsoap.schemas.ws._2003._03.business_process.TSwitchCase[] _case;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TActivityContainer otherwise;

    public TSwitch() {
    }

    public TSwitch(
           org.xmlsoap.schemas.ws._2003._03.business_process.TSwitchCase[] _case,
           org.xmlsoap.schemas.ws._2003._03.business_process.TActivityContainer otherwise) {
           this._case = _case;
           this.otherwise = otherwise;
    }


    /**
     * Gets the _case value for this TSwitch.
     * 
     * @return _case
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TSwitchCase[] get_case() {
        return _case;
    }


    /**
     * Sets the _case value for this TSwitch.
     * 
     * @param _case
     */
    public void set_case(org.xmlsoap.schemas.ws._2003._03.business_process.TSwitchCase[] _case) {
        this._case = _case;
    }

    public org.xmlsoap.schemas.ws._2003._03.business_process.TSwitchCase get_case(int i) {
        return this._case[i];
    }

    public void set_case(int i, org.xmlsoap.schemas.ws._2003._03.business_process.TSwitchCase _value) {
        this._case[i] = _value;
    }


    /**
     * Gets the otherwise value for this TSwitch.
     * 
     * @return otherwise
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TActivityContainer getOtherwise() {
        return otherwise;
    }


    /**
     * Sets the otherwise value for this TSwitch.
     * 
     * @param otherwise
     */
    public void setOtherwise(org.xmlsoap.schemas.ws._2003._03.business_process.TActivityContainer otherwise) {
        this.otherwise = otherwise;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TSwitch)) return false;
        TSwitch other = (TSwitch) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this._case==null && other.get_case()==null) || 
             (this._case!=null &&
              java.util.Arrays.equals(this._case, other.get_case()))) &&
            ((this.otherwise==null && other.getOtherwise()==null) || 
             (this.otherwise!=null &&
              this.otherwise.equals(other.getOtherwise())));
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
        if (get_case() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(get_case());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(get_case(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getOtherwise() != null) {
            _hashCode += getOtherwise().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TSwitch.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tSwitch"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_case");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "case"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", ">tSwitch>case"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("otherwise");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "otherwise"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tActivityContainer"));
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
