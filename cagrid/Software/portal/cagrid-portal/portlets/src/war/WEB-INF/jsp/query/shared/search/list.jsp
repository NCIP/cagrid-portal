<%@ include file="/WEB-INF/jsp/include/includes.jspf"%>
<%@ include file="/WEB-INF/jsp/query/tabs.jspf" %>

<%@ include file="/WEB-INF/jsp/include/table_styles.jspf" %>
<c:set var="formName"><portlet:namespace/>SharedQueryOpForm</c:set>
<script type="text/javascript">
	//<![CDATA[
	
    function <portlet:namespace/>doSharedQueryOp(queryId, operation){
		var form = document.<c:out value="${formName}"/>;
		form.operation.value = operation;
		form.queryId.value = queryId;
		form.submit();
    }
    //]]>
</script>
<portlet:actionURL var="action" />
<form:form action="${action}" id="${formName}" name="${formName}">
<input type="hidden" name="operation" value="" alt="Hidden" />
<input type="hidden" name="queryId" value="" alt="Hidden" />
</form:form>

<portlet:renderURL var="backUrl">
	<portlet:param name="selectedTabPath" value="/shared/search/form"/>
</portlet:renderURL>
<a href="<c:out value="${backUrl}"/>">&lt;&lt;&nbsp;Back to search form</a>
<br/>
<br/>
<c:choose>
	<c:when test="${empty sharedQueries}">
	Use the <i>Search</i> tab to search for shared queries.
	</c:when>
	<c:when test="${fn:length(sharedQueries.scroller.page) eq 0}">
	No shared queries matched those criteria.
	</c:when>
	<c:otherwise>
		<span class="scrollerStyle2">
			Found <c:out value="${fn:length(sharedQueries.scroller.objects)}"/> shared queries. 
			Displaying <c:out value="${sharedQueries.scroller.index + 1}"/> to <c:out value="${sharedQueries.scroller.endIndex}"/>.
		</span>
		<br/>
		<c:set var="scroller" value="${sharedQueries.scroller}"/>
		<c:set var="scrollOperation" value="scrollSharedQuerySearchResults"/>
		<%@ include file="/WEB-INF/jsp/include/scroll_controls.jspf" %>
		
	<table border="0" class="contentInnerTable">
		<thead>
			<tr class="contentTableHeader">
				<th>Operations</th>
				<th>Name</th>
				<th>Target Service</th>
				<th>Target UML Class</th>
				<th>Creator Name</th>
				<th>Creator Email</th>
				<th>Share Date</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${sharedQueries.scroller.page}" var="sharedQuery" varStatus="rowStatus">
			
				<c:choose>
					<c:when test="${rowStatus.index % 2 == 0}">
						<c:set var="rowClass" value="dataRowDark"/>
					</c:when>
					<c:otherwise>
						<c:set var="rowClass" value="dataRowLight"/>
					</c:otherwise>
				</c:choose>
			
			<tr class="<c:out value="${rowClass}"/>">
				<td class="dataCellText">
					<select id="${formName}Operation" name="operation"
						onchange="<portlet:namespace/>doSharedQueryOp('<c:out value="${sharedQuery.id}"/>', this.options[this.selectedIndex].value)">
						<option id="${formName}Selected" value="---" selected>---</option>
						<c:if test="${!empty portalUser and portalUser.id eq sharedQuery.owner.id}">
							<option id="${formName}Edit" value="selectSharedQuery">Edit</option>
						</c:if>
						<option id="${formName}Load" value="loadSharedQuery">Load</option>
						<option id="${formName}View" value="selectSharedQueryToView">View</option>
					</select>
				</td>
				<td class="dataCellText">
					<c:out value="${sharedQuery.name}"/>
				</td>
				<td class="dataCellText">
					<c:out value="${sharedQuery.targetService.serviceMetadata.serviceDescription.name}"/>
				</td>
				<td class="dataCellText">
					<c:out value="${sharedQuery.targetClass.className}"/>
				</td>
				<td class="dataCellText">
					<c:out value="${sharedQuery.owner.person.firstName}"/>&nbsp;<c:out value="${sharedQuery.owner.person.lastName}"/>
				</td>
				<td class="dataCellText">
					<c:out value="${sharedQuery.owner.person.emailAddress}"/>
				</td>
				<td class="dataCellText">
					<fmt:formatDate value="${sharedQuery.shareDate}" type="both"/>
				</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>		
		
		<%@ include file="/WEB-INF/jsp/include/scroll_controls.jspf" %>
	</c:otherwise>
</c:choose>