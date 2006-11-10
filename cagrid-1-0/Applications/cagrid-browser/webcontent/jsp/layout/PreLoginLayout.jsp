<html>

<%@ page contentType="text/html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>


<head>
    <title>cagrid-browser</title>
    <link rel="stylesheet" type="text/css" href="styleSheet.css"/>

    <script src="js/script.js" type="text/javascript"></script>
</head>

<body>


<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">

    <!-- nci hdr begins -->

    <tiles:insert attribute="NihHeader" flush="false"/>


    <!-- nci hdr ends -->

    <tr>
        <td height="100%" align="center" valign="top">
            <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%" width="771">


                <tr>
                    <td valign="top">
                        <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%" width="100%">

                            <!-- application hdr begins -->

                            <tiles:insert attribute="ProjectLogoHeader" flush="false"/>

                            <!-- application hdr ends -->


                            <!--_____ main content begins _____-->
                            <tr>
                                <td valign="top" width="100%" height="100%">

                                    <tiles:insert attribute="Content" flush="false"/>

                                </td>
                            </tr>

                            <!--_____ main content ends _____-->


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
