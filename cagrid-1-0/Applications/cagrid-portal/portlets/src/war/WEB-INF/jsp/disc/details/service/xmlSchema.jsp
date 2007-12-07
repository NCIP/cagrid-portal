<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<%@ include file="/WEB-INF/jsp/disc/tabs.jspf" %>
<style type="text/css">
<!--
<%@ include file="/css/xmlverbatim.css" %>
-->
</style>
<portlet:renderURL var="backUrl">
	<portlet:param name="selectedTabPath" value="/details/service/tree"/>
</portlet:renderURL>
<a href="<c:out value="${backUrl}"/>">&lt;&lt;&nbsp;Back to service details</a>
<br/>
<br/>
<c:choose>
<c:when test="${!empty xmlSchema}">
<c:set var="resizablePrefix"><portlet:namespace/>xmlSchema</c:set>
<%@ include file="/WEB-INF/jsp/include/resizable_div.jspf" %>
<div id="<c:out value="${resizablePrefix}"/>" style="width:100%; height:400px; overflow-y:auto; font-size:125%;">
<c:out value="${xmlSchema}" escapeXml="false"/>
</div>
</c:when>
<c:otherwise>
This namespace is not registered with the Global Model Exchange (GME) service.
</c:otherwise>
</c:choose>