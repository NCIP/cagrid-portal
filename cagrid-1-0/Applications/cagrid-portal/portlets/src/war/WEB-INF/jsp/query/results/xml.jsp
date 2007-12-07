<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<%@ include file="/WEB-INF/jsp/query/tabs.jspf" %>

<c:set var="resizablePrefix"><portlet:namespace/>resultsXml</c:set>
<%@ include file="/WEB-INF/jsp/include/resizable_div.jspf" %>

<style type="text/css">
<!--
<%@ include file="/css/xmlverbatim.css" %>
-->
</style>
<c:choose>
<c:when test="${!empty resultsCommand.instance.error}">
		
		<div id="<c:out value="${resizablePrefix}"/>" style="width:100%; height:400px; overflow-y:auto; font-size:125%;">
<pre>
<c:out value="${resultsCommand.instance.error}" escapeXml="false"/>
</pre>
		</div>
		
</c:when>
<c:when test="${empty resultsCommand.instance.result}">
No results to display.
</c:when>
<c:otherwise>
<c:set var="scroller" value="${resultsCommand.tableScroller}"/>
<c:choose>
	<c:when test="${fn:length(scroller.page) == 0}">
		Results are empty.
	</c:when>
	<c:otherwise>
		
		<div id="<c:out value="${resizablePrefix}"/>" style="width:100%; height:400px; overflow-y:auto; font-size:125%;">
<c:out value="${resultsCommand.prettyXml}" escapeXml="false"/>
		</div>
		
	</c:otherwise>
</c:choose>
</c:otherwise>
</c:choose>