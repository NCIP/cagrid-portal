<%@ include file="/WEB-INF/jsp/include/servlet_includes.jsp" %>
<c:set var="ns" value="${namespace}"/>
<c:forEach var="term" items="${terms}">
<tags:termSelector input_name="terms" term="${term}" id_prefix="${ns}"/>
</c:forEach>
