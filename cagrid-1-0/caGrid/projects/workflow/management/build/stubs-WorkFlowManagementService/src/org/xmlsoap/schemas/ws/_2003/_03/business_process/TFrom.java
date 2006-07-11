/**
 * TFrom.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.ws._2003._03.business_process;

public class TFrom  extends org.xmlsoap.schemas.ws._2003._03.business_process.TExtensibleElements  implements java.io.Serializable {
    private org.apache.axis.types.NCName variable;  // attribute
    private org.apache.axis.types.NCName part;  // attribute
    private java.lang.String query;  // attribute
    private javax.xml.namespace.QName property;  // attribute
    private org.apache.axis.types.NCName partnerLink;  // attribute
    private org.xmlsoap.schemas.ws._2003._03.business_process.TRoles endpointReference;  // attribute
    private java.lang.String expression;  // attribute
    private org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean opaque;  // attribute

    public TFrom() {
    }

    public TFrom(
           org.xmlsoap.schemas.ws._2003._03.business_process.TRoles endpointReference,
           java.lang.String expression,
           org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean opaque,
           org.apache.axis.types.NCName part,
           org.apache.axis.types.NCName partnerLink,
           javax.xml.namespace.QName property,
           java.lang.String query,
           org.apache.axis.types.NCName variable) {
           this.variable = variable;
           this.part = part;
           this.query = query;
           this.property = property;
           this.partnerLink = partnerLink;
           this.endpointReference = endpointReference;
           this.expression = expression;
           this.opaque = opaque;
    }


    /**
     * Gets the variable value for this TFrom.
     * 
     * @return variable
     */
    public org.apache.axis.types.NCName getVariable() {
        return variable;
    }


    /**
     * Sets the variable value for this TFrom.
     * 
     * @param variable
     */
    public void setVariable(org.apache.axis.types.NCName variable) {
        this.variable = variable;
    }


    /**
     * Gets the part value for this TFrom.
     * 
     * @return part
     */
    public org.apache.axis.types.NCName getPart() {
        return part;
    }


    /**
     * Sets the part value for this TFrom.
     * 
     * @param part
     */
    public void setPart(org.apache.axis.types.NCName part) {
        this.part = part;
    }


    /**
     * Gets the query value for this TFrom.
     * 
     * @return query
     */
    public java.lang.String getQuery() {
        return query;
    }


    /**
     * Sets the query value for this TFrom.
     * 
     * @param query
     */
    public void setQuery(java.lang.String query) {
        this.query = query;
    }


    /**
     * Gets the property value for this TFrom.
     * 
     * @return property
     */
    public javax.xml.namespace.QName getProperty() {
        return property;
    }


    /**
     * Sets the property value for this TFrom.
     * 
     * @param property
     */
    public void setProperty(javax.xml.namespace.QName property) {
        this.property = property;
    }


    /**
     * Gets the partnerLink value for this TFrom.
     * 
     * @return partnerLink
     */
    public org.apache.axis.types.NCName getPartnerLink() {
        return partnerLink;
    }


    /**
     * Sets the partnerLink value for this TFrom.
     * 
     * @param partnerLink
     */
    public void setPartnerLink(org.apache.axis.types.NCName partnerLink) {
        this.partnerLink = partnerLink;
    }


    /**
     * Gets the endpointReference value for this TFrom.
     * 
     * @return endpointReference
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TRoles getEndpointReference() {
        return endpointReference;
    }


    /**
     * Sets the endpointReference value for this TFrom.
     * 
     * @param endpointReference
     */
    public void setEndpointReference(org.xmlsoap.schemas.ws._2003._03.business_process.TRoles endpointReference) {
        this.endpointReference = endpointReference;
    }


    /**
     * Gets the expression value for this TFrom.
     * 
     * @return expression
     */
    public java.lang.String getExpression() {
        return expression;
    }


    /**
     * Sets the expression value for this TFrom.
     * 
     * @param expression
     */
    public void setExpression(java.lang.String expression) {
        this.expression = expression;
    }


    /**
     * Gets the opaque value for this TFrom.
     * 
     * @return opaque
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean getOpaque() {
        return opaque;
    }


    /**
     * Sets the opaque value for this TFrom.
     * 
     * @param opaque
     */
    public void setOpaque(org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean opaque) {
        this.opaque = opaque;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TFrom)) return false;
        TFrom other = (TFrom) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.variable==null && other.getVariable()==null) || 
             (this.variable!=null &&
              this.variable.equals(other.getVariable()))) &&
            ((this.part==null && other.getPart()==null) || 
             (this.part!=null &&
              this.part.equals(other.getPart()))) &&
            ((this.query==null && other.getQuery()==null) || 
             (this.query!=null &&
              this.query.equals(other.getQuery()))) &&
            ((this.property==null && other.getProperty()==null) || 
             (this.property!=null &&
              this.property.equals(other.getProperty()))) &&
            ((this.partnerLink==null && other.getPartnerLink()==null) || 
             (this.partnerLink!=null &&
              this.partnerLink.equals(other.getPartnerLink()))) &&
            ((this.endpointReference==null && other.getEndpointReference()==null) || 
             (this.endpointReference!=null &&
              this.endpointReference.equals(other.getEndpointReference()))) &&
            ((this.expression==null && other.getExpression()==null) || 
             (this.expression!=null &&
              this.expression.equals(other.getExpression()))) &&
            ((this.opaque==null && other.getOpaque()==null) || 
             (this.opaque!=null &&
              this.opaque.equals(other.getOpaque())));
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
        if (getVariable() != null) {
            _hashCode += getVariable().hashCode();
        }
        if (getPart() != null) {
            _hashCode += getPart().hashCode();
        }
        if (getQuery() != null) {
            _hashCode += getQuery().hashCode();
        }
        if (getProperty() != null) {
            _hashCode += getProperty().hashCode();
        }
        if (getPartnerLink() != null) {
            _hashCode += getPartnerLink().hashCode();
        }
        if (getEndpointReference() != null) {
            _hashCode += getEndpointReference().hashCode();
        }
        if (getExpression() != null) {
            _hashCode += getExpression().hashCode();
        }
        if (getOpaque() != null) {
            _hashCode += getOpaque().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TFrom.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tFrom"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("variable");
        attrField.setXmlName(new javax.xml.namespace.QName("", "variable"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("part");
        attrField.setXmlName(new javax.xml.namespace.QName("", "part"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("query");
        attrField.setXmlName(new javax.xml.namespace.QName("", "query"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("property");
        attrField.setXmlName(new javax.xml.namespace.QName("", "property"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "QName"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("partnerLink");
        attrField.setXmlName(new javax.xml.namespace.QName("", "partnerLink"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("endpointReference");
        attrField.setXmlName(new javax.xml.namespace.QName("", "endpointReference"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tRoles"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("expression");
        attrField.setXmlName(new javax.xml.namespace.QName("", "expression"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("opaque");
        attrField.setXmlName(new javax.xml.namespace.QName("", "opaque"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tBoolean"));
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
