<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


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
                 headerClass="contentTableHeader" columns="2">

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
            <h:commandLink action="#{centers.navigateToCenter}">
                <h:outputText value="#{services.navigatedService.researchCenter.shortName}"/>
                <f:param name="navigatedCenterPk"
                         value="#{services.navigatedService.researchCenter.pk}"/>
            </h:commandLink>
        </h:column>

        <h:column>
            <h:outputText value="Service Type"/>
        </h:column>
        <h:column>
            <h:outputText value="#{services.navigatedService.type}"/>
        </h:column>

    </h:panelGrid>
    <f:verbatim><br/><br/></f:verbatim>
</h:column>


<%-- Operations List --%>
<h:column rendered="#{not empty services.navigatedService.operationCollection}">
    <t:panelGrid styleClass="contentInnerTable" cellpadding="3"
                 rowClasses="dataRowLight,dataRowDark"
                 columnClasses="dataCellTextBold,dataCellText"
                 headerClass="contentTableHeader">

        <f:facet name="header">
            <h:column>
                <h:outputText value="Supported Operations"/>
            </h:column>
        </f:facet>


        <h:column>
            <t:dataTable var="operation"
                         styleClass="contentSubInnerTable" cellpadding="3"
                         rowClasses="dataRowLight,dataRowDark"
                         columnClasses="dataCellTextBold,dataCellText"
                         headerClass="contentSubInnerTableTitle"
                         value="#{services.navigatedService.operationCollection}">

                <h:column>
                    <f:facet name="header">
                        <h:outputText value="Operation Name"/>
                    </f:facet>

                    <h:outputText value="#{operation.name}"/>
                </h:column>


                <h:column>
                    <f:facet name="header">
                        <h:outputText value="Description"/>
                    </f:facet>

                    <h:outputText value="#{operation.description}"/>
                </h:column>


            </t:dataTable>
        </h:column>

    </t:panelGrid>
</h:column>


<%-- Domain Model Display --%>
<h:column rendered="#{not empty services.navigatedService.domainModel}">
    <h:panelGrid styleClass="contentInnerTable" cellpadding="3"
                 rowClasses="dataRowLight,dataRowDark"
                 columnClasses="dataCellTextBold,dataCellText"
                 headerClass="contentTableHeader" columns="2">

        <f:facet name="header">
            <h:column>
                <h:outputText value="Domain Model"/>
            </h:column>
        </f:facet>


        <h:column>
            <h:outputText value="Name"/>
        </h:column>
        <h:column>
            <h:outputText value="#{services.navigatedService.domainModel.longName}"/>
        </h:column>

        <h:column>
            <h:outputText value="Project Name"/>
        </h:column>
        <h:column>
            <h:outputText value="#{services.navigatedService.domainModel.projectShortName}"/>
        </h:column>


        <h:column>
            <h:outputText value="Project Version"/>
        </h:column>
        <h:column>
            <h:outputText value="#{services.navigatedService.domainModel.projectVersion}"/>
        </h:column>

        <h:column>
            <h:outputText value="Project Description"/>
        </h:column>
        <h:column>
            <h:outputText value="#{services.navigatedService.domainModel.projectDescription}"/>
        </h:column>


        <h:column>
            <h:outputText value="Object Model"/>
        </h:column>
        <h:column>
            <t:dataTable var="umlClass"
                         styleClass="contentSubInnerTable" cellpadding="3"
                         rowClasses="dataRowLight,dataRowDark"
                         columnClasses="dataCellTextBold,dataCellText"
                         headerClass="contentSubInnerTableTitle"
                         value="#{services.navigatedService.domainModel.umlClassCollection}">

                <h:column>
                    <f:facet name="header">
                        <h:outputText value="Class Name"/>
                    </f:facet>

                    <h:outputText value="#{umlClass.className}"/>
                </h:column>

                <h:column>
                    <f:facet name="header">
                        <h:outputText value="Package Name"/>
                    </f:facet>

                    <h:outputText value="#{umlClass.packageName}"/>
                </h:column>

            </t:dataTable>
        </h:column>


    </h:panelGrid>

</h:column>

</h:panelGrid>
</f:subview>