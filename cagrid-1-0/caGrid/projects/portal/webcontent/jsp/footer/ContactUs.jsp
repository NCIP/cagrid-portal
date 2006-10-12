<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>

<f:subview id="ftrContent">

    <h:panelGrid styleClass="ftrTable"
                 headerClass="ftrHeader" columnClasses="ftrContent">
        <f:facet name="header">
            <h:column>
                <h:outputText value="#{labels.contactTitle}"/>
            </h:column>
        </f:facet>

        <h:column>
            <f:verbatim><br/></f:verbatim>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.contactDescription}"/>
        </h:column>

        <h:column>
            <f:verbatim><br/></f:verbatim>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.contactAddressTitle}" styleClass="h3"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.contactAddressLine1} "/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.contactAddressLine2}"/>
        </h:column>


    </h:panelGrid>

</f:subview>