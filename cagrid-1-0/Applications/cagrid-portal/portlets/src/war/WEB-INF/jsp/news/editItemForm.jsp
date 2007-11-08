<%@ include file="/WEB-INF/jsp/include.jsp"%>

<c:set var="formName"><portlet:namespace/>editChanelForm</c:set>
<script type="text/javascript">
	//<![CDATA[
	
	function <portlet:namespace/>doEditItemOp(editOp){
		var form = document.<c:out value="${formName}"/>;
		var confirmed =  true;
		if('delete' == editOp){
			confirmed = confirm('Do you want to delete this item?');
		}
		if(confirmed){
			form.editOp.value = editOp;
			form.submit();
		}
	}
	
	//]]>
</script>

<portlet:renderURL var="backUrl">
	<portlet:param name="operation" value="listItems"/>
	<c:if test="${!empty channelId}">
		<portlet:param name="channelId" value="${channelId}"/>
	</c:if>
</portlet:renderURL>
<a href="<c:out value="${backUrl}"/>">&lt; Back to items list</a><br/>
<c:choose>
	<c:when test="${empty item}">
		<c:out value="${confirmMessage}"/>
	</c:when>
	<c:otherwise>
		
		<c:choose>
			<c:when test="${empty item.id}">
				New Item:
			</c:when>
			<c:otherwise>
				Editing Item: <c:out value="${item.title}"/>
			</c:otherwise>
		</c:choose>
		<br/>
		<span style="color:green"><c:out value="${confirmMessage}"/></span>
		<br/>
		<portlet:actionURL var="action"/>
		<form:form name="${formName}" action="${action}" commandName="item">
			<table>
				<tr>
					<td>Title:</td>
					<td>
						<form:input path="title"/><br/>
						<span style="color:red"><form:errors path="title"/></span>
					</td>
				</tr>
				<tr>
					<td>Link:</td>
					<td>
						<form:input path="link"/><br/>
						<span style="color:red"><form:errors path="link"/></span>
					</td>
				</tr>
				<tr>
					<td>Width:</td>
					<td>
						<form:input path="width"/><br/>
						<span style="color:red"><form:errors path="width"/></span>
					</td>
				</tr>
				<tr>
					<td>Height:</td>
					<td>
						<form:input path="height"/><br/>
						<span style="color:red"><form:errors path="height"/></span>
					</td>
				</tr>
				<tr>
					<td>Description:</td>
					<td>
						<form:textarea path="description"/><br/>
						<span style="color:red"><form:errors path="description"/></span>
					</td>
				</tr>
			</table>
			<input type="hidden" name="editOp" value="save"/>
			<input type="hidden" name="operation" value="editItem"/>
			<c:if test="${!empty item.id}">
				<input type="button" value="Delete" onclick="<portlet:namespace/>doEditItemOp('delete')"/>
			</c:if>
			<input type="button" value="Save" onclick="<portlet:namespace/>doEditItemOp('save')"/>
		</form:form>
		<br/>
	</c:otherwise>
</c:choose>