<%@ include file="/WEB-INF/jsp/include/includes.jspf"%>
<c:choose>
	<c:when test="${empty portalUser}">
		<a href="<c:out value="${loginUrl}"/>" style="text-decoration:none"><b>Log In</b></a>&nbsp;&nbsp;|&nbsp;	
		<a href="<c:out value="${registerUrl}"/>" style="text-decoration:none"><b>Register</b></a>
	</c:when>
	<c:otherwise>
		<portlet:actionURL var="logoutActionUrl">
			<portlet:param name="operation" value="logout"/>
		</portlet:actionURL>
		<a href="<c:out value="${logoutActionUrl}"/>" style="text-decoration:none"><b>Log Out</b></a>
	</c:otherwise>
</c:choose>
