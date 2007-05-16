<%@ include file="/WEB-INF/jsp/include.jsp" %>

<h1>Participants</h1>

<table border="0" cellpadding="4">
   <tr>
      <th>First Name</th>
      <th>Last Name</th>
      <th/>
      <th/>
   </tr>
   <c:forEach items="${participants}" var="participant">
      <tr>
          <td><c:out value="${participant.firstName}"/></td>
          <td><c:out value="${participant.lastName}"/></td>
          <td>
             <a href="<portlet:renderURL>
                         <portlet:param name="action" value="viewParticipant"/>
                         <portlet:param name="participant">
                             <jsp:attribute name="value">
                                 <c:out value="${participant.id}"/>
                             </jsp:attribute>
                         </portlet:param>
                      </portlet:renderURL>">
                View
             </a>
          </td>
          <td>
             <a href="<portlet:actionURL>
                         <portlet:param name="action" value="deleteParticipant"/>
                         <portlet:param name="participant">
                             <jsp:attribute name="value">
                                 <c:out value="${participant.id}"/>
                             </jsp:attribute>
                         </portlet:param>
                      </portlet:actionURL>">
                Remove
             </a>
           </td>                
       </tr>
    </c:forEach>
    
    <tr>
       <td colspan="4" align="right">
          <a href="<portlet:renderURL>
                      <portlet:param name="action" value="addParticipant"/>
                   </portlet:renderURL>">
             Add a Participant
          </a>
       </td>
    </tr>
		
</table>
<br/>
