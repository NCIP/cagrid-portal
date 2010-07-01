<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
	<head>
		<title>Success Page</title>
	</head>
	<body>
		Query Submitted
		<hr>
		Query : ${impromptuQuery.query}
		<br />
		Endpoint Url : ${impromptuQuery.endpointUrl}
		<br />
		Result : ${impromptuQuery.result}
	</body>
</html>