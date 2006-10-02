<%-- sub-items begin --%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>

<f:subview id="subMenu">

    <h:panelGrid styleClass="subMenuTable"
                 columnClasses="mainMenuSubItems,mainMenuSubItemsSpacer,mainMenuSubItems"
                 cellpadding="0" cellspacing="4"
                 columns="3">

        <h:column>
            <h:commandLink styleClass="mainMenuSubLink"
                           action="search.tiles">
                <h:outputText value="Search" styleClass="mainMenuSubLink"/>
            </h:commandLink>
        </h:column>

        <h:column>
            <t:graphicImage url="images/mainMenuSeparator.gif" height="16"/>
        </h:column>

        <h:column>
            <h:commandLink styleClass="mainMenuSubLink"
                           action="#{directory.navigateToCenterDirectory}">
                <h:outputText value="Advanced Search"/>
            </h:commandLink>
        </h:column>

    </h:panelGrid>
</f:subview>
<%-- sub-items end --%>