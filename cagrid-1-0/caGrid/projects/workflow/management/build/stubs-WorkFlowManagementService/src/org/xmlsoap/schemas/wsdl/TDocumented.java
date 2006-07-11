/**
 * TDocumented.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.wsdl;


/**
 * This type is extended by  component types to allow them to be documented
 */
public class TDocumented  implements java.io.Serializable {
    private org.xmlsoap.schemas.wsdl.TDocumentation documentation;

    public TDocumented() {
    }

    public TDocumented(
           org.xmlsoap.schemas.wsdl.TDocumentation documentation) {
           this.documentation = documentation;
    }


    /**
     * Gets the documentation value for this TDocumented.
     * 
     * @return documentation
     */
    public org.xmlsoap.schemas.wsdl.TDocumentation getDocumentation() {
        return documentation;
    }


    /**
     * Sets the documentation value for this TDocumented.
     * 
     * @param documentation
     */
    public void setDocumentation(org.xmlsoap.schemas.wsdl.TDocumentation documentation) {
        this.documentation = documentation;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TDocumented)) return false;
        TDocumented other = (TDocumented) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.documentation==null && other.getDocumentation()==null) || 
             (this.documentation!=null &&
              this.documentation.equals(other.getDocumentation())));
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
        if (getDocumentation() != null) {
            _hashCode += getDocumentation().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TDocumented.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tDocumented"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("documentation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "documentation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tDocumentation"));
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
