<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>

<f:subview id="mapHome">

    <%-- welcome begins --%>
    <h:panelGrid styleClass="contentMainTable" cellpadding="0" cellspacing="0" border="0"
                 headerClass="contentMainTitle" columnClasses="mainContent">
        <f:facet name="header">
            <h:column>
                <h:outputText value="caGrid Map View"/>
            </h:column>
        </f:facet>

        <h:column>
            <h:outputText value="caGrid Map displays caGrid Resources (Research Centers, Services) on
                 the US map "/>
        </h:column>
    </h:panelGrid>
    <%-- welcome ends --%>


</f:subview>