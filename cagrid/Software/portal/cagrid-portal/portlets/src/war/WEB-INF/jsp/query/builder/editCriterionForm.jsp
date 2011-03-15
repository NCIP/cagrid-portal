<%@ include file="/WEB-INF/jsp/include/includes.jspf"%>
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
<form:form commandName="criterionBean" id="${criterionBeanFormName}"
           name="${criterionBeanFormName}" action="${criterionBeanFormAction}">

<table>
	<thead>
		<th style="padding:5px"><b>Attribute Name</b></th>
		<th style="padding:5px"><b>Predicate</b></th>
		<th style="padding:5px"><b>Value(s)</b></th>
	</thead>
	<tbody>
		<tr>
			<td style="padding:5px"><c:out value="${criterionBean.umlAttribute.name}"/></td>
			<td style="padding:5px"><label for="${criterionBeanFormName}Predicate"/>
				<form:select id="${criterionBeanFormName}Predicate" path="predicate">
					<c:forEach var="pred" items="${predicates}">
						<form:option value="${pred}"/>
					</c:forEach>
				</form:select>
			</td>
			<td style="padding:5px"><label for="${criterionBeanFormName}Value"/>
				<form:input id="${criterionBeanFormName}Value"  alt="Value" path="value"/>
			</td>
		</tr>
	</tbody>
</table>
<br/>
<br/>
<input type="button" id="${criterionBeanFormName}Update" alt="Update" value="Update" onclick="<portlet:namespace/>doCriterionBeanOp('update')"/>
<input type="button" id="${criterionBeanFormName}Delete" alt="Delete" value="Delete" onclick="<portlet:namespace/>doCriterionBeanOp('delete')"/>
<input type="button" id="${criterionBeanFormName}Cancel" alt="Cancel"  value="Cancel" onclick="<portlet:namespace/>doCriterionBeanOp('cancel')"/>
<input type="hidden" name="operation" value="updateCriterion"/>
<input type="hidden" name="path" value="<c:out value="${path}"/>"/>
<input type="hidden" name="editOperation" value=""/>
</form:form> 