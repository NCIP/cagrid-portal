<b>Result Type:</b>&nbsp;
<c:choose>
	<c:when test="${'countOnly' eq cqlQueryBean.modifierType}">
		Count 
	</c:when>
	<c:when test="${'distinctAttribute' eq cqlQueryBean.modifierType}">
		Distinct Attribute [<c:out value="${cqlQueryBean.selectedAttributes[0]}"/>]
	</c:when>
	<c:when test="${'selectedAttributes' eq cqlQueryBean.modifierType}">
		Selected Attributes [
		<c:set var="numAtts" value="${fn:length(cqlQueryBean.selectedAttributes)}"/>
		<c:forEach var="attName" items="${cqlQueryBean.selectedAttributes}" varStatus="status">
			<c:out value="${attName}"/>
			<c:if test="${count + 1 < numAtts}">, </c:if>
		</c:forEach>
		]
	</c:when>
	<c:otherwise>
		Object
	</c:otherwise>
</c:choose>
<br/>
<portlet:renderURL var="editQueryModifiersAction"/>
<form:form action="${editQueryModifiersAction}">
	<input type="submit" value="Edit Query Modifiers"/>
	<input type="hidden" name="operation" value="editQueryModifiers"/>
</form:form>