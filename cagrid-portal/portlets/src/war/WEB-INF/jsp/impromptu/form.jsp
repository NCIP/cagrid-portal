<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>


<html>
<head>
    <title>Submit An Impromptu Query</title>

    <script type="text/javascript" src="/html/js/jquery/jquery.js"></script>
    <script type="text/javascript">
        jQuery(document).ready(function() {

            jQuery('#htmlSuccessPage_check_td > :checkbox').attr('checked', true);

        });
    </script>

</head>

<body>

<!-- 
<ns1:CQLQuery xmlns:ns1="http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery"><ns1:Target name="gov.nih.nci.caarray.domain.array.Array"/><ns1:QueryModifier countOnly="true"/></ns1:CQLQuery>

http://cagrid1.duhs.duke.edu:18080/wsrf/services/cagrid/CaArraySvc
-->

<form:form method="POST" commandName="impromptuQuery">
    <table>
        <thead>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td valign="top" style="font-weight:bold">Query :</td>
            <td><label for="query"/><form:textarea id="query" path="query" cols="80" rows="17"/></td>
            <td><form:errors path="query"/></td>
        </tr>
        <tr>
            <td valign="top" style="font-weight:bold">Endpoint Url :</td>
            <td><label for="endpointUrl"/><form:input id="endpointUrl" path="endpointUrl" size="80"/></td>
            <td><form:errors path="endpointUrl"/></td>
        </tr>
        <tr>
            <td valign="top" style="font-weight:bold">Clear Previous Results :</td>
            <td><label for="clearPrevious"/><form:checkbox id="clearPrevious" path="clearPrevious"/></td>
            <td><form:errors path="clearPrevious"/></td>
        </tr>
        <tr>
            <td valign="top" style="font-weight:bold">Format Results In HTML :</td>
            <td id="htmlSuccessPage_check_td"><label for="htmlSuccessPage"/><form:checkbox id="htmlSuccessPage" path="htmlSuccessPage"/></td>
        </tr>
        <tr>
            <td colspan="2"><input alt="Submit" type="submit"></td>
        </tr>
        </tbody>
    </table>
</form:form>

</body>
</html> 