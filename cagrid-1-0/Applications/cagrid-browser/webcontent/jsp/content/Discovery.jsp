<%--
Created by IntelliJ IDEA.
User: kherm
Date: Jun 22, 2005
Time: 3:44:44 PM
To change this template use File | Settings | File Templates.
--%>
<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

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

    </h:form>
</f:view>

