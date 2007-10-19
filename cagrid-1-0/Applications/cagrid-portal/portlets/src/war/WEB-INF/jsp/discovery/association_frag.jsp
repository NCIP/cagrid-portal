<table>
	<tbody>
		<tr>
			<td>Type:</td>
			<td><c:out value="${assoc.target.type.packageName}"/>.<c:out value="${assoc.target.type.className}"/></td>
		</tr>
		<tr>
			<td>Min Cardinality:</td>
			<td><c:out value="${assoc.target.minCardinality}"/></td>
		</tr>
		<tr>
			<td>Max Cardinality:</td>
			<td><c:out value="${assoc.target.maxCardinality}"/></td>
		</tr>
	</tbody>
</table>