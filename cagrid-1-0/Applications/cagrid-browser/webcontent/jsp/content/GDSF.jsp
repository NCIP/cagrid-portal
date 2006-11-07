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

<f:view locale="#{browserConfig.locale}">

<h:form>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td width="48%">&lt;&lt;
            <h:outputLink value="DiscoveryResults.tiles" styleClass="formText">
                <h:outputText value="#{messages.backToSearchResults}"></h:outputText>
            </h:outputLink>
        </td>
    </tr>
</table>

<br>
<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" summary="">
<tr>
    <td width="700">
        <!-- welcome begins -->
        <table width="100%" border="0" cellpadding="3" cellspacing="0" summary="">
            <tr>
                <td height="18" colspan="3" class="formHeader"><span class="title">
                                  <h:outputText value="#{messages.dataServiceDetailstitle}"/>
                                  </span></td>
            </tr>
            <tr>
                <td colspan="2" class="formTitle"><b>
                    <h:outputText value="#{messages.productInfo}"/>
                </b></td>
            </tr>
            <tr>
                <td width="41%" class="formLabel"><div align="left">
                    <h:outputText value="#{messages.productName}"/>
                </div></td>
                <td width="59%" class="formField">
                    <h:outputText value="#{discoveredServices.navigatedService.productInfo.productName}"/>
                </td>
            </tr>
            <tr>
                <td class="formLabelWhite"><div align="left">
                    <h:outputText value="#{messages.productVersion}"/></div></td>
                <td class="formFieldWhite"><span class="formField">
                                  <h:outputText
                                          value="#{discoveredServices.navigatedService.productInfo.productVersion}"/> </span>
                </td>
            </tr>
            <tr>
                <td class="formLabel"><div align="left">
                    <h:outputText value="#{messages.productVendor}"/>
                </div></td>
                <td class="formField">
                    <h:outputText value="#{discoveredServices.navigatedService.productInfo.vendorName}"/>
                </td>
            </tr>
            <tr>
                <td colspan="2" class="formTitle"><b>
                    <h:outputText value="#{messages.rcTitle}"/>
                </b></td>

            </tr>
            <tr>
                <td class="formLabel"><div align="left">
                    <h:outputText value="#{messages.rcName}"/>
                </div></td>
                <td class="formField">
                    <h:outputText value="#{discoveredServices.navigatedService.rcInfo.researchCenterName}"/>
                </td>
            </tr>
            <tr>
                <td class="formLabelWhite"><div align="left">
                    <h:outputText value="#{messages.rcType}"/>
                </div></td>
                <td class="formFieldWhite">
                    <h:outputText value="#{discoveredServices.navigatedService.rcInfo.researchCenterType}"/></td>
            </tr>
            <tr>
                <td class="formLabel"><div align="left">
                    <h:outputText value="#{messages.rcAddress}"/></div></td>
                <td class="formField">
                    <h:outputText value="#{discoveredServices.navigatedService.rcInfo.researchCenterAddress}"/></td>
    </td>
</tr>
<tr>
    <td class="formLabelWhite"><div align="left">
        <h:outputText value="#{messages.rcPhone}"/>
    </div></td>
    <td class="formFieldWhite">
        <h:outputText value="#{discoveredServices.navigatedService.rcInfo.researchCenterPhone}"/></td>

</tr>
<tr>
    <td class="formLabel"><div align="left">
        <h:outputText value="#{messages.rcFax}"/>
    </div></td>
    <td class="formField">
        <h:outputText value="#{discoveredServices.navigatedService.rcInfo.researchCenterFax}"/></td>

</tr>
<tr>
    <td class="formLabelWhite"><div align="left">
        <h:outputText value="#{messages.rcPOCName}"/></div></td>
    <td class="formFieldWhite">
        <h:outputText value="#{discoveredServices.navigatedService.rcInfo.researchCenterPOCName}"/>
    </td>

