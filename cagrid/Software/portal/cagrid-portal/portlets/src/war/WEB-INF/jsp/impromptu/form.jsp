<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
<title>Submit An Impromptu Query</title>
</head>
<body>

<form:form method="POST" commandName="impromptuQuery">
	<table>
		<tr>
			<td>Query :</td>
			<td><form:textarea path="query" /></td>
			<td><form:errors path="query" /></td>
		</tr>
		<tr>
			<td>Endpoint Url :</td>
			<td><form:input path="endpointUrl" /></td>
			<td><form:errors path="endpointUrl" /></td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit"></td>
		</tr>
	</table>
</form:form>

</body>
</html>