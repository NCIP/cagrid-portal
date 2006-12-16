<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>


<f:loadBundle basename="labels" var="labels"/>
<f:loadBundle basename="messages" var="messages"/>
<f:view locale="#{browserConfig.locale}">


<!-- target of anchor to skip menus --><a name="content" />
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%" height="100%">

<!-- banner begins -->
<tr>
    <td class="bannerHome"><img src="images/bannerHome.gif" height="140"></td>
</tr>
<!-- banner begins -->

<tr>
<td height="100%">

<!-- target of anchor to skip menus --><a name="content"/>

<table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
<tr>
<td width="70%">

    <!-- welcome begins -->
    <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
        <tr><td class="welcomeTitle" height="20">
            <h:outputText value="#{messages.siteWelcomeTitle}"/>
        </td>
        </tr>
        <tr height="10">
            <td/>
        </tr>
        <tr>
            <td class="formText" valign="top">
                <h:outputText value="#{messages.siteWelcomeMessage}"/>
            </td>
        </tr>
    </table>
    <!-- welcome ends -->

</td>
<td valign="top" width="30%">

<!-- sidebar begins -->
<table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">

<!-- login begins -->
<tr>
    <td valign="top">
        <h:form id="loginForm">
            <table summary="" cellpadding="2" cellspacing="0" border="0" width="100%" class="sidebarSection">

                <tr>
                    <td class="sidebarTitle" height="20">
                        <h:outputText value="#{messages.loginTitle}"/>
                    </td>
                </tr>
                <tr>
                    <td align="center">
                        <h:outputText styleClass="loginFailed" value="#{loginBean.loginFailureMessage}"/>
                    </td>
                </tr>
                <tr>
                    <td class="sidebarContent">
                        <table cellpadding="2" cellspacing="0" border="0">
                            <tr>
                                <td class="sidebarLogin" align="right">
                                    <h:outputText value="#{messages.loginLabel}"/>
                                </td>
                                <td class="formFieldLogin">
                                    <h:inputText id="userName" value="#{loginBean.username}" required="true" size="14"
                                                 styleClass="formField"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="sidebarLogin" align="right">
                                    <h:outputText value="#{messages.passwordLabel}"/>
                                </td>
                                <td class="formFieldLogin">
                                    <h:inputSecret id="userPasswd" value="#{loginBean.password}" required="false"
                                                   size="14" styleClass="formField"/>
                                </td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                                <td>
                                    <h:commandButton styleClass="actionButton" id="doLogin" value="#{labels.loginBtn}"
                                                     action="#{loginBean.doLogin}"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </h:form>
    </td>
</tr>
<!-- login ends -->

<!-- what's new begins -->
<tr>
    <td valign="top">
        <table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" class="sidebarSection">
            <tr>
                <td class="sidebarTitle" height="20">
                    <h:outputText value="#{messages.whatsNewLabel}"/>
                </td>
            </tr>
            <tr>
                <td class="sidebarContent">
                    <h:outputText value="#{messages.whatsNewMessage}"/>
                    <ul>
                        <li><h:outputText value="#{messages.refImpl1}"/></li>
                        <li><h:outputText value="#{messages.refImpl2}"/></li>
                        <%--
                        <li><h:outputText value="#{messages.refImpl3}"/></li>
                        <li><h:outputText value="#{messages.refImpl4}"/></li>
                        <li><h:outputText value="#{messages.refImpl5}"/></li>
                        --%>

                    </ul>
                </td>
            </tr>
        </table>
    </td>
</tr>
<!-- what's new ends -->

<!-- did you know? begins -->
<tr>
    <td valign="top">
        <table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%" class="sidebarSection">
            <tr>
                <td class="sidebarTitle" height="20">
                    <h:outputText value="#{messages.didYouKnowLabel}"/>
                </td>
            </tr>
            <tr>
                <td class="sidebarContent" valign="top">
                    <h:outputText value="#{messages.didYouKnowMessage}"/>
                </td>
            </tr>
        </table>
    </td>
</tr>
<!-- did you know? ends -->

<!-- spacer cell begins (keep for dynamic expanding) -->
<tr><td valign="top" height="100%">
    <table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%" class="sidebarSection">
        <tr>
            <td class="sidebarContent" valign="top">&nbsp;</td>
        </tr>
    </table>
</td></tr>
<!-- spacer cell ends -->

</table>
<!-- sidebar ends -->

</td>
</tr>
</table>
</td>
</tr>
</table>

</f:view>