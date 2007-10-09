<table border="0" class="contentInnerTable">
	<thead>
		<tr>
			<c:forEach var="headerName" items="${scroller.table.headers}">
				<th class="contentTableHeader">
					<c:out value="${headerName}"/>
				</th>
			</c:forEach>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="row" items="${scroller.page}" varStatus="rowStatus">
			<c:choose>
				<c:when test="${rowStatus.index % 2 == 0}">
					<c:set var="rowClass" value="dataRowDark"/>
				</c:when>
				<c:otherwise>
					<c:set var="rowClass" value="dataRowLight"/>
				</c:otherwise>
			</c:choose>
			<tr class="<c:out value="${rowClass}"/>">
				<c:forEach var="headerName" items="${scroller.table.headers}" varStatus="colStatus">
					<td class="dataCellText">
						<c:out value="${row[headerName]}"/>
					</td>
				</c:forEach>
			</tr>
		</c:forEach>
	</tbody>
</table>