<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<t:saveState id="ss1" value="#{directoryTabbedPaneBean}"/>


<f:subview id="panelTabbedPane1">

    <h:panelGrid align="left" width="80%">

        <t:panelTabbedPane bgcolor="#FFFFCC">
            <t:panelTab id="servicePane" label="Registered Services"
                        rendered="#{directoryTabbedPaneBean.servicePaneVisible}">
                <h:dataTable var="services" value="#{portal.services}">
                    <h:column>
                        Name
                    </h:column>
                    <h:column>
                        Value
                    </h:column>
                </h:dataTable>

                <f:verbatim><br/><br/></f:verbatim>
            </t:panelTab>

            <t:panelTab id="center" label="Research Center" rendered="#{directoryTabbedPaneBean.centerPaneVisible}">
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