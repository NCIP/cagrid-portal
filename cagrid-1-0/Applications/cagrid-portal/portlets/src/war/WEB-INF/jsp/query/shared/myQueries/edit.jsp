<%@ include file="/WEB-INF/jsp/include/includes.jspf"%>
<%@ include file="/WEB-INF/jsp/query/tabs.jspf" %>

<c:set var="resizablePrefix"><portlet:namespace/>description</c:set>
<%@ include file="/WEB-INF/jsp/include/resizable_div.jspf" %>

<c:set var="formName"><portlet:namespace/>editSharedQueryForm</c:set>
<script type="text/javascript">
	//<![CDATA[
	
	function <portlet:namespace/>doEditSharedQueryOp(editOp){
		var form = document.<c:out value="${formName}"/>;
		var confirmed =  true;
		if('delete' == editOp){
			confirmed = confirm('Do you want to delete this shared query?');
		}
		if(confirmed){
			if('cancel' == editOp){
				form.selectedTabPath.value = '/shared/myQueries/list';
				form.operation.value = '';
				form.action = '<portlet:renderURL/>';
			}else{
				form.editOp.value = editOp;
			}
			form.submit();
		}
	}
	
	//]]>
</script>

<portlet:renderURL var="backUrl">
	<portlet:param name="selectedTabPath" value="/shared/myQueries/list"/>
</portlet:renderURL>
<a href="<c:out value="${backUrl}"/>">&lt;&lt;&nbsp;To shared queries list</a>
<br/>
<span style="color:green"><c:out value="${confirmMessage}"/></span>
<br/>
<c:if test="${!empty sharedQueryBean}">
<portlet:actionURL var="action"/>
<form:form id="${formName}" name="${formName}" action="${action}" commandName="sharedQueryBean">
<span style="color:red"><form:errors path="*"/></span><br/>
<table>
	<tr>
		<td style="padding-right:5px"><b>Name:</b></td>
		<td>
			<form:input id="${formName}QueryName" alt="Query Name" path="query.name" size="100"/><br/>
			<span style="color:red"><form:errors path="query.name"/></span>
		</td>
	</tr>
	<tr>
		<td valign="top" style="padding-right:5px"><b>Description:</b></td>
		<td>
			<form:textarea id="${resizablePrefix}" cssStyle="width:100%; height:50px" path="query.description"/>
		<br/>
		<span style="color:red"><form:errors path="query.description"/></span>
		</td>				
	</tr>
</table>
<input type="hidden" name="editOp" value="save"/>
<input type="hidden" name="operation" value="updateSharedQuery"/>
<input type="hidden" name="selectedTabPath" value=""/>
<c:if test="${!empty sharedQueryBean.query.id}">
	<input type="button" id="${formName}Delete" alt="Delete" value="Delete" onclick="<portlet:namespace/>doEditSharedQueryOp('delete')"/>
</c:if>
<input type="button" id="${formName}Save" alt="Save" value="Save" onclick="<portlet:namespace/>doEditSharedQueryOp('save')"/>
<input type="button" id="${formName}Cancel" alt="Cancel" value="Cancel" onclick="<portlet:namespace/>doEditSharedQueryOp('cancel')"/>
</form:form>
</c:if>