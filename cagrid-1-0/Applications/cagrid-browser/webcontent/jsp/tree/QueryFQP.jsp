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
<f:view locale="#{browserConfig.locale}">


<h:form>

<div id="main">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td width="48%">&lt;&lt;
            <h:outputLink value="DomainObjectDetails.tiles" styleClass="formText">
                <h:outputText value="Back To Domain Object Details"></h:outputText>
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
                                  <h:outputText value="Federated Query Plan"/>
                                  </span></td>
</tr>
<tr>
    <td colspan="2" class="formTitle"><span class="menutd"><b>
        <h:outputText value="#{messages.gdsfURL}"/>
    </b></span></td>
</tr>
<tr>
    <td colspan="2" class="formLabel"><div align="left">

        <table border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td class="formText">
                    <h:outputLink value="GDSF.tiles">
                        <h:outputText value="#{discoveredServices.navigatedService.URL}"/>
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
    <td colspan="2" class="formLabel"><div align="left">
        <br>
        <h:inputTextarea value="#{queryWizard.fqpd}" id="fqpd" cols="90"
                         rows="20"></h:inputTextarea>


    </div>

    </td>
</tr>

</table>

</td>
</tr>
<tr>
    <td width="41%" class="formLabelWhite"><div align="center">

        <h:commandButton onclick="wait()" value="Submit Federated Query Plan}" type="submit"
                         action="#{queryWizard.submitFQPD}"/>
    </div></td>

</tr>
<tr>
    <td height="2" colspan="3" align="center">                                    <!-- action buttons end -->
    </td>
</tr>
</table>

</div>


<div id="wait" style="height:200px;visibility:hidden; position: absolute; top: 100; left: 0">
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