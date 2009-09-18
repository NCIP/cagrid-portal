<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>

<h1>Specify View New</h1>

<p> 
Current view is: <c:out value="${viewName}"/>
</p>

<c:set var="prefix"><portlet:namespace/></c:set>

<form id="${prefix}editForm" method="post" action="<portlet:actionURL portletMode="edit"/>">
	<input id="${prefix}viewName"
           alt="View Name"
           type="text" name="viewName" value="<c:out value="${viewName}"/>"/>
	<br/>
	<button id="${prefix}ModifyBtn" type="submit">Modify</button>
</form>

<br/>
Back to <a href="<portlet:renderURL portletMode="view"/>">View Mode</a>