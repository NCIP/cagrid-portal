<%@ include file="/WEB-INF/jsp/include.jsp"%>

<c:set var="refreshForm"><portlet:namespace/>refreshForm</c:set>

<script type="text/javascript">
	//<![CDATA[
    function <portlet:namespace/>executeHistoryOperation(form){
		var confirmed = true;
		var operation = form.operation.options[form.operation.selectedIndex].value;
		if(operation == 'delete'){
			confirmed = confirm('Do you want to delete the selected query?');
		}else if(operation == 'cancel'){
			confirmed = confirm('Do you want to cancel the selected query?');
		}
		if(confirmed){
			form.submit();
		}
    }
    
    function <portlet:namespace/>refreshHistoryDisplay(){
    	document.<c:out value="${refreshForm}"/>.submit();
    }
    
    setInterval("<portlet:namespace/>refreshHistoryDisplay()", 5000);
    
    // ]]>
</script>

<c:choose>
<c:when test="${fn:length(instances) == 0}">
No queries to display.
</c:when>
<c:otherwise>
<table>
	<thead>
		<tr>
			<th>
				<!-- Control column -->
			</th>
			<th>
				State	
			</th>
			<th>
				Start Time
			</th>
			<th>
				Finish Time
			</th>
			<th>
				Service
			</th>
		</tr>
	</thead>
	<tbody>

<portlet:renderURL var="render" />
<form:form action="${render}" name="${refreshForm}">
	<input type="hidden" name="operation" value="viewHistory"/>
</form:form>

<portlet:actionURL var="action" />
<c:forEach var="instance" items="${instances}">

	<c:set var="formId"><portlet:namespace/>-<c:out value="${instance.id}"/></c:set>	
	<form:form action="${action}" name="${formId}">	
	<tr>
		<td>
			
			
				<select name="operation" onchange="<portlet:namespace/>executeHistoryOperation(this.form)">		
					<option value="---" selected>---</option>
					
					<c:if test="${instance.state == 'COMPLETE'}">
					<option value="viewResults">View Results</option>
					</c:if>
					
					<c:if test="${instance.state == 'ERROR'}">
					<option value="viewError">View Error</option>
					</c:if>
					
					<c:if test="${instance.state == 'RUNNING'}">
					<option value="cancel">Cancel</option>
					</c:if>
					
					<option value="delete">Delete</option>
				
				</select>
			
			
		
		</td>
		<td>
			<c:out value="${instance.state}"/>
		</td>
		<td>
			<c:if test="${!empty instance.startTime}">
				<fmt:formatDate value="${instance.startTime}" type="both"/>
			</c:if>
		</td>
		<td>
			<c:if test="${!empty instance.finishTime}">
				<fmt:formatDate value="${instance.finishTime}" type="both"/>
			</c:if>
		</td>
		<td>
			<c:out value="${instance.dataService.url}"/>
		</td>

		<input type="hidden" name="selectedCqlQueryInstanceId" value="<c:out value="${instance.id}"/>"/>
	</tr>		
	</form:form>
	
</c:forEach>

	</tbody>
</table>
</c:otherwise>
</c:choose>