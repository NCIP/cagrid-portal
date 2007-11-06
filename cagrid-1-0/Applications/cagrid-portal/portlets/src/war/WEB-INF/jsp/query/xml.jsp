<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/query/tabs.jspf" %>

<c:set var="resizablePrefix"><portlet:namespace/>cqlXml</c:set>
<%@ include file="/WEB-INF/jsp/include/resizable_div.jspf" %>

<portlet:actionURL var="action" />
<form:form action="${action}" commandName="cqlQueryCommand">
	<input type="hidden" name="operation" value="submitQuery"/>
	<form:errors path="*"/>
	<table>
		<tr>
			<td>URL</td>
			<td><form:input path="dataServiceUrl" size="100"/><br/>
				<span style="color:red"><form:errors path="dataServiceUrl"/></span></td>
		</tr>
		<tr>
			<td valign="top">Query</td>
			<td>
				<div style="width:500px;">
				<form:textarea id="${resizablePrefix}" cssStyle="width:100%; height:200px" path="cqlQuery"/>
				</div>
			<br/>
			<span style="color:red"><form:errors path="cqlQuery"/></span>
			</td>				
		</tr>
		<tr>
			<td colspan="2">
				<input type="submit" value="Submit Query" />
			</td>
		</tr>
	</table>
</form:form>
