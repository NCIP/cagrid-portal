<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles" %>

<f:loadBundle basename="Portal-Labels" var="labels"/>

<f:subview id="centerDetails">

    <h:panelGrid styleClass="contentMainTable" headerClass="contentMainTitle"
                 columns="1">

        <f:facet name="header">
            <h:column>
                <h:outputText value="#{labels.centerDetails}"/>
            </h:column>
        </f:facet>

        <%/* Navigation tile */%>
        <h:column>
            <tiles:insert definition="navigation" ignore="true" flush="false"/>
        </h:column>

        <h:column>


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
                    <h:outputText value="#{centers.navigatedCenter.displayName}"/>
                </h:column>

                <h:column>
                    <h:outputText value="#{labels.description}"/>
                </h:column>
                <h:column>
                    <h:outputText value="#{centers.navigatedCenter.description}"/>
                </h:column>

                <h:column>
                    <h:outputText value="#{labels.homepageURL}"/>
                </h:column>
                <h:column>
                    <h:outputLink target="new" value="#{centers.navigatedCenter.homepageURL}">
                        <h:outputText value="#{centers.navigatedCenter.homepageURL}"/>
                    </h:outputLink>
                </h:column>

                <h:column>
                    <h:outputText value="#{labels.rssURL}"/>
                </h:column>
                <h:column>
                    <h:outputLink target="new" value="#{centers.navigatedCenter.homepageURL}"/>
                </h:column>

                <h:column>
                    <h:outputText value="#{labels.address}"/>
                </h:column>
                <h:column>
                    <h:panelGrid columnClasses="dataCellText">
                        <h:column rendered="#{not empty centers.navigatedCenter.street1}">
                            <h:outputText value="#{centers.navigatedCenter.street1}"/>
                        </h:column>
                        <h:column rendered="#{not empty centers.navigatedCenter.street2}">
                            <h:outputText value="#{centers.navigatedCenter.street2}"/>
                        </h:column>
                        <h:column rendered="#{not empty centers.navigatedCenter.state}">
                            <h:outputText value="#{centers.navigatedCenter.state}"/>
                            <h:outputText value=""/>
                        </h:column>
                        <h:column rendered="#{not empty centers.navigatedCenter.postalCode}">
                            <h:outputText value="#{centers.navigatedCenter.postalCode}"/>
                        </h:column>

                        <h:column rendered="#{not empty centers.navigatedCenter.country}">
                            <h:outputText value="#{centers.navigatedCenter.country}"/>
                        </h:column>
                    </h:panelGrid>
                </h:column>


            </h:panelGrid>

        </h:column>

    </h:panelGrid>

</f:subview>