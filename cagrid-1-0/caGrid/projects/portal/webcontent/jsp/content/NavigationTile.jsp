<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>


<%/*Load message bundle */%>
<f:loadBundle basename="Portal-Labels" var="labels"/>

<f:subview id="Navigation">
    <h:panelGrid styleClass="navigationTable"
                 cellspacing="4" cellpadding="4">

        <h:column>
            <h:outputLink onclick="people.history.back()" styleClass="navigationBtn">
                <h:outputText value="<<Back"/>
            </h:outputLink>
        </h:column>
    </h:panelGrid>

</f:subview>