<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles" %>

<%/*Load message bundle */%>
<f:loadBundle basename="Portal-Labels" var="labels"/>

<f:subview id="serviceDetails">

<h:panelGrid styleClass="contentMainTable" headerClass="contentMainTitle"
             columns="1">

<f:facet name="header">
    <h:column>
        <h:outputText value="#{labels.serviceDetails}"/>
    </h:column>
</f:facet>


<%/* Navigation tile */%>
<h:column>
    <tiles:insert definition="navigation" ignore="true" flush="false"/>
</h:column>


<h:column>

    <h:panelGrid styleClass="contentInnerTable" cellpadding="3"
                 rowClasses="dataRowLight,dataRowDark"
                 columnClasses="dataCellTextBold,dataCellText"
                 headerClass="contentTableHeader" columns="2">

        <f:facet name="header">
            <h:column>
                <h:outputText value="#{labels.serviceDetails}"/>
            </h:column>
        </f:facet>

        <h:column>
            <h:outputText value="#{labels.name}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{services.navigatedService.name}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.version}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{services.navigatedService.version}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.url}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{services.navigatedService.EPR}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.description}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{services.navigatedService.description}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.status}"/>
        </h:column>
        <h:column>
            <f:verbatim>&nbsp;</f:verbatim>
            <f:subview id="active" rendered="#{services.navigatedServiceStatus==1}">
                <h:graphicImage url="/images/activeService.jpg"
                                alt="Active Service"/>
                <h:outputText value=" Service Active" styleClass="dataCellTextBold"/>
            </f:subview>

            <f:subview id="inactive"
                       rendered="#{services.navigatedServiceStatus==0}">
                <h:graphicImage url="/images/inactiveService.jpg" value="Inactive"
                                alt="Inactive Service"/>
                <h:outputText value=" Service Inactive" styleClass="dataCellTextBold"/>
            </f:subview>

            <f:subview id="invalid"
                       rendered="#{services.navigatedServiceStatus==-1}">
                <h:graphicImage url="/images/activeService.jpg" value="Invalid"
                                alt="Invalid Service"/>
                <h:outputText value=" Invalid caGrid Service." styleClass="dataCellTextBold"/>
            </f:subview>
        </h:column>


        <h:column>
            <h:outputText value="#{labels.serviceType}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{services.navigatedService.type}"/>
        </h:column>

    </h:panelGrid>
</h:column>

<h:column>
    <f:verbatim><br/></f:verbatim>
</h:column>

<%/* Research Center information */%>
<h:column rendered="#{not empty services.navigatedService.researchCenter}">
    <t:panelGrid styleClass="contentInnerTable" cellpadding="3"
                 rowClasses="dataRowLight,dataRowDark"
                 columnClasses="dataCellTextBold,dataCellText"
                 headerClass="contentTableHeader" columns="2">

        <f:facet name="header">
            <h:column>
                <h:outputText value="#{labels.hostingCenter}"/>
            </h:column>
        </f:facet>

        <h:column>
            <h:outputText value="#{labels.shortName}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{services.navigatedService.researchCenter.shortName}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.displayName}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{services.navigatedService.researchCenter.displayName}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.description}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{services.navigatedService.researchCenter.description}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.homepageURL}"/>
        </h:column>
        <h:column>
            <h:outputLink target="new" value="#{services.navigatedService.researchCenter.homepageURL}">
                <h:outputText value="#{services.navigatedService.researchCenter.homepageURL}"/>
            </h:outputLink>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.rssURL}"/>
        </h:column>
        <h:column>
            <h:outputLink target="new" value="#{services.navigatedService.researchCenter.homepageURL}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.address}"/>
        </h:column>
        <h:column>
            <h:panelGrid columnClasses="dataCellText">
                <h:column rendered="#{not empty services.navigatedService.researchCenter.street1}">
                    <h:outputText value="#{services.navigatedService.researchCenter.street1}"/>
                </h:column>
                <h:column rendered="#{not empty services.navigatedService.researchCenter.street2}">
                    <h:outputText value="#{services.navigatedService.researchCenter.street2}"/>
                </h:column>
                <h:column rendered="#{not empty services.navigatedService.researchCenter.state}">
                    <h:outputText value="#{services.navigatedService.researchCenter.state}"/>
                    <h:outputText value=""/>
                </h:column>
                <h:column rendered="#{not empty services.navigatedService.researchCenter.postalCode}">
                    <h:outputText value="#{services.navigatedService.researchCenter.postalCode}"/>
                </h:column>

                <h:column rendered="#{not empty services.navigatedService.researchCenter.country}">
                    <h:outputText value="#{services.navigatedService.researchCenter.country}"/>
                </h:column>
            </h:panelGrid>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.poc}"/>
        </h:column>
        <h:column>
            <t:dataTable var="poc"
                         value="#{services.navigatedService.researchCenter.pocCollection}"
                         rowIndexVar="index">
                <h:column>
                    <h:commandLink action="#{services.navigateToPOC}" styleClass="link">
                        <h:outputText value="#{poc.firstName} #{poc.lastName}"/>
                        <f:param name="navigatedPOCIndex"
                                 value="#{index}"/>
                    </h:commandLink>
                </h:column>
            </t:dataTable>
        </h:column>

    </t:panelGrid>

