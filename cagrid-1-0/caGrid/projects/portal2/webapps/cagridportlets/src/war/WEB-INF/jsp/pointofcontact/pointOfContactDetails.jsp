<%@ include file="/WEB-INF/jsp/include.jsp"%>
<script src="<c:url value="/js/scriptaculous/prototype.js"/>"
	type="text/javascript"></script>
<script src="<c:url value="/js/scriptaculous/scriptaculous.js"/>"
	type="text/javascript"></script>
<script type="text/javascript">
	//<![CDATA[
    function toggleDiv(divId){
    	//new Effect.toggle(divId);
    	var node = document.getElementById(divId);
    	if(node.style.display == ""){
    		new Effect.BlindUp(node, {duration: 0.2});
    	}else{
    		new Effect.BlindDown(node, {duration: 0.2});
    	}
    }
    // ]]>
</script>
<c:choose>
	<c:when test="${!empty pointOfContact}">
<br>		 Point Of Contact
			<ul>
				<li>Name:&nbsp; <c:out value="${pointOfContact.firstName}" /> <c:out value="${pointOfContact.lastName}" />
				<li>Phone:&nbsp; <c:out value="${pointOfContact.phoneNumber}" /></li>
				<li>Email:&nbsp; <c:out value="${pointOfContact.emailAddress}" /></li>
				<li>Affiliation:&nbsp; <c:out value="${pointOfContact.affiliation}" /></li>
				<li>Role:&nbsp; <c:out value="${pointOfContact.role}" /></li>

			</ul>						
		
	</c:when>
	<c:otherwise>
	No Point of Contact is currently selected.
</c:otherwise>
</c:choose>
