/**
 * CQLQueryResultsType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Jun 16, 2005 (10:54:53 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.cql;

public class CQLQueryResultsType  implements java.io.Serializable {
    private gov.nih.nci.cagrid.cql.CQLQueryResultType[] CQLQueryResult;

    public CQLQueryResultsType() {
    }

    public CQLQueryResultsType(
           gov.nih.nci.cagrid.cql.CQLQueryResultType[] CQLQueryResult) {
           this.CQLQueryResult = CQLQueryResult;
    }


    /**
     * Gets the CQLQueryResult value for this CQLQueryResultsType.
     * 
     * @return CQLQueryResult
     */
    public gov.nih.nci.cagrid.cql.CQLQueryResultType[] getCQLQueryResult() {
        return CQLQueryResult;
    }


    /**
     * Sets the CQLQueryResult value for this CQLQueryResultsType.
     * 
     * @param CQLQueryResult
     */
    public void setCQLQueryResult(gov.nih.nci.cagrid.cql.CQLQueryResultType[] CQLQueryResult) {
        this.CQLQueryResult = CQLQueryResult;
    }

    public gov.nih.nci.cagrid.cql.CQLQueryResultType getCQLQueryResult(int i) {
        return this.CQLQueryResult[i];
    }

    public void setCQLQueryResult(int i, gov.nih.nci.cagrid.cql.CQLQueryResultType _value) {
        this.CQLQueryResult[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CQLQueryResultsType)) return false;
        CQLQueryResultsType other = (CQLQueryResultsType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.CQLQueryResult==null && other.getCQLQueryResult()==null) || 
             (this.CQLQueryResult!=null &&
              java.util.Arrays.equals(this.CQLQueryResult, other.getCQLQueryResult())));
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
        if (getCQLQueryResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCQLQueryResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCQLQueryResult(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CQLQueryResultsType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQL", "CQLQueryResultsType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CQLQueryResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQL", "CQLQueryResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQL", "CQLQueryResultType"));
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
