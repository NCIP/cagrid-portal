<%--
Created by IntelliJ IDEA.
User: kherm
Date: Jul 16, 2005
Time: 10:44:02 PM
To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:loadBundle basename="labels" var="labels"/>
<f:loadBundle basename="messages" var="messages"/>

<f:view>
<h:form>

<div id="main">
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
<table width="550" border="0" align="center" cellpadding="0" cellspacing="0" summary="">
<tr>
<td width="700">
<!-- welcome begins -->
<table width="100%" border="0" cellpadding="3" cellspacing="0" summary="">
<tr>
    <td height="18" colspan="3" class="formHeader"><span class="title">
                                  <h:outputText value="#{messages.queryGridService}"/>
                                  </span></td>
</tr>
<tr>
    <td colspan="2" class="formTitle"><span class="menutd"><b>
        <h:outputText value="#{messages.gdsfURL}"/>
    </b></span></td>
</tr>
<tr>
    <td colspan="2" class="formLabel">
    
    <div align="left">

        <table border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td class="formText">
                    <h:outputLink value="ServiceDetails.tiles">
                        <h:outputText value="#{discoveryResult.navigatedService.epr.address}"/>
                    </h:outputLink>
                </td>


            </tr>

        </table>

    </div>

    </td>
</tr>

<tr>
    <td colspan="2" class="formTitle"><span class="menutitle">
                                    <h:outputText value="#{messages.sendQuery}"/></span></td>
</tr>

<tr>
    <td colspan="2" class="formTitle">
         <h:outputText styleClass="loginFailed" value="#{discoveryResult.navigatedService.queryFailureMessage}"/>
    </td>
</tr>

<tr>
    <td colspan="2" class="formLabel">
    
    <div align="left">
        <br>
        <h:inputTextarea value="#{discoveryResult.navigatedService.query}" id="queryXML" cols="90"
                         rows="20"></h:inputTextarea>

    </div>

    </td>
</tr>
<br>

<tr>
    <td colspan="2" class="formLabel">
        <div align="left">
            <h:dataTable headerClass="formTitle" columnClasses="formText" value="#{discoveryResult.navigatedService.sampleQueries}"
            	rendered="#{not empty discoveryResult.navigatedService.sampleQueries}"
                         var="sampleQuery">

                <f:facet name="header">
                    <h:column>
                        <h:outputText value="#{messages.sampleQueries}"/>
                    </h:column>
                </f:facet>

                <h:column>
                    <h:commandLink value="#{sampleQuery.name}" action="#{discoveryResult.navigatedService.fillInSampleQuery}">
                    </h:commandLink>
                </h:column>
                
            </h:dataTable>
        </div>


    </td>
</tr>
<br>

</table>

</td>
</tr>
<tr>
    <td width="41%" class="formLabelWhite"><div align="center">

        <h:commandButton onclick="wait()" value="#{labels.queryGDSF}" type="submit"
                         action="#{discoveryResult.navigatedService.executeQuery}"/>
    </div></td>

</tr>
<tr>
    <td height="2" colspan="3" align="center">
    </td>
</tr>
</table>

</div>


<div id="wait" style="height:300px;visibility:hidden; position: absolute; top: 100; left: 0">
       <table width="100%" height ="300px">
         <tr>
           <td align="center" valign="bottom">
          <img src="images/loading_processing_query.gif">
           </td>
         </tr>
       </table>
    </div>
</h:form>
</f:view>