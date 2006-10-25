<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>
<%@ taglib uri="http://cagrid.nci.nih.gov/portal" prefix="portal" %>

<f:subview id="map">

    <h:panelGrid styleClass="contentMainTable" headerClass="contentMainTitle" columns="1">

        <f:facet name="header">
            <h:column>
                <h:outputText value="#{labels.mapcaGrid}"/>
            </h:column>
        </f:facet>

        <f:verbatim><br/></f:verbatim>

        <h:column>
            <!--Custom portal map component. See documentation for details
            <portal:map id="portalMap" styleClass="mapStyle"
                        var="service" value="#{map.nodes}"
                        useGoogle="true">
            </portal:map>

        </h:column>
    </h:panelGrid>

</f:subview>