/**
 * TScope.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.ws._2003._03.business_process;

public class TScope  extends org.xmlsoap.schemas.ws._2003._03.business_process.TActivity  implements java.io.Serializable {
    private org.xmlsoap.schemas.ws._2003._03.business_process.TVariables variables;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelationSets correlationSets;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TFaultHandlers faultHandlers;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TCompensationHandler compensationHandler;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TEventHandlers eventHandlers;
    private org.xmlsoap.schemas.ws._2003._03.business_process.Activity activity;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean variableAccessSerializable;  // attribute

    public TScope() {
    }

    public TScope(
           org.xmlsoap.schemas.ws._2003._03.business_process.Activity activity,
           org.xmlsoap.schemas.ws._2003._03.business_process.TCompensationHandler compensationHandler,
           org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelationSets correlationSets,
           org.xmlsoap.schemas.ws._2003._03.business_process.TEventHandlers eventHandlers,
           org.xmlsoap.schemas.ws._2003._03.business_process.TFaultHandlers faultHandlers,
           org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean variableAccessSerializable,
           org.xmlsoap.schemas.ws._2003._03.business_process.TVariables variables) {
           this.variables = variables;
           this.correlationSets = correlationSets;
           this.faultHandlers = faultHandlers;
           this.compensationHandler = compensationHandler;
           this.eventHandlers = eventHandlers;
           this.activity = activity;
           this.variableAccessSerializable = variableAccessSerializable;
    }


    /**
     * Gets the variables value for this TScope.
     * 
     * @return variables
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TVariables getVariables() {
        return variables;
    }


    /**
     * Sets the variables value for this TScope.
     * 
     * @param variables
     */
    public void setVariables(org.xmlsoap.schemas.ws._2003._03.business_process.TVariables variables) {
        this.variables = variables;
    }


    /**
     * Gets the correlationSets value for this TScope.
     * 
     * @return correlationSets
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelationSets getCorrelationSets() {
        return correlationSets;
    }


    /**
     * Sets the correlationSets value for this TScope.
     * 
     * @param correlationSets
     */
    public void setCorrelationSets(org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelationSets correlationSets) {
        this.correlationSets = correlationSets;
    }


    /**
     * Gets the faultHandlers value for this TScope.
     * 
     * @return faultHandlers
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TFaultHandlers getFaultHandlers() {
        return faultHandlers;
    }


    /**
     * Sets the faultHandlers value for this TScope.
     * 
     * @param faultHandlers
     */
    public void setFaultHandlers(org.xmlsoap.schemas.ws._2003._03.business_process.TFaultHandlers faultHandlers) {
        this.faultHandlers = faultHandlers;
    }


    /**
     * Gets the compensationHandler value for this TScope.
     * 
     * @return compensationHandler
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TCompensationHandler getCompensationHandler() {
        return compensationHandler;
    }


    /**
     * Sets the compensationHandler value for this TScope.
     * 
     * @param compensationHandler
     */
    public void setCompensationHandler(org.xmlsoap.schemas.ws._2003._03.business_process.TCompensationHandler compensationHandler) {
        this.compensationHandler = compensationHandler;
    }


    /**
     * Gets the eventHandlers value for this TScope.
     * 
     * @return eventHandlers
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TEventHandlers getEventHandlers() {
        return eventHandlers;
    }


    /**
     * Sets the eventHandlers value for this TScope.
     * 
     * @param eventHandlers
     */
    public void setEventHandlers(org.xmlsoap.schemas.ws._2003._03.business_process.TEventHandlers eventHandlers) {
        this.eventHandlers = eventHandlers;
    }


    /**
     * Gets the activity value for this TScope.
     * 
     * @return activity
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.Activity getActivity() {
        return activity;
    }


    /**
     * Sets the activity value for this TScope.
     * 
     * @param activity
     */
    public void setActivity(org.xmlsoap.schemas.ws._2003._03.business_process.Activity activity) {
        this.activity = activity;
    }


    /**
     * Gets the variableAccessSerializable value for this TScope.
     * 
     * @return variableAccessSerializable
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean getVariableAccessSerializable() {
        return variableAccessSerializable;
    }


    /**
     * Sets the variableAccessSerializable value for this TScope.
     * 
     * @param variableAccessSerializable
     */
    public void setVariableAccessSerializable(org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean variableAccessSerializable) {
        this.variableAccessSerializable = variableAccessSerializable;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TScope)) return false;
        TScope other = (TScope) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.variables==null && other.getVariables()==null) || 
             (this.variables!=null &&
              this.variables.equals(other.getVariables()))) &&
            ((this.correlationSets==null && other.getCorrelationSets()==null) || 
             (this.correlationSets!=null &&
              this.correlationSets.equals(other.getCorrelationSets()))) &&
            ((this.faultHandlers==null && other.getFaultHandlers()==null) || 
             (this.faultHandlers!=null &&
              this.faultHandlers.equals(other.getFaultHandlers()))) &&
            ((this.compensationHandler==null && other.getCompensationHandler()==null) || 
             (this.compensationHandler!=null &&
              this.compensationHandler.equals(other.getCompensationHandler()))) &&
            ((this.eventHandlers==null && other.getEventHandlers()==null) || 
             (this.eventHandlers!=null &&
              this.eventHandlers.equals(other.getEventHandlers()))) &&
            ((this.activity==null && other.getActivity()==null) || 
             (this.activity!=null &&
              this.activity.equals(other.getActivity()))) &&
            ((this.variableAccessSerializable==null && other.getVariableAccessSerializable()==null) || 
             (this.variableAccessSerializable!=null &&
              this.variableAccessSerializable.equals(other.getVariableAccessSerializable())));
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
        if (getVariables() != null) {
            _hashCode += getVariables().hashCode();
        }
        if (getCorrelationSets() != null) {
            _hashCode += getCorrelationSets().hashCode();
        }
        if (getFaultHandlers() != null) {
            _hashCode += getFaultHandlers().hashCode();
        }
        if (getCompensationHandler() != null) {
            _hashCode += getCompensationHandler().hashCode();
        }
        if (getEventHandlers() != null) {
            _hashCode += getEventHandlers().hashCode();
        }
        if (getActivity() != null) {
            _hashCode += getActivity().hashCode();
        }
        if (getVariableAccessSerializable() != null) {
            _hashCode += getVariableAccessSerializable().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TScope.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tScope"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("variableAccessSerializable");
        attrField.setXmlName(new javax.xml.namespace.QName("", "variableAccessSerializable"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tBoolean"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("variables");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "variables"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tVariables"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("correlationSets");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "correlationSets"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tCorrelationSets"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("faultHandlers");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "faultHandlers"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tFaultHandlers"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("compensationHandler");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "compensationHandler"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tCompensationHandler"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("eventHandlers");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "eventHandlers"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tEventHandlers"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "activity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "activity"));
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
