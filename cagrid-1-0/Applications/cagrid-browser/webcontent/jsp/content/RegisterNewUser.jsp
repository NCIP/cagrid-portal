<%--
  Created by IntelliJ IDEA.
  User: kherm
  Date: Sep 28, 2005
  Time: 12:37:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:loadBundle basename="labels" var="labels"/>
<f:loadBundle basename="messages" var="messages"/>
<f:view locale="#{browserConfig.locale}">


<h:form id="registrationForm" onsubmit="return confirmPassword()">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td width="60%">&lt;&lt;
            <h:outputLink value="PreLogin.tiles" styleClass="formText">
                <h:outputText value="#{messages.backToHomepage}"></h:outputText>
            </h:outputLink>
        </td>
    </tr>
</table>

<br>

<table width="627" border="0" align="center" cellpadding="0" cellspacing="0" summary="">
<tr>
<td width="700">
<!-- welcome begins -->
<table width="100%" border="0" cellpadding="3" cellspacing="0" summary="">
<tr>
    <td height="18" colspan="3" class="formHeader">
        <h:outputText value="#{messages.regTitle}"></h:outputText>
    </td>
</tr>
<tr>
    <td colspan="2" class="formLabel"><span class="menutitle" align="left">

                                </span></td>
</tr>
<tr>
    <td colspan="2" bordercolor="#111111" class="formTitle"><b>
        <h:outputText value="#{messages.regSubTitle1}"></h:outputText>

    </b></td>
</tr>
<tr>
    <td class="formLabel"><div align="left">
        <h:outputText value="#{messages.regFirstName}">

        </h:outputText>
    </div>
    </td>
    <td class="formField">
        <h:inputText id="firstName" value="#{loginBean.newUserInfo.firstName}" required="true" size="40">
            <f:validateLength minimum="1"/>
        </h:inputText>
        <h:outputText value="*"></h:outputText>
        <h:message for="firstName" styleClass="loginFailed"/>
    </td>

</tr>
<tr>
    <td class="formLabelWhite"><div align="left">
        <h:outputText value="#{messages.regLastName}"></h:outputText>
    </div></td>
    <td class="formFieldWhite">
        <h:inputText id="lastName" value="#{loginBean.newUserInfo.lastName}" required="true" size="40">
            <f:validateLength minimum="1"/>
        </h:inputText>
        <h:outputText value="*"></h:outputText>
        <h:message for="lastName" styleClass="loginFailed"/>
    </td>
</tr>

<tr>
    <td class="formLabelWhite"><div align="left">
        <h:outputText value="#{messages.regOrganization}"></h:outputText>
    </div></td>
    <td class="formFieldWhite">
        <h:inputText id="organization" value="#{loginBean.newUserInfo.organization}" required="true" size="40">
            <f:validateLength minimum="1"/>
        </h:inputText>
        <h:outputText value="*"></h:outputText>
        <h:message for="organization" styleClass="loginFailed"/>
    </td>
</tr>

<tr>
    <td class="formLabel"><div align="left">
        <h:outputText value="#{messages.regEmail}"></h:outputText>
    </div></td>
    <td class="formField">
        <h:inputText id="email" value="#{loginBean.newUserInfo.email}" required="true" size="40">
            <f:validateLength minimum="1"/>
        </h:inputText>
        <h:outputText value="*"></h:outputText>
        <h:message for="email" styleClass="loginFailed"/>
    </td>
</tr>

<tr>
    <td class="formLabelWhite"><div align="left">
        <h:outputText value="#{messages.regStreet}"></h:outputText>
    </div></td>
    <td class="formFieldWhite">
        <h:inputText id="address" value="#{loginBean.newUserInfo.address}" required="true" size="40">
        	<f:validateLength minimum="1"/>
        </h:inputText>
        <h:outputText value="*"></h:outputText>
        <h:message for="address" styleClass="loginFailed"/>
    </td>
</tr>

<tr>
    <td class="formLabelWhite"><div align="left">
        <h:outputText value="#{messages.regStreet2}"></h:outputText>
    </div></td>
    <td class="formFieldWhite">
        <h:inputText id="address2" value="#{loginBean.newUserInfo.address2}" required="false" size="40"/>
    </td>
</tr>

