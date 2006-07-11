/**
 * TCorrelationWithPattern.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.ws._2003._03.business_process;

public class TCorrelationWithPattern  extends org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelation  implements java.io.Serializable {
    private org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelationWithPatternPattern pattern;  // attribute

    public TCorrelationWithPattern() {
    }

    public TCorrelationWithPattern(
           org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelationWithPatternPattern pattern) {
           this.pattern = pattern;
    }


    /**
     * Gets the pattern value for this TCorrelationWithPattern.
     * 
     * @return pattern
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelationWithPatternPattern getPattern() {
        return pattern;
    }


    /**
     * Sets the pattern value for this TCorrelationWithPattern.
     * 
     * @param pattern
     */
    public void setPattern(org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelationWithPatternPattern pattern) {
        this.pattern = pattern;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TCorrelationWithPattern)) return false;
        TCorrelationWithPattern other = (TCorrelationWithPattern) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.pattern==null && other.getPattern()==null) || 
             (this.pattern!=null &&
              this.pattern.equals(other.getPattern())));
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
        if (getPattern() != null) {
            _hashCode += getPattern().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TCorrelationWithPattern.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tCorrelationWithPattern"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("pattern");
        attrField.setXmlName(new javax.xml.namespace.QName("", "pattern"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", ">tCorrelationWithPattern>pattern"));
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
