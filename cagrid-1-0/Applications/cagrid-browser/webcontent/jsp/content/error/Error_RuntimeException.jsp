<%--
  Created by IntelliJ IDEA.
  User: kherm
  Date: Jul 4, 2005
  Time: 5:06:02 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:loadBundle basename="labels" var="labels"/>
<f:loadBundle basename="messages" var="messages"/>
<f:view locale="#{browserConfig.locale}">


    <h:form>


        <h:panelGrid columns="1">
            <h:column>
                <f:facet name="header">
                    <h:outputText styleClass="formHeader" value="Error Page"/>
                </f:facet>
            </h:column>

            <h:column>
                <h:outputText styleClass="formText" value="#{messages.runtimeExceptionMessage}"/>


            </h:column>

        </h:panelGrid>


    </h:form>
</f:view>
</html>