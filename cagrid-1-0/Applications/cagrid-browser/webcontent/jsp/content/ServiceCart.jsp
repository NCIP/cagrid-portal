<%--
Created by IntelliJ IDEA.
User: kherm
Date: Jul 4, 2005
Time: 5:09:48 PM
To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:loadBundle basename="labels" var="labels"/>
<f:loadBundle basename="messages" var="messages"/>
<f:view>


    <h:form>


        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td width="48%">&lt;&lt;
                    <h:outputLink value="DiscoveryResults.tiles" styleClass="formText">
                        <h:outputText value="#{messages.backToSearchResults}"></h:outputText>
                    </h:outputLink>
                </td>
            </tr>
        </table>

        <br>
        <table width="550" border="0" align="center" cellpadding="0" cellspacing="0" summary="">
            <tr>
                <td width="700">

                    <!-- welcome begins -->
                    <table width="100%" border="0" cellpadding="3" cellspacing="0">
                        <tr>
                            <td height="18" class="formHeader">
                                <h:outputText value="#{messages.cartMainTitle}"/>
                            </td>
                        </tr>
                        
                        <tr>
                            <td class="formTitle">
                                <h:outputText value="#{messages.cartSubTitle}"/>
                                <br/>
                                <h:outputText styleClass="loginFailed" value="#{serviceCart.serviceCartFailureMessage}"/>
                            </td>
                        </tr>


                        <tr>
                            <td class="formLabel">


                                <h:dataTable rowClasses="formLabel,formLabelWhite"
                                             value="#{serviceCart.shoppedServices}" var="service">

                                    <h:column>
                                        <h:commandLink action="#{service.navigateToServiceDetails}" value="#{service.epr.address}">

                                        </h:commandLink>
                                    </h:column>

                                    <h:column>
                                        <h:commandButton value="#{labels.removeServiceCart}"
                                                         action="#{service.removeFromServiceCart}"></h:commandButton>
                                    </h:column>
                                </h:dataTable>

                            </td>


                        </tr>


                    </table>

                </td></tr></table>
    </h:form>
</f:view>