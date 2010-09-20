<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>


<portlet:renderURL var="viewUrl">
	<portlet:param name="operation" value="view"/>
</portlet:renderURL>
<a href="<c:out value="${viewUrl} }"/>"><img src="/cagridportlets/images/foop/back-arrow.png" alt="Back"/> Back</a>

<div id="catalogEntry.name">
<c:out value="${catalogEntry.name}"/>
</div>

<div id="catalogEntry.description">
<c:out value="${catalogEntry.description}"/>
</div>

