<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<!--Load message bundle-->
<f:loadBundle basename="Portal-Labels" var="labels"/>

<f:subview id="pocDirectory">

    <h:panelGrid styleClass="contentMainTable" headerClass="contentMainTitle" columns="1">

        <f:facet name="header">
            <h:column>
                <h:outputText value="#{labels.pocDetails}"/>
            </h:column>
        </f:facet>

        <f:verbatim><br/></f:verbatim>

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
                    <h:outputText value="#{people.navigatedPOC.email}"/>
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


                <h:column>
                    <h:outputText value="#{labels.hostingCenter}"/>
                </h:column>

                <h:column>
                    <h:commandLink action="#{centers.navigateToCenter}">
                        <h:outputText value="#{people.navigatedPOC.researchCenter.shortName}"/>
                        <f:param id="navigatedCenterPk" name="navigatedCenterPk"
                                 value="#{people.navigatedPOC.researchCenter.pk}"/>
                    </h:commandLink>
                </h:column>

            </h:panelGrid>


        </h:column>

    </h:panelGrid>
</f:subview>