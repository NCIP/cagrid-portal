<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>

<f:subview id="appSupport">

    <h:panelGrid styleClass="ftrTable"
                 headerClass="ftrHeader" columnClasses="ftrContent">
        <f:facet name="header">
            <h:column>
                <h:outputText value="#{labels.disclaimerTitle}"/>
            </h:column>
        </f:facet>

        <h:column>
            <h:outputText value="#{labels.disclaimerDescription1}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.disclaimerDescription2}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.disclaimerDescription3}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.disclaimerDescription4}"/>
        </h:column>

    </h:panelGrid>

</f:subview>