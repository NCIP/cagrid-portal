<%--
  Created by IntelliJ IDEA.
  User: kherm
  Date: Mar 14, 2006
  Time: 5:47:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:loadBundle basename="labels" var="labels"/>
<f:loadBundle basename="messages" var="messages"/>
<f:view locale="#{browserConfig.locale}">

<div id="main">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td width="48%">&lt;&lt;
            <h:outputLink value="DomainObjectDetails.tiles" styleClass="formText">
                <h:outputText value="#{messages.backToDomainObjectDetails}"></h:outputText>
            </h:outputLink>
        </td>
    </tr>
</table>

<br>
<h:form id="foo">
<h:panelGrid  width="90%" border="1" cellpadding="3" headerClass="formHeader" columnClasses="formLabel" cellspacing="0">

    <f:facet name="header">
        <h:column>
            <h:outputText value="#{messages.queryWizardTitle}"></h:outputText>
        </h:column>
    </f:facet>

    <h:column>
        <h:panelGrid id="queryWizardPanel" columns="3" width="100%">
            <h:column>

                    <t:tree2 id="clientTree" value="#{queryWizard.fqpTree}" var="node" varNodeToggler="t" clientSideToggle="true" preserveToggle="false">
                        <f:facet name="attributeNode">
                            <h:panelGroup>
                                <f:facet name="expand">
                                    <t:graphicImage value="images/tree/folder.png" rendered="#{t.nodeExpanded}" border="0"/>
                                </f:facet>
                                <f:facet name="collapse">
                                    <t:graphicImage value="images/tree/folder.png" rendered="#{!t.nodeExpanded}" border="0"/>
                                </f:facet>
                                <h:outputText value="#{node.description}" styleClass="formText"/>
                            </h:panelGroup>
                        </f:facet>
                        <f:facet name="associationNode">
                            <h:panelGroup>
                                <f:facet name="expand">
                                    <t:graphicImage value="images/tree/yellow-folder-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                                </f:facet>
                                <f:facet name="collapse">
                                    <t:graphicImage value="images/tree/yellow-folder-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                                </f:facet>
                                <h:outputText value="#{node.description}" styleClass="formText"/>
                            </h:panelGroup>
                        </f:facet>
                        <f:facet name="foreignObject">
                            <h:panelGroup>
                                <f:facet name="expand">
                                    <t:graphicImage value="images/tree/blue-folder-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                                </f:facet>
                                <f:facet name="collapse">
                                    <t:graphicImage value="images/tree/blue-folder-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                                </f:facet>
                                <h:outputText value="#{node.description}" styleClass="formText" />
                                    <h:commandLink action="#{node.gsh.doSetNavigatedService}" value=" (#{node.gsh.productInfo.productName})" styleClass="formTextBold">
                                </h:commandLink>
                            </h:panelGroup>
                        </f:facet>
                        <f:facet name="targetNode">
                            <h:panelGroup>
                                <f:facet name="expand">
                                    <t:graphicImage value="images/tree/yellow-folder-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                                </f:facet>
                                <f:facet name="collapse">
                                    <t:graphicImage value="images/tree/yellow-folder-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                                </f:facet>
                                <h:outputText value="#{node.description}" styleClass="formText"/>
                                    <h:commandLink action="#{node.gsh.doSetNavigatedService}" value=" (#{node.gsh.productInfo.productName})" styleClass="formText">
                                </h:commandLink>
                            </h:panelGroup>
                            <f:facet name="valueNode">
                                <h:panelGroup>
                                    <t:graphicImage value="images/tree/attribute.png" border="0"/>
                                    <h:inputText title="attributeValue" value="#{node.valueField}"></h:inputText>
                                    <h:selectBooleanCheckbox id="join" value="#{node.joinCondition}" valueChangeListener="#{queryWizard.setJoinCondition}"></h:selectBooleanCheckbox>
                                </h:panelGroup>
                            </f:facet>
                        </f:facet>
                    </t:tree2>
                </h:column>

            <!--- Instructions Table --->
                   <h:column>
                       <div align="cemter">
                       <h:panelGrid width="90%" id="Instructions" border="1" columns="2" headerClass="formHeader" columnClasses="formText" cellpadding="4" cellspacing="2">

                           <f:facet name="header">
                               <h:outputText value="  Instructions  "></h:outputText>
                           </f:facet>

                           <h:column>
                               <t:graphicImage value="images/tree/folder.png" border="0" align="center"/>
                           </h:column>
                           <h:column>
                               <h:outputText value="Object Attribute"></h:outputText>
                           </h:column>

                            <h:column>
                               <t:graphicImage value="images/tree/yellow-folder-closed.png" border="0"/>
                           </h:column>
                           <h:column>
                               <h:outputText value="Object Association"></h:outputText>
                           </h:column>

                              <h:column>
                               <t:graphicImage value="images/tree/blue-folder-closed.png" border="0"/>
                           </h:column>
                           <h:column>
                               <h:outputText value="Foreign Association"></h:outputText>
                           </h:column>

                           <h:column>
                               <t:graphicImage value="images/tree/attribute.png" border="0"/>
                           </h:column>
                           <h:column>
                               <h:outputText value="Attribute Value"></h:outputText>
                           </h:column>

                           <h:column>
                               <h:selectBooleanCheckbox></h:selectBooleanCheckbox>
                           </h:column>
                           <h:column>
                               <h:outputText value="Join Condition"></h:outputText>
                           </h:column>


                       </h:panelGrid>
                       </div>
                   </h:column>

               </h:panelGrid>


           </h:column>


       </h:panelGrid>

       &nbsp;&nbsp;
       <div align="center">
           <h:panelGrid>
               <h:column>
                   <h:commandButton value="Create Query" action="#{queryWizard.createFQP}"></h:commandButton>
               </h:column>
           </h:panelGrid>
       </div>


</div>

<div id="wait" style="height:300px;visibility:hidden; position: absolute; top: 150; left: 0">
       <table width="100%" height ="300px">
         <tr>
           <td align="center" valign="bottom">
          <img src="images/loading_preparing_query.gif">
           </td>
         </tr>
       </table>
    </div>
       </h:form>
       </f:view>


