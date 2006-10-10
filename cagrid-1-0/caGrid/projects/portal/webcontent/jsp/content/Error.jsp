<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<% pageContext.removeAttribute("javax.faces.UIViewTag.COMPONENT_STACK"); %>

<f:subview id="errorPage">

    <h:panelGrid style="height:100%;width:100%;" cellpadding="0" cellspacing="0" border="0"
                 headerClass="errorTitle" columnClasses="errorContent">

        <f:facet name="header">
            <h:column>
                <h:outputText value="Application Error"/>
            </h:column>
        </f:facet>

        <f:verbatim><br/></f:verbatim>

        <h:column>
            <h:outputText value="#{labels.errorMessage}"/>
        </h:column>


        <f:verbatim><br/></f:verbatim>
        <h:column>
            <h:outputText value="#{labels.errorStack}"
                    />
        </h:column>

        <h:column rendered="#{not empty errorDisplay.stackTrace}">
            <h:inputTextarea cols="90" rows="30" readonly="true"
                             value="#{errorDisplay.stackTrace}"
                    />
        </h:column>

        <f:verbatim><br/></f:verbatim>


    </h:panelGrid>
</f:subview>