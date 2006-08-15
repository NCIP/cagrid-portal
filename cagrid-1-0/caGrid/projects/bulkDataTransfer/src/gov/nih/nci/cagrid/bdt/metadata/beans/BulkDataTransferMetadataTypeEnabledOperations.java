/**
 * BulkDataTransferMetadataTypeEnabledOperations.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.bdt.metadata.beans;

public class BulkDataTransferMetadataTypeEnabledOperations  implements java.io.Serializable {
    private gov.nih.nci.cagrid.bdt.metadata.beans.BulkDataTransferMetadataTypeEnabledOperationsEnabledOperation enabledOperation;

    public BulkDataTransferMetadataTypeEnabledOperations() {
    }

    public BulkDataTransferMetadataTypeEnabledOperations(
           gov.nih.nci.cagrid.bdt.metadata.beans.BulkDataTransferMetadataTypeEnabledOperationsEnabledOperation enabledOperation) {
           this.enabledOperation = enabledOperation;
    }


    /**
     * Gets the enabledOperation value for this BulkDataTransferMetadataTypeEnabledOperations.
     * 
     * @return enabledOperation
     */
    public gov.nih.nci.cagrid.bdt.metadata.beans.BulkDataTransferMetadataTypeEnabledOperationsEnabledOperation getEnabledOperation() {
        return enabledOperation;
    }


    /**
     * Sets the enabledOperation value for this BulkDataTransferMetadataTypeEnabledOperations.
     * 
     * @param enabledOperation
     */
    public void setEnabledOperation(gov.nih.nci.cagrid.bdt.metadata.beans.BulkDataTransferMetadataTypeEnabledOperationsEnabledOperation enabledOperation) {
        this.enabledOperation = enabledOperation;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BulkDataTransferMetadataTypeEnabledOperations)) return false;
        BulkDataTransferMetadataTypeEnabledOperations other = (BulkDataTransferMetadataTypeEnabledOperations) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.enabledOperation==null && other.getEnabledOperation()==null) || 
             (this.enabledOperation!=null &&
              this.enabledOperation.equals(other.getEnabledOperation())));
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
        if (getEnabledOperation() != null) {
            _hashCode += getEnabledOperation().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BulkDataTransferMetadataTypeEnabledOperations.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://cagrid.nci.nih.gov/1/BulkDataTransferMetadata", ">BulkDataTransferMetadataType>EnabledOperations"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("enabledOperation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://cagrid.nci.nih.gov/1/BulkDataTransferMetadata", "EnabledOperation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://cagrid.nci.nih.gov/1/BulkDataTransferMetadata", ">>BulkDataTransferMetadataType>EnabledOperations>EnabledOperation"));
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
