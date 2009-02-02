<%@ include file="/WEB-INF/jsp/include/includes.jspf"%>

<c:set var="prefix"><portlet:namespace/></c:set>

<c:choose>
	<c:when test="${empty portalUser}">
	
		If you have an NIH username and password, then
		select the NCICB AuthenticationService IdP before pressing the Log
		In button. If you do not have an NIH username and password, but you
		have already registered through the portal (or through the caGrid Browser), then select the 
		NCICB Dorian Identity Provider. If you have not yet registered, you may do so by 
		clicking <a href="/web/guest/register">here</a>.
		<br/>
		<br/>
	
		<portlet:renderURL var="portalAuthnUrl"/>
		<portlet:actionURL var="action">
			<portlet:param name="operation" value="login"/>
		</portlet:actionURL>
		<form:form id="${prefix}Login" action="${action}" commandName="directLoginCommand" method="POST">
			<c:if test="${!empty authnErrorMessage}">
				<span style="color:red"><c:out value="${authnErrorMessage}"/></span>
			</c:if>
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
						<form:select path="idpUrl" id="${prefix}idpSelect">
							<form:options items="${idpUrls}" itemValue="url" itemLabel="label"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td></td>
					<td style="padding-top:10px;">
						<input alt="Log In" type="submit" value="Log In"/>
					</td>
				</tr>
			</table>
			<input type="hidden" name="portalAuthnUrl" value="<c:out value="${portalAuthnUrl}"/>"/>
		</form:form>


	</c:when>
	<c:otherwise>
	<a href="/web/guest/home" style="text-decoration:none;">&lt;&lt; To Full Page</a><br/><br/>
<%@ include file="/WEB-INF/jsp/directauthn/greeting.jspf"%>
	</c:otherwise>
</c:choose>
