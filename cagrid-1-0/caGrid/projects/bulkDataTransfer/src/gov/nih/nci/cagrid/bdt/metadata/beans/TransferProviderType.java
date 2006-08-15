/**
 * TransferProviderType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.bdt.metadata.beans;

public class TransferProviderType  implements java.io.Serializable {
    private gov.nih.nci.cagrid.bdt.metadata.beans.TransferProviderTypeOperations operations;
    private java.lang.String name;  // attribute
    private java.lang.String namespace;  // attribute
    private java.lang.String portType;  // attribute

    public TransferProviderType() {
    }

    public TransferProviderType(
           java.lang.String name,
           java.lang.String namespace,
           gov.nih.nci.cagrid.bdt.metadata.beans.TransferProviderTypeOperations operations,
           java.lang.String portType) {
           this.operations = operations;
           this.name = name;
           this.namespace = namespace;
           this.portType = portType;
    }


    /**
     * Gets the operations value for this TransferProviderType.
     * 
     * @return operations
     */
    public gov.nih.nci.cagrid.bdt.metadata.beans.TransferProviderTypeOperations getOperations() {
        return operations;
    }


    /**
     * Sets the operations value for this TransferProviderType.
     * 
     * @param operations
     */
    public void setOperations(gov.nih.nci.cagrid.bdt.metadata.beans.TransferProviderTypeOperations operations) {
        this.operations = operations;
    }


    /**
     * Gets the name value for this TransferProviderType.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this TransferProviderType.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the namespace value for this TransferProviderType.
     * 
     * @return namespace
     */
    public java.lang.String getNamespace() {
        return namespace;
    }


    /**
     * Sets the namespace value for this TransferProviderType.
     * 
     * @param namespace
     */
    public void setNamespace(java.lang.String namespace) {
        this.namespace = namespace;
    }


    /**
     * Gets the portType value for this TransferProviderType.
     * 
     * @return portType
     */
    public java.lang.String getPortType() {
        return portType;
    }


    /**
     * Sets the portType value for this TransferProviderType.
     * 
     * @param portType
     */
    public void setPortType(java.lang.String portType) {
        this.portType = portType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TransferProviderType)) return false;
        TransferProviderType other = (TransferProviderType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.operations==null && other.getOperations()==null) || 
             (this.operations!=null &&
              this.operations.equals(other.getOperations()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.namespace==null && other.getNamespace()==null) || 
             (this.namespace!=null &&
              this.namespace.equals(other.getNamespace()))) &&
            ((this.portType==null && other.getPortType()==null) || 
             (this.portType!=null &&
              this.portType.equals(other.getPortType())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getOperations() != null) {
            _hashCode += getOperations().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getNamespace() != null) {
            _hashCode += getNamespace().hashCode();
        }
        if (getPortType() != null) {
            _hashCode += getPortType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TransferProviderType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://cagrid.nci.nih.gov/1/BulkDataTransferMetadata", "TransferProviderType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("name");
        attrField.setXmlName(new javax.xml.namespace.QName("", "name"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("namespace");
        attrField.setXmlName(new javax.xml.namespace.QName("", "namespace"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("portType");
        attrField.setXmlName(new javax.xml.namespace.QName("", "portType"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("operations");
        elemField.setXmlName(new javax.xml.namespace.QName("http://cagrid.nci.nih.gov/1/BulkDataTransferMetadata", "Operations"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://cagrid.nci.nih.gov/1/BulkDataTransferMetadata", ">TransferProviderType>Operations"));
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
