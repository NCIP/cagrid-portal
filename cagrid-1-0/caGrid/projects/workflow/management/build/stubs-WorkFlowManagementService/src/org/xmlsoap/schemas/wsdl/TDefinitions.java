/**
 * TDefinitions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.wsdl;

public class TDefinitions  extends org.xmlsoap.schemas.wsdl.TExtensibleDocumented  implements java.io.Serializable {
    private org.xmlsoap.schemas.wsdl.AnyTopLevelOptionalElement[] anyTopLevelOptionalElement;
    private org.apache.axis.types.URI targetNamespace;  // attribute
    private org.apache.axis.types.NCName name;  // attribute

    public TDefinitions() {
    }

    public TDefinitions(
           org.xmlsoap.schemas.wsdl.AnyTopLevelOptionalElement[] anyTopLevelOptionalElement,
           org.apache.axis.types.NCName name,
           org.apache.axis.types.URI targetNamespace) {
           this.anyTopLevelOptionalElement = anyTopLevelOptionalElement;
           this.targetNamespace = targetNamespace;
           this.name = name;
    }


    /**
     * Gets the anyTopLevelOptionalElement value for this TDefinitions.
     * 
     * @return anyTopLevelOptionalElement
     */
    public org.xmlsoap.schemas.wsdl.AnyTopLevelOptionalElement[] getAnyTopLevelOptionalElement() {
        return anyTopLevelOptionalElement;
    }


    /**
     * Sets the anyTopLevelOptionalElement value for this TDefinitions.
     * 
     * @param anyTopLevelOptionalElement
     */
    public void setAnyTopLevelOptionalElement(org.xmlsoap.schemas.wsdl.AnyTopLevelOptionalElement[] anyTopLevelOptionalElement) {
        this.anyTopLevelOptionalElement = anyTopLevelOptionalElement;
    }

    public org.xmlsoap.schemas.wsdl.AnyTopLevelOptionalElement getAnyTopLevelOptionalElement(int i) {
        return this.anyTopLevelOptionalElement[i];
    }

    public void setAnyTopLevelOptionalElement(int i, org.xmlsoap.schemas.wsdl.AnyTopLevelOptionalElement _value) {
        this.anyTopLevelOptionalElement[i] = _value;
    }


    /**
     * Gets the targetNamespace value for this TDefinitions.
     * 
     * @return targetNamespace
     */
    public org.apache.axis.types.URI getTargetNamespace() {
        return targetNamespace;
    }


    /**
     * Sets the targetNamespace value for this TDefinitions.
     * 
     * @param targetNamespace
     */
    public void setTargetNamespace(org.apache.axis.types.URI targetNamespace) {
        this.targetNamespace = targetNamespace;
    }


    /**
     * Gets the name value for this TDefinitions.
     * 
     * @return name
     */
    public org.apache.axis.types.NCName getName() {
        return name;
    }


    /**
     * Sets the name value for this TDefinitions.
     * 
     * @param name
     */
    public void setName(org.apache.axis.types.NCName name) {
        this.name = name;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TDefinitions)) return false;
        TDefinitions other = (TDefinitions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.anyTopLevelOptionalElement==null && other.getAnyTopLevelOptionalElement()==null) || 
             (this.anyTopLevelOptionalElement!=null &&
              java.util.Arrays.equals(this.anyTopLevelOptionalElement, other.getAnyTopLevelOptionalElement()))) &&
            ((this.targetNamespace==null && other.getTargetNamespace()==null) || 
             (this.targetNamespace!=null &&
              this.targetNamespace.equals(other.getTargetNamespace()))) &&
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
        int _hashCode = super.hashCode();
        if (getAnyTopLevelOptionalElement() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAnyTopLevelOptionalElement());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAnyTopLevelOptionalElement(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTargetNamespace() != null) {
            _hashCode += getTargetNamespace().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TDefinitions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tDefinitions"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("targetNamespace");
        attrField.setXmlName(new javax.xml.namespace.QName("", "targetNamespace"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("name");
        attrField.setXmlName(new javax.xml.namespace.QName("", "name"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anyTopLevelOptionalElement");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "anyTopLevelOptionalElement"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "anyTopLevelOptionalElement"));
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
