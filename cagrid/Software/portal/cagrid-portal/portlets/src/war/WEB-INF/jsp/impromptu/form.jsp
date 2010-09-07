<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
<title>Submit An Impromptu Query</title>
</head>
<body>

<!-- 
<ns1:CQLQuery xmlns:ns1="http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery"><ns1:Target name="gov.nih.nci.caarray.domain.array.Array"/><ns1:QueryModifier countOnly="true"/></ns1:CQLQuery>

http://cagrid1.duhs.duke.edu:18080/wsrf/services/cagrid/CaArraySvc
-->

<form:form method="POST" commandName="impromptuQuery">
	<table>
		<tr>
			<td valign="top" style="font-weight:bold" >Query :</td>
			<td><form:textarea path="query"  cols="80" rows="17"/></td>
			<td><form:errors path="query" /></td>
		</tr>
		<tr>
			<td valign="top" style="font-weight:bold" >Endpoint Url :</td>
			<td><form:input path="endpointUrl" size="80" /></td>
			<td><form:errors path="endpointUrl" /></td>
		</tr>
		<tr>
			<td valign="top" style="font-weight:bold" >Clear Previous Results :</td>
			<td><form:checkbox path="clearPrevious"/></td>
			<td><form:errors path="clearPrevious" /></td>
		</tr>
		<tr>
			<td valign="top" style="font-weight:bold" >Format Results In HTML :</td>
			<td><form:checkbox path="htmlSuccessPage"/></td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit"></td>
		</tr>
	</table>
</form:form>

</body>
</html>