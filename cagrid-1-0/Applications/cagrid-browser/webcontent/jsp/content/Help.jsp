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
                    <%--
                        <h:outputText
                                value="#{messages.discoveryMessage}"/>
                                --%>
                       <f:verbatim>
                       <p>
                       		caGrid services may be discovered using three approaches: Semantic Metadata, 
                          	Service Metadata, and Service Type.
                       </p> 
                       <p>
                       		The Semantic Metadata search will 
                          	first translate keywords into concept codes from the NCI Metathesaurus, using the 
                          	EVS grid service, and then search the service metadata for those concept codes. 
                          	The Service Metadata search will query the service metadata directly using the 
                          	supplied keywords. The Service Type search allows the user to select services which 
                          	fall into three broad categories: all services, data services, analytical services. 
                          	Data services are those that publish a domain model. Analytical services publish no domain model.
                       </p> 
                       <p>
                        	All selected search catagories (checkboxes) are ORed together. If keywords are provided by the 
                        	user, but no search category is selected, then a Service Metadata search will be executed, 
                        	applying the keywords to all service metadata categories. If no keywords are provided and 
                        	no categories are selected, then all services are returned. Multiple keywords may be 
                        	specified by separating them with commas.
                       </p> 
                       </f:verbatim>
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
                                      value="ftp://ftp1.nci.nih.gov/pub/cacore/caGRID/caGrid-1-0_Users_Guide.pdf">
                            <h:outputText value="#{messages.helpTechGuideTitle}"/>
                        </h:outputLink>
                    </h:column>

                    <h:column>
                        <h:outputLink id="xmlSchema" value="examples/1_gov.nih.nci.cagrid.CQLQuery.xsd">
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
                        <h:outputLink title="caGrid Homepage" value="https://cabig.nci.nih.gov/workspaces/Architecture/caGrid1-0/">
                            <h:outputText
                                    value="#{messages.helpcaGridHomepage}"/>
                        </h:outputLink>
                    </h:column>
                </h:panelGrid>
            </h:column>




        </h:panelGrid>


    </h:form>
</f:view>
