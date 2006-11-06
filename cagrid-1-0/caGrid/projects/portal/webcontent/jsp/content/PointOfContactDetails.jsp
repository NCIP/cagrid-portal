<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles" %>

<%/*Load message bundle */%>
<f:loadBundle basename="Portal-Labels" var="labels"/>

<f:subview id="pocDirectory">

<h:panelGrid styleClass="contentMainTable" headerClass="contentMainTitle" columns="1">

<f:facet name="header">
    <h:column>
        <h:outputText value="#{labels.pocDetails}"/>
    </h:column>
</f:facet>

<%/* Navigation tile */%>
<h:column>
    <tiles:insert definition="navigation" ignore="true" flush="false"/>
</h:column>

<h:column>
    <h:panelGrid styleClass="contentInnerTable" border="0"
                 rowClasses="dataRowLight,dataRowDark"
                 columnClasses="dataCellTextBold,dataCellText"
                 headerClass="contentTableHeader" columns="2">
        <f:facet name="header">
            <h:column>
                <h:outputText value="#{labels.contactDetails}"/>
            </h:column>

        </f:facet>

        <h:column>
            <h:outputText value="#{labels.name}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{people.navigatedPOC.firstName} #{people.navigatedPOC.lastName}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.phNumber}"/>
        </h:column>
        <h:column>
            <h:outputLink target="new" value="#{people.navigatedPOC.phoneNumber}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.email}"/>
        </h:column>
        <h:column>
            <h:outputLink value="mailto:#{people.navigatedPOC.email}">
                <h:outputText value="#{people.navigatedPOC.email}"/>
            </h:outputLink>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.affiliation}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{people.navigatedPOC.affiliation}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.role}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{people.navigatedPOC.role}"/>
        </h:column>

    </h:panelGrid>
</h:column>

<h:column>
    <f:verbatim><br/></f:verbatim>
</h:column>

<%/* Research Center Details */%>
<h:column rendered="#{not empty people.navigatedPOC.researchCenter}">
    <h:panelGrid styleClass="contentInnerTable"
                 rowClasses="dataRowLight,dataRowDark"
                 columnClasses="dataCellTextBold,dataCellText"
                 headerClass="contentTableHeader" columns="2">
        <f:facet name="header">
            <h:column>
                <h:outputText value="#{labels.centerDetails}"/>
            </h:column>

        </f:facet>

        <h:column>
            <h:outputText value="#{labels.shortName}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{centers.navigatedCenter.shortName}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.displayName}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{people.navigatedPOC.researchCenter.displayName}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.description}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{people.navigatedPOC.researchCenter.description}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.homepageURL}"/>
        </h:column>
        <h:column>
            <h:outputLink target="new" value="#{people.navigatedPOC.researchCenter.homepageURL}">
                <h:outputText value="#{people.navigatedPOC.researchCenter.homepageURL}"/>
            </h:outputLink>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.rssURL}"/>
        </h:column>
        <h:column>
            <h:outputLink target="new" value="#{people.navigatedPOC.researchCenter.homepageURL}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.address}"/>
        </h:column>
        <h:column>
            <h:panelGrid columnClasses="dataCellText">
                <h:column rendered="#{not empty people.navigatedPOC.researchCenter.street1}">
                    <h:outputText value="#{people.navigatedPOC.researchCenter.street1}"/>
                </h:column>
                <h:column rendered="#{not empty people.navigatedPOC.researchCenter.street2}">
                    <h:outputText value="#{people.navigatedPOC.researchCenter.street2}"/>
                </h:column>
                <h:column rendered="#{not empty people.navigatedPOC.researchCenter.state}">
                    <h:outputText value="#{people.navigatedPOC.researchCenter.state}"/>
                    <h:outputText value=""/>
                </h:column>
                <h:column rendered="#{not empty people.navigatedPOC.researchCenter.postalCode}">
                    <h:outputText value="#{people.navigatedPOC.researchCenter.postalCode}"/>
                </h:column>

                <h:column rendered="#{not empty people.navigatedPOC.researchCenter.country}">
                    <h:outputText value="#{people.navigatedPOC.researchCenter.country}"/>
                </h:column>
            </h:panelGrid>
        </h:column>

    </h:panelGrid>
</h:column>

<h:column>
    <f:verbatim><br/></f:verbatim>
</h:column>

</h:panelGrid>
</f:subview>