<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>

<f:subview id="directoryHome">

    <%-- welcome begins --%>
    <h:panelGrid style="height:100%;width:100%;" cellpadding="0" cellspacing="0" border="0"
                 headerClass="homepageTitle" columnClasses="homepageContent">
        <f:facet name="header">
            <h:column>
                <h:outputText value="caGrid Directory"/>
            </h:column>
        </f:facet>

        <h:column>
            <h:outputText value="caGrid Directory is a yellow pages of all services, people, research center
                    that are part of the caGrid 1.0 infastructure. You can lookup information on caGrid through
                    the Portal Directory"/>
        </h:column>
    </h:panelGrid>
    <%-- welcome ends --%>


</f:subview>