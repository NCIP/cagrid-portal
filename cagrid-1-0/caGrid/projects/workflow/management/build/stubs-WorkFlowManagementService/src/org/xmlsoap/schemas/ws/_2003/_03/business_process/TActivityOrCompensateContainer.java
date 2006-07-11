/**
 * TActivityOrCompensateContainer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.ws._2003._03.business_process;

public class TActivityOrCompensateContainer  extends org.xmlsoap.schemas.ws._2003._03.business_process.TExtensibleElements  implements java.io.Serializable {
    private org.xmlsoap.schemas.ws._2003._03.business_process.Activity activity;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TCompensate compensate;

    public TActivityOrCompensateContainer() {
    }

    public TActivityOrCompensateContainer(
           org.xmlsoap.schemas.ws._2003._03.business_process.Activity activity,
           org.xmlsoap.schemas.ws._2003._03.business_process.TCompensate compensate) {
           this.activity = activity;
           this.compensate = compensate;
    }


    /**
     * Gets the activity value for this TActivityOrCompensateContainer.
     * 
     * @return activity
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.Activity getActivity() {
        return activity;
    }


    /**
     * Sets the activity value for this TActivityOrCompensateContainer.
     * 
     * @param activity
     */
    public void setActivity(org.xmlsoap.schemas.ws._2003._03.business_process.Activity activity) {
        this.activity = activity;
    }


    /**
     * Gets the compensate value for this TActivityOrCompensateContainer.
     * 
     * @return compensate
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TCompensate getCompensate() {
        return compensate;
    }


    /**
     * Sets the compensate value for this TActivityOrCompensateContainer.
     * 
     * @param compensate
     */
    public void setCompensate(org.xmlsoap.schemas.ws._2003._03.business_process.TCompensate compensate) {
        this.compensate = compensate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TActivityOrCompensateContainer)) return false;
        TActivityOrCompensateContainer other = (TActivityOrCompensateContainer) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.activity==null && other.getActivity()==null) || 
             (this.activity!=null &&
              this.activity.equals(other.getActivity()))) &&
            ((this.compensate==null && other.getCompensate()==null) || 
             (this.compensate!=null &&
              this.compensate.equals(other.getCompensate())));
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
        if (getActivity() != null) {
            _hashCode += getActivity().hashCode();
        }
        if (getCompensate() != null) {
            _hashCode += getCompensate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TActivityOrCompensateContainer.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tActivityOrCompensateContainer"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "activity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "activity"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("compensate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "compensate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tCompensate"));
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
