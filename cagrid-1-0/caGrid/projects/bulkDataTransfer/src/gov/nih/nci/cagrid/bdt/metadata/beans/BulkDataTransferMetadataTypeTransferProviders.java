/**
 * BulkDataTransferMetadataTypeTransferProviders.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.bdt.metadata.beans;

public class BulkDataTransferMetadataTypeTransferProviders  implements java.io.Serializable {
    private gov.nih.nci.cagrid.bdt.metadata.beans.TransferProviderType[] transferProvider;

    public BulkDataTransferMetadataTypeTransferProviders() {
    }

    public BulkDataTransferMetadataTypeTransferProviders(
           gov.nih.nci.cagrid.bdt.metadata.beans.TransferProviderType[] transferProvider) {
           this.transferProvider = transferProvider;
    }


    /**
     * Gets the transferProvider value for this BulkDataTransferMetadataTypeTransferProviders.
     * 
     * @return transferProvider
     */
    public gov.nih.nci.cagrid.bdt.metadata.beans.TransferProviderType[] getTransferProvider() {
        return transferProvider;
    }


    /**
     * Sets the transferProvider value for this BulkDataTransferMetadataTypeTransferProviders.
     * 
     * @param transferProvider
     */
    public void setTransferProvider(gov.nih.nci.cagrid.bdt.metadata.beans.TransferProviderType[] transferProvider) {
        this.transferProvider = transferProvider;
    }

    public gov.nih.nci.cagrid.bdt.metadata.beans.TransferProviderType getTransferProvider(int i) {
        return this.transferProvider[i];
    }

    public void setTransferProvider(int i, gov.nih.nci.cagrid.bdt.metadata.beans.TransferProviderType _value) {
        this.transferProvider[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BulkDataTransferMetadataTypeTransferProviders)) return false;
        BulkDataTransferMetadataTypeTransferProviders other = (BulkDataTransferMetadataTypeTransferProviders) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.transferProvider==null && other.getTransferProvider()==null) || 
             (this.transferProvider!=null &&
              java.util.Arrays.equals(this.transferProvider, other.getTransferProvider())));
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
        if (getTransferProvider() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTransferProvider());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTransferProvider(), i);
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
        new org.apache.axis.description.TypeDesc(BulkDataTransferMetadataTypeTransferProviders.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://cagrid.nci.nih.gov/1/BulkDataTransferMetadata", ">BulkDataTransferMetadataType>TransferProviders"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transferProvider");
        elemField.setXmlName(new javax.xml.namespace.QName("http://cagrid.nci.nih.gov/1/BulkDataTransferMetadata", "TransferProvider"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://cagrid.nci.nih.gov/1/BulkDataTransferMetadata", "TransferProviderType"));
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
