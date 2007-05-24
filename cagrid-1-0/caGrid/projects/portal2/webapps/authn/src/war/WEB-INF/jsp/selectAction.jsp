<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head>
<title>Select Action</title>
</head>
<body>

<h2>Select Action</h2>

<form:form>
<form:errors path="*" cssStyle="color:red"/>
<table>
	<tr>
		<td>
			<form:radiobutton path="selectedAction" value="GRID_LOGIN"/>
		</td>
		<td>
			Grid Login
		</td>
	</tr>
	<tr>
		<td>
			<form:radiobutton path="selectedAction" value="LOCAL_LOGIN"/>
		</td>
		<td>
			Local Login
		</td>
	</tr>
	<tr>
		<td>
			<form:radiobutton path="selectedAction" value="REGISTER"/>
		</td>
		<td>
			Register
		</td>
	</tr>
</table>
<form:hidden path="targetUrl"/>
<input type="submit" value="Continue"/>

</form:form>

</body>
</html>