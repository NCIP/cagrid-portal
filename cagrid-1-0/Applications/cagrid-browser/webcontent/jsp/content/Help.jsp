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


        <br>



        <h:panelGrid width="100%" border="0" cellpadding="3" cellspacing="0" headerClass="formHeader" columnClasses="formLabel">
            <f:facet name="header">
                <h:outputText value="#{messages.helpTitle}"/>
            </f:facet>

            <h:column>
                <h:panelGrid  width="100%"  headerClass="formTitle" rowClasses="formText">
                    <f:facet name="header">
                        <h:outputText value="#{messages.helpDiscoveryTitle}"/>
                    </f:facet>

                    <h:column>
                        <h:outputText
                                value="#{messages.discoveryMessage}"/>
                    </h:column>
                </h:panelGrid>
            </h:column>


            <h:column>
                <h:panelGrid width="100%"  headerClass="formTitle" rowClasses="formText">
                    <f:facet name="header">
                        <h:outputText value="#{messages.helpCartTitle}"/>
                    </f:facet>

                    <h:column>
                        <h:outputText
                                value="#{messages.helpCartMessage}"/>
                    </h:column>
                </h:panelGrid>
            </h:column>

            <h:column>
                <h:panelGrid width="100%"  headerClass="formTitle" rowClasses="formText">
                    <f:facet name="header">
                        <h:outputText value="#{messages.helpQueryTitle}"/>
                    </f:facet>

                    <h:column>
                        <h:outputText
                                value="#{messages.helpQueryMessage}"/>
                    </h:column>


                    <h:column>
                        <h:outputLink id="queryDoc0"
                                      value="ftp://ftp1.nci.nih.gov/pub/cacore/caGRID/caGrid-0-5_Technical_Guide.pdf">
                            <h:outputText value="#{messages.helpTechGuideTitle}"/>
                        </h:outputLink>
                    </h:column>

                    <h:column>
                        <h:outputLink id="xmlSchema" value="examples/caBIGXMLQuery.xsd">
                            <h:outputText value="#{messages.helpQuerySchemaMessage}"/>
                        </h:outputLink>
                    </h:column>

                </h:panelGrid>
            </h:column>


            <h:column>
                <h:panelGrid width="100%" headerClass="formTitle" rowClasses="formText">
                    <f:facet name="header">
                        <h:outputText   value="#{messages.helpMoreInfoTitle}"/>
                    </f:facet>

                    <h:column>
                        <h:outputText
                                value="#{messages.helpMoreInfoMessage}"/>
                    </h:column>

                    <h:column>
                        <h:outputLink title="caGrid Homepage" value="https://cabig.nci.nih.gov/workspaces/Architecture/caGrid/">
                            <h:outputText
                                    value="#{messages.helpcaGridHomepage}"/>
                        </h:outputLink>
                    </h:column>
                </h:panelGrid>
            </h:column>




        </h:panelGrid>


    </h:form>
</f:view>
