<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="Portal-Labels" var="labels"/>

<f:subview id="participantDetails">

<h:panelGrid styleClass="contentMainTable" headerClass="contentMainTitle"
             columns="1">

<f:facet name="header">
    <h:column>
        <h:outputText value="#{labels.participantDetails}"/>
    </h:column>
</f:facet>

<f:verbatim><br/></f:verbatim>
<%/*-- Scroller to scroll through results */%>

<h:column>


    <h:panelGrid styleClass="contentInnerTable"
                 rowClasses="dataRowLight,dataRowDark"
                 columnClasses="dataCellTextBold,dataCellText"
                 headerClass="contentTableHeader" columns="2">
        <f:facet name="header">
            <h:column>
                <h:outputText value="#{labels.participantDetails}"/>
            </h:column>

        </f:facet>

        <h:column>
            <h:outputText value="#{labels.name}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{participants.navigatedParticipant.name}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.participantInstitute}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{participants.navigatedParticipant.institute}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.homepageURL}"/>
        </h:column>
        <h:column>
            <h:outputLink target="new" value="#{participants.navigatedParticipant.homepageURL}">
                <h:outputText value="#{participants.navigatedParticipant.homepageURL}"/>
            </h:outputLink>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.email}"/>
        </h:column>
        <h:column>
            <h:outputLink target="new" value="#{participants.navigatedParticipant.email}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.phNumber}"/>
        </h:column>
        <h:column>
            <h:outputLink target="new" value="#{participants.navigatedParticipant.phoneNumber}"/>
        </h:column>


        <h:column>
            <h:outputText value="#{labels.address}"/>
        </h:column>
        <h:column>
            <h:panelGrid columnClasses="dataCellText">
                <h:column rendered="#{not empty participants.navigatedParticipant.street1}">
                    <h:outputText value="#{participants.navigatedParticipant.street1}"/>
                </h:column>
                <h:column rendered="#{not empty participants.navigatedParticipant.street2}">
                    <h:outputText value="#{participants.navigatedParticipant.street2}"/>
                </h:column>
                <h:column rendered="#{not empty participants.navigatedParticipant.state}">
                    <h:outputText value="#{participants.navigatedParticipant.state}"/>
                    <h:outputText value=""/>
                </h:column>
                <h:column rendered="#{not empty participants.navigatedParticipant.postalCode}">
                    <h:outputText value="#{participants.navigatedParticipant.postalCode}"/>
                </h:column>

                <h:column rendered="#{not empty participants.navigatedParticipant.country}">
                    <h:outputText value="#{participants.navigatedParticipant.country}"/>
                </h:column>
            </h:panelGrid>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.workspace}"/>
        </h:column>
        <h:column>
            <t:dataTable var="workspace"
                         columnClasses="dataCellText"
                         value="#{participants.navigatedParticipant.workspaceCollection}">
                <h:column>
                    <h:outputText value="#{workspace.longName}"/>
                </h:column>
            </t:dataTable>
        </h:column>
        <h:column>
            <h:outputText value="#{labels.status}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{participants.navigatedParticipant.status}"/>
        </h:column>

    </h:panelGrid>

</h:column>

</h:panelGrid>

</f:subview>