/**
 * BulkDataTransferMetadataType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.bdt.metadata.beans;

public class BulkDataTransferMetadataType  implements java.io.Serializable {
    private gov.nih.nci.cagrid.bdt.metadata.beans.BulkDataTransferMetadataTypeEnabledOperations[] enabledOperations;
    private gov.nih.nci.cagrid.bdt.metadata.beans.BulkDataTransferMetadataTypeTransferProviders transferProviders;

    public BulkDataTransferMetadataType() {
    }

    public BulkDataTransferMetadataType(
           gov.nih.nci.cagrid.bdt.metadata.beans.BulkDataTransferMetadataTypeEnabledOperations[] enabledOperations,
           gov.nih.nci.cagrid.bdt.metadata.beans.BulkDataTransferMetadataTypeTransferProviders transferProviders) {
           this.enabledOperations = enabledOperations;
           this.transferProviders = transferProviders;
    }


    /**
     * Gets the enabledOperations value for this BulkDataTransferMetadataType.
     * 
     * @return enabledOperations
     */
    public gov.nih.nci.cagrid.bdt.metadata.beans.BulkDataTransferMetadataTypeEnabledOperations[] getEnabledOperations() {
        return enabledOperations;
    }


    /**
     * Sets the enabledOperations value for this BulkDataTransferMetadataType.
     * 
     * @param enabledOperations
     */
    public void setEnabledOperations(gov.nih.nci.cagrid.bdt.metadata.beans.BulkDataTransferMetadataTypeEnabledOperations[] enabledOperations) {
        this.enabledOperations = enabledOperations;
    }

    public gov.nih.nci.cagrid.bdt.metadata.beans.BulkDataTransferMetadataTypeEnabledOperations getEnabledOperations(int i) {
        return this.enabledOperations[i];
    }

    public void setEnabledOperations(int i, gov.nih.nci.cagrid.bdt.metadata.beans.BulkDataTransferMetadataTypeEnabledOperations _value) {
        this.enabledOperations[i] = _value;
    }


    /**
     * Gets the transferProviders value for this BulkDataTransferMetadataType.
     * 
     * @return transferProviders
     */
    public gov.nih.nci.cagrid.bdt.metadata.beans.BulkDataTransferMetadataTypeTransferProviders getTransferProviders() {
        return transferProviders;
    }


    /**
     * Sets the transferProviders value for this BulkDataTransferMetadataType.
     * 
     * @param transferProviders
     */
    public void setTransferProviders(gov.nih.nci.cagrid.bdt.metadata.beans.BulkDataTransferMetadataTypeTransferProviders transferProviders) {
        this.transferProviders = transferProviders;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BulkDataTransferMetadataType)) return false;
        BulkDataTransferMetadataType other = (BulkDataTransferMetadataType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.enabledOperations==null && other.getEnabledOperations()==null) || 
             (this.enabledOperations!=null &&
              java.util.Arrays.equals(this.enabledOperations, other.getEnabledOperations()))) &&
            ((this.transferProviders==null && other.getTransferProviders()==null) || 
             (this.transferProviders!=null &&
              this.transferProviders.equals(other.getTransferProviders())));
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
        if (getEnabledOperations() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEnabledOperations());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEnabledOperations(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTransferProviders() != null) {
            _hashCode += getTransferProviders().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BulkDataTransferMetadataType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://cagrid.nci.nih.gov/1/BulkDataTransferMetadata", "BulkDataTransferMetadataType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("enabledOperations");
        elemField.setXmlName(new javax.xml.namespace.QName("http://cagrid.nci.nih.gov/1/BulkDataTransferMetadata", "EnabledOperations"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://cagrid.nci.nih.gov/1/BulkDataTransferMetadata", ">BulkDataTransferMetadataType>EnabledOperations"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transferProviders");
        elemField.setXmlName(new javax.xml.namespace.QName("http://cagrid.nci.nih.gov/1/BulkDataTransferMetadata", "TransferProviders"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://cagrid.nci.nih.gov/1/BulkDataTransferMetadata", ">BulkDataTransferMetadataType>TransferProviders"));
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
