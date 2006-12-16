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
    <h:messages/>
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
                    <f:verbatim><br/></f:verbatim>
                    <h:outputText styleClass="loginFailed" value="#{discoveryBean.discoveryFailureMessage}"/>
                </h:column>
            </f:facet>

            <h:column>
                <h:outputText value="#{messages.discoverySemanticKeywordTitle}"/>
            </h:column>

            <h:column>
                <h:inputText id="discoveryKeywords" value="#{discoveryBean.keywords}" 
                size="58"/>
            </h:column>

            <h:column>
                <h:outputText value="#{messages.discoverySemanticMetadataCategory}"/>
            </h:column>

            <h:column>
                <h:panelGrid columns="4">
                    <h:selectManyCheckbox value="#{discoveryBean.semanticMetadataCategoriesSelected}"
                                          styleClass="formText">
                        <f:selectItems value="#{discoveryBean.semanticMetadataCategories}"/>
                    </h:selectManyCheckbox>
                </h:panelGrid>
            </h:column>
            
			<h:column>
                <h:outputText value="#{messages.discoveryServiceMetadataCategory}"/>
            </h:column>

            <h:column>
                <h:panelGrid columns="4">
                    <h:selectManyCheckbox value="#{discoveryBean.serviceMetadataCategoriesSelected}"
                                          styleClass="formText">
                        <f:selectItems value="#{discoveryBean.serviceMetadataCategories}"/>
                    </h:selectManyCheckbox>
                </h:panelGrid>
            </h:column>
            
            <h:column>
                <h:outputText value="#{messages.discoveryServiceTypeCategory}"/>
            </h:column>

            <h:column>
                <h:panelGrid columns="4">
                    <h:selectManyCheckbox value="#{discoveryBean.serviceTypeSelected}"
                                          styleClass="formText">
                        <f:selectItem itemLabel="All" itemValue="any"/>
                        <f:selectItem itemLabel="Data" itemValue="data"/>
                        <f:selectItem itemLabel="Analytical" itemValue="analytical"/>
                    </h:selectManyCheckbox>
                </h:panelGrid>
            </h:column>
            

            <h:column>
                <f:verbatim>&nbsp;</f:verbatim>
            </h:column>

            <h:column>
                <h:commandButton id="discoveryBtn" value="#{labels.discoveryBtn}"
                                 action="#{discoveryBean.doDiscovery}"/>
            </h:column>


        </h:panelGrid>
        <!-- Discovery Panel Ends -->
    </h:form>
    
    <tiles:insert attribute="discoveryResults" ignore="true" flush="false"/>

</f:view>



