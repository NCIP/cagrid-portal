<table>
	<tbody>
		<tr>
			<td>Name:</td>
			<td><c:out value="${inputParam.name}"/></td>
		</tr>
		<tr>
			<td>QName:</td>
			<td><c:out value="${inputParam.QName}"/></td>
		</tr>
		<tr>
			<td>UML Class:</td>
			<td>
			<c:if test="${!empty inputParam.UMLClass}">
				<c:out value="${inputParam.UMLClass.packageName}"/>.<c:out value="${inputParam.UMLClass.className}"/>
			</c:if>
			</td>
		</tr>
		<tr>
			<td>Index:</td>
			<td><c:out value="${inputParam.index}"/></td>
		</tr>
		<tr>
			<td>Is Array?:</td>
			<td><c:out value="${inputParam.array}"/></td>
		</tr>
		<tr>
			<td>Dimensionality:</td>
			<td><c:out value="${inputParam.dimensionality}"/></td>
		</tr>
	</tbody>
</table>