<td>
<select name="operation"
	onchange="<c:out value="${namespace}"/>executeHistoryOperation('<c:out value="${instance.id}"/>', this.options[this.selectedIndex].value)">
	<option value="---" selected>---</option>
	<c:if test="${instance.state == 'COMPLETE'}">
		<option value="viewResults">View Results</option>
	</c:if>
	<c:if test="${instance.state == 'ERROR'}">
		<option value="viewError">View Error</option>
	</c:if>
	<c:if test="${instance.state == 'RUNNING'}">
		<option value="cancel">Cancel</option>
	</c:if>
	<option value="delete">Delete</option>
</select></td>
<td><c:out value="${instance.state}" /></td>
<td><c:if test="${!empty instance.startTime}">
	<fmt:formatDate value="${instance.startTime}" type="both" />
</c:if></td>
<td><c:if test="${!empty instance.finishTime}">
	<fmt:formatDate value="${instance.finishTime}" type="both" />
</c:if></td>
<td><c:out value="${instance.dataService.serviceMetadata.serviceDescription.name}" /></td>

