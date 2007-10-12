
<c:set var="contextPath"><%=request.getContextPath()%></c:set>
<!-- ContextPath: <c:out value="${contextPath}"/> -->


<c:forEach items="${directoryBean.scroller.page}" var="service">

	<table border="0" class="contentInnerTable">
		<thead>
			<tr>
				<th class="contentTableHeader" scope="colgroup" colspan="2">
				Service Details</th>
			</tr>
		</thead>
		<tbody>
			<tr class="dataRowLight">
				<td class="dataCellTextBold">Name</td>
				<td class="dataCellText"><c:out value="${service.serviceMetadata.serviceDescription.name}"/></td>
			</tr>
			<tr class="dataRowDark">
				<td class="dataCellTextBold">URL</td>
				<td class="dataCellText">
					<c:choose>
						<c:when test="${service.class.simpleName == 'GridDataService' && !empty service.domainModel}">
							<a href="<portlet:actionURL><portlet:param name="action" value="selectService"/><portlet:param name="sgs_url" value="${service.url}"/></portlet:actionURL>"
								alt="Query this data service"
							>
								<c:out value="${service.url}"/>
							</a>
						</c:when>
						<c:otherwise>
							<c:out value="${service.url}"/>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr class="dataRowLight">
				<td class="dataCellTextBold">Description</td>
				<td class="dataCellText"><c:out value="${service.serviceMetadata.serviceDescription.description}"/></td>
			</tr>
			<tr class="dataRowDark">
				<td class="dataCellTextBold"></td>
				<td class="dataCellText"><a href="<portlet:actionURL><portlet:param name="action" value="selectService"/><portlet:param name="sgs_url" value="${service.url}"/></portlet:actionURL>">
					<span
					onmouseover="changeMenuStyle(this,'txtHighlightOn'),showCursor()"
					onmouseout="changeMenuStyle(this,'txtHighlight'),hideCursor()"
					class="txtHighlight">More Details</span></a></td>
			</tr>
		</tbody>
	</table>
	<br/>
	
</c:forEach>
