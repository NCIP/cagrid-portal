/**
 * TInvoke.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.ws._2003._03.business_process;

public class TInvoke  extends org.xmlsoap.schemas.ws._2003._03.business_process.TActivity  implements java.io.Serializable {
    private org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelationsWithPattern correlations;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TCatch[] _catch;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TActivityOrCompensateContainer catchAll;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TCompensationHandler compensationHandler;
    private org.apache.axis.types.NCName partnerLink;  // attribute
    private javax.xml.namespace.QName portType;  // attribute
    private org.apache.axis.types.NCName operation;  // attribute
    private org.apache.axis.types.NCName inputVariable;  // attribute
    private org.apache.axis.types.NCName outputVariable;  // attribute

    public TInvoke() {
    }

    public TInvoke(
           org.xmlsoap.schemas.ws._2003._03.business_process.TCatch[] _catch,
           org.xmlsoap.schemas.ws._2003._03.business_process.TActivityOrCompensateContainer catchAll,
           org.xmlsoap.schemas.ws._2003._03.business_process.TCompensationHandler compensationHandler,
           org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelationsWithPattern correlations,
           org.apache.axis.types.NCName inputVariable,
           org.apache.axis.types.NCName operation,
           org.apache.axis.types.NCName outputVariable,
           org.apache.axis.types.NCName partnerLink,
           javax.xml.namespace.QName portType) {
           this.correlations = correlations;
           this._catch = _catch;
           this.catchAll = catchAll;
           this.compensationHandler = compensationHandler;
           this.partnerLink = partnerLink;
           this.portType = portType;
           this.operation = operation;
           this.inputVariable = inputVariable;
           this.outputVariable = outputVariable;
    }


    /**
     * Gets the correlations value for this TInvoke.
     * 
     * @return correlations
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelationsWithPattern getCorrelations() {
        return correlations;
    }


    /**
     * Sets the correlations value for this TInvoke.
     * 
     * @param correlations
     */
    public void setCorrelations(org.xmlsoap.schemas.ws._2003._03.business_process.TCorrelationsWithPattern correlations) {
        this.correlations = correlations;
    }


    /**
     * Gets the _catch value for this TInvoke.
     * 
     * @return _catch
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TCatch[] get_catch() {
        return _catch;
    }


    /**
     * Sets the _catch value for this TInvoke.
     * 
     * @param _catch
     */
    public void set_catch(org.xmlsoap.schemas.ws._2003._03.business_process.TCatch[] _catch) {
        this._catch = _catch;
    }

    public org.xmlsoap.schemas.ws._2003._03.business_process.TCatch get_catch(int i) {
        return this._catch[i];
    }

    public void set_catch(int i, org.xmlsoap.schemas.ws._2003._03.business_process.TCatch _value) {
        this._catch[i] = _value;
    }


    /**
     * Gets the catchAll value for this TInvoke.
     * 
     * @return catchAll
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TActivityOrCompensateContainer getCatchAll() {
        return catchAll;
    }


    /**
     * Sets the catchAll value for this TInvoke.
     * 
     * @param catchAll
     */
    public void setCatchAll(org.xmlsoap.schemas.ws._2003._03.business_process.TActivityOrCompensateContainer catchAll) {
        this.catchAll = catchAll;
    }


    /**
     * Gets the compensationHandler value for this TInvoke.
     * 
     * @return compensationHandler
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TCompensationHandler getCompensationHandler() {
        return compensationHandler;
    }


    /**
     * Sets the compensationHandler value for this TInvoke.
     * 
     * @param compensationHandler
     */
    public void setCompensationHandler(org.xmlsoap.schemas.ws._2003._03.business_process.TCompensationHandler compensationHandler) {
        this.compensationHandler = compensationHandler;
    }


    /**
     * Gets the partnerLink value for this TInvoke.
     * 
     * @return partnerLink
     */
    public org.apache.axis.types.NCName getPartnerLink() {
        return partnerLink;
    }


    /**
     * Sets the partnerLink value for this TInvoke.
     * 
     * @param partnerLink
     */
    public void setPartnerLink(org.apache.axis.types.NCName partnerLink) {
        this.partnerLink = partnerLink;
    }


    /**
     * Gets the portType value for this TInvoke.
     * 
     * @return portType
     */
    public javax.xml.namespace.QName getPortType() {
        return portType;
    }


    /**
     * Sets the portType value for this TInvoke.
     * 
     * @param portType
     */
    public void setPortType(javax.xml.namespace.QName portType) {
        this.portType = portType;
    }


    /**
     * Gets the operation value for this TInvoke.
     * 
     * @return operation
     */
    public org.apache.axis.types.NCName getOperation() {
        return operation;
    }


    /**
     * Sets the operation value for this TInvoke.
     * 
     * @param operation
     */
    public void setOperation(org.apache.axis.types.NCName operation) {
        this.operation = operation;
    }


    /**
     * Gets the inputVariable value for this TInvoke.
     * 
     * @return inputVariable
     */
    public org.apache.axis.types.NCName getInputVariable() {
        return inputVariable;
    }


    /**
     * Sets the inputVariable value for this TInvoke.
     * 
     * @param inputVariable
     */
    public void setInputVariable(org.apache.axis.types.NCName inputVariable) {
        this.inputVariable = inputVariable;
    }


    /**
     * Gets the outputVariable value for this TInvoke.
     * 
     * @return outputVariable
     */
    public org.apache.axis.types.NCName getOutputVariable() {
        return outputVariable;
    }


    /**
     * Sets the outputVariable value for this TInvoke.
     * 
     * @param outputVariable
     */
    public void setOutputVariable(org.apache.axis.types.NCName outputVariable) {
        this.outputVariable = outputVariable;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TInvoke)) return false;
        TInvoke other = (TInvoke) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.correlations==null && other.getCorrelations()==null) || 
             (this.correlations!=null &&
              this.correlations.equals(other.getCorrelations()))) &&
            ((this._catch==null && other.get_catch()==null) || 
             (this._catch!=null &&
              java.util.Arrays.equals(this._catch, other.get_catch()))) &&
            ((this.catchAll==null && other.getCatchAll()==null) || 
             (this.catchAll!=null &&
              this.catchAll.equals(other.getCatchAll()))) &&
            ((this.compensationHandler==null && other.getCompensationHandler()==null) || 
             (this.compensationHandler!=null &&
              this.compensationHandler.equals(other.getCompensationHandler()))) &&
            ((this.partnerLink==null && other.getPartnerLink()==null) || 
             (this.partnerLink!=null &&
              this.partnerLink.equals(other.getPartnerLink()))) &&
            ((this.portType==null && other.getPortType()==null) || 
             (this.portType!=null &&
              this.portType.equals(other.getPortType()))) &&
            ((this.operation==null && other.getOperation()==null) || 
             (this.operation!=null &&
              this.operation.equals(other.getOperation()))) &&
            ((this.inputVariable==null && other.getInputVariable()==null) || 
             (this.inputVariable!=null &&
              this.inputVariable.equals(other.getInputVariable()))) &&
            ((this.outputVariable==null && other.getOutputVariable()==null) || 
             (this.outputVariable!=null &&
              this.outputVariable.equals(other.getOutputVariable())));
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
        if (getCorrelations() != null) {
            _hashCode += getCorrelations().hashCode();
        }
        if (get_catch() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(get_catch());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(get_catch(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCatchAll() != null) {
            _hashCode += getCatchAll().hashCode();
        }
        if (getCompensationHandler() != null) {
            _hashCode += getCompensationHandler().hashCode();
        }
        if (getPartnerLink() != null) {
            _hashCode += getPartnerLink().hashCode();
        }
        if (getPortType() != null) {
            _hashCode += getPortType().hashCode();
        }
        if (getOperation() != null) {
            _hashCode += getOperation().hashCode();
        }
        if (getInputVariable() != null) {
            _hashCode += getInputVariable().hashCode();
        }
        if (getOutputVariable() != null) {
            _hashCode += getOutputVariable().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TInvoke.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tInvoke"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("partnerLink");
        attrField.setXmlName(new javax.xml.namespace.QName("", "partnerLink"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("portType");
        attrField.setXmlName(new javax.xml.namespace.QName("", "portType"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "QName"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("operation");
        attrField.setXmlName(new javax.xml.namespace.QName("", "operation"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("inputVariable");
        attrField.setXmlName(new javax.xml.namespace.QName("", "inputVariable"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("outputVariable");
        attrField.setXmlName(new javax.xml.namespace.QName("", "outputVariable"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("correlations");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "correlations"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tCorrelationsWithPattern"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_catch");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "catch"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tCatch"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("catchAll");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "catchAll"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tActivityOrCompensateContainer"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("compensationHandler");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "compensationHandler"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tCompensationHandler"));
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
