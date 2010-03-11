<%@ include file="/WEB-INF/jsp/include/includes.jspf"%>

<c:set var="formName"><portlet:namespace/>editChanelForm</c:set>
<script type="text/javascript">
	//<![CDATA[
	
	function <portlet:namespace/>doEditChannelOp(editOp){
		var form = document.<c:out value="${formName}"/>;
		var confirmed =  true;
		if('delete' == editOp){
			confirmed = confirm('Do you want to delete this channel?');
		}
		if(confirmed){
			form.editOp.value = editOp;
			form.submit();
		}
	}
	
	//]]>
</script>

<portlet:renderURL var="backUrl">
	<portlet:param name="operation" value="listChannels"/>
</portlet:renderURL>
<a href="<c:out value="${backUrl}"/>" style="text-decoration:none;">&lt;&lt; Back to channels list</a><br/>
<br/>
<c:choose>
	<c:when test="${empty channel}">
		<c:out value="${confirmMessage}"/>
	</c:when>
	<c:otherwise>
		
		<c:choose>
			<c:when test="${empty channel.id}">
				<b>New Channel</b>
			</c:when>
			<c:otherwise>
				<b>Editing Channel:</b> <c:out value="${channel.title}"/>
			</c:otherwise>
		</c:choose>
		<br/>
		<span style="color:green"><c:out value="${confirmMessage}"/></span>
		<br/>
		<portlet:actionURL var="action"/>
		<form:form id="${formName}" name="${formName}" action="${action}" commandName="channel">
			
			<table>
				<tr>
					<td style="padding-right:5px; text-align:right" valign="top">Title:</td>
					<td>
						<form:input id="${formName}Title"  alt="Title" path="title" size="100"/><br/>
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
					<td style="padding-right:5px; text-align:right" valign="top">Description:</td>
					<td>
						<form:textarea id="${formName}Desc" path="description" cols="97"/><br/>
						<span style="color:red"><form:errors path="description"/></span>
					</td>
				</tr>
				<tr>
					<td></td>
					<td style="padding-top:5px;">
						<c:if test="${!empty channel.id}">
							<input type="button" id="${formName}Delete" alt="Delete" value="Delete" onclick="<portlet:namespace/>doEditChannelOp('delete')"/>
						</c:if>
						<input type="button" id="${formName}Save" alt="Save" value="Save" onclick="<portlet:namespace/>doEditChannelOp('save')"/>		
					</td>
				</tr>
			</table>
			
			<input type="hidden" name="editOp" value="save"/>
			<input type="hidden" name="operation" value="editChannel"/>
			
		</form:form>
		<br/>
		<c:if test="${!empty channel.id}">
			<portlet:renderURL var="listItemsUrl">
				<portlet:param name="operation" value="listItems"/>
				<portlet:param name="channelId" value="${channel.id}"/>
			</portlet:renderURL>
			<a href="<c:out value="${listItemsUrl}"/>">Edit news items.</a>
		</c:if>
	</c:otherwise>
</c:choose>