<tr>
    <td class="formLabel"><div align="left">
        <h:outputText value="#{messages.regCity}"></h:outputText>
    </div></td>
    <td class="formField">
        <h:inputText id="city" value="#{loginBean.newUserInfo.city}" required="true" size="40">
        	<f:validateLength minimum="1"/>
        </h:inputText>
        <h:outputText value="*"></h:outputText>
        <h:message for="city" styleClass="loginFailed"/>
    </td>
</tr>

<tr>
    <td class="formLabelWhite"><div align="left">
        <h:outputText value="#{messages.regState}"></h:outputText>
    </div></td>
    <td class="formFieldWhite">
        <h:inputText id="stateCode" value="#{loginBean.newUserInfo.state}" required="false" maxlength="2" size="2"
        	converter="StateCodeConverter"/>

        <h:message for="stateCode" styleClass="loginFailed"/>
    </td>
</tr>

<tr>
    <td class="formLabel"><div align="left">
        <h:outputText value="#{messages.regZIP}"></h:outputText>
    </div></td>
    <td class="formField">
        <h:inputText id="zip" value="#{loginBean.newUserInfo.zipcode}" required="true" maxlength="5" size="5">
        	<f:validateLength minimum="1"/>
        </h:inputText>
        <h:outputText value="*"></h:outputText>
        <h:message for="zip" styleClass="loginFailed"/>
    </td>
</tr>

<tr>
    <td class="formLabelWhite"><div align="left">
        <h:outputText value="#{messages.regCountry}"></h:outputText>
    </div></td>
    <td class="formFieldWhite">
        <h:inputText id="countryCode" value="#{loginBean.newUserInfo.country}" required="true" maxlength="2" size="2"
        	converter="CountryCodeConverter">
        	<f:validateLength minimum="1"/>
        </h:inputText>
        <h:outputText value="*"></h:outputText>

        <h:message for="countryCode" styleClass="loginFailed"/>
    </td>
</tr>

<tr>
    <td class="formLabelWhite"><div align="left">
        <h:outputText value="#{messages.regPhone}"></h:outputText>
    </div></td>
    <td class="formFieldWhite">
        <h:inputText value="#{loginBean.newUserInfo.phoneNumber}" required="true" maxlength="10" size="10">
        	<f:validateLength minimum="1"/>
        </h:inputText>
        <h:outputText value="*"></h:outputText>
    </td>
</tr>

<tr>
    <td colspan="2" bordercolor="#111111" class="formTitle"><b>
        <h:outputText value="#{messages.regSubTitle2}"></h:outputText>
    </b></td>
</tr>
<tr>
    <td class="formLabel"><div align="left">
        <h:outputText value="#{messages.regUsername}"></h:outputText>
    </div></td>
    <td class="formField">
        <h:inputText id="username" value="#{loginBean.newUserInfo.userId}" required="true" maxlength="20" size="20">
            <f:validateLength minimum="4" maximum="10"/>
        </h:inputText>
        <h:outputText value="* [min 4 chars]"></h:outputText>

        <h:message for="username" styleClass="loginFailed"/>
    </td>
</tr>
<tr>
    <td class="formLabelWhite"><div align="left">
        <h:outputText value="#{messages.regPassword}"></h:outputText>
    </div></td>
    <td class="formFieldWhite">
        <h:inputSecret id="password" value="#{loginBean.newUserInfo.password}" required="true" size="20">
            <f:validateLength minimum="6" maximum="10"/>
        </h:inputSecret>
        <h:outputText value="* [between 6 and 10 chars]"></h:outputText>
        <h:message for="password" styleClass="loginFailed"/>
    </td>
</tr>

<tr>
    <td class="formLabel"><div align="left">
        <h:outputText value="#{messages.regPasswordConfirm}"></h:outputText>
    </div></td>
    <td class="formField">
        <h:inputSecret id="passwordConfirm" required="true" size="20">
            <f:validateLength minimum="6" maximum="10"/>
        </h:inputSecret>
        <h:outputText value="* [between 6 and 10 chars]"></h:outputText>
        <h:message for="passwordConfirm" styleClass="loginFailed"/>
    </td>
</tr>

<tr>
    <td colspan="2" class="formLabel"><div align="center">
        <h:commandButton action="#{loginBean.doRegister}" value="#{messages.register}"/>
    </div></td>
</tr>


</table>
<br>
<!-- welcome ends --></td>
</tr>
<tr>
    <td>
        * Fields are Required
    </td>
</tr>
<tr>
    <td>
        <h:messages/>
    </td>
</tr>
</table>

</h:form>


</f:view>

