/**
 * TSource.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.ws._2003._03.business_process;

public class TSource  extends org.xmlsoap.schemas.ws._2003._03.business_process.TExtensibleElements  implements java.io.Serializable {
    private org.apache.axis.types.NCName linkName;  // attribute
    private java.lang.String transitionCondition;  // attribute

    public TSource() {
    }

    public TSource(
           org.apache.axis.types.NCName linkName,
           java.lang.String transitionCondition) {
           this.linkName = linkName;
           this.transitionCondition = transitionCondition;
    }


    /**
     * Gets the linkName value for this TSource.
     * 
     * @return linkName
     */
    public org.apache.axis.types.NCName getLinkName() {
        return linkName;
    }


    /**
     * Sets the linkName value for this TSource.
     * 
     * @param linkName
     */
    public void setLinkName(org.apache.axis.types.NCName linkName) {
        this.linkName = linkName;
    }


    /**
     * Gets the transitionCondition value for this TSource.
     * 
     * @return transitionCondition
     */
    public java.lang.String getTransitionCondition() {
        return transitionCondition;
    }


    /**
     * Sets the transitionCondition value for this TSource.
     * 
     * @param transitionCondition
     */
    public void setTransitionCondition(java.lang.String transitionCondition) {
        this.transitionCondition = transitionCondition;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TSource)) return false;
        TSource other = (TSource) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.linkName==null && other.getLinkName()==null) || 
             (this.linkName!=null &&
              this.linkName.equals(other.getLinkName()))) &&
            ((this.transitionCondition==null && other.getTransitionCondition()==null) || 
             (this.transitionCondition!=null &&
              this.transitionCondition.equals(other.getTransitionCondition())));
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
        if (getLinkName() != null) {
            _hashCode += getLinkName().hashCode();
        }
        if (getTransitionCondition() != null) {
            _hashCode += getTransitionCondition().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TSource.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tSource"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("linkName");
        attrField.setXmlName(new javax.xml.namespace.QName("", "linkName"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("transitionCondition");
        attrField.setXmlName(new javax.xml.namespace.QName("", "transitionCondition"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tBoolean-expr"));
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
