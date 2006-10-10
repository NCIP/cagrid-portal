<%@ page session="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<head>
    <link rel="stylesheet" type="text/css" href="css/styleSheet.css"/>
    <script src="js/script.js" type="text/javascript"></script>
    <title>caGrid Portal</title>
</head>

<f:view>
<h:form>
<body>


<table cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">


    <%--nci hdr begins--%>
<tr>
    <td>
        <f:subview id="nciHdr">
            <tiles:insert attribute="nciHeader" flush="false"/>
        </f:subview>
    </td>
</tr>
    <%-- nci hdr ends --%>

<tr>
<td height="100%" align="center" valign="top">

<table cellpadding="0" cellspacing="0" border="0" height="100%" width="771">

        <%-- application hdr begins --%>
    <tr>
        <td height="50">
            <f:subview id="portalHdr">
                <tiles:insert attribute="portalHeader" flush="false" ignore="true"/>
            </f:subview>
        </td>
    </tr>
        <%-- application hdr ends --%>

    <tr>
        <td valign="top">
            <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%" width="100%">
                    <%--_____ menu begins _____--%>
                <tr>
                    <td height="20" width="100%" class="mainMenu">
                            <%-- main menu begins --%>

                        <table summary="" cellpadding="0" cellspacing="0" border="0" height="60" width="100%">
                            <tr>
                                <td height="30" valign="bottom">
                                    <tiles:insert attribute="mainMenu" flush="false"/>

                                </td>
                            </tr>
                            <tr>
                                <td height="30" class="mainMenuSub">
                                        <%--Sub menu begins--%>
                                    <tiles:insert attribute="subMenu" flush="false"/>
                                        <%--Sub menu ends--%>
                                </td>
                            </tr>
                        </table>

                            <%-- main menu ends --%>

                    </td>
                </tr>
                    <%--_____ menu ends _____--%>

                    <%--_____ main content begins _____--%>
                <tr>
                    <td valign="top">
                            <%-- target of anchor to skip menus --%><a name="content"/>
                        <table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage"
                               width="100%" height="100%">

                                <%-- banner begins --%>
                            <tr>
                                <td class="bannerHome">

                                    <tiles:insert attribute="portalBanner" flush="false" ignore="true"/>


                                </td>
                            </tr>

                                <%-- banner ends --%>

                            <tr valign="top">
                                <td height="100%" valign="top">

                                        <%-- target of anchor to skip menus --%><a name="content"/>
                                    <f:subview id="mainContent">
                                        <tiles:insert attribute="mainContent" flush="false"/>
                                    </f:subview>

                                    <f:verbatim><br/></f:verbatim>
                                    <!--Optional sub section-->
                                    <f:subview id="subMainContent">
                                        <tiles:insert attribute="subMainContent" flush="false" ignore="true"/>
                                    </f:subview>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                    <%--_____ main content ends _____--%>

                <tr>
                    <td height="20" class="footerMenu">

                            <%-- application ftr begins --%>
                        <f:subview id="appFtr">
                            <tiles:insert attribute="appFooter" flush="false"/>
                        </f:subview>

                            <%-- application ftr ends --%>

                    </td>
                </tr>
            </table>
        </td>
    </tr>

</table>
</td>
</tr>

<tr>
    <td>

            <%-- footer begins --%>
        <f:subview id="nciFtr">
            <tiles:insert attribute="nciFooter" flush="false"/>
        </f:subview>
            <%-- footer ends --%>

    </td>
</tr>
</table>
</body>
</h:form>
</f:view>
</html>
