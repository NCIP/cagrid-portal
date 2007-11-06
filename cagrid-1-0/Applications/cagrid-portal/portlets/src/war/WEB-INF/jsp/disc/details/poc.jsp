<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ include file="/WEB-INF/jsp/disc/tabs.jspf" %>
<b>Point of Contact</b>
<ul>
	<li>Name: <c:out value="${poc.firstName}"/> <c:out value="${poc.lastName}"/></li>
	<li>Email: <c:out value="${poc.emailAddress}"/></li>
	<li>Phone: <c:out value="${poc.phoneNumber}"/></li>
	<li>Roles:
		<ul>
			<c:forEach var="role" items="${poc.pointOfContacts}">
			<li>Role: <c:out value="${role.role}"/>
				<ul>
					<li>Affiliation: <c:out value="${role.affiliation}"/></li>
				</ul>
			</li>
			</c:forEach>
		</ul>
	</li>
</ul>
