<%@ include file="/WEB-INF/jsp/disc/search/header.jspf" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<link type="text/css" href="<c:url value="/css/service-discovery.css"/>"/>
<style type="text/css">
    <!--
    <%@ include file="/css/service-discovery.css" %>
    -->
</style>

<script type="text/javascript" src='<c:url value="/dwr/interface/EVSAutomcompleter.js"/>'></script>
<script type="text/javascript" src='<c:url value="/js/autocomplete-min.js"/>'></script>


<portlet:actionURL var="formAction">
    <portlet:param name="operation" value="searchServices"/>
</portlet:actionURL>

<table>
    <thead>
    <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>

            <%@ include file="/WEB-INF/jsp/disc/search/discovery_advanced.jspf" %>
            <%@ include file="/WEB-INF/jsp/disc/search/discovery_simple.jspf" %>

        </td>

        <td valign="top">
            <form>
                &nbsp;
                <input id="simple" name="level" alt="Simple" value="simple" type="radio" checked
                       onclick="jQuery('#<portlet:namespace/>evsDiv').hide();jQuery('#<portlet:namespace/>advancedDiv').hide();jQuery('#<portlet:namespace/>simpleDiv').show();"/><label for="simple">Simple&nbsp;</label>
                <input id="advanced" name="level" alt="Advanced" value="advanced" type="radio"
                       onclick="jQuery('#<portlet:namespace/>simpleDiv').hide();jQuery(document).find('#<portlet:namespace/>evsDiv').hide();jQuery('#<portlet:namespace/>advancedDiv').show();"/><label for="advanced">Advanced&nbsp;</label>
            </form>
        </td>
    </tr>
    </tbody>
</table> 