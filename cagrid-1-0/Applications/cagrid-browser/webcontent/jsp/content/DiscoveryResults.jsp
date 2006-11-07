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
<f:view locale="#{browserConfig.locale}">


<h:form>
    <!-- Discovery Panel begins -->
    <h:panelGrid columnClasses="formLabel,formField,formLabelWhite,formFieldWhite" columns="2" headerClass="formHeader" width="100%" border="0" cellpadding="3" cellspacing="0">
        <f:facet name="header">
            <h:column>
                <h:outputText value="#{messages.discoveryMenuTitle}"/>
            </h:column>
        </f:facet>

        <h:column>
            <h:outputText value="#{messages.discoveryKeywordTitle}"/>
        </h:column>

        <h:column>
            <h:inputText id="discoveryKeyword" value="#{keywordDiscoveryBean.keyword}" size="58"/>
        </h:column>

        <h:column>
            <h:outputText value="#{messages.discoveryMetadataCategory}"/>
            <h:outputText value="#{messages.optionalKeyword}"/>
        </h:column>

        <h:column>
            <h:panelGrid columns="4">
                <h:selectManyCheckbox value="#{keywordDiscoveryBean.metaDataCategories}" styleClass="formText">
                    <f:selectItems value="#{keywordDiscoveryBean.metaDataItems}"/>
                </h:selectManyCheckbox>
            </h:panelGrid>
        </h:column>

        <h:column>
            <h:outputText value="#{messages.discoveryServiceTypeCategory}"/>
            <h:outputText value="#{messages.optionalKeyword}"/>
        </h:column>

        <h:column>
            <h:panelGrid columns="3">
                <h:selectManyCheckbox value="#{keywordDiscoveryBean.serviceTypeCategories}"
                                      styleClass="formText">
                    <f:selectItems value="#{keywordDiscoveryBean.serviceTypeItems}"/>
                </h:selectManyCheckbox>
            </h:panelGrid>
        </h:column>

        <h:column>
            <h:outputText style="color:#FFFFFF;" value="Invisible"></h:outputText>
        </h:column>

        <h:column>
            <h:commandButton id="discoveryBtn" value="#{labels.discoveryBtn}"
                             action="#{keywordDiscoveryBean.doDiscovery}"/>

            <h:commandButton id="discoveryAllBtn" value="#{labels.discoveryAllBtn}"
                             action="#{keywordDiscoveryBean.doDiscoveryAll}"/>
        </h:column>

    </h:panelGrid>
    <!-- Discovery Panel Ends -->

<br>

<h:panelGrid width="90%" border="1" columns="1" headerClass="formHeader">
    <f:facet name="header">
        <h:outputText value="#{messages.resultTitle}"/>
    </f:facet>


    <h:dataTable width="100%" cellpadding="3" cellspacing="3" border="0"
                 value="#{keywordDiscoveryBean.services.servicesList}" var="gsh" rowClasses="formLabel,formLabelWhite">


        <h:column>

            <h:panelGrid columns="1" styleClass="formField">
                <h:commandLink action="#{gsh.doSetNavigatedService}" value="#{gsh.URL}">
                </h:commandLink>

                <h:panelGroup styleClass="formField">
                    <h:outputText value="#{messages.rcInfoName}"/>
                    <h:outputText value="#{gsh.rcInfo.researchCenterName}"/>
                </h:panelGroup>

                <h:panelGroup styleClass="formField">
                    <h:outputText value="#{messages.rcInfoDescription}"/>
                    <h:outputText value="#{gsh.rcInfo.researchCenterDescription}"/>
                </h:panelGroup>
            </h:panelGrid>

        </h:column>

        <h:column>
            <h:commandButton id="cartBtn" type="submit" value="Add to Service Cart" action="#{gsh.doAddToServiceCart}"/>
        </h:column>

    </h:dataTable>

</h:panelGrid>


</h:form>

</f:view>
      