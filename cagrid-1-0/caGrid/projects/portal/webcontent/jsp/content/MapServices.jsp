<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>
<%@ taglib uri="http://cagrid.nci.nih.gov/portal" prefix="portal" %>

<f:subview id="map">

    <h:panelGrid styleClass="contentMainTable" headerClass="contentMainTitle" columns="1">

        <f:facet name="header">
            <h:column>
                <h:outputText value="caGrid Map"/>
            </h:column>
        </f:facet>

        <f:verbatim><br/></f:verbatim>

        <h:column>
            <portal:map id="portalMap" style="width:700px;  height:430px"
                        var="service" value="#{services.list}"
                        useGoogle="true">

                <h:column>
                    <h:outputText value="#{service.researchCenter.latitude}"/>
                </h:column>
                <h:column>
                    <h:outputText value="#{service.researchCenter.longitude}"/>
                </h:column>
            </portal:map>

        </h:column>
    </h:panelGrid>

</f:subview>