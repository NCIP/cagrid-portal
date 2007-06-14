<%@ include file="/WEB-INF/jsp/include.jsp" %>

<h1>Search</h1>
<br/>
<portlet:actionURL var="formAction">
    <portlet:param name="action" value="pointOfContactSearch"/>
</portlet:actionURL>

<form:form action="${formAction}">
<table>
	<tr>
		<td>Keyword:</td>
		<td>
			<input type="text" name="keyword"/>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<input type="submit" value="Search"/>
		</td>
	</tr>
</table>
</form:form>