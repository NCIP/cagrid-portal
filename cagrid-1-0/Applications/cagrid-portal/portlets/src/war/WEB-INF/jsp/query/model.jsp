<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ include file="/WEB-INF/jsp/query/tabs.jspf" %>

<c:set var="resizablePrefix"><portlet:namespace/>umlClassList</c:set>
<%@ include file="/WEB-INF/jsp/include/resizable_div.jspf" %>

<portlet:actionURL var="selectServiceAction"/>
<form:form action="${selectServiceAction}" commandName="selectServiceCommand">
<input type="hidden" name="operation" value="selectService"/>
<table>
	<tr>
		<td>
			Service URL:
		</td>
		<td>
			<form:input path="serviceUrl"/><br/>
			<form:errors path="serviceUrl"/>
		</td>
		<td>
			<input type="submit" value="Select"/>
		</td>
	</tr>
</table>
<p/>
</form:form>

<c:choose>
	<c:when test="${empty umlClasses}">
		No UML classes to display.
	</c:when>
	<c:otherwise>
		<div style="width:500px;">
		<div id="<c:out value="${resizablePrefix}"/>" style="width:100%; height:200px; overflow:scroll">
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
		</div>
	</c:otherwise>
</c:choose>