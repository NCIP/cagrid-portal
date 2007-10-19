<c:forEach var="criterion" items="${criteriaBean.criteria}">
	<c:out value="${criterion.umlAttribute.name}"/>&nbps;
	<c:out value="${criterion.predicate}"/>&nbsp;	
	<c:if test="${criterion.predicate ne 'IS_NULL' and criterion.predicate ne 'IS_NOT_NULL'}">
		<c:out value="${criterion.value}"/>
	</c:if>
	<br/>
</c:forEach>
<script type="text/javascript">
	//<![CDATA[
	
	function <portlet:namespace/>doOperation(operation){
		var form = document.<portlet:namespace/>criteriaBeanForm;
		form.operation.value = operation;
		form.submit();
	}
	
	//]]>
</script>
<portlet:renderURL var="selectCriterionAction"/>
<c:set var="criteriaBeanFormName"><portlet:namespace/>criteriaBeanForm</c:set>
<form:form name="${criteriaBeanFormName}" action="${selectCriterionAction}">
	<input type="button" value="Add Criterion" onclick="<portlet:namespace/>doOperation('selectCriterion')"/>
	<input type="button" value="Export Query" onclick="<portlet:namespace/>doOperation('buildCQLQuery')"/>
	<input type="hidden" name="operation" value=""/>
</form:form>