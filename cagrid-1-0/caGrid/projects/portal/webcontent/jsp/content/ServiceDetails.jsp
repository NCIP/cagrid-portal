<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>


<f:subview id="serviceDetails">

<h:panelGrid styleClass="contentMainTable" headerClass="contentMainTitle"
             columns="1">

<f:facet name="header">
    <h:column>
        <h:outputText value="Service Details"/>
    </h:column>
</f:facet>

<f:verbatim><br/></f:verbatim>
<%/*-- Scroller to scroll through results */%>

<h:column>

    <h:panelGrid styleClass="contentInnerTable" cellpadding="3"
                 rowClasses="dataRowLight,dataRowDark"
                 columnClasses="dataCellTextBold,dataCellText"
                 headerClass="dataTableHeader" columns="2">

        <f:facet name="header">
            <h:column>
                <h:outputText value="Service Details"/>
            </h:column>
        </f:facet>

        <h:column>
            <h:outputText value="Name"/>
        </h:column>
        <h:column>
            <h:outputText value="#{services.navigatedService.name}"/>
        </h:column>

        <h:column>
            <h:outputText value="Version"/>
        </h:column>
        <h:column>
            <h:outputText value="#{services.navigatedService.version}"/>
        </h:column>

        <h:column>
            <h:outputText value="URL"/>
        </h:column>
        <h:column>
            <h:outputText value="#{services.navigatedService.EPR}"/>
        </h:column>

        <h:column>
            <h:outputText value="Descriptions"/>
        </h:column>
        <h:column>
            <h:outputText value="#{services.navigatedService.description}"/>
        </h:column>

        <h:column>
            <h:outputText value="Version"/>
        </h:column>
        <h:column>
            <h:outputText value="#{services.navigatedService.version}"/>
        </h:column>

        <h:column>
            <h:outputText value="Research Center"/>
        </h:column>
        <h:column>
            <h:commandLink action="#{centerDetails.navigateTo}">
                <h:outputText value="#{services.navigatedService.researchCenter.shortName}"/>
                <f:param id="navigatedCenterPk" name="navigatedCenterPk"
                         value="#{services.navigatedService.researchCenter.pk}"/>
            </h:commandLink>
        </h:column>

        <h:column>
            <h:outputText value="Service Type"/>
        </h:column>
        <h:column>
            <h:outputText value="#{services.navigatedType}"/>
        </h:column>

    </h:panelGrid>
    <f:verbatim><br/><br/></f:verbatim>
</h:column>

<h:column rendered="#{services.showModel}">
    <h:panelGrid styleClass="contentInnerTable" border="1"
                 cellpadding="3" cellspacing="0"
                 rowClasses="dataRowLight,dataRowDark"
                 columnClasses="dataCellTextBold,dataCellText"
                 headerClass="dataTableHeader" columns="2">

        <f:facet name="header">
            <h:column>
                <h:outputText value="Domain Model"/>
            </h:column>
        </f:facet>


        <h:column>
            <h:outputText value="Name"/>
        </h:column>
        <h:column>
            <h:outputText value="#{serviceDetails.service.domainModel.longName}"/>
        </h:column>

        <h:column>
            <h:outputText value="Project Name"/>
        </h:column>
        <h:column>
            <h:outputText value="#{serviceDetails.service.domainModel.projectShortName}"/>
        </h:column>


        <h:column>
            <h:outputText value="Project Version"/>
        </h:column>
        <h:column>
            <h:outputText value="#{serviceDetails.service.domainModel.projectVersion}"/>
        </h:column>

        <h:column>
            <h:outputText value="Project Description"/>
        </h:column>
        <h:column>
            <h:outputText value="#{serviceDetails.service.domainModel.projectDescription}"/>
        </h:column>


        <h:column>
            <h:outputText value="Object Model"/>
        </h:column>
        <h:column>
            <h:outputText value="#{serviceDetails.service.domainModel.projectDescription}"/>
        </h:column>


    </h:panelGrid>

</h:column>

</h:panelGrid>
</f:subview>