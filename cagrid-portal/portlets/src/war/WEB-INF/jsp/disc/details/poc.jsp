<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<%@ include file="/WEB-INF/jsp/disc/tabs.jspf" %>
<c:choose>
<c:when test="${!empty poc}">
<b>Point of Contact</b>
<div style="margin-left:10px">
<ul style="list-style-type:disc; margin-left:10px">
	<li><b>Name:</b> <c:out value="${poc.firstName}"/> <c:out value="${poc.lastName}"/></li>
	<li><b>Email:</b> <c:out value="${poc.emailAddress}"/></li>
	<li><b>Phone:</b> <c:out value="${poc.phoneNumber}"/></li>
	<li><b>Roles</b>
		<ul style="list-style-type:disc; margin-left:10px">
			<c:forEach var="role" items="${poc.pointOfContacts}">
			<li><b>Role:</b> <c:out value="${role.role}"/>
				<ul style="list-style-type:disc; margin-left:10px">
					<li><b>Affiliation:</b> <c:out value="${role.affiliation}"/></li>
					<c:set var="roleType"><c:out value="${role.class.simpleName}"/></c:set>
					<c:choose>
						<c:when test="${roleType eq 'ServicePointOfContact'}">
							<li><b>Service:</b> <c:out value="${role.serviceDescription.serviceMetadata.service.url}"/></li>
						</c:when>
						<c:otherwise>
							<li><b>Research Center:</b> <c:out value="${role.researchCenter.displayName}"/></li>
						</c:otherwise>
					</c:choose>
				</ul>
			</li>
			</c:forEach>
		</ul>
	</li>
</ul>
</div>
</c:when>
<c:otherwise>
No point of contact has been selected. Use the search tab to discover points of contact.
</c:otherwise>
</c:choose>