</tr>
<tr>
    <td class="formLabel"><div align="left">
        <h:outputText value="#{messages.rcDescription}"/>
    </div></td>
    <td class="formField">
        <h:outputText value="#{discoveredServices.navigatedService.rcInfo.researchCenterDescription}"/>
    </td>
</tr>
<tr>
    <td class="formLabelWhite"><div align="left">
        <h:outputText value="#{messages.rcComments}"/></div></td>
    <td class="formFieldWhite"><span class="formField">
                                    <h:outputText
                                            value="#{discoveredServices.navigatedService.rcInfo.researchCenterComments}"/>
                                    </span></td>
</tr>
<tr>
    <td height="2" colspan="3" align="center">                                    <!-- action buttons end -->
    </td>
</tr>
</table>

<!-- List of activities -->

<table width="100%" border="0" cellpadding="3" cellspacing="0">
    <tr>
        <td height="18" class="formHeader"><span class="menutd">
                                    <span class="title">
                                  <h:outputText value="#{messages.serviceActivities}"/>
                                  </span></span></td>
    </tr>
    <tr>
        <td class="formBorder">

            <!-- Activity list -->
            <h:dataTable columnClasses="formText" value="#{discoveredServices.navigatedService.activityInfo}"
                         var="activity">
                <h:column>
                    <h:commandLink action="#{discoveredServices.navigatedService.doActivity}" value="#{activity.name}">
                    </h:commandLink>
                </h:column>
                <h:column>
                    <h:outputText value="#{activity.description}"/>
                </h:column>

            </h:dataTable>


        </td>
    </tr>
</table>
<br>

<!-- Service URL -->

<table width="100%" border="0" cellpadding="3" cellspacing="0">
    <tr>
        <td height="18" class="formHeader"><span class="menutd">
                                   <span class="title">
                                  <h:outputText value="#{messages.serviceURLtitle}"/>
                                  </span></span></td>
    </tr>
    <tr>
        <td class="formBorder">

            <table>
                <tr>
                    <td class="formText"><h:commandLink
                            action="#{discoveredServices.navigatedService.doSetNavigatedService}"
                            value="#{discoveredServices.navigatedService.URL}"/></td>
                </tr>

            </table>


        </td>
    </tr>
</table>
<br>
<table width="100%" border="0" cellpadding="3" cellspacing="0">
    <tr>

        <td width="60%" class="formField"><div align="left">
            <h:commandButton id="addServiceCart" value="#{labels.addServiceCart}"
                             action="#{discoveredServices.navigatedService.doAddToServiceCart}">
            </h:commandButton>

        </div></td>
    </tr>
</table>
<br>


<table width="100%" border="0" cellpadding="3" cellspacing="0">
    <tr>
        <td width="100%" height="18" class="formHeader"><span class="menutd">
                                    <span class="title">
                                   <h:outputText value="#{messages.caDSRInfo}"/>
                                   </span>
                                        </span>

        </td>
    </tr>

    <tr>
        <td width="100%" align="left" class="formBorder">
            <h:dataTable width="100%" cellpadding="0" cellspacing="0" headerClass="formTitle" columnClasses="formText"
                         value="#{discoveredServices.navigatedService.domainModel.domainObject}" var="domainObject">
                <h:column>
                    <f:facet name="header">
                        <h:outputText value="#{messages.className}"/>
                    </f:facet>


                    <h:panelGrid>


                        <h:commandLink styleClass="formText"
                                       action="#{discoveredServices.navigatedService.doSetNavigatedObject}"
                                       value="#{domainObject.fullName.className}">
                        </h:commandLink>


                    </h:panelGrid>
                </h:column>


                <h:column>
                    <f:facet name="header">
                        <h:outputText value="#{messages.description}"/>
                    </f:facet>

                    <h:outputText value="#{domainObject.longName}"/>
                </h:column>
            </h:dataTable>


        </td>
    </tr>
</table>

<br>

</h:form>
</f:view>



