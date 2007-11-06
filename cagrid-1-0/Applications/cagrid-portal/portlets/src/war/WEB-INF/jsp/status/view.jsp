<%@ include file="/WEB-INF/jsp/include.jsp" %>

<table>
<tr>
	<td>Total Participants:<td>
	<td>
		<portlet:actionURL var="participantsAction">
			<portlet:param name="operation" value="selectDirectoryForDiscovery"/>
			<portlet:param name="selectedDirectory" value="${statusBean.participantsDirectory.id}"/>
		</portlet:actionURL>
		<a href="<c:out value="${participantsAction}"/>">
			<c:out value="${fn:length(statusBean.participantsDirectory.objects)}"/>
		</a>
	</td>
</tr>
<tr>
	<td>Total Services:</td>
	<td>
		<portlet:actionURL var="servicessAction">
			<portlet:param name="operation" value="selectDirectoryForDiscovery"/>
			<portlet:param name="selectedDirectory" value="${statusBean.servicesDirectory.id}"/>
		</portlet:actionURL>
		<a href="<c:out value="${servicessAction}"/>">
			<c:out value="${fn:length(statusBean.servicesDirectory.objects)}"/>
		</a>
	</td>
</tr>
<tr>
	<td>Data Services:</td>
	<td>
		<portlet:actionURL var="dataServicesAction">
			<portlet:param name="operation" value="selectDirectoryForDiscovery"/>
			<portlet:param name="selectedDirectory" value="${statusBean.dataServicesDirectory.id}"/>
		</portlet:actionURL>
		<a href="<c:out value="${dataServicesAction}"/>">
			<c:out value="${fn:length(statusBean.dataServicesDirectory.objects)}"/>
		</a>
	</td>
</tr>
<tr>
	<td>Analytical Services:</td>
	<td>
		<portlet:actionURL var="analyticalServicesAction">
			<portlet:param name="operation" value="selectDirectoryForDiscovery"/>
			<portlet:param name="selectedDirectory" value="${statusBean.analyticalServicesDirectory.id}"/>
		</portlet:actionURL>
		<a href="<c:out value="${analyticalServicesAction}"/>">
			<c:out value="${fn:length(statusBean.analyticalServicesDirectory.objects)}"/>
		</a>
	</td>
</tr>
</table>
<br/>
<br/>
Latest Services:
<table>
	<thead>
		<tr>
			<th>Name</th><th>Type</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="serviceInfo" items="${statusBean.latestServices}">
			<tr>
				<td>
					<portlet:actionURL var="selectItemAction">
						<portlet:param name="operation" value="selectItemForDiscovery"/>
						<portlet:param name="selectedId" value="${serviceInfo.id}"/>
						<portlet:param name="type" value="SERVICE"/>
					</portlet:actionURL>
					<a href="<c:out value="${selectItemAction}"/>">			
						<c:out value="${serviceInfo.name}"/>
					</a>
				</td>
				<td>
					<c:out value="${serviceInfo.type}"/>
				</td>
			</tr>
		</c:forEach>	
	</tbody>
</table>
