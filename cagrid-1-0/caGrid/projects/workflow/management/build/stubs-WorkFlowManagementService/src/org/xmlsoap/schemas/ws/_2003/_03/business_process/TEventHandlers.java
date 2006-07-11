/**
 * TEventHandlers.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.ws._2003._03.business_process;

public class TEventHandlers  extends org.xmlsoap.schemas.ws._2003._03.business_process.TExtensibleElements  implements java.io.Serializable {
    private org.xmlsoap.schemas.ws._2003._03.business_process.TOnMessage[] onMessage;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TOnAlarm[] onAlarm;

    public TEventHandlers() {
    }

    public TEventHandlers(
           org.xmlsoap.schemas.ws._2003._03.business_process.TOnAlarm[] onAlarm,
           org.xmlsoap.schemas.ws._2003._03.business_process.TOnMessage[] onMessage) {
           this.onMessage = onMessage;
           this.onAlarm = onAlarm;
    }


    /**
     * Gets the onMessage value for this TEventHandlers.
     * 
     * @return onMessage
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TOnMessage[] getOnMessage() {
        return onMessage;
    }


    /**
     * Sets the onMessage value for this TEventHandlers.
     * 
     * @param onMessage
     */
    public void setOnMessage(org.xmlsoap.schemas.ws._2003._03.business_process.TOnMessage[] onMessage) {
        this.onMessage = onMessage;
    }

    public org.xmlsoap.schemas.ws._2003._03.business_process.TOnMessage getOnMessage(int i) {
        return this.onMessage[i];
    }

    public void setOnMessage(int i, org.xmlsoap.schemas.ws._2003._03.business_process.TOnMessage _value) {
        this.onMessage[i] = _value;
    }


    /**
     * Gets the onAlarm value for this TEventHandlers.
     * 
     * @return onAlarm
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TOnAlarm[] getOnAlarm() {
        return onAlarm;
    }


    /**
     * Sets the onAlarm value for this TEventHandlers.
     * 
     * @param onAlarm
     */
    public void setOnAlarm(org.xmlsoap.schemas.ws._2003._03.business_process.TOnAlarm[] onAlarm) {
        this.onAlarm = onAlarm;
    }

    public org.xmlsoap.schemas.ws._2003._03.business_process.TOnAlarm getOnAlarm(int i) {
        return this.onAlarm[i];
    }

    public void setOnAlarm(int i, org.xmlsoap.schemas.ws._2003._03.business_process.TOnAlarm _value) {
        this.onAlarm[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TEventHandlers)) return false;
        TEventHandlers other = (TEventHandlers) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.onMessage==null && other.getOnMessage()==null) || 
             (this.onMessage!=null &&
              java.util.Arrays.equals(this.onMessage, other.getOnMessage()))) &&
            ((this.onAlarm==null && other.getOnAlarm()==null) || 
             (this.onAlarm!=null &&
              java.util.Arrays.equals(this.onAlarm, other.getOnAlarm())));
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
        if (getOnMessage() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOnMessage());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOnMessage(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getOnAlarm() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOnAlarm());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOnAlarm(), i);
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
        new org.apache.axis.description.TypeDesc(TEventHandlers.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tEventHandlers"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("onMessage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "onMessage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tOnMessage"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("onAlarm");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "onAlarm"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tOnAlarm"));
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
