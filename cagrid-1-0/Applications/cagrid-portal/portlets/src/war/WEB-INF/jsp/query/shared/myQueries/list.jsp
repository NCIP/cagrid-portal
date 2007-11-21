<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/query/tabs.jspf" %>

<c:forEach var="sharedQuery" items="${sharedQueries}">

	<portlet:actionURL var="selectUrl">
		<portlet:param name="operation" value="selectSharedQuery"/>
		<portlet:param name="queryId" value="${sharedQuery.id}"/>
	</portlet:actionURL>
	<a href="<c:out value="${selectUrl}"/>"><c:out value="${sharedQuery.name}"/></a><br/>

</c:forEach>