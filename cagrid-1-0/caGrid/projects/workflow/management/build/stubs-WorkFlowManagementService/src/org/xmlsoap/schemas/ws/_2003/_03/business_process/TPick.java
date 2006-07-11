/**
 * TPick.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.ws._2003._03.business_process;

public class TPick  extends org.xmlsoap.schemas.ws._2003._03.business_process.TActivity  implements java.io.Serializable {
    private org.xmlsoap.schemas.ws._2003._03.business_process.TOnMessage[] onMessage;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TOnAlarm[] onAlarm;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean createInstance;  // attribute

    public TPick() {
    }

    public TPick(
           org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean createInstance,
           org.xmlsoap.schemas.ws._2003._03.business_process.TOnAlarm[] onAlarm,
           org.xmlsoap.schemas.ws._2003._03.business_process.TOnMessage[] onMessage) {
           this.onMessage = onMessage;
           this.onAlarm = onAlarm;
           this.createInstance = createInstance;
    }


    /**
     * Gets the onMessage value for this TPick.
     * 
     * @return onMessage
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TOnMessage[] getOnMessage() {
        return onMessage;
    }


    /**
     * Sets the onMessage value for this TPick.
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
     * Gets the onAlarm value for this TPick.
     * 
     * @return onAlarm
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TOnAlarm[] getOnAlarm() {
        return onAlarm;
    }


    /**
     * Sets the onAlarm value for this TPick.
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


    /**
     * Gets the createInstance value for this TPick.
     * 
     * @return createInstance
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean getCreateInstance() {
        return createInstance;
    }


    /**
     * Sets the createInstance value for this TPick.
     * 
     * @param createInstance
     */
    public void setCreateInstance(org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean createInstance) {
        this.createInstance = createInstance;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TPick)) return false;
        TPick other = (TPick) obj;
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
              java.util.Arrays.equals(this.onAlarm, other.getOnAlarm()))) &&
            ((this.createInstance==null && other.getCreateInstance()==null) || 
             (this.createInstance!=null &&
              this.createInstance.equals(other.getCreateInstance())));
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
        if (getCreateInstance() != null) {
            _hashCode += getCreateInstance().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TPick.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tPick"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("createInstance");
        attrField.setXmlName(new javax.xml.namespace.QName("", "createInstance"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tBoolean"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("onMessage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "onMessage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tOnMessage"));
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
