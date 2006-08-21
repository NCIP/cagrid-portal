<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<head>
    <link rel="stylesheet" type="text/css" href="css/styleSheet.css"/>
    <script src="js/script.js" type="text/javascript"></script>
</head>

<f:view>
<body>


<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
<tr>

    <td>
        <!-- nci hdr begins -->
        <f:subview id="nciHdr">
            <tiles:insert attribute="nciHeader" flush="false" />
        </f:subview>
        <!-- nci hdr ends -->
    </td>
</tr>

<tr>
    <td height="100%" align="center" valign="top">

        <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%" width="771">

            <tr>
                <td height="50">
                    <!-- application hdr begins -->
                    <f:subview id="portalHdr">
                        <tiles:insert attribute="portalHeader" flush="false" />
                    </f:subview>
                </td>
            </tr>

            <!-- application hdr ends -->
            <tr>
                <td valign="top">
                    <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%" width="100%">
                        <tr>
                            <td height="20" class="mainMenu">

                                <!-- main menu begins -->
                                <f:subview id="mainMenuHdr">
                                    <tiles:insert attribute="mainMenu" flush="false" />
                                </f:subview>


                                <!-- main menu ends -->

                            </td>
                        </tr>

                        <!--_____ main content begins _____-->
                        <tr>
                            <td valign="top">
                                <!-- target of anchor to skip menus --><a name="content"/>
                                <table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%" height="100%">

                                    <!-- banner begins -->
                                    <tr>
                                        <td class="bannerHome"><img src="images/portal_banner.jpg" height="140"></td>
                                    </tr>
                                    <!-- banner begins -->

                                    <tr>
                                        <td height="100%">

                                            <!-- target of anchor to skip menus --><a name="content"/>
                                            <f:subview id="mainContent">
                                                <tiles:insert attribute="mainContent" flush="false" />
                                            </f:subview>


                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <!--_____ main content ends _____-->

            <tr>
                <td height="20" class="footerMenu">

                    <!-- application ftr begins -->
                    <f:subview id="appFtr">
                        <tiles:insert attribute="appFooter" flush="false" />
                    </f:subview>

                    <!-- application ftr ends -->

                </td>
            </tr>
        </table>
    </td>
</tr>



<tr>
    <td>

        <!-- footer begins -->
        <f:subview id="nciFtr">
            <tiles:insert attribute="nciFooter" flush="false" />
        </f:subview>
        <!-- footer ends -->

    </td>
</tr>
</table>
</body>
</f:view>
</html>
