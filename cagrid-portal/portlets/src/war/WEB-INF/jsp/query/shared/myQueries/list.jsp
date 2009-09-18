<%@ include file="/WEB-INF/jsp/include/includes.jspf"%>
<%@ include file="/WEB-INF/jsp/query/tabs.jspf" %>
<%@ include file="/WEB-INF/jsp/include/table_styles.jspf" %>
<c:set var="formName"><portlet:namespace/>MySharedQueryOpForm</c:set>
<script type="text/javascript">
	//<![CDATA[
	
    function <portlet:namespace/>doMySharedQueryOp(queryId, operation){
		var form = document.<c:out value="${formName}"/>;
		form.operation.value = operation;
		form.queryId.value = queryId;
		form.submit();
    }
    //]]>
</script>
<portlet:actionURL var="action" />
<form:form action="${action}" id="${formName}" name="${formName}">
<input type="hidden" name="operation" value=""/>
<input type="hidden" name="queryId" value=""/>
</form:form>
<br/>
<c:choose>
<c:when test="${fn:length(sharedQueries) gt 0}">
	<table border="0" class="contentInnerTable">
		<thead>
			<tr class="contentTableHeader">
				<th>Operations</th>
				<th>Name</th>
				<th>Target Service</th>
				<th>Target UML Class</th>
				<th>Share Date</th>
			</tr>
		</thead>
		<tbody>
<c:forEach var="sharedQuery" items="${sharedQueries}" varStatus="rowStatus">
	
				<c:choose>
					<c:when test="${rowStatus.index % 2 == 0}">
						<c:set var="rowClass" value="dataRowDark"/>
					</c:when>
					<c:otherwise>
						<c:set var="rowClass" value="dataRowLight"/>
					</c:otherwise>
				</c:choose>
			
			<tr class="<c:out value="${rowClass}"/>">
				<td class="dataCellText">
					<select name="operation" id="${formName}Operation"
						onchange="<portlet:namespace/>doMySharedQueryOp('<c:out value="${sharedQuery.id}"/>', this.options[this.selectedIndex].value)">
						<option id="${formName}Selected" value="---" selected>---</option>
						<option id="${formName}Edit" value="selectSharedQuery">Edit</option>
						<option id="${formName}Load" value="loadSharedQuery">Load</option>
						<option id="${formName}View" value="selectMySharedQueryToView">View</option>
					</select>
				</td>
				<td class="dataCellText">
					<c:out value="${sharedQuery.name}"/>
				</td>
				<td class="dataCellText">
					<c:out value="${sharedQuery.targetService.serviceMetadata.serviceDescription.name}"/>
				</td>
				<td class="dataCellText">
					<c:out value="${sharedQuery.targetClass.className}"/>
				</td>
				<td class="dataCellText">
					<fmt:formatDate value="${sharedQuery.shareDate}" type="both"/>
				</td>
			</tr>	

</c:forEach>
		</tbody>
	</table>
</c:when>
<c:otherwise>
There are no shared queries to display. Use the <i>Query</i> tab to select queries that you would like to share.
</c:otherwise>
</c:choose>