<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>

<h:panelGrid columns="2" style="height:100%;" cellpadding="0" cellspacing="0" border="0">
    <h:column>

        <%-- welcome begins --%>
        <h:panelGrid style="height:100%" cellpadding="0" cellspacing="0" border="0"
                     headerClass="welcomeTitle" columnClasses="welcomeContent">
            <f:facet name="header">
                <h:column>
                    <h:outputText value="Welcome to caGrid Portal"/>
                </h:column>
            </f:facet>

            <h:column>
                <h:outputText value="caGrid Monitoring and Discovery Portal (MD Portal)
                 provides an overview of caGrid. Site is under development"/>
            </h:column>
        </h:panelGrid>
        <%-- welcome ends --%>

    </h:column>

    <h:column>
        <%-- sidebar begins --%>
        <h:panelGrid style="height:100%;" cellpadding="0" cellspacing="0" border="0">
            <%-- what's new begins --%>
            <h:column>
                <h:panelGrid columnClasses="sidebarContent" headerClass="sidebarTitle" style="height:100%"
                             styleClass="sidebarSection" summary="" cellpadding="0" cellspacing="0" border="0"
                             width="100%">
                    <f:facet name="header">
                        <h:column>
                            <h:outputText value="WHAT'S NEW"/>
                        </h:column>
                    </f:facet>

                    <h:column>
                        <h:outputText value="caGrid 1.0 beta has been released"/>
                    </h:column>
                </h:panelGrid>
            </h:column>
            <%-- what's new ends --%>

            <%-- did you know? begins --%>
            <h:column>
                <h:panelGrid style="width:100%;height:100%;" styleClass="sidebarSection"
                             headerClass="sidebarTitle" columnClasses="sidebarContent"
                             summary="" cellpadding="0" cellspacing="0" border="0" width="100%">
                    <f:facet name="header">
                        <h:column>
                            <h:outputText value="DID YOU KNOW"/>
                        </h:column>
                    </f:facet>

                    <h:column>
                        <h:outputText value="MD Portal will release November 1st 2006"/>
                    </h:column>
                </h:panelGrid>
            </h:column>
            <%-- did you know? ends --%>


        </h:panelGrid>
        <%-- sidebar ends --%>
    </h:column>
</h:panelGrid>
