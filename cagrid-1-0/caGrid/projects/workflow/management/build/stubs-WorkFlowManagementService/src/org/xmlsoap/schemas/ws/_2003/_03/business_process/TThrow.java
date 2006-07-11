/**
 * TThrow.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.ws._2003._03.business_process;

public class TThrow  extends org.xmlsoap.schemas.ws._2003._03.business_process.TActivity  implements java.io.Serializable {
    private javax.xml.namespace.QName faultName;  // attribute
    private org.apache.axis.types.NCName faultVariable;  // attribute

    public TThrow() {
    }

    public TThrow(
           javax.xml.namespace.QName faultName,
           org.apache.axis.types.NCName faultVariable) {
           this.faultName = faultName;
           this.faultVariable = faultVariable;
    }


    /**
     * Gets the faultName value for this TThrow.
     * 
     * @return faultName
     */
    public javax.xml.namespace.QName getFaultName() {
        return faultName;
    }


    /**
     * Sets the faultName value for this TThrow.
     * 
     * @param faultName
     */
    public void setFaultName(javax.xml.namespace.QName faultName) {
        this.faultName = faultName;
    }


    /**
     * Gets the faultVariable value for this TThrow.
     * 
     * @return faultVariable
     */
    public org.apache.axis.types.NCName getFaultVariable() {
        return faultVariable;
    }


    /**
     * Sets the faultVariable value for this TThrow.
     * 
     * @param faultVariable
     */
    public void setFaultVariable(org.apache.axis.types.NCName faultVariable) {
        this.faultVariable = faultVariable;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TThrow)) return false;
        TThrow other = (TThrow) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.faultName==null && other.getFaultName()==null) || 
             (this.faultName!=null &&
              this.faultName.equals(other.getFaultName()))) &&
            ((this.faultVariable==null && other.getFaultVariable()==null) || 
             (this.faultVariable!=null &&
              this.faultVariable.equals(other.getFaultVariable())));
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
        if (getFaultName() != null) {
            _hashCode += getFaultName().hashCode();
        }
        if (getFaultVariable() != null) {
            _hashCode += getFaultVariable().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TThrow.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tThrow"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("faultName");
        attrField.setXmlName(new javax.xml.namespace.QName("", "faultName"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "QName"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("faultVariable");
        attrField.setXmlName(new javax.xml.namespace.QName("", "faultVariable"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
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
