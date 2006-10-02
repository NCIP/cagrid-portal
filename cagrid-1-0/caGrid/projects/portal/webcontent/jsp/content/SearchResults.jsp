<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles" %>


<f:subview id="searchResults">

    <t:panelTabbedPane activeTabStyleClass="searchTabbedPaneActive"
                       inactiveTabStyleClass="searchTabbedPaneInactive"
                       width="80%"
                       styleClass="contentInnerTable">

        <t:panelTab id="services" label="Registered Service">
            <tiles:insert attribute="serviceSearchResult" flush="false" ignore="true"/>
        </t:panelTab>

        <t:panelTab id="rc" label="Research Center">
            <tiles:insert attribute="rcSearchResult" flush="false" ignore="true"/>
        </t:panelTab>


        <t:panelTab id="poc" label="People">
            <tiles:insert attribute="pocSearchResult" flush="false" ignore="true"/>
        </t:panelTab>

    </t:panelTabbedPane>
</f:subview>
