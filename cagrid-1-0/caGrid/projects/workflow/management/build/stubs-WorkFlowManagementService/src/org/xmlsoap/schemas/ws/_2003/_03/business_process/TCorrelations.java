/**
 * TCorrelations.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.ws._2003._03.business_process;

public class TCorrelations  extends org.xmlsoap.schemas.ws._2003._03.business_process.TExtensibleElements  implements java.io.Serializable {
    private org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelation[] correlation;

    public TCorrelations() {
    }

    public TCorrelations(
           org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelation[] correlation) {
           this.correlation = correlation;
    }


    /**
     * Gets the correlation value for this TCorrelations.
     * 
     * @return correlation
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelation[] getCorrelation() {
        return correlation;
    }


    /**
     * Sets the correlation value for this TCorrelations.
     * 
     * @param correlation
     */
    public void setCorrelation(org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelation[] correlation) {
        this.correlation = correlation;
    }

    public org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelation getCorrelation(int i) {
        return this.correlation[i];
    }

    public void setCorrelation(int i, org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelation _value) {
        this.correlation[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TCorrelations)) return false;
        TCorrelations other = (TCorrelations) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.correlation==null && other.getCorrelation()==null) || 
             (this.correlation!=null &&
              java.util.Arrays.equals(this.correlation, other.getCorrelation())));
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
        if (getCorrelation() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCorrelation());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCorrelation(), i);
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
        new org.apache.axis.description.TypeDesc(TCorrelations.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tCorrelations"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("correlation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "correlation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tCorrelation"));
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
