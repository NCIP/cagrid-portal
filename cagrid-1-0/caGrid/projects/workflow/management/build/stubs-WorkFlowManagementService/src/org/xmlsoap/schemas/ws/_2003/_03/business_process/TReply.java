/**
 * TReply.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.ws._2003._03.business_process;

public class TReply  extends org.xmlsoap.schemas.ws._2003._03.business_process.TActivity  implements java.io.Serializable {
    private org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelations correlations;
    private org.apache.axis.types.NCName partnerLink;  // attribute
    private javax.xml.namespace.QName portType;  // attribute
    private org.apache.axis.types.NCName operation;  // attribute
    private org.apache.axis.types.NCName variable;  // attribute
    private javax.xml.namespace.QName faultName;  // attribute

    public TReply() {
    }

    public TReply(
           org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelations correlations,
           javax.xml.namespace.QName faultName,
           org.apache.axis.types.NCName operation,
           org.apache.axis.types.NCName partnerLink,
           javax.xml.namespace.QName portType,
           org.apache.axis.types.NCName variable) {
           this.correlations = correlations;
           this.partnerLink = partnerLink;
           this.portType = portType;
           this.operation = operation;
           this.variable = variable;
           this.faultName = faultName;
    }


    /**
     * Gets the correlations value for this TReply.
     * 
     * @return correlations
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelations getCorrelations() {
        return correlations;
    }


    /**
     * Sets the correlations value for this TReply.
     * 
     * @param correlations
     */
    public void setCorrelations(org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelations correlations) {
        this.correlations = correlations;
    }


    /**
     * Gets the partnerLink value for this TReply.
     * 
     * @return partnerLink
     */
    public org.apache.axis.types.NCName getPartnerLink() {
        return partnerLink;
    }


    /**
     * Sets the partnerLink value for this TReply.
     * 
     * @param partnerLink
     */
    public void setPartnerLink(org.apache.axis.types.NCName partnerLink) {
        this.partnerLink = partnerLink;
    }


    /**
     * Gets the portType value for this TReply.
     * 
     * @return portType
     */
    public javax.xml.namespace.QName getPortType() {
        return portType;
    }


    /**
     * Sets the portType value for this TReply.
     * 
     * @param portType
     */
    public void setPortType(javax.xml.namespace.QName portType) {
        this.portType = portType;
    }


    /**
     * Gets the operation value for this TReply.
     * 
     * @return operation
     */
    public org.apache.axis.types.NCName getOperation() {
        return operation;
    }


    /**
     * Sets the operation value for this TReply.
     * 
     * @param operation
     */
    public void setOperation(org.apache.axis.types.NCName operation) {
        this.operation = operation;
    }


    /**
     * Gets the variable value for this TReply.
     * 
     * @return variable
     */
    public org.apache.axis.types.NCName getVariable() {
        return variable;
    }


    /**
     * Sets the variable value for this TReply.
     * 
     * @param variable
     */
    public void setVariable(org.apache.axis.types.NCName variable) {
        this.variable = variable;
    }


    /**
     * Gets the faultName value for this TReply.
     * 
     * @return faultName
     */
    public javax.xml.namespace.QName getFaultName() {
        return faultName;
    }


    /**
     * Sets the faultName value for this TReply.
     * 
     * @param faultName
     */
    public void setFaultName(javax.xml.namespace.QName faultName) {
        this.faultName = faultName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TReply)) return false;
        TReply other = (TReply) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.correlations==null && other.getCorrelations()==null) || 
             (this.correlations!=null &&
              this.correlations.equals(other.getCorrelations()))) &&
            ((this.partnerLink==null && other.getPartnerLink()==null) || 
             (this.partnerLink!=null &&
              this.partnerLink.equals(other.getPartnerLink()))) &&
            ((this.portType==null && other.getPortType()==null) || 
             (this.portType!=null &&
              this.portType.equals(other.getPortType()))) &&
            ((this.operation==null && other.getOperation()==null) || 
             (this.operation!=null &&
              this.operation.equals(other.getOperation()))) &&
            ((this.variable==null && other.getVariable()==null) || 
             (this.variable!=null &&
              this.variable.equals(other.getVariable()))) &&
            ((this.faultName==null && other.getFaultName()==null) || 
             (this.faultName!=null &&
              this.faultName.equals(other.getFaultName())));
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
        if (getCorrelations() != null) {
            _hashCode += getCorrelations().hashCode();
        }
        if (getPartnerLink() != null) {
            _hashCode += getPartnerLink().hashCode();
        }
        if (getPortType() != null) {
            _hashCode += getPortType().hashCode();
        }
        if (getOperation() != null) {
            _hashCode += getOperation().hashCode();
        }
        if (getVariable() != null) {
            _hashCode += getVariable().hashCode();
        }
        if (getFaultName() != null) {
            _hashCode += getFaultName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TReply.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tReply"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("partnerLink");
        attrField.setXmlName(new javax.xml.namespace.QName("", "partnerLink"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("portType");
        attrField.setXmlName(new javax.xml.namespace.QName("", "portType"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "QName"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("operation");
        attrField.setXmlName(new javax.xml.namespace.QName("", "operation"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("variable");
        attrField.setXmlName(new javax.xml.namespace.QName("", "variable"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("faultName");
        attrField.setXmlName(new javax.xml.namespace.QName("", "faultName"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "QName"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("correlations");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "correlations"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tCorrelations"));
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
