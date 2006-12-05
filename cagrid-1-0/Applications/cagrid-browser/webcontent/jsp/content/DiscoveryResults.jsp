<%--
  Created by IntelliJ IDEA.
  User: kherm
  Date: Jun 23, 2005
  Time: 4:03:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:loadBundle basename="labels" var="labels"/>
<f:loadBundle basename="messages" var="messages"/>

<f:subview id="discoveryResults">

    <h:form id="serviceDetails">

        <h:panelGrid border="1" cellpadding="0" cellspacing="0"
                     columns="1" headerClass="formHeader"
                     width="100%">
            <f:facet name="header">
                <h:outputText value="#{messages.resultTitle}"/>
            </f:facet>


            <h:dataTable cellpadding="3" cellspacing="1" border="0"
                         styleClass="dataTable"
                         value="#{discoveryResult.list}" var="service"
                         rowClasses="dataRowLight,dataRowDark"
                         columnClasses="dataCellTextBold,dataCellText">

                <h:column>
                    <h:panelGrid>
                        <h:column>
                            <h:commandLink
                                    action="#{service.navigateToServiceDetails}">
                                <h:outputText value="#{service.epr.address}" styleClass="formText"/>
                            </h:commandLink>
                        </h:column>

                        <h:column>
                            <h:panelGroup>
                                <h:outputText value="#{messages.serviceName} " styleClass="formTextBold"/>
                                <h:outputText value="#{service.serviceMetadata.serviceDescription.service.name}" styleClass="formText"/>
                            </h:panelGroup>
                        </h:column>


                        <h:column>
                            <h:panelGroup styleClass="formField">
                                <h:outputText value="#{messages.serviceDescription} " styleClass="formTextBold"/>
                                <h:outputText value="#{service.serviceMetadata.serviceDescription.service.description}" styleClass="formText"/>
                            </h:panelGroup>
                        </h:column>

                    </h:panelGrid>
                </h:column>

                <h:column>
                    <h:commandButton id="cartBtn" type="submit" value="#{labels.addServiceCart}"
                    	action="#{service.addToServiceCart}"/>
                </h:column>

            </h:dataTable>

        </h:panelGrid>


    </h:form>

</f:subview>
