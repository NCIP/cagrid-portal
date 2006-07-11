/**
 * WMSInputType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.workflow.management.stubs;


/**
 * This type represents the input type for a BPEL workflow
 */
public class WMSInputType  implements java.io.Serializable {
    private java.lang.String workflowName;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TProcess process;
    private gov.nih.nci.cagrid.workflow.management.stubs.WorkflowInputType inputArgs;

    public WMSInputType() {
    }

    public WMSInputType(
           gov.nih.nci.cagrid.workflow.management.stubs.WorkflowInputType inputArgs,
           org.xmlsoap.schemas.ws._2003._03.business_process.TProcess process,
           java.lang.String workflowName) {
           this.workflowName = workflowName;
           this.process = process;
           this.inputArgs = inputArgs;
    }


    /**
     * Gets the workflowName value for this WMSInputType.
     * 
     * @return workflowName
     */
    public java.lang.String getWorkflowName() {
        return workflowName;
    }


    /**
     * Sets the workflowName value for this WMSInputType.
     * 
     * @param workflowName
     */
    public void setWorkflowName(java.lang.String workflowName) {
        this.workflowName = workflowName;
    }


    /**
     * Gets the process value for this WMSInputType.
     * 
     * @return process
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TProcess getProcess() {
        return process;
    }


    /**
     * Sets the process value for this WMSInputType.
     * 
     * @param process
     */
    public void setProcess(org.xmlsoap.schemas.ws._2003._03.business_process.TProcess process) {
        this.process = process;
    }


    /**
     * Gets the inputArgs value for this WMSInputType.
     * 
     * @return inputArgs
     */
    public gov.nih.nci.cagrid.workflow.management.stubs.WorkflowInputType getInputArgs() {
        return inputArgs;
    }


    /**
     * Sets the inputArgs value for this WMSInputType.
     * 
     * @param inputArgs
     */
    public void setInputArgs(gov.nih.nci.cagrid.workflow.management.stubs.WorkflowInputType inputArgs) {
        this.inputArgs = inputArgs;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WMSInputType)) return false;
        WMSInputType other = (WMSInputType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.workflowName==null && other.getWorkflowName()==null) || 
             (this.workflowName!=null &&
              this.workflowName.equals(other.getWorkflowName()))) &&
            ((this.process==null && other.getProcess()==null) || 
             (this.process!=null &&
              this.process.equals(other.getProcess()))) &&
            ((this.inputArgs==null && other.getInputArgs()==null) || 
             (this.inputArgs!=null &&
              this.inputArgs.equals(other.getInputArgs())));
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
        if (getWorkflowName() != null) {
            _hashCode += getWorkflowName().hashCode();
        }
        if (getProcess() != null) {
            _hashCode += getProcess().hashCode();
        }
        if (getInputArgs() != null) {
            _hashCode += getInputArgs().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WMSInputType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://workflow.cagrid.nci.nih.gov//WorkFlowManagementService", "WMSInputType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workflowName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://workflow.cagrid.nci.nih.gov//WorkFlowManagementService", "workflowName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("process");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "process"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tProcess"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inputArgs");
        elemField.setXmlName(new javax.xml.namespace.QName("http://workflow.cagrid.nci.nih.gov//WorkFlowManagementService", "inputArgs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://workflow.cagrid.nci.nih.gov//WorkFlowManagementService", "WorkflowInputType"));
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
