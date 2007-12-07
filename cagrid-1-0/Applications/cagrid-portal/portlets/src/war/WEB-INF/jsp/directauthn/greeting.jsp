<%@ include file="/WEB-INF/jsp/include/includes.jspf"%>
<c:choose>
	<c:when test="${empty portalUser}">
		<a href="<c:out value="${loginUrl}"/>" style="text-decoration:none"><b>Login</b></a>&nbsp;|&nbsp;	
		<a href="<c:out value="${registerUrl}"/>" style="text-decoration:none"><b>Register</b></a>
	</c:when>
	<c:otherwise>
<%@ include file="/WEB-INF/jsp/directauthn/greeting.jspf"%>
	</c:otherwise>
</c:choose>