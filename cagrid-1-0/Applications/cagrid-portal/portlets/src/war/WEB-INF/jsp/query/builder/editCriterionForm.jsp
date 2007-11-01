<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/query/tabs.jspf" %>

<script type="text/javascript">
	//<![CDATA[
	
	function <portlet:namespace/>doCriterionBeanOp(editOp){
		var form = document.<portlet:namespace/>criterionBeanForm;
		var confirmed =  true;
		if('delete' == editOp){
			confirmed = confirm('Do you want to delete this criterion?');
		}
		if(confirmed){
			form.editOperation.value = editOp;
			form.submit();
		}
	}
	
	//]]>
</script>

<portlet:actionURL var="criterionBeanFormAction"/>
<c:set var="criterionBeanFormName"><portlet:namespace/>criterionBeanForm</c:set>
<form:form commandName="criterionBean" name="${criterionBeanFormName}" action="${criterionBeanFormAction}">

<table>
	<thead>
		<th>Attribute Name</th>
		<th>Predicate</th>
		<th>Value(s)</th>
	</thead>
	<tbody>
		<tr>
			<td><c:out value="${criterionBean.umlAttribute.name}"/></td>
			<td>
				<form:select path="predicate">
					<c:forEach var="pred" items="${predicates}">
						<form:option value="${pred}"/>
					</c:forEach>
				</form:select>
			</td>
			<td>
				<form:input path="value"/>
			</td>
		</tr>
	</tbody>
</table>
<input type="button" value="Update" onclick="<portlet:namespace/>doCriterionBeanOp('update')"/>
<input type="button" value="Delete" onclick="<portlet:namespace/>doCriterionBeanOp('delete')"/>
<input type="button" value="Cancel" onclick="<portlet:namespace/>doCriterionBeanOp('cancel')"/>
<input type="hidden" name="operation" value="updateCriterion"/>
<input type="hidden" name="path" value="<c:out value="${path}"/>"/>
<input type="hidden" name="editOperation" value=""/>
</form:form>