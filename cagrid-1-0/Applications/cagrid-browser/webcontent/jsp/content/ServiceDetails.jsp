<%--
Created by IntelliJ IDEA.
User: kherm
Date: Jul 6, 2005
Time: 2:47:35 PM
To change this template use File | Settings | File Templates.
--%>


<%@ page session="true" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>


<f:loadBundle basename="labels" var="labels"/>
<f:loadBundle basename="messages" var="messages"/>

<f:view>

<h:form>
<f:verbatim><br></f:verbatim>

<h:panelGrid width="100%" border="0" cellspacing="0" cellpadding="0">
    <h:column><f:verbatim>&lt;&lt;</f:verbatim>
        <h:outputLink value="DiscoveryResults.tiles" styleClass="formText">
            <h:outputText value="#{messages.backToSearchResults}"></h:outputText>
        </h:outputLink>
    </h:column>
</h:panelGrid>

<f:verbatim><br></f:verbatim>


<h:panelGrid headerClass="formHeader" columns="1"
			rowClasses="formLabel,formLabelWhite"
             width="100%" border="0" cellpadding="0" cellspacing="0">
    <f:facet name="header">
        <h:column>
            <h:outputText value="#{messages.appServiceDetailstitle}"/>
        </h:column>
    </f:facet>

    <!--Service Details-->
    <h:column>
        <h:panelGrid headerClass="formTitle"
        				rowClasses="formLabel,formLabelWhite"
                     columns="2" columnClasses="detailsCellKey,detailsCellValue"
                     width="100%" cellpadding="3" cellspacing="0">
            <f:facet name="header">
                <h:column>
                    <h:outputText value="#{messages.appServiceDetailstitle}"/>
                </h:column>
            </f:facet>

            <h:column>
                <h:outputText value="#{messages.serviceURLtitle}"/>
            </h:column>
            <h:column>
                <h:outputText value="#{discoveryResult.navigatedService.epr.address}"/>
               	<f:verbatim>&nbsp;</f:verbatim>
            </h:column>

            <h:column>
                <h:outputText value="#{messages.appServiceName}"/>
            </h:column>
            <h:column>
                <h:outputText value="#{discoveryResult.navigatedService.serviceMetadata.serviceDescription.service.name}"/>
               	<f:verbatim>&nbsp;</f:verbatim>
            </h:column>
            
            <h:column>
                <h:outputText value="#{messages.appServiceVersion}"/>
            </h:column>
            <h:column>
                <h:outputText
                        value="#{discoveryResult.navigatedService.serviceMetadata.serviceDescription.service.version}"/>
                <f:verbatim>&nbsp;</f:verbatim>
            </h:column>

        </h:panelGrid>
    </h:column>

    <!--RC Details-->
    <h:column>
        <h:panelGrid headerClass="formTitle"
        	rowClasses="formLabel,formLabelWhite"
                     columns="2" columnClasses="detailsCellKey,detailsCellValue"
                     width="100%" cellpadding="3" cellspacing="0">
            <f:facet name="header">
                <h:column>
                    <h:outputText value="#{messages.rcTitle}"/>
                </h:column>
            </f:facet>

            <h:column>
                <h:outputText value="#{messages.rcShortName}"/>
            </h:column>
            <h:column>
                <h:outputText value="#{discoveryResult.navigatedService.serviceMetadata.hostingResearchCenter.researchCenter.shortName}"/>
                <f:verbatim>&nbsp;</f:verbatim>
            </h:column>

            <h:column>
                <h:outputText value="#{messages.rcDisplayName}"/>
            </h:column>
            <h:column>
                <h:outputText
                        value="#{discoveryResult.navigatedService.serviceMetadata.hostingResearchCenter.researchCenter.displayName}"/>
                <f:verbatim>&nbsp;</f:verbatim>
            </h:column>

            <h:column>
                <h:outputText value="#{messages.rcAddress}"/>
            </h:column>
            <h:column>
                <h:outputText
                        value="#{discoveryResult.navigatedService.serviceMetadata.hostingResearchCenter.researchCenter.address.country}"/>
                <f:verbatim>&nbsp;</f:verbatim>
            </h:column>
        </h:panelGrid>
    </h:column>

</h:panelGrid>

<f:verbatim><br/></f:verbatim>

