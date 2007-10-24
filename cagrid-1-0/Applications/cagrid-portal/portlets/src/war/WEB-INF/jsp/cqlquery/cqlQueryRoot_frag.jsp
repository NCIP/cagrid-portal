<script type="text/javascript">
	//<![CDATA[
    	
	function <portlet:namespace/>doCriteriaBeanOp(operation){
		var form = document.<portlet:namespace/>criteriaBeanForm;
		if(operation == 'buildCQLQuery'){
			form.action = '<portlet:actionURL/>';
		}else{
			form.action = '<portlet:renderURL/>';
		}
		form.operation.value = operation;
		form.submit();
	}
	
	//]]>
</script>
<c:set var="criteriaBeanFormName"><portlet:namespace/>criteriaBeanForm</c:set>
<form:form name="${criteriaBeanFormName}">
	<input type="button" value="Edit Query Modifiers" onclick="<portlet:namespace/>doCriteriaBeanOp('editQueryModifier')"/>
	<input type="button" value="Add Criterion" onclick="<portlet:namespace/>doCriteriaBeanOp('selectCriterion')"/>
	<input type="button" value="Export Query" onclick="<portlet:namespace/>doCriteriaBeanOp('buildCQLQuery')"/>
	<input type="hidden" name="operation" value=""/>
</form:form>
<br/>
<b>Result Type:</b>&nbsp;
<c:set var="modifierType"><c:out value="${cqlQueryBean.modifierType}"/></c:set>
<c:choose>
	<c:when test="${'COUNT_ONLY' eq modifierType}">
		Count 
	</c:when>
	<c:when test="${'DISTINCT_ATTRIBUTE' eq modifierType}">
		Distinct Attribute [<c:out value="${cqlQueryBean.selectedAttributes[0]}"/>]
	</c:when>
	<c:when test="${'SELECTED_ATTRIBUTES' eq modifierType}">
		Selected Attributes [
		<c:set var="numAtts" value="${fn:length(cqlQueryBean.selectedAttributes)}"/>
		<c:forEach var="attName" items="${cqlQueryBean.selectedAttributes}" varStatus="status">
			<c:out value="${attName}"/>
			<c:if test="${status.count < numAtts}">, </c:if>
		</c:forEach>
		]
	</c:when>
	<c:otherwise>
		Object
	</c:otherwise>
</c:choose>