/**
 * BulkDataTransferMetadataTypeEnabledOperationsEnabledOperation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.bdt.metadata.beans;

public class BulkDataTransferMetadataTypeEnabledOperationsEnabledOperation  implements java.io.Serializable {
    private java.lang.String name;  // attribute

    public BulkDataTransferMetadataTypeEnabledOperationsEnabledOperation() {
    }

    public BulkDataTransferMetadataTypeEnabledOperationsEnabledOperation(
           java.lang.String name) {
           this.name = name;
    }


    /**
     * Gets the name value for this BulkDataTransferMetadataTypeEnabledOperationsEnabledOperation.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this BulkDataTransferMetadataTypeEnabledOperationsEnabledOperation.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BulkDataTransferMetadataTypeEnabledOperationsEnabledOperation)) return false;
        BulkDataTransferMetadataTypeEnabledOperationsEnabledOperation other = (BulkDataTransferMetadataTypeEnabledOperationsEnabledOperation) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName())));
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
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BulkDataTransferMetadataTypeEnabledOperationsEnabledOperation.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://cagrid.nci.nih.gov/1/BulkDataTransferMetadata", ">>BulkDataTransferMetadataType>EnabledOperations>EnabledOperation"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("name");
        attrField.setXmlName(new javax.xml.namespace.QName("", "name"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
