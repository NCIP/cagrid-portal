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
<f:view locale="#{browserConfig.locale}">


    <h:form>


        <!-- welcome begins -->
        <table width="100%" border="0" cellpadding="3" cellspacing="0">
            <tr>
                <td height="18" class="formHeader">
                    <h:outputText value="#{messages.configMainTitle}"/>
                </td>
            </tr>


            <tr>
                <td class="formTitle">
                    <h:outputText value="#{messages.configSubTitle}"/>
                </td>
            </tr>
            <tr>
                <td class="formLabel">

                    <h:panelGrid width="100%" rowClasses="formLabelWhite,formLabel" columns="2">

                        <h:column>
                            <h:outputText value="#{messages.indexConfig}"/>
                        </h:column>

                        <h:column>
                            <h:selectOneMenu id="url" valueChangeListener="#{indexService.changeURLListener}" immediate="true" onchange="submit()">
                                <f:selectItems value="#{indexService.indexItems}"/>
                            </h:selectOneMenu>
                        </h:column>


                    </h:panelGrid>
                </td>
            </tr>


        </table>


    </h:form>
</f:view>