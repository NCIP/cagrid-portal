<%@ include file="/WEB-INF/jsp/include.jsp"%>

<portlet:actionURL var="action" />
<form:form action="${action}" commandName="queryBean">

	<form:errors path="*" />
	<table>
		<tr>
			<td>Grid Service URL</td>
			<td><form:input path="url" /></td>
			<td><form:errors path="url" /></td>
		</tr>
		<tr>
			<td>Query</td>
			<td><form:textarea path="query" rows="4" cols="50" /></td>
			<td></td>
		</tr>
		<c:choose>
			<c:when test="${empty error}">
				<tr>
					<td>Results</td>
					<td><form:textarea path="results" readonly="true" rows="4" cols="50"/></td>
					<td></td>
				</tr>
			</c:when>
			<c:otherwise>
				<tr>
					<td>Error</td>
					<td><form:textarea path="error" readonly="true" rows="4" cols="50"/></td>
					<td></td>
				</tr>
			</c:otherwise>
		</c:choose>
		<tr>
			<td colspan="3"><input type="submit" value="Query" /></td>
		</tr>
	</table>
</form:form>
