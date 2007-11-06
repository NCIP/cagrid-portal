<%@ include file="/WEB-INF/jsp/include.jsp" %>
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
		<div style="width:500px;">
		<div id="<c:out value="${resizablePrefix}"/>" style="width:100%; height:200px; overflow:scroll">
<pre>
<c:out value="${resultsCommand.instance.error}" escapeXml="false"/>
</pre>
		</div>
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
		<div style="width:500px;">
		<div id="<c:out value="${resizablePrefix}"/>" style="width:100%; height:200px; overflow:scroll">
<c:out value="${resultsCommand.prettyXml}" escapeXml="false"/>
		</div>
		</div>
	</c:otherwise>
</c:choose>
</c:otherwise>
</c:choose>