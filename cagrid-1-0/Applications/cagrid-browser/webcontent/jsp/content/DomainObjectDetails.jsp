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

 <div id="main">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td width="48%">&lt;&lt;
            <h:outputLink value="GDSF.tiles" styleClass="formText">
                <h:outputText value="#{messages.backToService}"></h:outputText>
            </h:outputLink>
        </td>
    </tr>
</table>

<br>



<h:panelGrid headerClass="formHeader" columns="2" rowClasses="formLabel,formLabelWhite" width="100%" border="1" cellpadding="3" cellspacing="0">

<f:facet name="header">
    <h:column>
        <h:outputText value="#{messages.objectDetailsTitle}"/>
    </h:column>
</f:facet>

<h:column>
    <h:outputText value="#{messages.id}"/>
</h:column>
<h:column>
    <h:outputText value="#{discoveredServices.navigatedService.navigatedObject.id}"/>
</h:column>



<h:column>
    <h:outputText value="#{messages.className}"/>
</h:column>
<h:column>
    <h:outputText value="#{discoveredServices.navigatedService.navigatedObject.fullName.className}"/>
</h:column>

<h:column>
    <h:outputText value="#{messages.packageName}"/>
</h:column>
<h:column>
    <h:outputText value="#{discoveredServices.navigatedService.navigatedObject.fullName.packageName}"/>
</h:column>


<h:column>
    <h:outputText value="#{messages.version}"/>
</h:column>
<h:column>
    <h:outputText value="#{discoveredServices.navigatedService.navigatedObject.version}"/>
</h:column>


<h:column>
    <h:outputText value="#{messages.shortName}"/>
</h:column>
<h:column>
    <h:outputText value="#{discoveredServices.navigatedService.navigatedObject.shortName}"/>
</h:column>


<h:column>
    <h:outputText value="#{messages.longName}"/>
</h:column>
<h:column>
    <h:outputText value="#{discoveredServices.navigatedService.navigatedObject.longName}"/>
</h:column>


<h:column>
    <h:outputText value="#{messages.attributes}"/>
</h:column>
<h:column>
    <!-- attribute table -->
    <h:dataTable width="100%"  rowClasses="formLabel" border="1" cellpadding="3" cellspacing="0" headerClass="formTitle"  value="#{discoveredServices.navigatedService.navigatedObject.attributesList.attribute}" var="attribute">
        <h:column>
            <f:facet name="header">
                <h:outputText value="#{messages.attributeName}"/>
            </f:facet>
            <h:column>
                <h:outputText value="#{attribute.name}"/>
            </h:column>
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:outputText value="#{messages.attributeLongName}"/>
            </f:facet>
            <h:column>
                <h:outputText value="#{attribute.longName}"/>
            </h:column>
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:outputText value="#{messages.attributeVersion}"/>
            </f:facet>
            <h:column>
                <h:outputText value="#{attribute.version}"/>
            </h:column>
        </h:column>
    </h:dataTable>
</h:column>


<h:column>
    <h:outputText value="#{messages.description}"/>
</h:column>
<h:column>
    <h:outputText value="#{discoveredServices.navigatedService.navigatedObject.description}"/>
</h:column>

<h:column>
    <h:outputText value="#{messages.associations}"/>
</h:column>

<h:column>
    <!--association table-->
    <h:dataTable width="100%" rowClasses="formLabel" border="1" cellpadding="3" cellspacing="0" headerClass="formTitle"  value="#{discoveredServices.navigatedService.navigatedObject.associationsList.associationElement}" var="association">
        <h:column>
            <f:facet name="header">
                <h:outputText value="#{messages.associationRoleName}"/>
            </f:facet>
            <h:column>
                <h:outputText value="#{association.target.roleName}"/>
            </h:column>
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:outputText value="#{messages.associationMultiplicity}"/>
            </f:facet>
            <h:column>
                <h:outputText value="#{association.source.lowMultiplicity}"/>
                <h:outputText value="..."/>
                <h:outputText value="#{association.source.highMultiplicity}"/>
            </h:column>
        </h:column>

    </h:dataTable>
</h:column>


<h:column>
    <h:outputText value="#{messages.evsConcepts}"/>
</h:column>
<h:column>
    <!--concept code table-->
    <h:dataTable width="100%" rowClasses="formLabelWhite" border="1" cellpadding="3" cellspacing="0" headerClass="formTitle"  value="#{discoveredServices.navigatedService.navigatedObject.conceptCodesList.conceptElement}" var="concept">
        <h:column>
            <f:facet name="header">
                <h:outputText value="#{messages.conceptName}"/>
            </f:facet>
            <h:column>
                <h:outputText value="#{concept.conceptCode}"/>
            </h:column>
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:outputText value="#{messages.conceptPreferredName}"/>
            </f:facet>
            <h:column>
                <h:outputText value="#{concept.conceptPreferredName}"/>
            </h:column>

        </h:column>

        <h:column>
            <f:facet name="header">
                <h:outputText value="#{messages.conceptDefinition}"/>
            </f:facet>
            <h:column>
                <h:outputText value="#{concept.conceptDefinition}"/>
            </h:column>

        </h:column>

        <h:column>
            <f:facet name="header">
                <h:outputText value="#{messages.conceptOrder}"/>
            </f:facet>
            <h:column>
                <h:outputText value="#{concept.order}"/>
            </h:column>

        </h:column>


    </h:dataTable>
</h:column>



</h:panelGrid>

&nbsp;&nbsp;

<div align="center">
    <h:panelGrid columns="1">
        <h:column>
            <h:commandButton onclick="wait()"  value="Query Object" action="#{queryWizard.createCQLQueryWizard}"></h:commandButton>
        </h:column>


    </h:panelGrid>
</div>



</div>

<div id="wait" style="height:300px;visibility:hidden; position: absolute; top: 300; left: 0">
       <table width="100%" height ="300px">
         <tr>
           <td align="center" valign="bottom">
          <img src="images/loading_query_wizard.gif">
           </td>
         </tr>
       </table>
    </div>
</h:form>
</f:view>