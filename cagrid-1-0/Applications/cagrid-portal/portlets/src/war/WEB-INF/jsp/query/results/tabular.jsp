<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ include file="/WEB-INF/jsp/query/tabs.jspf" %>
<%@ include file="/WEB-INF/jsp/include/table_styles.jspf" %>
<c:choose>
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