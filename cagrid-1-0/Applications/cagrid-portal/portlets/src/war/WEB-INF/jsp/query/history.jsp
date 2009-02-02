<%@ include file="/WEB-INF/jsp/include/includes.jspf"%>
<%@ include file="/WEB-INF/jsp/query/tabs.jspf" %>

<script type='text/javascript' src='<c:url value="/dwr/interface/QueryHistoryFacade.js"/>'></script>
<script type='text/javascript' src='<c:url value="/dwr/engine.js"/>'></script>
<script type='text/javascript' src='<c:url value="/dwr/util.js"/>'></script>

<script type="text/javascript">
	//<![CDATA[
	
    function <portlet:namespace/>executeHistoryOperation(instanceId, operation){

		var form = document.<portlet:namespace/>HistoryOperationForm;
		form.operation.value = operation;
		form.instanceId.value = instanceId;
		
		var confirmed = true;
		if(operation == 'delete'){
			confirmed = confirm('Do you want to delete the selected query?');
		}else if(operation == 'cancel'){
			confirmed = confirm('Do you want to cancel the selected query?');
		}
		if(confirmed){
			form.submit();
		}
    }
    
    var <portlet:namespace/>histIntvId;
    
    var <portlet:namespace/>activeInstances = new Array();
<c:forEach var="instance" items="${instances}">
	<c:if test="${'UNSCHEDULED' eq instance.state or 'SCHEDULED' eq instance.state or 'RUNNING' eq instance.state}">
    <portlet:namespace/>activeInstances.push(
    	{
    		id: <c:out value="${instance.id}"/>,
    		finishTime: new Date("<fmt:formatDate value="${instance.finishTime}" type="both" />"),
    		startTime: new Date("<fmt:formatDate value="${instance.startTime}" type="both" />"),
    		state: "<c:out value="${instance.state}"/>"
    	}
    );
    </c:if>
</c:forEach>
    var <portlet:namespace/>newActiveInstances;
    var <portlet:namespace/>currIdx; 
	
    function <portlet:namespace/>checkActiveInstances(){

		<portlet:namespace/>newActiveInstances = new Array();
    	if(<portlet:namespace/>activeInstances.length == 0){
    		clearInterval(<portlet:namespace/>histIntvId);
    	}
    	for(var i = 0; i < <portlet:namespace/>activeInstances.length; i++){
    		<portlet:namespace/>currIdx = i;
    		QueryHistoryFacade.getInstance(<portlet:namespace/>activeInstances[i].id,
    		{
    			callback:function(instance){
    				if(<portlet:namespace/>isInstanceActive(instance)){
    					<portlet:namespace/>newActiveInstances.push(instance);
    				}
    				<portlet:namespace/>updateInstanceDisplay(instance);
    				
    				if((<portlet:namespace/>currIdx + 1) == <portlet:namespace/>activeInstances.length){
    					//We are finished checking all of the instances.
    					<portlet:namespace/>activeInstances = <portlet:namespace/>newActiveInstances;
						if(<portlet:namespace/>activeInstances.length == 0){
    						clearInterval(<portlet:namespace/>histIntvId);
    					}    				
    				}
    			},
    			errorHandler:function(errorString, exception){
    				alert("Error getting instance: " + errorString);
    			}
    		});
    	}
    }
    
    function <portlet:namespace/>isInstanceActive(instance){
    	return instance.state == 'RUNNING' || instance.state == 'SCHEDULED' || instance.state == 'UNSCHEDULED';
    }
    
    function <portlet:namespace/>startHistoryPoll(){
    	<portlet:namespace/>histIntvId = setInterval("<portlet:namespace/>checkActiveInstances()", 5000);
    }
    
    function <portlet:namespace/>updateInstanceDisplay(instance){
        if(instance.type = "CQL"){
	    	QueryHistoryFacade.renderCQLInstance(instance, '<portlet:namespace/>',
	    	{
	    		callback:function(html){
	    			jQuery("#<portlet:namespace/>-" + instance.id).html(html);
	    		},
	    		errorHandler:function(errorString, exception){
	    			alert("Error updating display: " + errorString);
	    		}
	    	}); 
    	}else{
	    	QueryHistoryFacade.renderDCQLInstance(instance, '<portlet:namespace/>',
	    	{
	    		callback:function(html){
	    			jQuery("#<portlet:namespace/>-" + instance.id).html(html);
	    		},
	    		errorHandler:function(errorString, exception){
	    			alert("Error updating display: " + errorString);
	    		}
	    	});    	
    	}
    }
    
    jQuery(document).ready(<portlet:namespace/>startHistoryPoll);
    
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
			<th style="padding-right:5px">
				<b>Operations</b>
			</th>
			<th style="padding-right:5px">
				<b>State</b>	
			</th>
			<th style="padding-right:5px">
				<b>Start Time</b>
			</th>
			<th style="padding-right:5px">
				<b>Finish Time</b>
			</th>
			<th style="padding-right:5px">
				<b>Service</b>
			</th>
		</tr>
	</thead>
	<tbody>

<portlet:actionURL var="action" />
<c:set var="formName"><portlet:namespace/>HistoryOperationForm</c:set>
<form:form action="${action}" id="${formName}" name="${formName}">
<input type="hidden" name="operation" value=""/>
<input type="hidden" name="instanceId" value=""/>
</form:form>

<c:forEach var="instance" items="${instances}">
<c:set var="namespace"><portlet:namespace/></c:set>
<c:set var="instanceId"><c:out value="${namespace}"/>-<c:out value="${instance.id}"/></c:set>
<tr id="<c:out value="${instanceId}"/>">
<%@ include file="/WEB-INF/jsp/query/history/instance.jspf" %>
</tr>
</c:forEach>

	</tbody>
</table>
</c:otherwise>
</c:choose>