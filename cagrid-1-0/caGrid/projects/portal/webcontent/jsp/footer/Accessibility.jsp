<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>

<f:subview id="ftrContent">

    <h:panelGrid styleClass="ftrTable"
                 headerClass="ftrHeader" columnClasses="ftrContent">

        <f:facet name="header">
            <h:column>
                <h:outputText value="#{labels.accessibilityTitle}"/>
            </h:column>
        </f:facet>

        <h:column>
            <h:outputText value="#{labels.accessibilityDescription1}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.accessibilityDescription2} "/>
            <h:outputLink value="http://www.section508.gov/"
                          target="new">
                <h:outputText value="#{labels.section508LinkTitle}"/>
            </h:outputLink>

        </h:column>

        <h:column>
            <h:outputText value="#{labels.accessibilityDescription4} "/>
            <h:outputLink target="new" value="">
                <h:outputText value="#{labels.accessiblityLinkTitle}"/>
            </h:outputLink>
        </h:column>


    </h:panelGrid>

</f:subview>