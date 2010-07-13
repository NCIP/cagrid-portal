<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html>
	<head>
		<title>Success Page</title>
	</head>
	<body>
		Query Submitted
		<hr>
		UUID : ${impromptuQuery.uuid}
		<br />
		Query : ${impromptuQuery.query}
		<br />
		Endpoint Url : ${impromptuQuery.endpointUrl}
		<br />
		Result : <br/>
		<textarea cols="80" rows="17">${impromptuQuery.result}</textarea>
	</body>
</html>