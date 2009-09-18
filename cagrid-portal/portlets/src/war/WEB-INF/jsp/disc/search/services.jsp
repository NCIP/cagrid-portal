<%@ include file="/WEB-INF/jsp/disc/search/header.jspf" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<link type="text/css" href="<c:url value="/css/service-discovery.css"/>"/>
<style type="text/css">
    <!--
    <%@ include file="/css/service-discovery.css" %>
    -->
</style>

<script type="text/javascript" src='<c:url value="/dwr/interface/EVSAutomcompleter.js"/>'></script>
<script type="text/javascript" src='<c:url value="/js/autocomplete.js"/>'></script>


<portlet:actionURL var="formAction">
    <portlet:param name="operation" value="searchServices"/>
</portlet:actionURL>

<table>
<tr>
<td>

    <%@ include file="/WEB-INF/jsp/disc/search/discovery_advanced.jspf" %>
    <%@ include file="/WEB-INF/jsp/disc/search/discovery_simple.jspf" %>
    <%@ include file="/WEB-INF/jsp/disc/search/discovery_evs.jspf" %>



</td>

<td valign="top">
    <form>
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input name="level" value="simple" type="radio" checked
               onclick="jQuery('#<portlet:namespace/>evsDiv').hide();jQuery('#<portlet:namespace/>advancedDiv').hide();jQuery('#<portlet:namespace/>simpleDiv').show();"/>Simple&nbsp;
        <input name="level" value="advanced" type="radio"
               onclick="jQuery('#<portlet:namespace/>simpleDiv').hide();jQuery(document).find('#<portlet:namespace/>evsDiv').hide();jQuery('#<portlet:namespace/>advancedDiv').show();"/>Advanced&nbsp;
        <input name="level" value="evs" type="radio"
               onclick="jQuery('#<portlet:namespace/>simpleDiv').hide();jQuery('#<portlet:namespace/>advancedDiv').hide();jQuery('#<portlet:namespace/>evsDiv').show();"/>EVS&nbsp;
    </form>
</td>
</tr>
</table>