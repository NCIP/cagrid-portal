/**
 * RunWorkFlow.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.workflow.management.stubs;

public class RunWorkFlow  implements java.io.Serializable {
    private gov.nih.nci.cagrid.workflow.management.stubs.RunWorkFlowWorkflowInput workflowInput;

    public RunWorkFlow() {
    }

    public RunWorkFlow(
           gov.nih.nci.cagrid.workflow.management.stubs.RunWorkFlowWorkflowInput workflowInput) {
           this.workflowInput = workflowInput;
    }


    /**
     * Gets the workflowInput value for this RunWorkFlow.
     * 
     * @return workflowInput
     */
    public gov.nih.nci.cagrid.workflow.management.stubs.RunWorkFlowWorkflowInput getWorkflowInput() {
        return workflowInput;
    }


    /**
     * Sets the workflowInput value for this RunWorkFlow.
     * 
     * @param workflowInput
     */
    public void setWorkflowInput(gov.nih.nci.cagrid.workflow.management.stubs.RunWorkFlowWorkflowInput workflowInput) {
        this.workflowInput = workflowInput;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RunWorkFlow)) return false;
        RunWorkFlow other = (RunWorkFlow) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.workflowInput==null && other.getWorkflowInput()==null) || 
             (this.workflowInput!=null &&
              this.workflowInput.equals(other.getWorkflowInput())));
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
        if (getWorkflowInput() != null) {
            _hashCode += getWorkflowInput().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RunWorkFlow.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://workflow.cagrid.nci.nih.gov//WorkFlowManagementService", ">runWorkFlow"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workflowInput");
        elemField.setXmlName(new javax.xml.namespace.QName("http://workflow.cagrid.nci.nih.gov//WorkFlowManagementService", "workflowInput"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://workflow.cagrid.nci.nih.gov//WorkFlowManagementService", ">>runWorkFlow>workflowInput"));
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
