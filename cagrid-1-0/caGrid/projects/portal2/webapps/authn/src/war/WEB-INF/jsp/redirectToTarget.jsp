<%@ include file="/WEB-INF/jsp/include.jsp" %>
<html>
<head>
<title>Redirecting to <c:out value="${targetUrl}"/></title>
<script language="javascript">
function doRedirect(){
	document.redirectForm.submit();
}
</script>
</head>
<body onLoad="doRedirect()">
<form name="redirectForm" method="post" action="<c:out value="${targetUrl}"/>">
<c:if test="${!empty loginKey}">
<input type="hidden" name="cagrid_authn_loginKey" value="<c:out value="${loginKey}"/>"/>
</c:if>
</form>
</body>
</html>