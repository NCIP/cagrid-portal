<%@ include file="/WEB-INF/jsp/include/includes.jspf"%>
<c:choose>
	<c:when test="${empty portalUser}">
		<portlet:renderURL var="portalAuthnUrl"/>
		<portlet:actionURL var="action">
			<portlet:param name="operation" value="login"/>
		</portlet:actionURL>
		<form:form action="${action}" commandName="directLoginCommand" method="POST">
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
						<form:select path="idpUrl">
							<form:options items="${idpUrls}" itemValue="url" itemLabel="label"/>
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


	</c:when>
	<c:otherwise>
	<a href="/web/guest/home" style="text-decoration:none;">&lt;&lt; To Full Page</a><br/><br/>
<%@ include file="/WEB-INF/jsp/directauthn/greeting.jspf"%>
	</c:otherwise>
</c:choose>