<%--service context information--%>
<h:panelGrid headerClass="formHeader" columns="1"
             width="100%" border="0" cellpadding="0" cellspacing="0"
        >
    <f:facet name="header">
        <h:column>

            <h:outputText value="#{messages.serviceContextDetailsTitle}"/>

        </h:column>
    </f:facet>
    
    <h:dataTable var="serviceContext" value="#{discoveryResult.navigatedService.serviceMetadata.serviceDescription.service.serviceContextCollection.serviceContext}"
    	width="100%" border="0" cellpadding="0" cellspacing="0"
    >
    
    	<h:column rendered="#{not empty serviceContext.operationCollection.operation}">
    	
	    	<h:panelGrid headerClass="formTitle"

	    				columns="1" columnClasses="detailsCellKey"
	                     width="100%" cellpadding="3" cellspacing="0">
	            <f:facet name="header">
	                <h:column>
	                    <h:outputText value="#{serviceContext.name}"/>
	                </h:column>
	            </f:facet>
	            
	            <h:column rendered="#{not empty serviceContext.operationCollection.operation}">
			        <h:dataTable var="oper" value="#{serviceContext.operationCollection.operation}"
	    		rowClasses="formLabel,formLabelWhite"			        
			                     headerClass="formTitle"
			                     columnClasses="detailsCellKey,detailsCellValue"
			                     width="100%" cellpadding="3" cellspacing="0">
	
			            <h:column>
			            	<f:facet name="header">
							  	<h:outputText value="#{messages.operationTitle}"/>
							</f:facet>
			                <h:outputText value="#{oper.name}"/>
			            </h:column>
			            <h:column>
			            	<f:facet name="header">
							   	<h:outputText value="#{messages.description}"/>
							</f:facet>
			                <h:outputText value="#{oper.description}"/>
			                <f:verbatim>&nbsp;</f:verbatim>
			            </h:column>
			        </h:dataTable>
			    </h:column>
	            
	        </h:panelGrid>
        </h:column>
	    
    </h:dataTable>
    
</h:panelGrid>

<f:verbatim><br/></f:verbatim>

<h:panelGrid width="100%" border="0" cellpadding="3" cellspacing="0" columns="1">
    <h:column>

       <h:commandButton id="cartBtn" type="submit" value="#{labels.addServiceCart}"
                    	action="#{discoveryResult.navigatedService.addToServiceCart}"/>
       
       <f:verbatim>&nbsp;</f:verbatim>
       
        <h:commandButton id="addServiceCart" value="#{labels.queryGDSF}"  rendered="#{not empty discoveryResult.navigatedService.domainModel}"
                         action="#{discoveryResult.navigatedService.navigateToQuery}"/>
       
    </h:column>
</h:panelGrid>

<f:verbatim><br/></f:verbatim>

<%--cadsr information--%>
<h:panelGrid headerClass="formHeader" columns="1"
             width="100%" border="0" cellpadding="0" cellspacing="0"
        >
    <f:facet name="header">
        <h:column>
            <h:outputText value="#{messages.caDSRDetailsTitle}"/>
        </h:column>
    </f:facet>

    <!--caDSR Registration Details-->
    <h:column rendered="#{not empty discoveryResult.navigatedService.serviceMetadata.serviceDescription.service.caDSRRegistration}">
        <h:panelGrid headerClass="formTitle"
        	rowClasses="formLabel,formLabelWhite"
                     columns="2" columnClasses="detailsCellKey,detailsCellValue"
                     width="100%" cellpadding="3" cellspacing="0">
            <f:facet name="header">
                <h:column>
                    <h:outputText value="#{messages.caDSRRegistrationTitle}"/>
                </h:column>
            </f:facet>

            <h:column>
                <h:outputText value="#{messages.caDSRregistrationStatus}"/>
            </h:column>
            <h:column>
                <h:outputText value="#{discoveryResult.navigatedService.serviceMetadata.serviceDescription.service.caDSRRegistration.registrationStatus}"/>
                <f:verbatim>&nbsp;</f:verbatim>
            </h:column>

            <h:column>
                <h:outputText value="#{messages.caDSRregistrationWorkflowStatus}"/>
            </h:column>
            <h:column>
                <h:outputText
                        value="#{discoveryResult.navigatedService.serviceMetadata.serviceDescription.service.caDSRRegistration.workflowStatus}"/>
                <f:verbatim>&nbsp;</f:verbatim>
            </h:column>

        </h:panelGrid>
    </h:column>

    <!--domain model info-->
    <h:column rendered="#{not empty discoveryResult.navigatedService.domainModel}">
    
    	<h:panelGrid headerClass="formTitle"
                     columns="1" columnClasses="detailsCellKey"
                     width="100%" cellpadding="3" cellspacing="0">
            <f:facet name="header">
                <h:column>
                    <h:outputText value="#{messages.domainModelTitle}"/>
                </h:column>
            </f:facet>
            
            <h:column>
		        <h:dataTable var="umlClass"
		        	rowClasses="formLabel,formLabelWhite"
		                     value="#{discoveryResult.navigatedService.domainModel.exposedUMLClassCollection.UMLClass}"
		                     headerClass="formTitle"
		                     columnClasses="detailsCellKey,detailsCellValue"
		                     width="100%" cellpadding="3" cellspacing="0">
		            <h:column>
					    <f:facet name="header">
					    	<h:outputText value="#{messages.className}"/>
					    </f:facet>
					    <h:commandLink styleClass="formText"
                                       action="#{discoveryResult.navigatedService.navigateToUMLClassDetails}"
                                       value="#{umlClass.className}">
                        </h:commandLink>
					    
					</h:column>
					<h:column>
					    <f:facet name="header">
					    	<h:outputText value="#{messages.description}"/>
					    </f:facet>
					    <h:outputText value="#{umlClass.description}"/>
		                <f:verbatim>&nbsp;</f:verbatim>
					</h:column>

		        </h:dataTable>            
            </h:column>
            
        </h:panelGrid>
    	

    </h:column>
    



</h:panelGrid>

<f:verbatim><br/></f:verbatim>

</h:form>
</f:view>


