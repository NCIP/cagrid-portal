<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:choose>
	<c:when test="${empty portalUser}">
		<portlet:actionURL var="loginActionUrl">
			<portlet:param name="operation" value="login"/>
		</portlet:actionURL>
		<a href="<c:out value="${loginActionUrl}"/>">Login</a>&nbsp;|&nbsp;
		<a href="<c:out value="${registerUrl}"/>">Register</a><br/>
		<h2>Why Register?</h2>
		<p>
			If you register, you'll be able to interact with secure
			caGrid services.
		</p>
	</c:when>
	<c:otherwise>
		Welcome, <c:out value="${portalUser.person.firstName}"/> <c:out value="${portalUser.person.lastName}"/><br/>
		<br/>
		<portlet:actionURL var="logoutActionUrl">
			<portlet:param name="operation" value="logout"/>
		</portlet:actionURL>
		<a href="<c:out value="${logoutActionUrl}"/>">Logout</a>
	</c:otherwise>
</c:choose>
