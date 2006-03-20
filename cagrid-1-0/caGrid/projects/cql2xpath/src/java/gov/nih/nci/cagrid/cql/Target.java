/**
 * Target.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Jun 16, 2005 (10:54:53 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.cql;


/**
 * This is the query target.
 */
public class Target  implements java.io.Serializable {
    private gov.nih.nci.cagrid.cql.Group[] group;
    private gov.nih.nci.cagrid.cql.Objects objects;
    private java.lang.String name;  // attribute

    public Target() {
    }

    public Target(
           gov.nih.nci.cagrid.cql.Group[] group,
           java.lang.String name,
           gov.nih.nci.cagrid.cql.Objects objects) {
           this.group = group;
           this.objects = objects;
           this.name = name;
    }


    /**
     * Gets the group value for this Target.
     * 
     * @return group
     */
    public gov.nih.nci.cagrid.cql.Group[] getGroup() {
        return group;
    }


    /**
     * Sets the group value for this Target.
     * 
     * @param group
     */
    public void setGroup(gov.nih.nci.cagrid.cql.Group[] group) {
        this.group = group;
    }

    public gov.nih.nci.cagrid.cql.Group getGroup(int i) {
        return this.group[i];
    }

    public void setGroup(int i, gov.nih.nci.cagrid.cql.Group _value) {
        this.group[i] = _value;
    }


    /**
     * Gets the objects value for this Target.
     * 
     * @return objects
     */
    public gov.nih.nci.cagrid.cql.Objects getObjects() {
        return objects;
    }


    /**
     * Sets the objects value for this Target.
     * 
     * @param objects
     */
    public void setObjects(gov.nih.nci.cagrid.cql.Objects objects) {
        this.objects = objects;
    }


    /**
     * Gets the name value for this Target.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this Target.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Target)) return false;
        Target other = (Target) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.group==null && other.getGroup()==null) || 
             (this.group!=null &&
              java.util.Arrays.equals(this.group, other.getGroup()))) &&
            ((this.objects==null && other.getObjects()==null) || 
             (this.objects!=null &&
              this.objects.equals(other.getObjects()))) &&
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
        int _hashCode = 1;
        if (getGroup() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGroup());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGroup(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getObjects() != null) {
            _hashCode += getObjects().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Target.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQL", "Target"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("name");
        attrField.setXmlName(new javax.xml.namespace.QName("", "name"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("group");
        elemField.setXmlName(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQL", "Group"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQL", "Group"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
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
