<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>

<h1>Select Browse Type</h1>

<c:set var="prefix"><portlet:namespace/></c:set>

<c:set var="formName"><portlet:namespace/>browseForm</c:set>
<portlet:actionURL var="action"/>
<form:form id="${formName}" name="${formName}" action="${action}" commandName="command">

<c:if test="${empty command}">
No command object</br>
</c:if>
<c:if test="${empty command.browseTypes}">
No browse types</br>
</c:if>


<form:select id="${formName}BrowseType" path="browseType">
	<c:forEach var="type" items="${command.browseTypes}">
		<form:option value="${type}"/>
	</c:forEach>
</form:select>

<input type="hidden" name="operation" value="update" alt="Update"/>
<button id="${formName}UpdateBtn" type="submit">Update</button>
	
</form:form>

<portlet:actionURL var="addRelTypeUrl">
	<portlet:param name="operation" value="createRelationshipType"/>
</portlet:actionURL>
<a href="${addRelTypeUrl}">Add New Relationship Type</a><br/>
<c:choose>
	<c:when test="${empty relationshipTypes}">
No relationship types.	
	</c:when>
	<c:otherwise>
		<ul>
			<c:forEach var="relType" items="${relationshipTypes}">
				<li>
					<portlet:renderURL var="viewRelTypeUrl">
						<portlet:param name="operation" value="viewRelationshipType"/>
						<portlet:param name="relTypeId" value="${relType.id}"/>
					</portlet:renderURL>
					<a href="${viewRelTypeUrl}"><c:out value="${relType.name}"/></a>
				</li>
			</c:forEach>
		</ul>
	</c:otherwise>
</c:choose>


<br/>
Back to <a href="<portlet:renderURL portletMode="view"/>">View Mode</a>