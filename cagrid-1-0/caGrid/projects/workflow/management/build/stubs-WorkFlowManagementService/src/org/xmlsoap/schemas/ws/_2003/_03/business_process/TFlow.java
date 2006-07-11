/**
 * TFlow.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.ws._2003._03.business_process;

public class TFlow  extends org.xmlsoap.schemas.ws._2003._03.business_process.TActivity  implements java.io.Serializable {
    private org.xmlsoap.schemas.ws._2003._03.business_process.TLinks links;
    private org.xmlsoap.schemas.ws._2003._03.business_process.Activity[] activity;

    public TFlow() {
    }

    public TFlow(
           org.xmlsoap.schemas.ws._2003._03.business_process.Activity[] activity,
           org.xmlsoap.schemas.ws._2003._03.business_process.TLinks links) {
           this.links = links;
           this.activity = activity;
    }


    /**
     * Gets the links value for this TFlow.
     * 
     * @return links
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TLinks getLinks() {
        return links;
    }


    /**
     * Sets the links value for this TFlow.
     * 
     * @param links
     */
    public void setLinks(org.xmlsoap.schemas.ws._2003._03.business_process.TLinks links) {
        this.links = links;
    }


    /**
     * Gets the activity value for this TFlow.
     * 
     * @return activity
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.Activity[] getActivity() {
        return activity;
    }


    /**
     * Sets the activity value for this TFlow.
     * 
     * @param activity
     */
    public void setActivity(org.xmlsoap.schemas.ws._2003._03.business_process.Activity[] activity) {
        this.activity = activity;
    }

    public org.xmlsoap.schemas.ws._2003._03.business_process.Activity getActivity(int i) {
        return this.activity[i];
    }

    public void setActivity(int i, org.xmlsoap.schemas.ws._2003._03.business_process.Activity _value) {
        this.activity[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TFlow)) return false;
        TFlow other = (TFlow) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.links==null && other.getLinks()==null) || 
             (this.links!=null &&
              this.links.equals(other.getLinks()))) &&
            ((this.activity==null && other.getActivity()==null) || 
             (this.activity!=null &&
              java.util.Arrays.equals(this.activity, other.getActivity())));
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
        if (getLinks() != null) {
            _hashCode += getLinks().hashCode();
        }
        if (getActivity() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getActivity());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getActivity(), i);
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
        new org.apache.axis.description.TypeDesc(TFlow.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tFlow"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("links");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "links"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tLinks"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "activity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "activity"));
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
