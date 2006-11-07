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
                    <h:outputLink value="QueryGDSF.tiles" styleClass="formText">
                        <h:outputText value="#{messages.backToQueryGDSF}"></h:outputText>
                    </h:outputLink>
                </td>

            </tr>
        </table>
        <br>
        <table width="700" border="0" align="center" cellpadding="0" cellspacing="0" summary="">
            <tr>
                <td width="700">
                    <!-- welcome begins -->
                    <table width="100%" border="0" cellpadding="3" cellspacing="0" summary="">
                        <tr>
                            <td height="18" colspan="3" class="formHeader"><span class="title">
                                                            <h:outputText value="#{messages.queryErrorTitle}"/>
                                                            </span></td>
                        </tr>
                        <tr>
                            <td colspan="2" class="formTitle"><span class="menutd"><b>
                                <h:outputText value="#{messages.gdsfURL}"/>
                                <h:outputText value="#{discoveredServices.navigatedService.URL}"/>

                            </b></span></td>
                        </tr>
                        <tr>
                            <td colspan="2">

                                <div align="left">


                                        <h:outputText escape="true" styleClass="loginFailed"
                                                      value="#{discoveredServices.navigatedService.queryResult}"/>

                                </div>

                            </td>
                        </tr>
                    </table>

                </td>
            </tr></table>


    </h:form>
</f:view>