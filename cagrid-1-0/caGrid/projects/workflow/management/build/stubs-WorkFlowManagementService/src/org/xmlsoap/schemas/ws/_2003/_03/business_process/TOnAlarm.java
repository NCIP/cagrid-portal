/**
 * TOnAlarm.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.ws._2003._03.business_process;

public class TOnAlarm  extends org.xmlsoap.schemas.ws._2003._03.business_process.TActivityContainer  implements java.io.Serializable {
    private java.lang.String _for;  // attribute
    private java.lang.String until;  // attribute

    public TOnAlarm() {
    }

    public TOnAlarm(
           java.lang.String _for,
           java.lang.String until) {
           this._for = _for;
           this.until = until;
    }


    /**
     * Gets the _for value for this TOnAlarm.
     * 
     * @return _for
     */
    public java.lang.String get_for() {
        return _for;
    }


    /**
     * Sets the _for value for this TOnAlarm.
     * 
     * @param _for
     */
    public void set_for(java.lang.String _for) {
        this._for = _for;
    }


    /**
     * Gets the until value for this TOnAlarm.
     * 
     * @return until
     */
    public java.lang.String getUntil() {
        return until;
    }


    /**
     * Sets the until value for this TOnAlarm.
     * 
     * @param until
     */
    public void setUntil(java.lang.String until) {
        this.until = until;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TOnAlarm)) return false;
        TOnAlarm other = (TOnAlarm) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this._for==null && other.get_for()==null) || 
             (this._for!=null &&
              this._for.equals(other.get_for()))) &&
            ((this.until==null && other.getUntil()==null) || 
             (this.until!=null &&
              this.until.equals(other.getUntil())));
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
        if (get_for() != null) {
            _hashCode += get_for().hashCode();
        }
        if (getUntil() != null) {
            _hashCode += getUntil().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TOnAlarm.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tOnAlarm"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("_for");
        attrField.setXmlName(new javax.xml.namespace.QName("", "for"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tDuration-expr"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("until");
        attrField.setXmlName(new javax.xml.namespace.QName("", "until"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tDeadline-expr"));
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
