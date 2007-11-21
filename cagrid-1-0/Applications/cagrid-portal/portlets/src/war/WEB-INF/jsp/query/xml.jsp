<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/query/tabs.jspf" %>

<c:set var="formName"><portlet:namespace/>submitQueryForm</c:set>
<script type="text/javascript">
	//<![CDATA[
	function <portlet:namespace/>shareQuery(){
		var form = document.<c:out value="${formName}"/>;
		form.action = '<portlet:actionURL/>';
		form.operation.value = 'shareQuery';
		form.submit();
	}
	//]]>
</script>

<c:set var="resizablePrefix"><portlet:namespace/>cqlXml</c:set>
<%@ include file="/WEB-INF/jsp/include/resizable_div.jspf" %>

<portlet:actionURL var="action" />
<form:form name="${formName}" action="${action}" commandName="cqlQueryCommand">
	<input type="hidden" name="operation" value="submitQuery"/>
	<span style="color:red"><form:errors path="*"/></span>
	<table>
		<tr>
			<td style="padding-right:5px"><b>URL:</b></td>
			<td><form:input path="dataServiceUrl" size="100"/><br/>
				<span style="color:red"><form:errors path="dataServiceUrl"/></span></td>
		</tr>
		<tr>
			<td valign="top" style="padding-right:5px"><b>Query:</b></td>
			<td>
				<form:textarea id="${resizablePrefix}" cssStyle="width:100%; height:200px" path="cqlQuery"/>
			<br/>
			<span style="color:red"><form:errors path="cqlQuery"/></span>
			</td>				
		</tr>
		<tr>
			<td colspan="2">
				<input type="submit" value="Submit Query" />
				<input type="button" value="Share Query" onclick="<portlet:namespace/>shareQuery()" />
			</td>
		</tr>
	</table>
</form:form>
