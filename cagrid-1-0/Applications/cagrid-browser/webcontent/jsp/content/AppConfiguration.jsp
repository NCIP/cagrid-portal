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
                    <br/>
                    <h:outputText styleClass="loginFailed" value="#{discoveryBean.changeIndexServiceUrlsFailureMessage}"/>
                </td>
            </tr>
            <tr>
                <td class="formLabel">

                    <h:panelGrid rowClasses="formLabelWhite,formLabel" columns="1" headerClass="formTitle" >


						<f:facet name="header">
                        <h:column>
                            <h:outputText value="#{messages.indexServiceUrls}"/>
                        </h:column>
                        </f:facet>

                        <h:column>
                            <h:selectManyListbox id="url" value="#{discoveryBean.indexServiceUrlsSelected}" size="10">
                                <f:selectItems value="#{discoveryBean.indexServiceUrls}"/>
                            </h:selectManyListbox>
                        </h:column>
                        
                        <h:column>
                        	<h:commandButton id="addServiceCart" value="#{labels.changeIndexServiceUrls}"
                         		action="#{discoveryBean.changeIndexServiceUrls}"/>
                        </h:column>

                    </h:panelGrid>
                </td>
            </tr>


        </table>


    </h:form>
</f:view>