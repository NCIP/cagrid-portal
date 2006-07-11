/**
 * TCorrelationSets.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.ws._2003._03.business_process;

public class TCorrelationSets  extends org.xmlsoap.schemas.ws._2003._03.business_process.TExtensibleElements  implements java.io.Serializable {
    private org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelationSet[] correlationSet;

    public TCorrelationSets() {
    }

    public TCorrelationSets(
           org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelationSet[] correlationSet) {
           this.correlationSet = correlationSet;
    }


    /**
     * Gets the correlationSet value for this TCorrelationSets.
     * 
     * @return correlationSet
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelationSet[] getCorrelationSet() {
        return correlationSet;
    }


    /**
     * Sets the correlationSet value for this TCorrelationSets.
     * 
     * @param correlationSet
     */
    public void setCorrelationSet(org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelationSet[] correlationSet) {
        this.correlationSet = correlationSet;
    }

    public org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelationSet getCorrelationSet(int i) {
        return this.correlationSet[i];
    }

    public void setCorrelationSet(int i, org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelationSet _value) {
        this.correlationSet[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TCorrelationSets)) return false;
        TCorrelationSets other = (TCorrelationSets) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.correlationSet==null && other.getCorrelationSet()==null) || 
             (this.correlationSet!=null &&
              java.util.Arrays.equals(this.correlationSet, other.getCorrelationSet())));
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
        if (getCorrelationSet() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCorrelationSet());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCorrelationSet(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TCorrelationSets.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tCorrelationSets"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("correlationSet");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "correlationSet"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tCorrelationSet"));
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
