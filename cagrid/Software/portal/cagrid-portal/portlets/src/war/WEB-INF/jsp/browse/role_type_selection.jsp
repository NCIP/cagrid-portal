<%@ include file="/WEB-INF/jsp/include/servlet_includes.jsp" %>

<br/>
<p>
<b>Kinds of Relationships</b>
<c:choose>
	<c:when test="${empty roleTypes}">
		There are no relationships that apply to this catalog entry type.
		
	</c:when>
	<c:otherwise>
		<ul>
			<c:forEach var="roleType" items="${roleTypes}" varStatus="status">
				<li>
					<label for="${roleType.id}">
					<input type="radio" alt="Role" id="${roleType.id}" name="roleType" value="<c:out value="${roleType.id}"/>"
						<c:if test="${status.index eq 0}">checked="true"</c:if>
					/>
					<b><c:out value="${roleType.name}"/></b></label><br/>
					<c:out value="${roleType.description}"/>
					<%--<div class="relationshipDescription">--%>
						<%--<p>--%>
							<%--<b><c:out value="${roleType.relationshipType.name}"/></b><br/>--%>
							<%--<c:out value="${roleType.relationshipType.description}"/>--%>
						<%--</p>--%>
					<%--</div>					--%>
				</li>
			</c:forEach>
		</ul>
		
	</c:otherwise>
</c:choose>

</p>