<m:tree node="${node}" prefix="${prefix}">

	<jsp:attribute name="nodeFragment">
		<b><c:out value="${currChildNode.label}"/></b>
	</jsp:attribute>

	<jsp:attribute name="contentFragment">
		<c:choose>

			<c:when test="${!empty currNode.content}">
				<b><c:out value="${currNode.content.umlClass.className}"/></b>
				<ul>
					<c:forEach var="att" items="${currNode.content.attributes}">
						<li>
							<portlet:renderURL var="editCriterionUrl">
								<portlet:param name="operation" value="editCriterion"/>
								<portlet:param name="path"><c:out value="${currNode.path}"/>.<c:out value="${att.name}"/></portlet:param>
							</portlet:renderURL>
							<a href="<c:out value="${editCriterionUrl}"/>"><c:out value="${att.name}"/></a>
						</li>
					</c:forEach>
				</ul>
			</c:when>
			<c:otherwise>
				<span color="red">ERROR: no content for node <c:out value="${currNode.path}"/></span>
			</c:otherwise>
		</c:choose>	
	</jsp:attribute>
	
</m:tree>