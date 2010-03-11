<%@ include file="/WEB-INF/jsp/include/includes.jspf"%>
<c:choose>
	<c:when test="${empty selectedItem}">
		No news items to display.
	</c:when>
	<c:otherwise>
		<span style="font-size:200%"><c:out value="${selectedItem.title}"/></span><br/>
		<i>[<fmt:formatDate value="${selectedItem.pubDate}" type="both"/>]</i><br/>
		<p/>
		<c:out value="${selectedItem.description}" escapeXml="false"/>
		<p/>
		<a target="_blank" href="<c:out value="${selectedItem.link}"/>"/>View in new window.</a><br/> 
		<iframe 
			src="<c:out value="${selectedItem.link}"/>" 
			width="<c:out value="${selectedItem.width}"/>" 
			height="<c:out value="${selectedItem.height}"/>">
			Click 
			<a target="_blank" href="<c:out value="${selectedItem.link}"/>"/>here</a> 
			to view content.
		</iframe>
		<br/>
		<hr/>
		<span style="font-size:150%">Other News:</span>
		<c:forEach var="item" items="${items}">
			<c:if test="${item.id ne selectedItem.id}">
			<portlet:actionURL var="selectItemAction">
				<portlet:param name="operation" value="selectItem"/>
				<portlet:param name="itemId" value="${item.id}"/>
			</portlet:actionURL>
			<h4><a href="<c:out value="${selectItemAction}"/>"><c:out value="${item.title}"/></a></h4><br/>
			<fmt:formatDate value="${item.pubDate}" type="both"/><br/>
			<c:out value="${item.description}"/><br/>
			<hr/>
			</c:if>
		</c:forEach>
	</c:otherwise>
</c:choose>