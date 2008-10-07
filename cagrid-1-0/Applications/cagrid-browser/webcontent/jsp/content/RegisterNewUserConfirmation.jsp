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

        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td width="48%">&lt;&lt;
                    <h:outputLink value="PreLogin.tiles" styleClass="formText">
                        <h:outputText value="#{messages.MenuItem1}"></h:outputText>
                    </h:outputLink>

                </td>
            </tr>
        </table>


        <h:panelGrid columns="1">
            <h:column>
                <f:facet name="header">
                    <h:outputText styleClass="formHeader" value="Registration request received"/>
                </f:facet>
            </h:column>

            <h:column>
                <h:outputText styleClass="formText"
                              value="#{loginBean.registrationSuccessMessage}"/>


            </h:column>

        </h:panelGrid>


    </h:form>
</f:view>