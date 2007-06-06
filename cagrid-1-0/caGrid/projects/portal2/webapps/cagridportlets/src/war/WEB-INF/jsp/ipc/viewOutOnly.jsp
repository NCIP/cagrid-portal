<%@ include file="/WEB-INF/jsp/include.jsp" %>

<h1>IPC Test Out Only</h1>
<br/>
<portlet:actionURL var="formAction">
    <portlet:param name="action" value="search"/>
</portlet:actionURL>

<form:form action="${formAction}">
<table>
	<tr>
		<td>Output:</td>
		<td>
			<input type="text" name="ipcOutput"/>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<input type="submit" value="Send"/>
		</td>
	</tr>
</table>
</form:form>