/**
 * Activity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Mar 03, 2006 (12:17:06 EST) WSDL2Java emitter.
 */

package org.xmlsoap.schemas.ws._2003._03.business_process;

public class Activity  implements java.io.Serializable {
    private org.xmlsoap.schemas.ws._2003._03.business_process.TEmpty empty;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TInvoke invoke;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TReceive receive;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TReply reply;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TAssign assign;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TWait wait;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TThrow _throw;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TTerminate terminate;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TFlow flow;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TSwitch _switch;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TWhile _while;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TSequence sequence;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TPick pick;
    private org.xmlsoap.schemas.ws._2003._03.business_process.TScope scope;

    public Activity() {
    }

    public Activity(
           org.xmlsoap.schemas.ws._2003._03.business_process.TSwitch _switch,
           org.xmlsoap.schemas.ws._2003._03.business_process.TThrow _throw,
           org.xmlsoap.schemas.ws._2003._03.business_process.TWhile _while,
           org.xmlsoap.schemas.ws._2003._03.business_process.TAssign assign,
           org.xmlsoap.schemas.ws._2003._03.business_process.TEmpty empty,
           org.xmlsoap.schemas.ws._2003._03.business_process.TFlow flow,
           org.xmlsoap.schemas.ws._2003._03.business_process.TInvoke invoke,
           org.xmlsoap.schemas.ws._2003._03.business_process.TPick pick,
           org.xmlsoap.schemas.ws._2003._03.business_process.TReceive receive,
           org.xmlsoap.schemas.ws._2003._03.business_process.TReply reply,
           org.xmlsoap.schemas.ws._2003._03.business_process.TScope scope,
           org.xmlsoap.schemas.ws._2003._03.business_process.TSequence sequence,
           org.xmlsoap.schemas.ws._2003._03.business_process.TTerminate terminate,
           org.xmlsoap.schemas.ws._2003._03.business_process.TWait wait) {
           this.empty = empty;
           this.invoke = invoke;
           this.receive = receive;
           this.reply = reply;
           this.assign = assign;
           this.wait = wait;
           this._throw = _throw;
           this.terminate = terminate;
           this.flow = flow;
           this._switch = _switch;
           this._while = _while;
           this.sequence = sequence;
           this.pick = pick;
           this.scope = scope;
    }


    /**
     * Gets the empty value for this Activity.
     * 
     * @return empty
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TEmpty getEmpty() {
        return empty;
    }


    /**
     * Sets the empty value for this Activity.
     * 
     * @param empty
     */
    public void setEmpty(org.xmlsoap.schemas.ws._2003._03.business_process.TEmpty empty) {
        this.empty = empty;
    }


    /**
     * Gets the invoke value for this Activity.
     * 
     * @return invoke
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TInvoke getInvoke() {
        return invoke;
    }


    /**
     * Sets the invoke value for this Activity.
     * 
     * @param invoke
     */
    public void setInvoke(org.xmlsoap.schemas.ws._2003._03.business_process.TInvoke invoke) {
        this.invoke = invoke;
    }


    /**
     * Gets the receive value for this Activity.
     * 
     * @return receive
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TReceive getReceive() {
        return receive;
    }


    /**
     * Sets the receive value for this Activity.
     * 
     * @param receive
     */
    public void setReceive(org.xmlsoap.schemas.ws._2003._03.business_process.TReceive receive) {
        this.receive = receive;
    }


    /**
     * Gets the reply value for this Activity.
     * 
     * @return reply
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TReply getReply() {
        return reply;
    }


    /**
     * Sets the reply value for this Activity.
     * 
     * @param reply
     */
    public void setReply(org.xmlsoap.schemas.ws._2003._03.business_process.TReply reply) {
        this.reply = reply;
    }


    /**
     * Gets the assign value for this Activity.
     * 
     * @return assign
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TAssign getAssign() {
        return assign;
    }


    /**
     * Sets the assign value for this Activity.
     * 
     * @param assign
     */
    public void setAssign(org.xmlsoap.schemas.ws._2003._03.business_process.TAssign assign) {
        this.assign = assign;
    }


    /**
     * Gets the wait value for this Activity.
     * 
     * @return wait
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TWait getWait() {
        return wait;
    }


    /**
     * Sets the wait value for this Activity.
     * 
     * @param wait
     */
    public void setWait(org.xmlsoap.schemas.ws._2003._03.business_process.TWait wait) {
        this.wait = wait;
    }


    /**
     * Gets the _throw value for this Activity.
     * 
     * @return _throw
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TThrow get_throw() {
        return _throw;
    }


    /**
     * Sets the _throw value for this Activity.
     * 
     * @param _throw
     */
    public void set_throw(org.xmlsoap.schemas.ws._2003._03.business_process.TThrow _throw) {
        this._throw = _throw;
    }


    /**
     * Gets the terminate value for this Activity.
     * 
     * @return terminate
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TTerminate getTerminate() {
        return terminate;
    }


    /**
     * Sets the terminate value for this Activity.
     * 
     * @param terminate
     */
    public void setTerminate(org.xmlsoap.schemas.ws._2003._03.business_process.TTerminate terminate) {
        this.terminate = terminate;
    }


    /**
     * Gets the flow value for this Activity.
     * 
     * @return flow
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TFlow getFlow() {
        return flow;
    }


    /**
     * Sets the flow value for this Activity.
     * 
     * @param flow
     */
    public void setFlow(org.xmlsoap.schemas.ws._2003._03.business_process.TFlow flow) {
        this.flow = flow;
    }


