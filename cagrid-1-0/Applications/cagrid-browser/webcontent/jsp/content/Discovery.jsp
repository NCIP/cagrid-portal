<%--
Created by IntelliJ IDEA.
User: kherm
Date: Jun 22, 2005
Time: 3:44:44 PM
To change this template use File | Settings | File Templates.
--%>
<%@ page session="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>

<f:loadBundle basename="labels" var="labels"/>
<f:loadBundle basename="messages" var="messages"/>


<f:view>
    <f:verbatim><br/></f:verbatim>
    <h:form>
        <!-- Discovery Panel begins -->
        <h:panelGrid columns="2"
                     columnClasses="dataCellTextBold,dataCellText"
                     rowClasses="dataRowLight,dataRowDark"
                     styleClass="dataTable" headerClass="dataTableHeader"
                     border="0" cellpadding="3" cellspacing="0">
            <f:facet name="header">
                <h:column>
                    <h:outputText value="#{messages.discoveryMenuTitle}"/>
                </h:column>
            </f:facet>

            <h:column>
                <h:outputText value="#{messages.discoveryKeywordTitle}"/>
            </h:column>

            <h:column>
                <h:inputText id="discoveryKeyword" value="#{keywordDiscoveryBean.keyword}"
                             size="58">
                    <f:validateLength minimum="1"/>
                </h:inputText>
                <h:message for="lastName" styleClass="loginFailed"/>
            </h:column>

            <h:column>
                <h:outputText value="#{messages.discoveryMetadataCategory}"/>
                <h:outputText value="#{messages.optionalKeyword}"/>
            </h:column>

            <h:column>
                <h:panelGrid columns="4">
                    <h:selectManyCheckbox value="#{keywordDiscoveryBean.metaDataCategories}" styleClass="formText">
                        <f:selectItems value="#{keywordDiscoveryBean.metaDataCategoryItems}"/>
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
                        <f:selectItems value="#{keywordDiscoveryBean.serviceTypeCategoryItems}"/>
                    </h:selectManyCheckbox>
                </h:panelGrid>
            </h:column>

            <h:column>
                <f:verbatim>&nbsp;</f:verbatim>
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


    <tiles:insert attribute="discoveryResults" ignore="true" flush="false"/>


</f:view>



