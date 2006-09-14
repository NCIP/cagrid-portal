<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<t:saveState id="ss1" value="#{directoryTabbedPaneBean}"/>


<f:subview id="panelTabbedPane1">

    <h:panelGrid align="top" width="80%">

        <t:panelTabbedPane bgcolor="#FFFFCC" align="top">
            <t:panelTab id="servicePane" label="Registered Services"
                        rendered="#{directoryTabbedPaneBean.servicePaneVisible}"
                        styleClass="dataTablePrimaryLabel">

                <h:dataTable var="service" value="#{portal.services}"
                             width="80%" columnClasses="dataCellText"
                             rowClasses="dataRowDark,dataRowLight">

                    <h:panelGrid rowClasses="dataRowDark,dataRowLight"
                                 headerClass="dataTableHeader" columns="2">
                        <f:facet name="header">
                            <h:column>
                                <h:outputText value="Service"/>
                            </h:column>
                        </f:facet>
                        <h:column>
                            Name:
                        </h:column>
                        <h:column>
                            <<h:outputText value="service.name"/>
                        </h:column>

                        <h:column>
                            Version:
                        </h:column>
                        <h:column>
                            <<h:outputText value="service.version"/>
                        </h:column>

                        <h:column>
                            Description:
                        </h:column>
                        <h:column>
                            <<h:outputText value="service.description"/>
                        </h:column>

                        <h:column/>
                        <h:column>
                            <h:outputLink value="More>>"/>
                        </h:column>

                    </h:panelGrid>


                </h:dataTable>

                <f:verbatim><br/><br/></f:verbatim>
            </t:panelTab>

            <t:panelTab id="center" label="Research Center" rendered="#{directoryTabbedPaneBean.centerPaneVisible}">
                <h:panelGrid columns="2">
                    <h:column>
                        Name:
                    </h:column>

                    <h:column>
                        Value
                    </h:column>
                </h:panelGrid>
                <f:verbatim><br/><br/></f:verbatim>
            </t:panelTab>

            <t:panelTab id="poc" label="Point of Contact" rendered="#{directoryTabbedPaneBean.pocPaneVisible}">
                <h:panelGrid columns="2">
                    <h:column>
                        Name
                    </h:column>
                    <h:column>
                        Value
                    </h:column>
                </h:panelGrid>
                <f:verbatim><br/><br/></f:verbatim>
            </t:panelTab>

            <f:verbatim><br/><hr/><br/></f:verbatim>

        </t:panelTabbedPane>

    </h:panelGrid>
</f:subview>