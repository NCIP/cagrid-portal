<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:choose>
	<c:when test="${empty portalUser}">
		<portlet:actionURL var="loginActionUrl">
			<portlet:param name="operation" value="login"/>
		</portlet:actionURL>
		<a href="<c:out value="${loginActionUrl}"/>" style="text-decoration:none"><b>Login</b></a>&nbsp;|&nbsp;
		<a href="<c:out value="${registerUrl}"/>" style="text-decoration:none"><b>Register</b></a>&nbsp;
		<a href="<c:out value="${registerUrl}"/>" style="text-decoration:none">Why Register?</a>

	</c:when>
	<c:otherwise>
		<b>You are logged in as <c:out value="${portalUser.person.firstName}"/> <c:out value="${portalUser.person.lastName}"/></b><br/>
		<portlet:actionURL var="logoutActionUrl">
			<portlet:param name="operation" value="logout"/>
		</portlet:actionURL>
		<a href="<c:out value="${logoutActionUrl}"/>">Logout</a>
	</c:otherwise>
</c:choose>
