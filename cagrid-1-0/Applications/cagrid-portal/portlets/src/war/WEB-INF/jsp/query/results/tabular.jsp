<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ include file="/WEB-INF/jsp/query/tabs.jspf" %>
<%@ include file="/WEB-INF/jsp/include/table_styles.jspf" %>
<c:choose>
<c:when test="${!empty resultsCommand.instance.error}">

<c:set var="resizablePrefix"><portlet:namespace/>error</c:set>
<%@ include file="/WEB-INF/jsp/include/resizable_div.jspf" %>

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
		<span class="scrollerStyle2">
			Displaying <c:out value="${scroller.index + 1}"/> to <c:out value="${scroller.endIndex}"/></span> of <c:out value="${fn:length(scroller.objects)}"/> results.
		<br/>		
		<c:set var="scroller" value="${scroller}"/>
		<c:set var="scrollOperation" value="scrollQueryInstanceResults"/>
		<%@ include file="/WEB-INF/jsp/include/scroll_controls.jspf" %>
		<%@ include file="/WEB-INF/jsp/query/results/cqlResultsTable.jspf" %>
		<%@ include file="/WEB-INF/jsp/include/scroll_controls.jspf" %>
	</c:otherwise>
</c:choose>
</c:otherwise>
</c:choose>