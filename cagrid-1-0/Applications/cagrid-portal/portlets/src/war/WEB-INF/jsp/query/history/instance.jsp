<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:set var="instanceId"><c:out value="${namespace}"/>-<c:out value="${instance.id}"/></c:set>
<%@ include file="/WEB-INF/jsp/query/history/instance.jspf" %>