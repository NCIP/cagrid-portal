<%@ include file="/WEB-INF/jsp/include.jsp"%>
<a href="<c:out value="${rssUrl}"/>">Subscribe</a><br/>
<br/>
<hr/>
<c:choose>
	<c:when test="${empty items}">
		No news items to display.
	</c:when>
	<c:otherwise>
	<c:forEach var="item" items="${items}">
			<portlet:actionURL var="selectItemAction">
				<portlet:param name="operation" value="selectItemForNews"/>
				<portlet:param name="itemId" value="${item.id}"/>
			</portlet:actionURL>
			<h4><a href="<c:out value="${selectItemAction}"/>"><c:out value="${item.title}"/></a></h4> (<fmt:formatDate value="${item.pubDate}" type="both"/>)
			<br/>
			<br/>
			<c:out value="${item.description}"/><br/>
			<hr/>
		</c:forEach>
	</c:otherwise>
</c:choose>