    /**
     * Gets the _switch value for this Activity.
     * 
     * @return _switch
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TSwitch get_switch() {
        return _switch;
    }


    /**
     * Sets the _switch value for this Activity.
     * 
     * @param _switch
     */
    public void set_switch(org.xmlsoap.schemas.ws._2003._03.business_process.TSwitch _switch) {
        this._switch = _switch;
    }


    /**
     * Gets the _while value for this Activity.
     * 
     * @return _while
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TWhile get_while() {
        return _while;
    }


    /**
     * Sets the _while value for this Activity.
     * 
     * @param _while
     */
    public void set_while(org.xmlsoap.schemas.ws._2003._03.business_process.TWhile _while) {
        this._while = _while;
    }


    /**
     * Gets the sequence value for this Activity.
     * 
     * @return sequence
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TSequence getSequence() {
        return sequence;
    }


    /**
     * Sets the sequence value for this Activity.
     * 
     * @param sequence
     */
    public void setSequence(org.xmlsoap.schemas.ws._2003._03.business_process.TSequence sequence) {
        this.sequence = sequence;
    }


    /**
     * Gets the pick value for this Activity.
     * 
     * @return pick
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TPick getPick() {
        return pick;
    }


    /**
     * Sets the pick value for this Activity.
     * 
     * @param pick
     */
    public void setPick(org.xmlsoap.schemas.ws._2003._03.business_process.TPick pick) {
        this.pick = pick;
    }


    /**
     * Gets the scope value for this Activity.
     * 
     * @return scope
     */
    public org.xmlsoap.schemas.ws._2003._03.business_process.TScope getScope() {
        return scope;
    }


    /**
     * Sets the scope value for this Activity.
     * 
     * @param scope
     */
    public void setScope(org.xmlsoap.schemas.ws._2003._03.business_process.TScope scope) {
        this.scope = scope;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Activity)) return false;
        Activity other = (Activity) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.empty==null && other.getEmpty()==null) || 
             (this.empty!=null &&
              this.empty.equals(other.getEmpty()))) &&
            ((this.invoke==null && other.getInvoke()==null) || 
             (this.invoke!=null &&
              this.invoke.equals(other.getInvoke()))) &&
            ((this.receive==null && other.getReceive()==null) || 
             (this.receive!=null &&
              this.receive.equals(other.getReceive()))) &&
            ((this.reply==null && other.getReply()==null) || 
             (this.reply!=null &&
              this.reply.equals(other.getReply()))) &&
            ((this.assign==null && other.getAssign()==null) || 
             (this.assign!=null &&
              this.assign.equals(other.getAssign()))) &&
            ((this.wait==null && other.getWait()==null) || 
             (this.wait!=null &&
              this.wait.equals(other.getWait()))) &&
            ((this._throw==null && other.get_throw()==null) || 
             (this._throw!=null &&
              this._throw.equals(other.get_throw()))) &&
            ((this.terminate==null && other.getTerminate()==null) || 
             (this.terminate!=null &&
              this.terminate.equals(other.getTerminate()))) &&
            ((this.flow==null && other.getFlow()==null) || 
             (this.flow!=null &&
              this.flow.equals(other.getFlow()))) &&
            ((this._switch==null && other.get_switch()==null) || 
             (this._switch!=null &&
              this._switch.equals(other.get_switch()))) &&
            ((this._while==null && other.get_while()==null) || 
             (this._while!=null &&
              this._while.equals(other.get_while()))) &&
            ((this.sequence==null && other.getSequence()==null) || 
             (this.sequence!=null &&
              this.sequence.equals(other.getSequence()))) &&
            ((this.pick==null && other.getPick()==null) || 
             (this.pick!=null &&
              this.pick.equals(other.getPick()))) &&
            ((this.scope==null && other.getScope()==null) || 
             (this.scope!=null &&
              this.scope.equals(other.getScope())));
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
        if (getEmpty() != null) {
            _hashCode += getEmpty().hashCode();
        }
        if (getInvoke() != null) {
            _hashCode += getInvoke().hashCode();
        }
        if (getReceive() != null) {
            _hashCode += getReceive().hashCode();
        }
        if (getReply() != null) {
            _hashCode += getReply().hashCode();
        }
        if (getAssign() != null) {
            _hashCode += getAssign().hashCode();
        }
        if (getWait() != null) {
            _hashCode += getWait().hashCode();
        }
        if (get_throw() != null) {
            _hashCode += get_throw().hashCode();
        }
        if (getTerminate() != null) {
            _hashCode += getTerminate().hashCode();
        }
        if (getFlow() != null) {
            _hashCode += getFlow().hashCode();
        }
        if (get_switch() != null) {
            _hashCode += get_switch().hashCode();
        }
        if (get_while() != null) {
            _hashCode += get_while().hashCode();
        }
        if (getSequence() != null) {
            _hashCode += getSequence().hashCode();
        }
        if (getPick() != null) {
            _hashCode += getPick().hashCode();
        }
        if (getScope() != null) {
            _hashCode += getScope().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Activity.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "activity"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("empty");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "empty"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tEmpty"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("invoke");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "invoke"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tInvoke"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("receive");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "receive"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tReceive"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reply");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "reply"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tReply"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("assign");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "assign"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tAssign"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("wait");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "wait"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tWait"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_throw");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "throw"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tThrow"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("terminate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "terminate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tTerminate"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("flow");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "flow"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tFlow"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_switch");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "switch"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tSwitch"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_while");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "while"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tWhile"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sequence");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "sequence"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tSequence"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pick");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "pick"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tPick"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scope");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "scope"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2003/03/business-process/", "tScope"));
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
