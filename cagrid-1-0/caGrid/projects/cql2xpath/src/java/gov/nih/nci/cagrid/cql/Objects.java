/**
 * Objects.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Jun 16, 2005 (10:54:53 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.cql;


/**
 * Classes of objects to be queried.
 */
public class Objects  implements java.io.Serializable {
    private gov.nih.nci.cagrid.cql.Objects objects;
    private gov.nih.nci.cagrid.cql.Group group;
    private gov.nih.nci.cagrid.cql.Property[] property;
    private java.lang.String name;  // attribute

    public Objects() {
    }

    public Objects(
           gov.nih.nci.cagrid.cql.Group group,
           java.lang.String name,
           gov.nih.nci.cagrid.cql.Objects objects,
           gov.nih.nci.cagrid.cql.Property[] property) {
           this.objects = objects;
           this.group = group;
           this.property = property;
           this.name = name;
    }


    /**
     * Gets the objects value for this Objects.
     * 
     * @return objects
     */
    public gov.nih.nci.cagrid.cql.Objects getObjects() {
        return objects;
    }


    /**
     * Sets the objects value for this Objects.
     * 
     * @param objects
     */
    public void setObjects(gov.nih.nci.cagrid.cql.Objects objects) {
        this.objects = objects;
    }


    /**
     * Gets the group value for this Objects.
     * 
     * @return group
     */
    public gov.nih.nci.cagrid.cql.Group getGroup() {
        return group;
    }


    /**
     * Sets the group value for this Objects.
     * 
     * @param group
     */
    public void setGroup(gov.nih.nci.cagrid.cql.Group group) {
        this.group = group;
    }


    /**
     * Gets the property value for this Objects.
     * 
     * @return property
     */
    public gov.nih.nci.cagrid.cql.Property[] getProperty() {
        return property;
    }


    /**
     * Sets the property value for this Objects.
     * 
     * @param property
     */
    public void setProperty(gov.nih.nci.cagrid.cql.Property[] property) {
        this.property = property;
    }

    public gov.nih.nci.cagrid.cql.Property getProperty(int i) {
        return this.property[i];
    }

    public void setProperty(int i, gov.nih.nci.cagrid.cql.Property _value) {
        this.property[i] = _value;
    }


    /**
     * Gets the name value for this Objects.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this Objects.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Objects)) return false;
        Objects other = (Objects) obj;
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
              this.objects.equals(other.getObjects()))) &&
            ((this.group==null && other.getGroup()==null) || 
             (this.group!=null &&
              this.group.equals(other.getGroup()))) &&
            ((this.property==null && other.getProperty()==null) || 
             (this.property!=null &&
              java.util.Arrays.equals(this.property, other.getProperty()))) &&
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
        if (getObjects() != null) {
            _hashCode += getObjects().hashCode();
        }
        if (getGroup() != null) {
            _hashCode += getGroup().hashCode();
        }
        if (getProperty() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getProperty());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getProperty(), i);
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
        new org.apache.axis.description.TypeDesc(Objects.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQL", "Objects"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("name");
        attrField.setXmlName(new javax.xml.namespace.QName("", "name"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("objects");
        elemField.setXmlName(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQL", "Objects"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQL", "Objects"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("group");
        elemField.setXmlName(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQL", "Group"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQL", "Group"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("property");
        elemField.setXmlName(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQL", "Property"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQL", "Property"));
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
