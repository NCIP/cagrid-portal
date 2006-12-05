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

<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td width="48%">&lt;&lt;
            <h:outputLink value="ServiceDetails.tiles" styleClass="formText">
                <h:outputText value="#{messages.backToService}"></h:outputText>
            </h:outputLink>
        </td>
    </tr>
</table>

<br>



<h:panelGrid headerClass="formHeader" 
	columns="2" 
	rowClasses="formLabel,formLabelWhite" width="100%" border="1" cellpadding="3" cellspacing="0">

<f:facet name="header">
    <h:column>
        <h:outputText value="#{messages.objectDetailsTitle}"/>
    </h:column>
</f:facet>

<h:column>
    <h:outputText value="#{messages.id}"/>
</h:column>
<h:column>
    <h:outputText value="#{discoveryResult.navigatedService.navigatedClass.umlClass.id}"/>
</h:column>


<h:column>
    <h:outputText value="#{messages.className}"/>
</h:column>
<h:column>
    <h:outputText value="#{discoveryResult.navigatedService.navigatedClass.umlClass.className}"/>
</h:column>


<h:column>
    <h:outputText value="#{messages.packageName}"/>
</h:column>
<h:column>
    <h:outputText value="#{discoveryResult.navigatedService.navigatedClass.umlClass.packageName}"/>
</h:column>


<h:column>
    <h:outputText value="#{messages.description}"/>
</h:column>
<h:column>
    <h:outputText value="#{discoveryResult.navigatedService.navigatedClass.umlClass.description}"/>
</h:column>


<h:column>
    <h:outputText value="#{messages.attributes}"/>
</h:column>
<h:column>
    <!-- attribute table -->
    <h:dataTable width="100%"  rowClasses="formLabel" border="1" 
    	cellpadding="3" cellspacing="0" headerClass="formTitle"  
    	value="#{discoveryResult.navigatedService.navigatedClass.umlClass.umlAttributeCollection.UMLAttribute}" var="attribute">
        <h:column>
            <f:facet name="header">
                <h:outputText value="#{messages.attributeName}"/>
            </f:facet>
            <h:column>
                <h:outputText value="#{attribute.name}"/>
                <f:verbatim>&nbsp;</f:verbatim>
            </h:column>
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:outputText value="#{messages.attributeType}"/>
            </f:facet>
            <h:column>
                <h:outputText value="#{attribute.dataTypeName}"/>
                <f:verbatim>&nbsp;</f:verbatim>
            </h:column>
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:outputText value="#{messages.description}"/>
            </f:facet>
            <h:column>
                <h:outputText value="#{attribute.description}"/>
                <f:verbatim>&nbsp;</f:verbatim>
            </h:column>
        </h:column>
    </h:dataTable>
</h:column>


<h:column rendered="#{not empty discoveryResult.navigatedService.navigatedClass.associationBeans}">
    <h:outputText value="#{messages.associations}"/>
</h:column>

<h:column rendered="#{not empty discoveryResult.navigatedService.navigatedClass.associationBeans}">
    <!--association table-->
    <h:dataTable width="100%" rowClasses="formLabel" border="1" cellpadding="3" cellspacing="0" 
    	headerClass="formTitle"  
    	value="#{discoveryResult.navigatedService.navigatedClass.associationBeans}" var="association">
        <h:column>
            <f:facet name="header">
                <h:outputText value="#{messages.associationRoleName}"/>
            </f:facet>
            <h:column>
                <h:outputText value="#{association.umlAssociation.targetUMLAssociationEdge.UMLAssociationEdge.roleName}"/>
            </h:column>
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:outputText value="#{messages.associationMultiplicity}"/>
            </f:facet>
            <h:column>
                <h:outputText value="#{association.umlAssociation.targetUMLAssociationEdge.UMLAssociationEdge.minCardinality}"/>
                <h:outputText value="..."/>
                <h:outputText value="#{association.umlAssociation.targetUMLAssociationEdge.UMLAssociationEdge.maxCardinality}"/>
            </h:column>
        </h:column>
        
        <h:column>
            <f:facet name="header">
                <h:outputText value="#{messages.associationType}"/>
            </f:facet>
            <h:column>
                <h:outputText value="#{association.umlClass.className}"/>
            </h:column>
        </h:column>

    </h:dataTable>
</h:column>
</h:panelGrid>

</h:form>
</f:view>