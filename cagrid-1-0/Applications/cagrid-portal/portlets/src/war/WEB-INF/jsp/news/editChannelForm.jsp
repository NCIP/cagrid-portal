<%@ include file="/WEB-INF/jsp/include.jsp"%>

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
<a href="<c:out value="${backUrl}"/>">&lt; Back to channels list</a><br/>

<c:choose>
	<c:when test="${empty channel}">
		<c:out value="${confirmMessage}"/>
	</c:when>
	<c:otherwise>
		
		<c:choose>
			<c:when test="${empty channel.id}">
				New Channel:
			</c:when>
			<c:otherwise>
				Editing Channel: <c:out value="${channel.title}"/>
			</c:otherwise>
		</c:choose>
		<br/>
		<span style="color:green"><c:out value="${confirmMessage}"/></span>
		<br/>
		<portlet:actionURL var="action"/>
		<form:form name="${formName}" action="${action}" commandName="channel">
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
					<td>Description:</td>
					<td>
						<form:textarea path="description"/><br/>
						<span style="color:red"><form:errors path="description"/></span>
					</td>
				</tr>
			</table>
			<input type="hidden" name="editOp" value="save"/>
			<input type="hidden" name="operation" value="editChannel"/>
			<c:if test="${!empty channel.id}">
				<input type="button" value="Delete" onclick="<portlet:namespace/>doEditChannelOp('delete')"/>
			</c:if>
			<input type="button" value="Save" onclick="<portlet:namespace/>doEditChannelOp('save')"/>
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