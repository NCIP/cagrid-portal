<%@ include file="/WEB-INF/jsp/include/includes.jspf"%>
<%@ include file="/WEB-INF/jsp/include/table_styles.jspf" %>
<portlet:renderURL var="backUrl">
	<portlet:param name="operation" value="editChannel"/>
	<portlet:param name="channelId" value="${itemsListBean.channel.id}"/>
</portlet:renderURL>
<a href="<c:out value="${backUrl}"/>" style="text-decoration:none;">&lt;&lt; Back to channel</a><br/>
<br/>
<portlet:renderURL var="addItemUrl">
	<portlet:param name="operation" value="editItem"/>
	<portlet:param name="channelId" value="${itemsListBean.channel.id}"/>
</portlet:renderURL>
<a href="<c:out value="${addItemUrl}"/>">Add item.</a><br/>
<c:choose>
	<c:when test="${empty itemsListBean.scroller.objects}">
		No news items to display.
	</c:when>
	<c:otherwise>
		<b>Items for Channel:</b> <c:out value="${itemsListBean.channel.title}"/><br/>
		Displaying <c:out value="${itemsListBean.scroller.index + 1}"/> to <c:out value="${itemsListBean.scroller.endIndex}"/> of <c:out value="${fn:length(itemsListBean.scroller.objects)}"/> items.<br/>
		<c:set var="scroller" value="${itemsListBean.scroller}"/>
		<c:set var="scrollOperation" value="scrollItems"/>
		<%@ include file="/WEB-INF/jsp/include/scroll_controls.jspf" %>
		<br/>
		<table border="0" class="contentInnerTable">
		<thead>
			<tr class="contentTableHeader">
				<th>News Items</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${itemsListBean.scroller.page}" varStatus="rowStatus">
				<c:choose>
					<c:when test="${rowStatus.index % 2 == 0}">
						<c:set var="rowClass" value="dataRowDark"/>
					</c:when>
					<c:otherwise>
						<c:set var="rowClass" value="dataRowLight"/>
					</c:otherwise>
				</c:choose>
				<tr class="<c:out value="${rowClass}"/>">
					<td class="dataCellText">
						<portlet:renderURL var="editItemUrl">
							<portlet:param name="operation" value="editItem"/>
							<portlet:param name="itemId" value="${item.id}"/>
						</portlet:renderURL>
						<a href="<c:out value="${editItemUrl}"/>">
							<c:out value="${item.title}"/>
						</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
		</table>
		<br/>
		<%@ include file="/WEB-INF/jsp/include/scroll_controls.jspf" %>
		<br/>
	</c:otherwise>

</c:choose>