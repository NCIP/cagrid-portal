<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>

<h1>Specify View New</h1>

<p> 
Current view is: <c:out value="${viewName}"/>
</p>

<form method="post" action="<portlet:actionURL/>">
	<input type="text" name="viewName" value="<c:out value="${viewName} }"/>"/>
	<br/>
	<button type="submit">Modify</button>
</form>

<br/>
Back to <a href="<portlet:renderURL portletMode="view"/>">View Mode</a>