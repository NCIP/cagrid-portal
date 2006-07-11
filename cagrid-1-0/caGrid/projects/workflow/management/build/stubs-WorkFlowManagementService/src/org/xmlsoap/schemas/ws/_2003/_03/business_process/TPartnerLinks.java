/**
 * TPartnerLinks.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.ws._2003._03.business_process;

public class TPartnerLinks  extends org.xmlsoap.schemas.ws._2003._03.business_process.TExtensibleElements  implements java.io.Serializable {
    private org.xmlsoap.schemas.ws._2003._03.business_process.TPartnerLink[] partnerLink;

    public TPartnerLinks() {
    }

    public TPartnerLinks(
           org.xmlsoap.schemas.ws._2003._03.business_process.TPartnerLink[] partnerLink) {
           this.partnerLink = partnerLink;
    }


    /**
     * Gets the partnerLink value for this TPartnerLinks.
     * 
     * @return partnerLink
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TPartnerLink[] getPartnerLink() {
        return partnerLink;
    }


    /**
     * Sets the partnerLink value for this TPartnerLinks.
     * 
     * @param partnerLink
     */
    public void setPartnerLink(org.xmlsoap.schemas.ws._2003._03.business_process.TPartnerLink[] partnerLink) {
        this.partnerLink = partnerLink;
    }

    public org.xmlsoap.schemas.ws._2003._03.business_process.TPartnerLink getPartnerLink(int i) {
        return this.partnerLink[i];
    }

    public void setPartnerLink(int i, org.xmlsoap.schemas.ws._2003._03.business_process.TPartnerLink _value) {
        this.partnerLink[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TPartnerLinks)) return false;
        TPartnerLinks other = (TPartnerLinks) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.partnerLink==null && other.getPartnerLink()==null) || 
             (this.partnerLink!=null &&
              java.util.Arrays.equals(this.partnerLink, other.getPartnerLink())));
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
        if (getPartnerLink() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPartnerLink());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPartnerLink(), i);
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
        new org.apache.axis.description.TypeDesc(TPartnerLinks.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tPartnerLinks"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("partnerLink");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "partnerLink"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tPartnerLink"));
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
