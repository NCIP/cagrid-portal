/**
 * Group.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Jun 16, 2005 (10:54:53 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.cql;


/**
 * Binary joint of two conditions
 */
public class Group  implements java.io.Serializable {
    private gov.nih.nci.cagrid.cql.Objects[] objects;
    private java.lang.String name;  // attribute
    private gov.nih.nci.cagrid.cql.LogicalOperator logicRelation;  // attribute

    public Group() {
    }

    public Group(
           gov.nih.nci.cagrid.cql.LogicalOperator logicRelation,
           java.lang.String name,
           gov.nih.nci.cagrid.cql.Objects[] objects) {
           this.objects = objects;
           this.name = name;
           this.logicRelation = logicRelation;
    }


    /**
     * Gets the objects value for this Group.
     * 
     * @return objects
     */
    public gov.nih.nci.cagrid.cql.Objects[] getObjects() {
        return objects;
    }


    /**
     * Sets the objects value for this Group.
     * 
     * @param objects
     */
    public void setObjects(gov.nih.nci.cagrid.cql.Objects[] objects) {
        this.objects = objects;
    }

    public gov.nih.nci.cagrid.cql.Objects getObjects(int i) {
        return this.objects[i];
    }

    public void setObjects(int i, gov.nih.nci.cagrid.cql.Objects _value) {
        this.objects[i] = _value;
    }


    /**
     * Gets the name value for this Group.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this Group.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the logicRelation value for this Group.
     * 
     * @return logicRelation
     */
    public gov.nih.nci.cagrid.cql.LogicalOperator getLogicRelation() {
        return logicRelation;
    }


    /**
     * Sets the logicRelation value for this Group.
     * 
     * @param logicRelation
     */
    public void setLogicRelation(gov.nih.nci.cagrid.cql.LogicalOperator logicRelation) {
        this.logicRelation = logicRelation;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Group)) return false;
        Group other = (Group) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.objects==null && other.getObjects()==null) || 
             (this.objects!=null &&
              java.util.Arrays.equals(this.objects, other.getObjects()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.logicRelation==null && other.getLogicRelation()==null) || 
             (this.logicRelation!=null &&
              this.logicRelation.equals(other.getLogicRelation())));
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
        if (getObjects() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getObjects());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getObjects(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getLogicRelation() != null) {
            _hashCode += getLogicRelation().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Group.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQL", "Group"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("name");
        attrField.setXmlName(new javax.xml.namespace.QName("", "name"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("logicRelation");
        attrField.setXmlName(new javax.xml.namespace.QName("", "LogicRelation"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQL", "LogicalOperator"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("objects");
        elemField.setXmlName(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQL", "Objects"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQL", "Objects"));
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
