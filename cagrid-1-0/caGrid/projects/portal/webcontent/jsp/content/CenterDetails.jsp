<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:subview id="centerDetails">

    <h:panelGrid styleClass="contentMainTable" headerClass="contentMainTitle"
                 columns="1">

        <f:facet name="header">
            <h:column>
                <h:outputText value="Research Center Details"/>
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
                        <h:outputText value="Research Center Details"/>
                    </h:column>

                </f:facet>

                <h:column>
                    <h:outputText value="Short Name"/>
                </h:column>
                <h:column>
                    <h:outputText value="#{centers.navigatedCenter.shortName}"/>
                </h:column>

                <h:column>
                    <h:outputText value="Display Name"/>
                </h:column>
                <h:column>
                    <h:outputText value="#{centers.navigatedCenter.displayName}"/>
                </h:column>

                <h:column>
                    <h:outputText value="Description"/>
                </h:column>
                <h:column>
                    <h:outputText value="#{centers.navigatedCenter.description}"/>
                </h:column>

                <h:column>
                    <h:outputText value="Homepage URL"/>
                </h:column>
                <h:column>
                    <h:outputLink target="new" value="#{centers.navigatedCenter.homepageURL}"/>
                </h:column>

                <h:column>
                    <h:outputText value="RSS URL"/>
                </h:column>
                <h:column>
                    <h:outputLink target="new" value="#{centers.navigatedCenter.homepageURL}"/>
                </h:column>

                <h:column>
                    <h:outputText value="Address"/>
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