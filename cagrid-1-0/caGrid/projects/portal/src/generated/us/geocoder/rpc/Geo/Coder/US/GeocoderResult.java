/**
 * GeocoderResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package us.geocoder.rpc.Geo.Coder.US;

public class GeocoderResult  implements java.io.Serializable {
    private int zip;
    private java.lang.String state;
    private java.lang.String city;
    private float lat;
    private float _long;
    private int number;
    private java.lang.String suffix;
    private java.lang.String prefix;
    private java.lang.String type;
    private java.lang.String street;
    private java.lang.String suffix1;
    private java.lang.String prefix1;
    private java.lang.String type1;
    private java.lang.String street1;
    private java.lang.String suffix2;
    private java.lang.String prefix2;
    private java.lang.String type2;
    private java.lang.String street2;

    public GeocoderResult() {
    }

    public GeocoderResult(
           float _long,
           java.lang.String city,
           float lat,
           int number,
           java.lang.String prefix,
           java.lang.String prefix1,
           java.lang.String prefix2,
           java.lang.String state,
           java.lang.String street,
           java.lang.String street1,
           java.lang.String street2,
           java.lang.String suffix,
           java.lang.String suffix1,
           java.lang.String suffix2,
           java.lang.String type,
           java.lang.String type1,
           java.lang.String type2,
           int zip) {
           this.zip = zip;
           this.state = state;
           this.city = city;
           this.lat = lat;
           this._long = _long;
           this.number = number;
           this.suffix = suffix;
           this.prefix = prefix;
           this.type = type;
           this.street = street;
           this.suffix1 = suffix1;
           this.prefix1 = prefix1;
           this.type1 = type1;
           this.street1 = street1;
           this.suffix2 = suffix2;
           this.prefix2 = prefix2;
           this.type2 = type2;
           this.street2 = street2;
    }


    /**
     * Gets the zip value for this GeocoderResult.
     * 
     * @return zip
     */
    public int getZip() {
        return zip;
    }


    /**
     * Sets the zip value for this GeocoderResult.
     * 
     * @param zip
     */
    public void setZip(int zip) {
        this.zip = zip;
    }


    /**
     * Gets the state value for this GeocoderResult.
     * 
     * @return state
     */
    public java.lang.String getState() {
        return state;
    }


    /**
     * Sets the state value for this GeocoderResult.
     * 
     * @param state
     */
    public void setState(java.lang.String state) {
        this.state = state;
    }


    /**
     * Gets the city value for this GeocoderResult.
     * 
     * @return city
     */
    public java.lang.String getCity() {
        return city;
    }


    /**
     * Sets the city value for this GeocoderResult.
     * 
     * @param city
     */
    public void setCity(java.lang.String city) {
        this.city = city;
    }


    /**
     * Gets the lat value for this GeocoderResult.
     * 
     * @return lat
     */
    public float getLat() {
        return lat;
    }


    /**
     * Sets the lat value for this GeocoderResult.
     * 
     * @param lat
     */
    public void setLat(float lat) {
        this.lat = lat;
    }


    /**
     * Gets the _long value for this GeocoderResult.
     * 
     * @return _long
     */
    public float get_long() {
        return _long;
    }


    /**
     * Sets the _long value for this GeocoderResult.
     * 
     * @param _long
     */
    public void set_long(float _long) {
        this._long = _long;
    }


    /**
     * Gets the number value for this GeocoderResult.
     * 
     * @return number
     */
    public int getNumber() {
        return number;
    }


    /**
     * Sets the number value for this GeocoderResult.
     * 
     * @param number
     */
    public void setNumber(int number) {
        this.number = number;
    }


    /**
     * Gets the suffix value for this GeocoderResult.
     * 
     * @return suffix
     */
    public java.lang.String getSuffix() {
        return suffix;
    }


    /**
     * Sets the suffix value for this GeocoderResult.
     * 
     * @param suffix
     */
    public void setSuffix(java.lang.String suffix) {
        this.suffix = suffix;
    }


    /**
     * Gets the prefix value for this GeocoderResult.
     * 
     * @return prefix
     */
    public java.lang.String getPrefix() {
        return prefix;
    }


    /**
     * Sets the prefix value for this GeocoderResult.
     * 
     * @param prefix
     */
    public void setPrefix(java.lang.String prefix) {
        this.prefix = prefix;
    }


    /**
     * Gets the type value for this GeocoderResult.
     * 
     * @return type
     */
    public java.lang.String getType() {
        return type;
    }


    /**
     * Sets the type value for this GeocoderResult.
     * 
     * @param type
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }


    /**
     * Gets the street value for this GeocoderResult.
     * 
     * @return street
     */
    public java.lang.String getStreet() {
        return street;
    }


    /**
     * Sets the street value for this GeocoderResult.
     * 
     * @param street
     */
    public void setStreet(java.lang.String street) {
        this.street = street;
    }


    /**
     * Gets the suffix1 value for this GeocoderResult.
     * 
     * @return suffix1
     */
    public java.lang.String getSuffix1() {
        return suffix1;
    }


    /**
     * Sets the suffix1 value for this GeocoderResult.
     * 
     * @param suffix1
     */
    public void setSuffix1(java.lang.String suffix1) {
        this.suffix1 = suffix1;
    }


    /**
     * Gets the prefix1 value for this GeocoderResult.
     * 
     * @return prefix1
     */
    public java.lang.String getPrefix1() {
        return prefix1;
    }


    /**
     * Sets the prefix1 value for this GeocoderResult.
     * 
     * @param prefix1
     */
    public void setPrefix1(java.lang.String prefix1) {
        this.prefix1 = prefix1;
    }


    /**
     * Gets the type1 value for this GeocoderResult.
     * 
     * @return type1
     */
    public java.lang.String getType1() {
        return type1;
    }


    /**
     * Sets the type1 value for this GeocoderResult.
     * 
     * @param type1
     */
    public void setType1(java.lang.String type1) {
        this.type1 = type1;
    }


    /**
     * Gets the street1 value for this GeocoderResult.
     * 
     * @return street1
     */
    public java.lang.String getStreet1() {
        return street1;
    }


    /**
     * Sets the street1 value for this GeocoderResult.
     * 
     * @param street1
     */
    public void setStreet1(java.lang.String street1) {
        this.street1 = street1;
    }


    /**
     * Gets the suffix2 value for this GeocoderResult.
     * 
     * @return suffix2
     */
    public java.lang.String getSuffix2() {
        return suffix2;
    }


    /**
     * Sets the suffix2 value for this GeocoderResult.
     * 
     * @param suffix2
     */
    public void setSuffix2(java.lang.String suffix2) {
        this.suffix2 = suffix2;
    }


    /**
     * Gets the prefix2 value for this GeocoderResult.
     * 
     * @return prefix2
     */
    public java.lang.String getPrefix2() {
        return prefix2;
    }


    /**
     * Sets the prefix2 value for this GeocoderResult.
     * 
     * @param prefix2
     */
    public void setPrefix2(java.lang.String prefix2) {
        this.prefix2 = prefix2;
    }


    /**
     * Gets the type2 value for this GeocoderResult.
     * 
     * @return type2
     */
    public java.lang.String getType2() {
        return type2;
    }


    /**
     * Sets the type2 value for this GeocoderResult.
     * 
     * @param type2
     */
    public void setType2(java.lang.String type2) {
        this.type2 = type2;
    }


    /**
     * Gets the street2 value for this GeocoderResult.
     * 
     * @return street2
     */
    public java.lang.String getStreet2() {
        return street2;
    }


    /**
     * Sets the street2 value for this GeocoderResult.
     * 
     * @param street2
     */
    public void setStreet2(java.lang.String street2) {
        this.street2 = street2;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GeocoderResult)) return false;
        GeocoderResult other = (GeocoderResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.zip == other.getZip() &&
            ((this.state==null && other.getState()==null) || 
             (this.state!=null &&
              this.state.equals(other.getState()))) &&
            ((this.city==null && other.getCity()==null) || 
             (this.city!=null &&
              this.city.equals(other.getCity()))) &&
            this.lat == other.getLat() &&
            this._long == other.get_long() &&
            this.number == other.getNumber() &&
            ((this.suffix==null && other.getSuffix()==null) || 
             (this.suffix!=null &&
              this.suffix.equals(other.getSuffix()))) &&
            ((this.prefix==null && other.getPrefix()==null) || 
             (this.prefix!=null &&
              this.prefix.equals(other.getPrefix()))) &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType()))) &&
            ((this.street==null && other.getStreet()==null) || 
             (this.street!=null &&
              this.street.equals(other.getStreet()))) &&
            ((this.suffix1==null && other.getSuffix1()==null) || 
             (this.suffix1!=null &&
              this.suffix1.equals(other.getSuffix1()))) &&
            ((this.prefix1==null && other.getPrefix1()==null) || 
             (this.prefix1!=null &&
              this.prefix1.equals(other.getPrefix1()))) &&
            ((this.type1==null && other.getType1()==null) || 
             (this.type1!=null &&
              this.type1.equals(other.getType1()))) &&
            ((this.street1==null && other.getStreet1()==null) || 
             (this.street1!=null &&
              this.street1.equals(other.getStreet1()))) &&
            ((this.suffix2==null && other.getSuffix2()==null) || 
             (this.suffix2!=null &&
              this.suffix2.equals(other.getSuffix2()))) &&
            ((this.prefix2==null && other.getPrefix2()==null) || 
             (this.prefix2!=null &&
              this.prefix2.equals(other.getPrefix2()))) &&
            ((this.type2==null && other.getType2()==null) || 
             (this.type2!=null &&
              this.type2.equals(other.getType2()))) &&
            ((this.street2==null && other.getStreet2()==null) || 
             (this.street2!=null &&
              this.street2.equals(other.getStreet2())));
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
        _hashCode += getZip();
        if (getState() != null) {
            _hashCode += getState().hashCode();
        }
        if (getCity() != null) {
            _hashCode += getCity().hashCode();
        }
        _hashCode += new Float(getLat()).hashCode();
        _hashCode += new Float(get_long()).hashCode();
        _hashCode += getNumber();
        if (getSuffix() != null) {
            _hashCode += getSuffix().hashCode();
        }
        if (getPrefix() != null) {
            _hashCode += getPrefix().hashCode();
        }
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        if (getStreet() != null) {
            _hashCode += getStreet().hashCode();
        }
        if (getSuffix1() != null) {
            _hashCode += getSuffix1().hashCode();
        }
        if (getPrefix1() != null) {
            _hashCode += getPrefix1().hashCode();
        }
        if (getType1() != null) {
            _hashCode += getType1().hashCode();
        }
        if (getStreet1() != null) {
            _hashCode += getStreet1().hashCode();
        }
        if (getSuffix2() != null) {
            _hashCode += getSuffix2().hashCode();
        }
        if (getPrefix2() != null) {
            _hashCode += getPrefix2().hashCode();
        }
        if (getType2() != null) {
            _hashCode += getType2().hashCode();
        }
        if (getStreet2() != null) {
            _hashCode += getStreet2().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GeocoderResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rpc.geocoder.us/Geo/Coder/US/", "GeocoderResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("zip");
        elemField.setXmlName(new javax.xml.namespace.QName("", "zip"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("state");
        elemField.setXmlName(new javax.xml.namespace.QName("", "state"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("city");
        elemField.setXmlName(new javax.xml.namespace.QName("", "city"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lat");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_long");
        elemField.setXmlName(new javax.xml.namespace.QName("", "long"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("number");
        elemField.setXmlName(new javax.xml.namespace.QName("", "number"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("suffix");
        elemField.setXmlName(new javax.xml.namespace.QName("", "suffix"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("prefix");
        elemField.setXmlName(new javax.xml.namespace.QName("", "prefix"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("street");
        elemField.setXmlName(new javax.xml.namespace.QName("", "street"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("suffix1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "suffix1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("prefix1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "prefix1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "type1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("street1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "street1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("suffix2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "suffix2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("prefix2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "prefix2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "type2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("street2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "street2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
