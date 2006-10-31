<%-- sub-items begin --%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>


<f:subview id="subMenu">

    <h:panelGrid styleClass="subMenuTable"
                 columnClasses="mainMenuSubItems,mainMenuSubItemsSpacer,mainMenuSubItems,mainMenuSubItemsSpacer,mainMenuSubItems,mainMenuSubItemsSpacer"
                 cellpadding="0" cellspacing="4"
                 columns="4">

        <h:column>
            <h:commandLink styleClass="mainMenuSubLink"
                           action="#{map.navigateToServicesMap}">
                <h:outputText value="#{labels.services}" styleClass="mainMenuSubLink"/>
            </h:commandLink>
        </h:column>

        <h:column>
            <t:graphicImage url="images/mainMenuSeparator.gif" height="16"/>
        </h:column>

        <h:column>
            <h:commandLink styleClass="mainMenuSubLink"
                           action="#{map.navigateToParticipantMap}">
                <h:outputText value="#{labels.participants}"/>
            </h:commandLink>
        </h:column>

        <h:column>
            <t:graphicImage url="images/mainMenuSeparator.gif" width="1" height="16"/>
        </h:column>


    </h:panelGrid>
</f:subview>

<%-- sub-items end --%>