<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>


<f:loadBundle basename="labels" var="labels"/>
<f:loadBundle basename="messages" var="messages"/>

<f:view>
<h:form>


<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage"
       width="100%" height="100%">

<!-- banner begins -->
<tr>
    <td class="bannerHome">
        <img src="images/bannerHome.gif" height="140">
    </td>
</tr>
<!-- banner begins -->

<tr valign="top">
<td height="100%" valign="top">


<table style="height:100%;" cellpadding="0" cellspacing="0" border="0">
<tr>
    <td>

        <!-- welcome begins -->
        <table style="height:100%;vertical-align:top;" cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td class="welcomeTitle">
                    <h:outputText value="#{messages.siteWelcomeTitle}"/>
                </td>
            </tr>

            <tr>
                <td class="welcomeContent" valign="top">
                    <h:outputText value="#{messages.siteWelcomeMessage}"/>
                </td>
            </tr>
        </table>
        <!-- welcome ends -->

    </td>

    <td>

        <!-- sidebar begins -->
        <table style="height:100%;" cellpadding="0" cellspacing="0" border="0">

            <!-- login begins -->
            <tr>
                <td>
                    <table cellpadding="0" cellspacing="0" border="0" style="height:100%" width="100%"
                           class="sidebarSection">
                        <tr>
                            <td class="sidebarTitle" height="20">
                                <h:outputText value="User Logged In"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="sidebarContent">
                                <h:commandButton action="#{loginBean.doLogout}" value="Log Out"/>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>

            <!-- login ends -->

            <!-- what's new begins -->
            <tr>
                <td>
                    <table summary="" cellpadding="0" cellspacing="0" border="0" style="height:100%" width="100%"
                           class="sidebarSection">
                        <tr>
                            <td class="sidebarTitle" height="20">
                                <h:outputText value="#{messages.whatsNewLabel}"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="sidebarContent" valign="top">
                                    <h:outputText value="#{messages.whatsNewMessage}"/>
                            <ul>
                                <li><h:outputText value="#{messages.refImpl1}"/></li>
                                <li><h:outputText value="#{messages.refImpl2}"/></li>
                                <%--
                                <li><h:outputText value="#{messages.refImpl3}"/></li>
                                <li><h:outputText value="#{messages.refImpl4}"/></li>
                                <li><h:outputText value="#{messages.refImpl5}"/></li>
                                <li><h:outputText value="#{messages.refImpl6}"/></li>
                                --%>

                            </ul>
                        </tr>
                    </table>
                </td>
            </tr>
            <!-- what's new ends -->

            <!-- did you know? begins -->
            <tr>
                <td>
                    <table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%"
                           class="sidebarSection">
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


        </table>
        <!-- sidebar ends -->

    </td>
</tr>
</table>
</td>
</tr>
</table>

</h:form>
</f:view>