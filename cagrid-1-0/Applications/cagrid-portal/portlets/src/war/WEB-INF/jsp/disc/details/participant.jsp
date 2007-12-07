<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<%@ include file="/WEB-INF/jsp/disc/tabs.jspf" %>
<c:choose>
<c:when test="${!empty participant}">
<b>Participant</b>
<div style="margin-left:10px">
<ul style="list-style-type:disc; margin-left:10px">
	<li><b>Name:</b> <c:out value="${participant.name}"/></li>
	<li><b>Institution:</b> <c:out value="${participant.institution}"/></li>
	<li><b>Email:</b> <a href="mailto:<c:out value="${participant.emailAddress}"/>"><c:out value="${participant.emailAddress}"/></a></li>
	<li><b>Homepage:</b> <a target="_blank" href="<c:out value="${participant.homepageUrl}"/>"><c:out value="${participant.homepageUrl}"/></a></li>
	<li><b>Phone:</b> <c:out value="${participant.phone}"/></li>
	<li><b>Address:</b>
		<ul style="list-style-type:disc; margin-left:10px">
			<li><b>Street 1:</b> <c:out value="${participant.address.street1}"/></li>
			<li><b>Street 2:</b> <c:out value="${participant.address.street2}"/></li>
			<li><b>Locality:</b> <c:out value="${participant.address.locality}"/></li>
			<li><b>Postal Code:</b> <c:out value="${participant.address.postalCode}"/></li>
			<li><b>State/Province:</b> <c:out value="${participant.address.stateProvince}"/></li>
			<li><b>Country:</b> <c:out value="${participant.address.country}"/></li>
		</ul>
	</li>
	<li><b>Workspaces</b>
		<ul style="list-style-type:disc; margin-left:10px">
		<c:forEach var="p" items="${participant.participation}">
			<li><c:out value="${p.workspace.name}"/>: <c:out value="${p.status}"/></li>
		</c:forEach>
		</ul>
	</li>
</ul>
</div>
</c:when>
<c:otherwise>
No participant has been selected. Use the search tab to discover participants.
</c:otherwise>
</c:choose>