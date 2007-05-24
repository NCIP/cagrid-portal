<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head>
<title>Local Login</title>
</head>
<body>

<h2>Local Login</h2>
<p>

<form:form>
	<form:errors path="*" cssStyle="color:red"/>
	<table>
		<tr>
			<td>User Name:</td>
			<td>
				<form:input path="username"/>
				<br/>
				<form:errors path="username"/>
			</td>
		</tr>
		<tr>
			<td>Password:</td>
			<td>
				<form:password path="password"/>
				<br/>
				<form:errors path="password"/>
			</td>
		</tr>
	</table>
	<form:hidden path="targetUrl"/>
	<input type="submit" value="Login"/>
	
</form:form>

</p>
</body>
</html>