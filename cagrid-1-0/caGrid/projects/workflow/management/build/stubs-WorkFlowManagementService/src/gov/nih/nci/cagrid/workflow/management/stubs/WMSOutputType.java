/**
 * WMSOutputType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.workflow.management.stubs;


/**
 * This type represents the output from a workflow
 */
public class WMSOutputType  implements java.io.Serializable {
    private gov.nih.nci.cagrid.workflow.management.stubs.WorkflowOuputType outputType;

    public WMSOutputType() {
    }

    public WMSOutputType(
           gov.nih.nci.cagrid.workflow.management.stubs.WorkflowOuputType outputType) {
           this.outputType = outputType;
    }


    /**
     * Gets the outputType value for this WMSOutputType.
     * 
     * @return outputType
     */
    public gov.nih.nci.cagrid.workflow.management.stubs.WorkflowOuputType getOutputType() {
        return outputType;
    }


    /**
     * Sets the outputType value for this WMSOutputType.
     * 
     * @param outputType
     */
    public void setOutputType(gov.nih.nci.cagrid.workflow.management.stubs.WorkflowOuputType outputType) {
        this.outputType = outputType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WMSOutputType)) return false;
        WMSOutputType other = (WMSOutputType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.outputType==null && other.getOutputType()==null) || 
             (this.outputType!=null &&
              this.outputType.equals(other.getOutputType())));
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
        if (getOutputType() != null) {
            _hashCode += getOutputType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WMSOutputType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://workflow.cagrid.nci.nih.gov//WorkFlowManagementService", "WMSOutputType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("outputType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://workflow.cagrid.nci.nih.gov//WorkFlowManagementService", "outputType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://workflow.cagrid.nci.nih.gov//WorkFlowManagementService", "WorkflowOuputType"));
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
