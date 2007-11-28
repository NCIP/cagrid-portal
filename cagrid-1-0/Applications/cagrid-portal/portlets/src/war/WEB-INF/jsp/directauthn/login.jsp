<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:choose>
	<c:when test="${empty portalUser}">
		<portlet:renderURL var="portalAuthnUrl"/>
		<portlet:actionURL var="action">
			<portlet:param name="operation" value="login"/>
		</portlet:actionURL>
		<form:form action="${action}" commandName="directLoginCommand" method="POST">
			<span style="color:red"><form:errors path="*"/></span>
			<table>
				<tr>
					<td style="padding-right:5px; text-align:right">Username:</td><td><form:input path="username" size="29"/></td>
				</tr>
				<tr>
					<td style="padding-right:5px; text-align:right;">Password:</td><td><form:password path="password" size="29"/></td>
				</tr>
				<tr>
					<td style="padding-right:5px; text-align:right;">Identity Provider:</td>
					<td>
						<form:select path="idpUrl">
							<option value="https://cagrid-dorian.nci.nih.gov:8443/wsrf/services/cagrid/Dorian">NCICB Dorian</option>
							<option value="https://cagrid-auth.nci.nih.gov:8443/wsrf/services/cagrid/AuthenticationService">NCICB AuthenticationService</option>
						</form:select>
					</td>
				</tr>
				<tr>
					<td></td>
					<td style="padding-top:10px;">
						<input type="submit" value="Login"/>
					</td>
				</tr>
			</table>
			<input type="hidden" name="portalAuthnUrl" value="<c:out value="${portalAuthnUrl}"/>"/>
		</form:form>
		<br/>		
		Don't have an account? 
		<a href="<c:out value="${registerUrl}"/>"><b>Register</b></a>&nbsp;
		<a href="<c:out value="${registerUrl}"/>">Why Register?</a>

	</c:when>
	<c:otherwise>
		<b>You are logged in as <c:out value="${portalUser.person.firstName}"/> <c:out value="${portalUser.person.lastName}"/></b><br/>
		<portlet:actionURL var="logoutActionUrl">
			<portlet:param name="operation" value="logout"/>
		</portlet:actionURL>
		<a href="<c:out value="${logoutActionUrl}"/>">Logout</a>
	</c:otherwise>
</c:choose>
