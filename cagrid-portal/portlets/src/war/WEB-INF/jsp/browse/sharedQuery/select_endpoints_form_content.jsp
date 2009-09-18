<%@ include file="/WEB-INF/jsp/include/servlet_includes.jsp" %>
<c:set var="ns" value="${namespace}"/>
<c:choose>
<c:when test="${empty endpoints}">
No data sources support this query.
</c:when>
<c:otherwise>
<c:forEach var="endpoint" items="${endpoints}">
<tags:endpointSelector input_name="endpoints" endpoint="${endpoint}" id_prefix="${ns}_endpoint"/>
<br/>
</c:forEach>
</c:otherwise>
</c:choose>