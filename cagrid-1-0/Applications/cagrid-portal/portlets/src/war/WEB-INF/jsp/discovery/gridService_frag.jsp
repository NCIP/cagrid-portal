<c:set var="sd" value="${gridService.serviceMetadata.serviceDescription}"/>
<table>
	<thead>
		<th colspan="2">
			<b><c:out value="${sd.name}"/></b>
		</th>
	</thead>
	<tbody>
		<tr>
			<td>URL:</td>
			<td><c:out value="${gridService.url}"/></td>
		</tr>
		<tr>
			<td>Status:</td>
			<td><c:out value="${gridService.currentStatus}"/></td>
		</tr>
		<tr>
			<td>Description:</td>
			<td><c:out value="${sd.description}"/></td>
		</tr>
		<tr>
			<td>Version:</td>
			<td><c:out value="${sd.version}"/></td>
		</tr>
		<tr>
			<td>caDSR Registration Status:</td>
			<td><c:out value="${sd.caDSRRegistration.registrationStatus}"/></td>
		</tr>
		<tr>
			<td>caDSR Workflow Status:</td>
			<td><c:out value="${sd.caDSRRegistration.workflowStatus}"/></td>
		</tr>
	</tbody>
</table>