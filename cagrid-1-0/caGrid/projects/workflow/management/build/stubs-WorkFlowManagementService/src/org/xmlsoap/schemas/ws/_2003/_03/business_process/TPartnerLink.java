/**
 * TPartnerLink.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.ws._2003._03.business_process;

public class TPartnerLink  extends org.xmlsoap.schemas.ws._2003._03.business_process.TExtensibleElements  implements java.io.Serializable {
    private org.apache.axis.types.NCName name;  // attribute
    private javax.xml.namespace.QName partnerLinkType;  // attribute
    private org.apache.axis.types.NCName myRole;  // attribute
    private org.apache.axis.types.NCName partnerRole;  // attribute

    public TPartnerLink() {
    }

    public TPartnerLink(
           org.apache.axis.types.NCName myRole,
           org.apache.axis.types.NCName name,
           javax.xml.namespace.QName partnerLinkType,
           org.apache.axis.types.NCName partnerRole) {
           this.name = name;
           this.partnerLinkType = partnerLinkType;
           this.myRole = myRole;
           this.partnerRole = partnerRole;
    }


    /**
     * Gets the name value for this TPartnerLink.
     * 
     * @return name
     */
    public org.apache.axis.types.NCName getName() {
        return name;
    }


    /**
     * Sets the name value for this TPartnerLink.
     * 
     * @param name
     */
    public void setName(org.apache.axis.types.NCName name) {
        this.name = name;
    }


    /**
     * Gets the partnerLinkType value for this TPartnerLink.
     * 
     * @return partnerLinkType
     */
    public javax.xml.namespace.QName getPartnerLinkType() {
        return partnerLinkType;
    }


    /**
     * Sets the partnerLinkType value for this TPartnerLink.
     * 
     * @param partnerLinkType
     */
    public void setPartnerLinkType(javax.xml.namespace.QName partnerLinkType) {
        this.partnerLinkType = partnerLinkType;
    }


    /**
     * Gets the myRole value for this TPartnerLink.
     * 
     * @return myRole
     */
    public org.apache.axis.types.NCName getMyRole() {
        return myRole;
    }


    /**
     * Sets the myRole value for this TPartnerLink.
     * 
     * @param myRole
     */
    public void setMyRole(org.apache.axis.types.NCName myRole) {
        this.myRole = myRole;
    }


    /**
     * Gets the partnerRole value for this TPartnerLink.
     * 
     * @return partnerRole
     */
    public org.apache.axis.types.NCName getPartnerRole() {
        return partnerRole;
    }


    /**
     * Sets the partnerRole value for this TPartnerLink.
     * 
     * @param partnerRole
     */
    public void setPartnerRole(org.apache.axis.types.NCName partnerRole) {
        this.partnerRole = partnerRole;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TPartnerLink)) return false;
        TPartnerLink other = (TPartnerLink) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.partnerLinkType==null && other.getPartnerLinkType()==null) || 
             (this.partnerLinkType!=null &&
              this.partnerLinkType.equals(other.getPartnerLinkType()))) &&
            ((this.myRole==null && other.getMyRole()==null) || 
             (this.myRole!=null &&
              this.myRole.equals(other.getMyRole()))) &&
            ((this.partnerRole==null && other.getPartnerRole()==null) || 
             (this.partnerRole!=null &&
              this.partnerRole.equals(other.getPartnerRole())));
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
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getPartnerLinkType() != null) {
            _hashCode += getPartnerLinkType().hashCode();
        }
        if (getMyRole() != null) {
            _hashCode += getMyRole().hashCode();
        }
        if (getPartnerRole() != null) {
            _hashCode += getPartnerRole().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TPartnerLink.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tPartnerLink"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("name");
        attrField.setXmlName(new javax.xml.namespace.QName("", "name"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("partnerLinkType");
        attrField.setXmlName(new javax.xml.namespace.QName("", "partnerLinkType"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "QName"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("myRole");
        attrField.setXmlName(new javax.xml.namespace.QName("", "myRole"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("partnerRole");
        attrField.setXmlName(new javax.xml.namespace.QName("", "partnerRole"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
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
