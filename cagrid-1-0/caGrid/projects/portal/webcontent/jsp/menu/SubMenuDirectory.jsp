<%-- sub-items begin --%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>

<f:subview id="subMenu">

    <h:panelGrid styleClass="subMenuTable"
                 columnClasses="mainMenuSubItems,mainMenuSubItemsSpacer,mainMenuSubItems,mainMenuSubItemsSpacer,mainMenuSubItems,mainMenuSubItemsSpacer"
                 cellpadding="0" cellspacing="4"
                 columns="6">

        <h:column>
            <h:commandLink styleClass="mainMenuSubLink"
                           action="#{directory.navigateToServicesDirectory}">
                <h:outputText value="Services" styleClass="mainMenuSubLink"/>
            </h:commandLink>
        </h:column>

        <h:column>
            <t:graphicImage url="images/mainMenuSeparator.gif" height="16"/>
        </h:column>

        <h:column>
            <h:commandLink styleClass="mainMenuSubLink"
                           action="#{directory.navigateToCenterDirectory}">
                <h:outputText value="Research Center"/>
            </h:commandLink>
        </h:column>

        <h:column>
            <t:graphicImage url="images/mainMenuSeparator.gif" width="1" height="16"/>
        </h:column>

        <h:column>
            <h:commandLink styleClass="mainMenuSubLink"
                           action="#{directory.navigateToPeopleDirectory}">
                <h:outputText value="People"/>
            </h:commandLink>
        </h:column>

        <h:column>
            <t:graphicImage url="images/mainMenuSeparator.gif" width="1" height="16"/>
        </h:column>

    </h:panelGrid>
</f:subview>
<%-- sub-items end --%>