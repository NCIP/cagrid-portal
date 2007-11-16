<%@ include file="/WEB-INF/jsp/include.jsp" %>
There are currently...<br/>
<table cellpadding="3">
<tr>
	<td>
		<portlet:actionURL var="participantsAction">
			<portlet:param name="operation" value="selectDirectoryForDiscovery"/>
			<portlet:param name="selectedDirectory" value="${statusBean.participantsDirectory.id}"/>
		</portlet:actionURL>
		<a href="<c:out value="${participantsAction}"/>">
			<c:out value="${fn:length(statusBean.participantsDirectory.objects)}"/>
		</a>
	</td>
	<td>caBIG Participants,</td>
</tr>
<tr>
	<td>
		<portlet:actionURL var="servicessAction">
			<portlet:param name="operation" value="selectDirectoryForDiscovery"/>
			<portlet:param name="selectedDirectory" value="${statusBean.servicesDirectory.id}"/>
		</portlet:actionURL>
		<a href="<c:out value="${servicessAction}"/>">
			<c:out value="${fn:length(statusBean.servicesDirectory.objects)}"/>
		</a>
	</td>
	<td>grid services, which includes</td>
</tr>
<tr>
	
	<td>
		<portlet:actionURL var="dataServicesAction">
			<portlet:param name="operation" value="selectDirectoryForDiscovery"/>
			<portlet:param name="selectedDirectory" value="${statusBean.dataServicesDirectory.id}"/>
		</portlet:actionURL>
		<a href="<c:out value="${dataServicesAction}"/>">
			<c:out value="${fn:length(statusBean.dataServicesDirectory.objects)}"/>
		</a>
	</td>
	<td>data services, and</td>
</tr>
<tr>
	
	<td>
		<portlet:actionURL var="analyticalServicesAction">
			<portlet:param name="operation" value="selectDirectoryForDiscovery"/>
			<portlet:param name="selectedDirectory" value="${statusBean.analyticalServicesDirectory.id}"/>
		</portlet:actionURL>
		<a href="<c:out value="${analyticalServicesAction}"/>">
			<c:out value="${fn:length(statusBean.analyticalServicesDirectory.objects)}"/>
		</a>
	</td>
	<td>analytical services.</td>
</tr>
</table>

<br/>
Here are the five newest services...
<table cellpadding="10">
	<thead>
		<tr>
			<th><b>Name</b></th><th><b>Type</b></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="serviceInfo" items="${statusBean.latestServices}">
			<tr>
				<td style="padding: 1px 8px 1px 1px;">
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
