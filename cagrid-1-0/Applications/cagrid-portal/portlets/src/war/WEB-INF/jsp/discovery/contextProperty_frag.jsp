<table>
	<thead>
		<th>Name</th><th>Description</th>
	</thead>
	<tbody>
		<c:forEach var="contextProperty" items="${cps}">
		<tr>
			<td><c:out value="${contextProperty.name}"/></td>
			<td><c:out value="${contextProperty.description}"/></td>
		</tr>
		</c:forEach>
	</tbody>
</table>