<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:set var="resizablePrefix"><portlet:namespace/>summary</c:set>
<%@ include file="/WEB-INF/jsp/include/resizable_div.jspf" %>
<a alt="Subscribe to caGrid RSS News Feed" href="<c:out value="${rssUrl}"/>" style="text-decoration:none">
	<img alt="Subscribe to caGrid RSS News Feed" src="<c:url value="/images/rss.png"/>"/>
	&nbsp;Subscribe
</a>

<hr/>
<c:choose>
	<c:when test="${empty items}">
		No news items to display.
	</c:when>
	<c:otherwise>
	
	<div id="<c:out value="${resizablePrefix}"/>" style="width:100%; height:100px; overflow-y:auto">
	<c:forEach var="item" items="${items}">
			<portlet:actionURL var="selectItemAction">
				<portlet:param name="operation" value="selectItemForNews"/>
				<portlet:param name="itemId" value="${item.id}"/>
			</portlet:actionURL>
			<b><a href="<c:out value="${selectItemAction}"/>"><c:out value="${item.title}"/></a></b>
			<br/>
			<i>[<fmt:formatDate value="${item.pubDate}" type="both"/>]</i>
			<br/>
			<c:out value="${item.description}"/><br/>
			<hr/>
		</c:forEach>
	</div>
	
	</c:otherwise>
</c:choose>