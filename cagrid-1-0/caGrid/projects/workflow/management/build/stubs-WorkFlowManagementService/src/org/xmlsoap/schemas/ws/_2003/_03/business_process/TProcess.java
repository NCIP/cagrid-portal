/**
 * TProcess.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.ws._2003._03.business_process;

public class TProcess  extends org.xmlsoap.schemas.ws._2003._03.business_process.TExtensibleElements  implements java.io.Serializable {
    private org.xmlsoap.schemas.ws._2003._03.business_process.TPartnerLinks partnerLinks;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TPartners partners;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TVariables variables;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelationSets correlationSets;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TFaultHandlers faultHandlers;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TCompensationHandler compensationHandler;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TEventHandlers eventHandlers;
    private org.xmlsoap.schemas.ws._2003._03.business_process.Activity activity;
    private org.apache.axis.types.NCName name;  // attribute
    private org.apache.axis.types.URI targetNamespace;  // attribute
    private org.apache.axis.types.URI queryLanguage;  // attribute
    private org.apache.axis.types.URI expressionLanguage;  // attribute
    private org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean suppressJoinFailure;  // attribute
    private org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean enableInstanceCompensation;  // attribute
    private org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean abstractProcess;  // attribute

    public TProcess() {
    }

    public TProcess(
           org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean abstractProcess,
           org.xmlsoap.schemas.ws._2003._03.business_process.Activity activity,
           org.xmlsoap.schemas.ws._2003._03.business_process.TCompensationHandler compensationHandler,
           org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelationSets correlationSets,
           org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean enableInstanceCompensation,
           org.xmlsoap.schemas.ws._2003._03.business_process.TEventHandlers eventHandlers,
           org.apache.axis.types.URI expressionLanguage,
           org.xmlsoap.schemas.ws._2003._03.business_process.TFaultHandlers faultHandlers,
           org.apache.axis.types.NCName name,
           org.xmlsoap.schemas.ws._2003._03.business_process.TPartnerLinks partnerLinks,
           org.xmlsoap.schemas.ws._2003._03.business_process.TPartners partners,
           org.apache.axis.types.URI queryLanguage,
           org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean suppressJoinFailure,
           org.apache.axis.types.URI targetNamespace,
           org.xmlsoap.schemas.ws._2003._03.business_process.TVariables variables) {
           this.partnerLinks = partnerLinks;
           this.partners = partners;
           this.variables = variables;
           this.correlationSets = correlationSets;
           this.faultHandlers = faultHandlers;
           this.compensationHandler = compensationHandler;
           this.eventHandlers = eventHandlers;
           this.activity = activity;
           this.name = name;
           this.targetNamespace = targetNamespace;
           this.queryLanguage = queryLanguage;
           this.expressionLanguage = expressionLanguage;
           this.suppressJoinFailure = suppressJoinFailure;
           this.enableInstanceCompensation = enableInstanceCompensation;
           this.abstractProcess = abstractProcess;
    }


    /**
     * Gets the partnerLinks value for this TProcess.
     * 
     * @return partnerLinks
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TPartnerLinks getPartnerLinks() {
        return partnerLinks;
    }


    /**
     * Sets the partnerLinks value for this TProcess.
     * 
     * @param partnerLinks
     */
    public void setPartnerLinks(org.xmlsoap.schemas.ws._2003._03.business_process.TPartnerLinks partnerLinks) {
        this.partnerLinks = partnerLinks;
    }


    /**
     * Gets the partners value for this TProcess.
     * 
     * @return partners
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TPartners getPartners() {
        return partners;
    }


    /**
     * Sets the partners value for this TProcess.
     * 
     * @param partners
     */
    public void setPartners(org.xmlsoap.schemas.ws._2003._03.business_process.TPartners partners) {
        this.partners = partners;
    }


    /**
     * Gets the variables value for this TProcess.
     * 
     * @return variables
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TVariables getVariables() {
        return variables;
    }


    /**
     * Sets the variables value for this TProcess.
     * 
     * @param variables
     */
    public void setVariables(org.xmlsoap.schemas.ws._2003._03.business_process.TVariables variables) {
        this.variables = variables;
    }


    /**
     * Gets the correlationSets value for this TProcess.
     * 
     * @return correlationSets
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelationSets getCorrelationSets() {
        return correlationSets;
    }


    /**
     * Sets the correlationSets value for this TProcess.
     * 
     * @param correlationSets
     */
    public void setCorrelationSets(org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelationSets correlationSets) {
        this.correlationSets = correlationSets;
    }


    /**
     * Gets the faultHandlers value for this TProcess.
     * 
     * @return faultHandlers
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TFaultHandlers getFaultHandlers() {
        return faultHandlers;
    }


    /**
     * Sets the faultHandlers value for this TProcess.
     * 
     * @param faultHandlers
     */
    public void setFaultHandlers(org.xmlsoap.schemas.ws._2003._03.business_process.TFaultHandlers faultHandlers) {
        this.faultHandlers = faultHandlers;
    }


    /**
     * Gets the compensationHandler value for this TProcess.
     * 
     * @return compensationHandler
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TCompensationHandler getCompensationHandler() {
        return compensationHandler;
    }


    /**
     * Sets the compensationHandler value for this TProcess.
     * 
     * @param compensationHandler
     */
    public void setCompensationHandler(org.xmlsoap.schemas.ws._2003._03.business_process.TCompensationHandler compensationHandler) {
        this.compensationHandler = compensationHandler;
    }


    /**
     * Gets the eventHandlers value for this TProcess.
     * 
     * @return eventHandlers
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TEventHandlers getEventHandlers() {
        return eventHandlers;
    }


    /**
     * Sets the eventHandlers value for this TProcess.
     * 
     * @param eventHandlers
     */
    public void setEventHandlers(org.xmlsoap.schemas.ws._2003._03.business_process.TEventHandlers eventHandlers) {
        this.eventHandlers = eventHandlers;
    }


    /**
     * Gets the activity value for this TProcess.
     * 
     * @return activity
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.Activity getActivity() {
        return activity;
    }


    /**
     * Sets the activity value for this TProcess.
     * 
     * @param activity
     */
    public void setActivity(org.xmlsoap.schemas.ws._2003._03.business_process.Activity activity) {
        this.activity = activity;
    }


    /**
     * Gets the name value for this TProcess.
     * 
     * @return name
     */
    public org.apache.axis.types.NCName getName() {
        return name;
    }


    /**
     * Sets the name value for this TProcess.
     * 
     * @param name
     */
    public void setName(org.apache.axis.types.NCName name) {
        this.name = name;
    }


    /**
     * Gets the targetNamespace value for this TProcess.
     * 
     * @return targetNamespace
     */
    public org.apache.axis.types.URI getTargetNamespace() {
        return targetNamespace;
    }


    /**
     * Sets the targetNamespace value for this TProcess.
     * 
     * @param targetNamespace
     */
    public void setTargetNamespace(org.apache.axis.types.URI targetNamespace) {
        this.targetNamespace = targetNamespace;
    }


    /**
     * Gets the queryLanguage value for this TProcess.
     * 
     * @return queryLanguage
     */
    public org.apache.axis.types.URI getQueryLanguage() {
        return queryLanguage;
    }


    /**
     * Sets the queryLanguage value for this TProcess.
     * 
     * @param queryLanguage
     */
    public void setQueryLanguage(org.apache.axis.types.URI queryLanguage) {
        this.queryLanguage = queryLanguage;
    }


    /**
     * Gets the expressionLanguage value for this TProcess.
     * 
     * @return expressionLanguage
     */
    public org.apache.axis.types.URI getExpressionLanguage() {
        return expressionLanguage;
    }


    /**
     * Sets the expressionLanguage value for this TProcess.
     * 
     * @param expressionLanguage
     */
    public void setExpressionLanguage(org.apache.axis.types.URI expressionLanguage) {
        this.expressionLanguage = expressionLanguage;
    }


    /**
     * Gets the suppressJoinFailure value for this TProcess.
     * 
     * @return suppressJoinFailure
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean getSuppressJoinFailure() {
        return suppressJoinFailure;
    }


    /**
     * Sets the suppressJoinFailure value for this TProcess.
     * 
     * @param suppressJoinFailure
     */
    public void setSuppressJoinFailure(org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean suppressJoinFailure) {
        this.suppressJoinFailure = suppressJoinFailure;
    }


    /**
     * Gets the enableInstanceCompensation value for this TProcess.
     * 
     * @return enableInstanceCompensation
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean getEnableInstanceCompensation() {
        return enableInstanceCompensation;
    }


    /**
     * Sets the enableInstanceCompensation value for this TProcess.
     * 
     * @param enableInstanceCompensation
     */
    public void setEnableInstanceCompensation(org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean enableInstanceCompensation) {
        this.enableInstanceCompensation = enableInstanceCompensation;
    }


    /**
     * Gets the abstractProcess value for this TProcess.
     * 
     * @return abstractProcess
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean getAbstractProcess() {
        return abstractProcess;
    }


    /**
     * Sets the abstractProcess value for this TProcess.
     * 
     * @param abstractProcess
     */
    public void setAbstractProcess(org.xmlsoap.schemas.ws._2003._03.business_process.TBoolean abstractProcess) {
        this.abstractProcess = abstractProcess;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TProcess)) return false;
        TProcess other = (TProcess) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.partnerLinks==null && other.getPartnerLinks()==null) || 
             (this.partnerLinks!=null &&
              this.partnerLinks.equals(other.getPartnerLinks()))) &&
            ((this.partners==null && other.getPartners()==null) || 
             (this.partners!=null &&
              this.partners.equals(other.getPartners()))) &&
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
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.targetNamespace==null && other.getTargetNamespace()==null) || 
             (this.targetNamespace!=null &&
              this.targetNamespace.equals(other.getTargetNamespace()))) &&
            ((this.queryLanguage==null && other.getQueryLanguage()==null) || 
             (this.queryLanguage!=null &&
              this.queryLanguage.equals(other.getQueryLanguage()))) &&
            ((this.expressionLanguage==null && other.getExpressionLanguage()==null) || 
             (this.expressionLanguage!=null &&
              this.expressionLanguage.equals(other.getExpressionLanguage()))) &&
            ((this.suppressJoinFailure==null && other.getSuppressJoinFailure()==null) || 
             (this.suppressJoinFailure!=null &&
              this.suppressJoinFailure.equals(other.getSuppressJoinFailure()))) &&
            ((this.enableInstanceCompensation==null && other.getEnableInstanceCompensation()==null) || 
             (this.enableInstanceCompensation!=null &&
              this.enableInstanceCompensation.equals(other.getEnableInstanceCompensation()))) &&
            ((this.abstractProcess==null && other.getAbstractProcess()==null) || 
             (this.abstractProcess!=null &&
              this.abstractProcess.equals(other.getAbstractProcess())));
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
        if (getPartnerLinks() != null) {
            _hashCode += getPartnerLinks().hashCode();
        }
        if (getPartners() != null) {
            _hashCode += getPartners().hashCode();
        }
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
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getTargetNamespace() != null) {
            _hashCode += getTargetNamespace().hashCode();
        }
        if (getQueryLanguage() != null) {
            _hashCode += getQueryLanguage().hashCode();
        }
        if (getExpressionLanguage() != null) {
            _hashCode += getExpressionLanguage().hashCode();
        }
        if (getSuppressJoinFailure() != null) {
            _hashCode += getSuppressJoinFailure().hashCode();
        }
        if (getEnableInstanceCompensation() != null) {
            _hashCode += getEnableInstanceCompensation().hashCode();
        }
        if (getAbstractProcess() != null) {
            _hashCode += getAbstractProcess().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TProcess.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tProcess"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("name");
        attrField.setXmlName(new javax.xml.namespace.QName("", "name"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("targetNamespace");
        attrField.setXmlName(new javax.xml.namespace.QName("", "targetNamespace"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("queryLanguage");
        attrField.setXmlName(new javax.xml.namespace.QName("", "queryLanguage"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("expressionLanguage");
        attrField.setXmlName(new javax.xml.namespace.QName("", "expressionLanguage"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("suppressJoinFailure");
        attrField.setXmlName(new javax.xml.namespace.QName("", "suppressJoinFailure"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tBoolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("enableInstanceCompensation");
        attrField.setXmlName(new javax.xml.namespace.QName("", "enableInstanceCompensation"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tBoolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("abstractProcess");
        attrField.setXmlName(new javax.xml.namespace.QName("", "abstractProcess"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tBoolean"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("partnerLinks");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "partnerLinks"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tPartnerLinks"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("partners");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "partners"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tPartners"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
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
