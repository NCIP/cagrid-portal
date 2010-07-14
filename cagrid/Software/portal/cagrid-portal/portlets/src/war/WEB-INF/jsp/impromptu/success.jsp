<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html>
	<head>
		<title>Success Page</title>
	</head>
	<body>
		<h3>Query Submitted Successfully.</h3> 
		The results will be shown in this URL: <a href="view/${impromptuQuery.uuid}">view/${impromptuQuery.uuid}</a>
		
		<%--
		<table>
			<tr>
				<td valign="top" style="font-weight:bold" >Query :</td>
				<td><textarea path="query"  cols="80" rows="17"/>${impromptuQuery.query}</textarea></td>
			</tr>
			<tr>
				<td valign="top" style="font-weight:bold" >Endpoint Url :</td>
				<td>${impromptuQuery.endpointUrl}</td>
			</tr>
		</table>
	    --%>
	     
	</body>
</html>