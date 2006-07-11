/**
 * TOperation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.wsdl;

public class TOperation  extends org.xmlsoap.schemas.wsdl.TExtensibleDocumented  implements java.io.Serializable {
    private org.xmlsoap.schemas.wsdl.RequestResponseOrOneWayOperation requestResponseOrOneWayOperation;
    private org.xmlsoap.schemas.wsdl.SolicitResponseOrNotificationOperation solicitResponseOrNotificationOperation;
    private org.apache.axis.types.NCName name;  // attribute
    private org.apache.axis.types.NMTokens parameterOrder;  // attribute

    public TOperation() {
    }

    public TOperation(
           org.apache.axis.types.NCName name,
           org.apache.axis.types.NMTokens parameterOrder,
           org.xmlsoap.schemas.wsdl.RequestResponseOrOneWayOperation requestResponseOrOneWayOperation,
           org.xmlsoap.schemas.wsdl.SolicitResponseOrNotificationOperation solicitResponseOrNotificationOperation) {
           this.requestResponseOrOneWayOperation = requestResponseOrOneWayOperation;
           this.solicitResponseOrNotificationOperation = solicitResponseOrNotificationOperation;
           this.name = name;
           this.parameterOrder = parameterOrder;
    }


    /**
     * Gets the requestResponseOrOneWayOperation value for this TOperation.
     * 
     * @return requestResponseOrOneWayOperation
     */
    public org.xmlsoap.schemas.wsdl.RequestResponseOrOneWayOperation getRequestResponseOrOneWayOperation() {
        return requestResponseOrOneWayOperation;
    }


    /**
     * Sets the requestResponseOrOneWayOperation value for this TOperation.
     * 
     * @param requestResponseOrOneWayOperation
     */
    public void setRequestResponseOrOneWayOperation(org.xmlsoap.schemas.wsdl.RequestResponseOrOneWayOperation requestResponseOrOneWayOperation) {
        this.requestResponseOrOneWayOperation = requestResponseOrOneWayOperation;
    }


    /**
     * Gets the solicitResponseOrNotificationOperation value for this TOperation.
     * 
     * @return solicitResponseOrNotificationOperation
     */
    public org.xmlsoap.schemas.wsdl.SolicitResponseOrNotificationOperation getSolicitResponseOrNotificationOperation() {
        return solicitResponseOrNotificationOperation;
    }


    /**
     * Sets the solicitResponseOrNotificationOperation value for this TOperation.
     * 
     * @param solicitResponseOrNotificationOperation
     */
    public void setSolicitResponseOrNotificationOperation(org.xmlsoap.schemas.wsdl.SolicitResponseOrNotificationOperation solicitResponseOrNotificationOperation) {
        this.solicitResponseOrNotificationOperation = solicitResponseOrNotificationOperation;
    }


    /**
     * Gets the name value for this TOperation.
     * 
     * @return name
     */
    public org.apache.axis.types.NCName getName() {
        return name;
    }


    /**
     * Sets the name value for this TOperation.
     * 
     * @param name
     */
    public void setName(org.apache.axis.types.NCName name) {
        this.name = name;
    }


    /**
     * Gets the parameterOrder value for this TOperation.
     * 
     * @return parameterOrder
     */
    public org.apache.axis.types.NMTokens getParameterOrder() {
        return parameterOrder;
    }


    /**
     * Sets the parameterOrder value for this TOperation.
     * 
     * @param parameterOrder
     */
    public void setParameterOrder(org.apache.axis.types.NMTokens parameterOrder) {
        this.parameterOrder = parameterOrder;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TOperation)) return false;
        TOperation other = (TOperation) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.requestResponseOrOneWayOperation==null && other.getRequestResponseOrOneWayOperation()==null) || 
             (this.requestResponseOrOneWayOperation!=null &&
              this.requestResponseOrOneWayOperation.equals(other.getRequestResponseOrOneWayOperation()))) &&
            ((this.solicitResponseOrNotificationOperation==null && other.getSolicitResponseOrNotificationOperation()==null) || 
             (this.solicitResponseOrNotificationOperation!=null &&
              this.solicitResponseOrNotificationOperation.equals(other.getSolicitResponseOrNotificationOperation()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.parameterOrder==null && other.getParameterOrder()==null) || 
             (this.parameterOrder!=null &&
              this.parameterOrder.equals(other.getParameterOrder())));
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
        if (getRequestResponseOrOneWayOperation() != null) {
            _hashCode += getRequestResponseOrOneWayOperation().hashCode();
        }
        if (getSolicitResponseOrNotificationOperation() != null) {
            _hashCode += getSolicitResponseOrNotificationOperation().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getParameterOrder() != null) {
            _hashCode += getParameterOrder().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TOperation.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tOperation"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("name");
        attrField.setXmlName(new javax.xml.namespace.QName("", "name"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("parameterOrder");
        attrField.setXmlName(new javax.xml.namespace.QName("", "parameterOrder"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NMTOKENS"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("requestResponseOrOneWayOperation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "request-response-or-one-way-operation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "request-response-or-one-way-operation"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("solicitResponseOrNotificationOperation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "solicit-response-or-notification-operation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "solicit-response-or-notification-operation"));
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
