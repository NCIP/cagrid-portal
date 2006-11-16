<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles" %>


<f:subview id="searchResults">

    <t:panelTabbedPane activeTabStyleClass="activeTab"
                       inactiveTabStyleClass="inactiveTab"
                       styleClass="tabbedPane"
                       width="80%"
            >

        <t:panelTab id="services" rendered="#{not empty services.list}"
                    label="Registered Service  (#{services.listSize})">
            <tiles:insert attribute="serviceSearchResult" flush="false" ignore="true"/>
        </t:panelTab>

        <t:panelTab id="people" rendered="#{not empty people.list}" label="People (#{people.listSize})">
            <tiles:insert attribute="pocSearchResult" flush="false" ignore="true"/>
        </t:panelTab>


        <t:panelTab id="participants" rendered="#{not empty participants.list}"
                    label="caBIG Participants (#{participants.listSize})">
            <tiles:insert attribute="participantsResult" flush="false" ignore="true"/>
        </t:panelTab>

    </t:panelTabbedPane>
</f:subview>
