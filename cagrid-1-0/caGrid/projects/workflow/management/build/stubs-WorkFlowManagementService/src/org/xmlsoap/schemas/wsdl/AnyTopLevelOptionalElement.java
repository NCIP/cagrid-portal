/**
 * AnyTopLevelOptionalElement.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.wsdl;


/**
 * Any top level optional element allowed to appear more then once
 * - any child of definitions element except wsdl:types. Any extensibility
 * element is allowed in any place.
 */
public class AnyTopLevelOptionalElement  implements java.io.Serializable {
    private org.xmlsoap.schemas.wsdl.TImport _import;
    private org.xmlsoap.schemas.wsdl.TTypes types;
    private org.xmlsoap.schemas.wsdl.TMessage message;
    private org.xmlsoap.schemas.wsdl.TPortType portType;
    private org.xmlsoap.schemas.wsdl.TBinding binding;
    private org.xmlsoap.schemas.wsdl.TService service;

    public AnyTopLevelOptionalElement() {
    }

    public AnyTopLevelOptionalElement(
           org.xmlsoap.schemas.wsdl.TImport _import,
           org.xmlsoap.schemas.wsdl.TBinding binding,
           org.xmlsoap.schemas.wsdl.TMessage message,
           org.xmlsoap.schemas.wsdl.TPortType portType,
           org.xmlsoap.schemas.wsdl.TService service,
           org.xmlsoap.schemas.wsdl.TTypes types) {
           this._import = _import;
           this.types = types;
           this.message = message;
           this.portType = portType;
           this.binding = binding;
           this.service = service;
    }


    /**
     * Gets the _import value for this AnyTopLevelOptionalElement.
     * 
     * @return _import
     */
    public org.xmlsoap.schemas.wsdl.TImport get_import() {
        return _import;
    }


    /**
     * Sets the _import value for this AnyTopLevelOptionalElement.
     * 
     * @param _import
     */
    public void set_import(org.xmlsoap.schemas.wsdl.TImport _import) {
        this._import = _import;
    }


    /**
     * Gets the types value for this AnyTopLevelOptionalElement.
     * 
     * @return types
     */
    public org.xmlsoap.schemas.wsdl.TTypes getTypes() {
        return types;
    }


    /**
     * Sets the types value for this AnyTopLevelOptionalElement.
     * 
     * @param types
     */
    public void setTypes(org.xmlsoap.schemas.wsdl.TTypes types) {
        this.types = types;
    }


    /**
     * Gets the message value for this AnyTopLevelOptionalElement.
     * 
     * @return message
     */
    public org.xmlsoap.schemas.wsdl.TMessage getMessage() {
        return message;
    }


    /**
     * Sets the message value for this AnyTopLevelOptionalElement.
     * 
     * @param message
     */
    public void setMessage(org.xmlsoap.schemas.wsdl.TMessage message) {
        this.message = message;
    }


    /**
     * Gets the portType value for this AnyTopLevelOptionalElement.
     * 
     * @return portType
     */
    public org.xmlsoap.schemas.wsdl.TPortType getPortType() {
        return portType;
    }


    /**
     * Sets the portType value for this AnyTopLevelOptionalElement.
     * 
     * @param portType
     */
    public void setPortType(org.xmlsoap.schemas.wsdl.TPortType portType) {
        this.portType = portType;
    }


    /**
     * Gets the binding value for this AnyTopLevelOptionalElement.
     * 
     * @return binding
     */
    public org.xmlsoap.schemas.wsdl.TBinding getBinding() {
        return binding;
    }


    /**
     * Sets the binding value for this AnyTopLevelOptionalElement.
     * 
     * @param binding
     */
    public void setBinding(org.xmlsoap.schemas.wsdl.TBinding binding) {
        this.binding = binding;
    }


    /**
     * Gets the service value for this AnyTopLevelOptionalElement.
     * 
     * @return service
     */
    public org.xmlsoap.schemas.wsdl.TService getService() {
        return service;
    }


    /**
     * Sets the service value for this AnyTopLevelOptionalElement.
     * 
     * @param service
     */
    public void setService(org.xmlsoap.schemas.wsdl.TService service) {
        this.service = service;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AnyTopLevelOptionalElement)) return false;
        AnyTopLevelOptionalElement other = (AnyTopLevelOptionalElement) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this._import==null && other.get_import()==null) || 
             (this._import!=null &&
              this._import.equals(other.get_import()))) &&
            ((this.types==null && other.getTypes()==null) || 
             (this.types!=null &&
              this.types.equals(other.getTypes()))) &&
            ((this.message==null && other.getMessage()==null) || 
             (this.message!=null &&
              this.message.equals(other.getMessage()))) &&
            ((this.portType==null && other.getPortType()==null) || 
             (this.portType!=null &&
              this.portType.equals(other.getPortType()))) &&
            ((this.binding==null && other.getBinding()==null) || 
             (this.binding!=null &&
              this.binding.equals(other.getBinding()))) &&
            ((this.service==null && other.getService()==null) || 
             (this.service!=null &&
              this.service.equals(other.getService())));
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
        if (get_import() != null) {
            _hashCode += get_import().hashCode();
        }
        if (getTypes() != null) {
            _hashCode += getTypes().hashCode();
        }
        if (getMessage() != null) {
            _hashCode += getMessage().hashCode();
        }
        if (getPortType() != null) {
            _hashCode += getPortType().hashCode();
        }
        if (getBinding() != null) {
            _hashCode += getBinding().hashCode();
        }
        if (getService() != null) {
            _hashCode += getService().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AnyTopLevelOptionalElement.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "anyTopLevelOptionalElement"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_import");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "import"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tImport"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("types");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "types"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tTypes"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("message");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "message"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tMessage"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("portType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "portType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tPortType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("binding");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "binding"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tBinding"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("service");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "service"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tService"));
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
