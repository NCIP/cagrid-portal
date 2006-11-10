<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
    <link rel="stylesheet" type="text/css" href="styleSheet.css"/>
    <script src="js/script.js" type="text/javascript"></script>
</head>

<%/*Load message bundle */%>


<body>


<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">

    <!-- nci hdr begins -->

    <tiles:insert attribute="NihHeader" flush="false"/>


    <!-- nci hdr ends -->

    <tr>
        <td height="100%" align="center" valign="top">
            <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%" width="771">

                <!-- application hdr begins -->

                <tiles:insert attribute="ProjectLogoHeader" flush="false"/>

                <!-- application hdr ends -->


                <tr>
                    <td valign="top">
                        <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%" width="100%">
                            <tr>
                                <td height="20" class="mainMenu">

                                    <!-- app menu begins -->

                                    <tiles:insert attribute="AppMenu" flush="false"/>

                                    <!-- app menu ends -->

                                </td>
                            </tr>

                            <!--_____ main content begins _____-->
                            <tr>
                                <td valign="top">

                                    <tiles:insert attribute="Content" flush="false"/>

                                </td>
                            </tr>

                            <!--_____ main content ends _____-->

                            <tr>
                                <td height="20" class="footerMenu">

                                    <!-- application ftr begins -->
                                    <tiles:insert attribute="AppFooter" flush="false"/>
                                    <!-- application ftr ends -->

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

            <!-- footer begins -->
            <tiles:insert attribute="NihFooter" flush="false"/>
            <!-- footer ends -->

        </td>
    </tr>
</table>


</body>

</html>
