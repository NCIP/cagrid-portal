<%@ include file="/WEB-INF/jsp/include.jsp"%>

<portlet:actionURL var="action" />
<c:if test="${!empty cqlQueryCommand.cqlQuerySubmitError}">
	<span style="color:red"><c:out value="${cqlQueryCommand.cqlQuerySubmitError}"/></span>
	<br/>
</c:if>
<form:form action="${action}">
	<table>
		<tr>
			<td>URL</td>
			<td><input type="text" name="dataServiceUrl" value="<c:out value="${cqlQueryCommand.dataServiceUrl}"/>"/></td>
			<td>
				<c:if test="${!empty cqlQueryCommand.dataServiceUrlError}">
					<span style="color:red"/><c:out value="${cqlQueryCommand.dataServiceUrlError}"/></span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td>Query</td>
			<td>
				<textarea name="cqlQuery" rows="4" cols="50">
<c:out value="${cqlQueryCommand.cqlQuery}"/>			
				</textarea>
			</td>
			<td>
				<c:if test="${!empty cqlQueryCommand.cqlQueryError}">
					<span style="color:red"/><c:out value="${cqlQueryCommand.cqlQueryError}"/></span>
				</c:if>
			</td>				
		</tr>
		<tr>
			<td colspan="3">
				<input type="submit" value="Query" />
				<c:if test="${!empty cqlQueryCommand.confirmationMessage}">
					<br/>
					<span style="color:green"/><c:out value="${cqlQueryCommand.confirmationMessage}"/></span>
				</c:if>
			</td>
		</tr>
	</table>
</form:form>
