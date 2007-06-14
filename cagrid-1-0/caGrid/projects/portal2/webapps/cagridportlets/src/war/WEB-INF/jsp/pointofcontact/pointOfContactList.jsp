<%@ include file="/WEB-INF/jsp/include.jsp" %>

<h1>Point Of Contacts</h1>
<br/>
<p>
<c:choose>
	<c:when test="${empty pointOfContactsInRange}">
No Point Of Contacts to display.
	</c:when>
	<c:otherwise>
<div class="scrollControls">
	<c:choose>
		<c:when test="${!empty first}">
 <a href="<portlet:renderURL>
 	<portlet:param name="action" value="listPointOfContacts"/>
  	<portlet:param name="offset">
       	<jsp:attribute name="value">
           	<c:out value="${first}"/>
           </jsp:attribute>
    </portlet:param>
  		 </portlet:renderURL>"
    alt="Scroll To First">
	&lt;&lt;
 </a>
 		</c:when>
 		<c:otherwise>
 	&lt;&lt;
 		</c:otherwise>
 	</c:choose>
 |  	
<c:choose>
		<c:when test="${!empty previous}">
 <a href="<portlet:renderURL>
 	<portlet:param name="action" value="listPointOfContacts"/>
  	<portlet:param name="offset">
       	<jsp:attribute name="value">
           	<c:out value="${previous}"/>
           </jsp:attribute>
    </portlet:param>
  		 </portlet:renderURL>"
    alt="Scroll To Previous">
	&lt;
 </a>
 		</c:when>
 		<c:otherwise>
 	&lt;
 		</c:otherwise>
 	</c:choose> 
 |
 <c:choose>
		<c:when test="${!empty next}">
 <a href="<portlet:renderURL>
 	<portlet:param name="action" value="listPointOfContacts"/>
  	<portlet:param name="offset">
       	<jsp:attribute name="value">
           	<c:out value="${next}"/>
           </jsp:attribute>
    </portlet:param>
  		 </portlet:renderURL>"
    alt="Scroll To Next">
	&gt;
 </a>
 		</c:when>
 		<c:otherwise>
 	&gt;
 		</c:otherwise>
 	</c:choose>
 |
<c:choose>
		<c:when test="${!empty last}">
 <a href="<portlet:renderURL>
 	<portlet:param name="action" value="listPointOfContacts"/>
  	<portlet:param name="offset">
       	<jsp:attribute name="value">
           	<c:out value="${last}"/>
           </jsp:attribute>
    </portlet:param>
  		 </portlet:renderURL>"
    alt="Scroll To Last">
	&gt;&gt;
 </a>
 		</c:when>
 		<c:otherwise>
 	&gt;&gt;
 		</c:otherwise>
 	</c:choose> 
</div>

<table border="0" cellpadding="4">

   <tr>
      <th>Name</th>
      <th>Status</th>
   </tr>
   <c:forEach items="${pointOfContactsInRange}" var="pointOfContact">
   
   	<tr>
   		<td>
             <a href="<portlet:actionURL>
                         <portlet:param name="action" value="selectPointOfContact"/>
                         <portlet:param name="spoc_id">
                             <jsp:attribute name="value">
                                 <c:out value="${pointOfContact.id}"/>
                             </jsp:attribute>
                         </portlet:param>
                      </portlet:actionURL>"
                 alt="Select Point Of Contact">
                <c:out value="${pointOfContact.lastName}"/>, <c:out value="${pointOfContact.firstName}"/>
             </a>
   			
   		</td>
   		<td align="center">
   			<c:out value="${pointOfContact.affiliation}"/>
   		</td>
   		
   	</tr>
   
   </c:forEach>
   
</table>
	</c:otherwise>
</c:choose>
</p>