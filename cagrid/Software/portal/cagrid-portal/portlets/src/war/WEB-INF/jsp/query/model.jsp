<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<%@ include file="/WEB-INF/jsp/query/tabs.jspf" %>

<c:set var="prefix"><portlet:namespace/>selectServiceForm</c:set>
<c:set var="resizablePrefix"><portlet:namespace/>umlClassList</c:set>
<%@ include file="/WEB-INF/jsp/include/resizable_div.jspf" %>

<portlet:actionURL var="selectServiceAction"/>
<form:form id="${prefix}" action="${selectServiceAction}" commandName="selectServiceCommand">
<input type="hidden" name="operation" value="selectService"/>
<table>
	<tr>
		<td style="padding-right:5px;  text-align:right" valign="top">
			<b>Service URL:</b>
		</td>
		<td style="padding-right:5px;  text-align:right" valign="top">
			<form:input id="${prefix}url" alt="Data Service Url" path="dataServiceUrl" size="50" maxlength="1000"/><br/>
			<span style="color:red"><form:errors path="dataServiceUrl"/></span>
		</td>
		<td valign="top">
			<input type="submit" id="${prefix}Select" alt="Select" value="Select"/>
		</td>
	</tr>
</table>
<br/>
<br/>
</form:form>

<c:choose>
	<c:when test="${empty umlClasses}">
		Enter a data service URL above.
	</c:when>
	<c:otherwise>

		<div id="<c:out value="${resizablePrefix}"/>" style="width:100%; height:400px; overflow-y:auto">
		<c:forEach var="umlClass" items="${umlClasses}">
			
			<portlet:actionURL var="selectUmlClassAction">
				<portlet:param name="operation" value="selectUmlClass"/>
				<portlet:param name="umlClassId" value="${umlClass.id}"/>
			</portlet:actionURL>
			<a href="<c:out value="${selectUmlClassAction}"/>" alt="Select for query.">
				<c:out value="${umlClass.packageName}"/>.<c:out value="${umlClass.className}"/>
			</a>
			<br/>
		</c:forEach>
		</div>

	</c:otherwise>
</c:choose>