<m:tree node="${node}" prefix="${prefix}">

	<jsp:attribute name="nodeFragment">
		<b><c:out value="${currChildNode.label}"/></b>
	</jsp:attribute>

	<jsp:attribute name="contentFragment">
		<c:choose>
			<c:when test="${!empty currNode.content}">
				Querying: <c:out value="${currNode.content.umlClass.className}"/><br/>
				<c:if test="${fn:startsWith(currNode.name, 'UMLClass:')}">
					<c:set var="cqlQueryBean" value="${currNode.content}"/>
					<%@ include file="/WEB-INF/jsp/cqlquery/cqlQueryRoot_frag.jsp"%>
				</c:if>
				<br/>
				<c:set var="criteriaBean" value="${currNode.content}"/>
				<%@ include file="/WEB-INF/jsp/cqlquery/criteriaBean_frag.jsp"%>
			</c:when>
			<c:otherwise>
				<span color="red">ERROR: no content for node <c:out value="${currNode.path}"/></span>
			</c:otherwise>
		</c:choose>	
	</jsp:attribute>
	
</m:tree>