</h:column>

<h:column>
    <f:verbatim><br/></f:verbatim>
</h:column>

<%/*-- Operations List --*/%>
<h:column rendered="#{not empty services.navigatedService.operationCollection}">
    <t:panelGrid styleClass="contentInnerTable" cellpadding="1"
                 rowClasses="dataRowLight,dataRowDark"
                 columnClasses="dataCellTextBold,dataCellText"
                 headerClass="contentTableHeader">

        <f:facet name="header">
            <h:column>
                <h:outputText value="#{labels.supportedOperations}"/>
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
                        <h:outputText value="#{labels.operationName}"/>
                    </f:facet>

                    <h:outputText value="#{operation.name}"/>
                </h:column>


                <h:column>
                    <f:facet name="header">
                        <h:outputText value="#{labels.description}"/>
                    </f:facet>

                    <h:outputText value="#{operation.description}"/>
                </h:column>


            </t:dataTable>
        </h:column>

    </t:panelGrid>
</h:column>


<%/* Domain Model Display */%>
<h:column rendered="#{not empty services.navigatedService.domainModel}">
    <h:panelGrid styleClass="contentInnerTable" cellpadding="3"
                 rowClasses="dataRowLight,dataRowDark"
                 columnClasses="dataCellTextBold,dataCellText"
                 headerClass="contentTableHeader" columns="2">

        <f:facet name="header">
            <h:column>
                <h:outputText value="#{labels.domainModel}"/>
            </h:column>
        </f:facet>


        <h:column>
            <h:outputText value="#{labels.name}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{services.navigatedService.domainModel.longName}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.projectName}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{services.navigatedService.domainModel.projectShortName}"/>
        </h:column>


        <h:column>
            <h:outputText value="#{labels.projectVersion}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{services.navigatedService.domainModel.projectVersion}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.projectDescription}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{services.navigatedService.domainModel.projectDescription}"/>
        </h:column>


        <h:column>
            <h:outputText value="#{labels.objectModel}"/>
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
                        <h:outputText value="#{labels.className}"/>
                    </f:facet>

                    <h:outputText value="#{umlClass.className}"/>
                </h:column>

                <h:column>
                    <f:facet name="header">
                        <h:outputText value="#{labels.packageName}"/>
                    </f:facet>

                    <h:outputText value="#{umlClass.packageName}"/>
                </h:column>

            </t:dataTable>
        </h:column>


    </h:panelGrid>

</h:column>

<h:column>
    <f:verbatim><br/></f:verbatim>
</h:column>

</h:panelGrid>
</f:subview>