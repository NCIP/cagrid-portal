<%@ include file="/WEB-INF/jsp/include/includes.jspf"%>
<portlet:renderURL var="addChannelUrl">
	<portlet:param name="operation" value="editChannel"/>
</portlet:renderURL>
<a href="${addChannelUrl}">Add Channel</a><br/>
<c:choose>
	<c:when test="${empty channels}">
		No channels to display.
	</c:when>
	<c:otherwise>
		<c:forEach var="channel" items="${channels}">
			<fmt:formatDate value="${channel.pubDate}" type="both"/>
			<portlet:renderURL var="editUrl">
				<portlet:param name="operation" value="editChannel"/>
				<portlet:param name="channelId" value="${channel.id}"/>
			</portlet:renderURL>
			<a href="<c:out value="${editUrl}"/>">
				<c:out value="${channel.title}"/>
			</a>
			<br/>
		</c:forEach>
	</c:otherwise>
</c:choose>