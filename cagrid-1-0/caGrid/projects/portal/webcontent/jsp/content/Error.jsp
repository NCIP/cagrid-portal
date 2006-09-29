<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:subview id="errorPage">

    <h:panelGrid style="height:100%;width:100%;" cellpadding="0" cellspacing="0" border="0"
                 headerClass="errorTitle" columnClasses="mainContent">

        <f:facet name="header">
            <h:column>
                <h:outputText value="Application Error"/>
            </h:column>
        </f:facet>

        <f:verbatim><br/></f:verbatim>

        <%/*-- Scroller to scroll through results */%>


        <h:column>

            <h:panelGrid columns="1"
                         styleClass="contentTable"
                         columnClasses="errorContent">

                <h:column>
                    <h:outputText value="caGrid Portal has encountered an error condition.
                Please try again or contact a system administrator"/>
                </h:column>


                <h:column>
                    <h:outputText value="#{errorDisplay.infoMessage}"/>
                </h:column>


                <h:column>
                    <h:outputText value="Complete Error Stack"
                                  rendered="#{errorDisplay.showError}"
                            />
                </h:column>

                <h:column rendered="#{errorDisplay.showError}">
                    <h:inputTextarea cols="80" rows="30" readonly="true"
                                     value="#{errorDisplay.stackTrace}"
                            />
                </h:column>

            </h:panelGrid>
            <f:verbatim><br/><br/></f:verbatim>

        </h:column>

    </h:panelGrid>
</f:subview>