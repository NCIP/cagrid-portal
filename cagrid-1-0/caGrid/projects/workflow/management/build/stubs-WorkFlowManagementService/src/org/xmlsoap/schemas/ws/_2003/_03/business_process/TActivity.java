/**
 * TActivity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.ws._2003._03.business_process;

public class TActivity  extends org.xmlsoap.schemas.ws._2003._03.business_process.TExtensibleElements  implements java.io.Serializable {
    private org.xmlsoap.schemas.ws._2003._03.business_process.TTarget[] target;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TSource[] source;
    private org.apache.axis.types.NCName name;  // attribute
    private java.lang.String joinCondition;  // attribute
    private org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean suppressJoinFailure;  // attribute

    public TActivity() {
    }

    public TActivity(
           java.lang.String joinCondition,
           org.apache.axis.types.NCName name,
           org.xmlsoap.schemas.ws._2003._03.business_process.TSource[] source,
           org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean suppressJoinFailure,
           org.xmlsoap.schemas.ws._2003._03.business_process.TTarget[] target) {
           this.target = target;
           this.source = source;
           this.name = name;
           this.joinCondition = joinCondition;
           this.suppressJoinFailure = suppressJoinFailure;
    }


    /**
     * Gets the target value for this TActivity.
     * 
     * @return target
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TTarget[] getTarget() {
        return target;
    }


    /**
     * Sets the target value for this TActivity.
     * 
     * @param target
     */
    public void setTarget(org.xmlsoap.schemas.ws._2003._03.business_process.TTarget[] target) {
        this.target = target;
    }

    public org.xmlsoap.schemas.ws._2003._03.business_process.TTarget getTarget(int i) {
        return this.target[i];
    }

    public void setTarget(int i, org.xmlsoap.schemas.ws._2003._03.business_process.TTarget _value) {
        this.target[i] = _value;
    }


    /**
     * Gets the source value for this TActivity.
     * 
     * @return source
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TSource[] getSource() {
        return source;
    }


    /**
     * Sets the source value for this TActivity.
     * 
     * @param source
     */
    public void setSource(org.xmlsoap.schemas.ws._2003._03.business_process.TSource[] source) {
        this.source = source;
    }

    public org.xmlsoap.schemas.ws._2003._03.business_process.TSource getSource(int i) {
        return this.source[i];
    }

    public void setSource(int i, org.xmlsoap.schemas.ws._2003._03.business_process.TSource _value) {
        this.source[i] = _value;
    }


    /**
     * Gets the name value for this TActivity.
     * 
     * @return name
     */
    public org.apache.axis.types.NCName getName() {
        return name;
    }


    /**
     * Sets the name value for this TActivity.
     * 
     * @param name
     */
    public void setName(org.apache.axis.types.NCName name) {
        this.name = name;
    }


    /**
     * Gets the joinCondition value for this TActivity.
     * 
     * @return joinCondition
     */
    public java.lang.String getJoinCondition() {
        return joinCondition;
    }


    /**
     * Sets the joinCondition value for this TActivity.
     * 
     * @param joinCondition
     */
    public void setJoinCondition(java.lang.String joinCondition) {
        this.joinCondition = joinCondition;
    }


    /**
     * Gets the suppressJoinFailure value for this TActivity.
     * 
     * @return suppressJoinFailure
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean getSuppressJoinFailure() {
        return suppressJoinFailure;
    }


    /**
     * Sets the suppressJoinFailure value for this TActivity.
     * 
     * @param suppressJoinFailure
     */
    public void setSuppressJoinFailure(org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean suppressJoinFailure) {
        this.suppressJoinFailure = suppressJoinFailure;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TActivity)) return false;
        TActivity other = (TActivity) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.target==null && other.getTarget()==null) || 
             (this.target!=null &&
              java.util.Arrays.equals(this.target, other.getTarget()))) &&
            ((this.source==null && other.getSource()==null) || 
             (this.source!=null &&
              java.util.Arrays.equals(this.source, other.getSource()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.joinCondition==null && other.getJoinCondition()==null) || 
             (this.joinCondition!=null &&
              this.joinCondition.equals(other.getJoinCondition()))) &&
            ((this.suppressJoinFailure==null && other.getSuppressJoinFailure()==null) || 
             (this.suppressJoinFailure!=null &&
              this.suppressJoinFailure.equals(other.getSuppressJoinFailure())));
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
        if (getTarget() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTarget());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTarget(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSource() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSource());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSource(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getJoinCondition() != null) {
            _hashCode += getJoinCondition().hashCode();
        }
        if (getSuppressJoinFailure() != null) {
            _hashCode += getSuppressJoinFailure().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TActivity.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tActivity"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("name");
        attrField.setXmlName(new javax.xml.namespace.QName("", "name"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("joinCondition");
        attrField.setXmlName(new javax.xml.namespace.QName("", "joinCondition"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tBoolean-expr"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("suppressJoinFailure");
        attrField.setXmlName(new javax.xml.namespace.QName("", "suppressJoinFailure"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tBoolean"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("target");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "target"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tTarget"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("source");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "source"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tSource"));
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
