<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>

<t:panelTabbedPane selectedIndex="1"
                   activeTabStyleClass="dataTableHeader"
                   inactiveTabStyleClass="dataTableSecondaryLabel"
                   disabledTabStyleClass="dataTableSecondaryLabel"
                   activeSubStyleClass="dataTableHeader"
                   inactiveSubStyleClass="dataTableSecondaryLabel"
                   tabContentStyleClass="dataTablePrimaryLabel">
    <t:panelTab>
        <h:panelGrid columns="2">
            <h:column>
                Name
            </h:column>
            <h:column>
                Value
            </h:column>
        </h:panelGrid>
    </t:panelTab>
</t:panelTabbedPane>
