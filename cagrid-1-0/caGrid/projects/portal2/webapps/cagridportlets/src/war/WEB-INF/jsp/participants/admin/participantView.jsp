<%@ include file="/WEB-INF/jsp/include.jsp" %>

<h1>Participant Info for: <c:out value="${participant.firstName}"/> <c:out value="${participant.lastName}"/></h1>

<ul>
   <li>Email: <c:out value="${participant.emailAddress}"/></li>
   <li>Phone: <c:out value="${participant.phoneNumber}"/></li>
</ul>

<br/>

<a href="<portlet:renderURL>
            <portlet:param name="action" value="listParticipants"/>
         </portlet:renderURL>">
    List All Participants
</a>

<br/>
