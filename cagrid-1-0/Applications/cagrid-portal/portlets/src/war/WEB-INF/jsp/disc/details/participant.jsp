<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ include file="/WEB-INF/jsp/disc/tabs.jspf" %>
<b>Participant</b>
<ul>
	<li>Name: <c:out value="${participant.name}"/></li>
	<li>Institution: <c:out value="${participant.institution}"/></li>
	<li>Email: <a href="mailto:<c:out value="${participant.emailAddress}"/>"><c:out value="${participant.emailAddress}"/></a></li>
	<li>Homepage: <a target="_blank" href="<c:out value="${participant.homepageUrl}"/>"><c:out value="${participant.homepageUrl}"/></a></li>
	<li>Phone: <c:out value="${participant.phone}"/></li>
	<li>Address:
		<ul>
			<li>Street 1: <c:out value="${participant.address.street1}"/></li>
			<li>Street 2: <c:out value="${participant.address.street2}"/></li>
			<li>Locality: <c:out value="${participant.address.locality}"/></li>
			<li>Postal Code: <c:out value="${participant.address.postalCode}"/></li>
			<li>State/Province: <c:out value="${participant.address.stateProvince}"/></li>
			<li>Country: <c:out value="${participant.address.country}"/></li>
		</ul>
	</li>
	<li>Workspaces:
		<ul>
		<c:forEach var="p" items="${participant.participation}">
			<li><c:out value="${p.workspace.name}"/>: <c:out value="${p.status}"/></li>
		</c:forEach>
		</ul>
	</li>
</ul>
