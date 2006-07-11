/**
 * TBindingOperation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.wsdl;

public class TBindingOperation  extends org.xmlsoap.schemas.wsdl.TExtensibleDocumented  implements java.io.Serializable {
    private org.xmlsoap.schemas.wsdl.TBindingOperationMessage input;
    private org.xmlsoap.schemas.wsdl.TBindingOperationMessage output;
    private org.xmlsoap.schemas.wsdl.TBindingOperationFault[] fault;
    private org.apache.axis.types.NCName name;  // attribute

    public TBindingOperation() {
    }

    public TBindingOperation(
           org.xmlsoap.schemas.wsdl.TBindingOperationFault[] fault,
           org.xmlsoap.schemas.wsdl.TBindingOperationMessage input,
           org.apache.axis.types.NCName name,
           org.xmlsoap.schemas.wsdl.TBindingOperationMessage output) {
           this.input = input;
           this.output = output;
           this.fault = fault;
           this.name = name;
    }


    /**
     * Gets the input value for this TBindingOperation.
     * 
     * @return input
     */
    public org.xmlsoap.schemas.wsdl.TBindingOperationMessage getInput() {
        return input;
    }


    /**
     * Sets the input value for this TBindingOperation.
     * 
     * @param input
     */
    public void setInput(org.xmlsoap.schemas.wsdl.TBindingOperationMessage input) {
        this.input = input;
    }


    /**
     * Gets the output value for this TBindingOperation.
     * 
     * @return output
     */
    public org.xmlsoap.schemas.wsdl.TBindingOperationMessage getOutput() {
        return output;
    }


    /**
     * Sets the output value for this TBindingOperation.
     * 
     * @param output
     */
    public void setOutput(org.xmlsoap.schemas.wsdl.TBindingOperationMessage output) {
        this.output = output;
    }


    /**
     * Gets the fault value for this TBindingOperation.
     * 
     * @return fault
     */
    public org.xmlsoap.schemas.wsdl.TBindingOperationFault[] getFault() {
        return fault;
    }


    /**
     * Sets the fault value for this TBindingOperation.
     * 
     * @param fault
     */
    public void setFault(org.xmlsoap.schemas.wsdl.TBindingOperationFault[] fault) {
        this.fault = fault;
    }

    public org.xmlsoap.schemas.wsdl.TBindingOperationFault getFault(int i) {
        return this.fault[i];
    }

    public void setFault(int i, org.xmlsoap.schemas.wsdl.TBindingOperationFault _value) {
        this.fault[i] = _value;
    }


    /**
     * Gets the name value for this TBindingOperation.
     * 
     * @return name
     */
    public org.apache.axis.types.NCName getName() {
        return name;
    }


    /**
     * Sets the name value for this TBindingOperation.
     * 
     * @param name
     */
    public void setName(org.apache.axis.types.NCName name) {
        this.name = name;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TBindingOperation)) return false;
        TBindingOperation other = (TBindingOperation) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.input==null && other.getInput()==null) || 
             (this.input!=null &&
              this.input.equals(other.getInput()))) &&
            ((this.output==null && other.getOutput()==null) || 
             (this.output!=null &&
              this.output.equals(other.getOutput()))) &&
            ((this.fault==null && other.getFault()==null) || 
             (this.fault!=null &&
              java.util.Arrays.equals(this.fault, other.getFault()))) &&
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
        if (getInput() != null) {
            _hashCode += getInput().hashCode();
        }
        if (getOutput() != null) {
            _hashCode += getOutput().hashCode();
        }
        if (getFault() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFault());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFault(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TBindingOperation.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tBindingOperation"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("name");
        attrField.setXmlName(new javax.xml.namespace.QName("", "name"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("input");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "input"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tBindingOperationMessage"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("output");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "output"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tBindingOperationMessage"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fault");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "fault"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tBindingOperationFault"));
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
