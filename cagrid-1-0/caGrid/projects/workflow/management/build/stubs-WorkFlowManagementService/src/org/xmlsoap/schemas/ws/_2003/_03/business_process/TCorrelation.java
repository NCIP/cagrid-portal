/**
 * TCorrelation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.ws._2003._03.business_process;

public class TCorrelation  extends org.xmlsoap.schemas.ws._2003._03.business_process.TExtensibleElements  implements java.io.Serializable {
    private org.apache.axis.types.NCName set;  // attribute
    private org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean initiate;  // attribute

    public TCorrelation() {
    }

    public TCorrelation(
           org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean initiate,
           org.apache.axis.types.NCName set) {
           this.set = set;
           this.initiate = initiate;
    }


    /**
     * Gets the set value for this TCorrelation.
     * 
     * @return set
     */
    public org.apache.axis.types.NCName getSet() {
        return set;
    }


    /**
     * Sets the set value for this TCorrelation.
     * 
     * @param set
     */
    public void setSet(org.apache.axis.types.NCName set) {
        this.set = set;
    }


    /**
     * Gets the initiate value for this TCorrelation.
     * 
     * @return initiate
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean getInitiate() {
        return initiate;
    }


    /**
     * Sets the initiate value for this TCorrelation.
     * 
     * @param initiate
     */
    public void setInitiate(org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean initiate) {
        this.initiate = initiate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TCorrelation)) return false;
        TCorrelation other = (TCorrelation) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.set==null && other.getSet()==null) || 
             (this.set!=null &&
              this.set.equals(other.getSet()))) &&
            ((this.initiate==null && other.getInitiate()==null) || 
             (this.initiate!=null &&
              this.initiate.equals(other.getInitiate())));
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
        if (getSet() != null) {
            _hashCode += getSet().hashCode();
        }
        if (getInitiate() != null) {
            _hashCode += getInitiate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TCorrelation.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tCorrelation"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("set");
        attrField.setXmlName(new javax.xml.namespace.QName("", "set"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("initiate");
        attrField.setXmlName(new javax.xml.namespace.QName("", "initiate"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tBoolean"));
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
