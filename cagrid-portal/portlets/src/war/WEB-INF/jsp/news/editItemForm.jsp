<%@ include file="/WEB-INF/jsp/include/includes.jspf"%>

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
<a href="<c:out value="${backUrl}"/>" style="text-decoration:none;">&lt;&lt; Back to items list</a><br/>
<br/>
<c:choose>
	<c:when test="${empty item}">
		<c:out value="${confirmMessage}"/>
	</c:when>
	<c:otherwise>
		
		<c:choose>
			<c:when test="${empty item.id}">
				<b>New Item</b>
			</c:when>
			<c:otherwise>
				<b>Editing Item:</b> <c:out value="${item.title}"/>
			</c:otherwise>
		</c:choose>
		<br/>
		<span style="color:green"><c:out value="${confirmMessage}"/></span>
		<br/>
		<portlet:actionURL var="action"/>
		<form:form id="${formName}" name="${formName}" action="${action}" commandName="item">
			<table>
				<tr>
					<td style="padding-right:5px; text-align:right" valign="top">Title:</td>
					<td>
						<form:input id="${formName}Title" alt="Title" path="title" size="100"/><br/>
						<span style="color:red"><form:errors path="title"/></span>
					</td>
				</tr>
				<tr>
					<td style="padding-right:5px; text-align:right" valign="top">Link:</td>
					<td>
						<form:input id="${formName}Link" alt="Link" path="link" size="100"/><br/>
						<span style="color:red"><form:errors path="link"/></span>
					</td>
				</tr>
				<tr>
					<td style="padding-right:5px; text-align:right" valign="top">Width:</td>
					<td>
						<form:input id="${formName}Width" alt="Width" path="width" size="10"/><br/>
						<span style="color:red"><form:errors path="width"/></span>
					</td>
				</tr>
				<tr>
					<td style="padding-right:5px; text-align:right" valign="top">Height:</td>
					<td>
						<form:input id="${formName}Height" alt="Height" path="height" size="10"/><br/>
						<span style="color:red"><form:errors path="height"/></span>
					</td>
				</tr>
				<tr>
					<td style="padding-right:5px; text-align:right" valign="top">Description:</td>
					<td>
						<form:textarea id="${formName}Desc"  path="description" cols="97"/><br/>
						<span style="color:red"><form:errors path="description"/></span>
					</td>
				</tr>
				<tr>
					<td></td>
					<td style="padding-top:5px;">
						<c:if test="${!empty item.id}">
							<input type="button" id="${formName}Delete" alt="Delete" value="Delete" onclick="<portlet:namespace/>doEditItemOp('delete')"/>
						</c:if>
						<input type="button" id="${formName}Save" alt="Save" value="Save" onclick="<portlet:namespace/>doEditItemOp('save')"/>
					</td>
				</tr>
			</table>
			<input type="hidden" name="editOp" value="save"/>
			<input type="hidden" name="operation" value="editItem"/>
			<c:if test="${!empty item.id}">
				<input type="hidden" name="itemId" value="<c:out value="${item.id}"/>"/>
			</c:if>
		</form:form>
		<br/>
	</c:otherwise>
</c:choose>