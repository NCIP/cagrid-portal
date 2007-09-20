<c:forEach items="${directoryBean.scroller.page}" var="participant">

	<table border="0" class="contentInnerTable">
		<thead>
			<tr>
				<th class="contentTableHeader" scope="colgroup" colspan="2">
				Participant Details</th>
			</tr>
		</thead>
		<tbody>
			<tr class="dataRowLight">
				<td class="dataCellTextBold">Name</td>
				<td class="dataCellText"><c:out value="${participant.name}"/></td>
			</tr>
			<tr class="dataRowDark">
				<td class="dataCellTextBold">Institution</td>
				<td class="dataCellText"><c:out value="${participant.institution}"/></td>
			</tr>
			<tr class="dataRowDark">
				<td class="dataCellTextBold"></td>
				<td class="dataCellText"><a href="<portlet:actionURL><portlet:param name="action" value="selectParticipant"/></portlet:actionURL>">
					<span
					onmouseover="changeMenuStyle(this,'txtHighlightOn'),showCursor()"
					onmouseout="changeMenuStyle(this,'txtHighlight'),hideCursor()"
					class="txtHighlight">More Details</span></a></td>
			</tr>
		</tbody>
	</table>
	<br/>
	
</c:forEach>
