<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>
<%@ taglib uri="http://cagrid.nci.nih.gov/portal" prefix="portal" %>

<f:subview id="map">
    <h:form>
        <h:panelGrid styleClass="contentMainTable" headerClass="contentMainTitle" columns="1">

            <f:facet name="header">
                <h:column>
                    <h:outputText value="caGrid Map"/>
                </h:column>
            </f:facet>

            <f:verbatim><br/></f:verbatim>

            <h:column>

                <portal:map id="portalMap"
                            style="background-color: #5C5C5C;  border:2px; border-color:black; width:700px; height:430px"
                            var="rc" value="#{centers.list}"
                            useGoogle="true">
                    <h:column>
                        <h:outputText value="#{rc.latitude}"/>
                    </h:column>
                    <h:column>
                        <h:outputText value="#{rc.longitude}"/>
                    </h:column>


                </portal:map>

            </h:column>
        </h:panelGrid>
    </h:form>
</f:subview>