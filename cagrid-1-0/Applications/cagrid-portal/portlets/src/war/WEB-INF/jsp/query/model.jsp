<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ include file="/WEB-INF/jsp/query/tabs.jspf" %>

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
	</c:otherwise>
</c:choose>