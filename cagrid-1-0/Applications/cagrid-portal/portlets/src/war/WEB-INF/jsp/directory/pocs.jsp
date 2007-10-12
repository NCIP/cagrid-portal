<c:forEach items="${directoryBean.scroller.page}" var="poc">

	<table border="0" class="contentInnerTable">
		<thead>
			<tr>
				<th class="contentTableHeader" scope="colgroup" colspan="2">
				POC Details</th>
			</tr>
		</thead>
		<tbody>
			<tr class="dataRowLight">
				<td class="dataCellTextBold leftColumn">Name</td>
				<td class="dataCellText"><c:out value="${poc.person.firstName}"/> <c:out value="${pocs.person.lastName}"/></td>
			</tr>
			<tr class="dataRowDark">
				<td class="dataCellTextBold leftColumn">Role</td>
				<td class="dataCellText"><c:out value="${poc.role}"/></td>
			</tr>
			<tr class="dataRowLight">
				<td class="dataCellTextBold leftColumn">Affiliation</td>
				<td class="dataCellText"><c:out value="${poc.affiliation}"/></td>
			</tr>
			<tr class="dataRowDark">
				<td class="dataCellTextBold leftColumn">Email</td>
				<td class="dataCellText"><c:out value="${poc.person.emailAddress}"/></td>
			</tr>
		</tbody>
	</table>
	<br/>
	
</c:forEach>
