<%@ include file="/WEB-INF/jsp/include/includes.jspf"%>
<%@ include file="/WEB-INF/jsp/query/tabs.jspf" %>

<style type="text/css">
<!--
<%@ include file="/css/xmlverbatim.css" %>
-->
</style>
<portlet:renderURL var="backUrl">
	<portlet:param name="selectedTabPath" value="/shared/search/list"/>
</portlet:renderURL>
<a href="<c:out value="${backUrl}"/>"><img src="/cagridportlets/images/foop/back-arrow.png" alt=""/> Back to search results</a>
<br/>
<br/>
<%@ include file="/WEB-INF/jsp/query/shared/search/view.jspf" %>