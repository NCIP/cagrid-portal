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
				<td class="dataCellText"><c:out value="${service.url}"/></td>
			</tr>
			<tr class="dataRowLight">
				<td class="dataCellTextBold">Description</td>
				<td class="dataCellText"><c:out value="${service.serviceMetadata.serviceDescription.description}"/></td>
			</tr>
			<tr class="dataRowDark">
				<td class="dataCellTextBold"></td>
				<td class="dataCellText"><a href="<portlet:actionURL><portlet:param name="action" value="selectService"/></portlet:actionURL>">
					<span
					onmouseover="changeMenuStyle(this,'txtHighlightOn'),showCursor()"
					onmouseout="changeMenuStyle(this,'txtHighlight'),hideCursor()"
					class="txtHighlight">More Details</span></a></td>
			</tr>
		</tbody>
	</table>
	<br/>
	
</c:forEach>
