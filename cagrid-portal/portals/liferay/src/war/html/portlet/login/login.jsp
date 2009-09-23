<%@ include file="/html/portlet/login/init.jsp" %>

<script type='text/javascript'
	src='/cagridportlets/dwr/interface/CredentialManagerFacade.js'></script>
<script type='text/javascript' src='/cagridportlets/dwr/engine.js'></script>
<script type='text/javascript' src='/cagridportlets/dwr/util.js'></script>

<c:choose>
	<c:when test="<%= themeDisplay.isSignedIn() %>">
		<%
		String signedInAs = user.getFullName();
		if (themeDisplay.isShowMyAccountIcon()) {
			signedInAs = "<a href=\"" + HtmlUtil.escape(themeDisplay.getURLMyAccount().toString()) + "\">" + signedInAs + "</a>";
		}
		%>
		<%= LanguageUtil.format(pageContext, "you-are-signed-in-as-x", signedInAs) %>

		<br/>
		Your grid identity is: <div id="<portal:namespace/>gridIdentity"></div>


		<%
		String cgpPortalUserId = user.getCompanyId() + ":" + user.getUserId();
		%>
				
		<script type="text/javascript">
		
		function <portlet:namespace/>getGridIdentity(userId){
			CredentialManagerFacade.getGridIdentity(userId,
			{
				callback: function(gridIdentity){
					jQuery("#<portlet:namespace/>gridIdentity").html(gridIdentity);
				},
				errorHandler: function(errorString, exception){
					alert("Error authenticating: " + errorString);
				}
			});
		}
		
		jQuery(document).ready(
			<portlet:namespace/>getGridIdentity("<%= cgpPortalUserId %>");
		);
		</script>		
		
	</c:when>
	<c:otherwise>
		<%
		String redirect = ParamUtil.getString(renderRequest, "redirect");
		String login = LoginUtil.getLogin(request, "login", company);
		String password = StringPool.BLANK;
		boolean rememberMe = ParamUtil.getBoolean(request, "rememberMe");
		if (Validator.isNull(authType)) {
			authType = company.getAuthType();
		}
		%>

		<portlet:actionURL var="loginFormActionUrl" 
			secure="<%= PropsValues.COMPANY_SECURITY_AUTH_REQUIRES_HTTPS || request.isSecure() %>">
			<portlet:param name="saveLastPath" value="0" />
			<portlet:param name="struts_action" value="/login/login" />
		</portlet:actionURL>		
		<form action="${loginFormActionUrl}" 
			class="uni-form" 
			method="post" name="<portlet:namespace />fm">
			
		<input name="<portlet:namespace />redirect" type="hidden" value="<%= HtmlUtil.escape(redirect) %>" />
		<input id="<portlet:namespace />rememberMe" name="<portlet:namespace />rememberMe" type="hidden" value="<%= rememberMe %>" />

		<c:if test='<%= SessionMessages.contains(request, "user_added") %>'>
			<%
			String userEmailAddress = (String)SessionMessages.get(request, "user_added");
			String userPassword = (String)SessionMessages.get(request, "user_added_password");
			%>

			<span class="portlet-msg-success">
				<c:choose>
					<c:when test="<%= company.isStrangersVerify() || Validator.isNull(userPassword) %>">
						<%= LanguageUtil.format(pageContext, "thank-you-for-creating-an-account-your-password-has-been-sent-to-x", userEmailAddress) %>
					</c:when>
					<c:otherwise>
						<%= LanguageUtil.format(pageContext, "thank-you-for-creating-an-account-your-password-is-x", new Object[] {userPassword, userEmailAddress}) %>
					</c:otherwise>
				</c:choose>
			</span>

		</c:if>

		<liferay-ui:error exception="<%= AuthException.class %>" message="authentication-failed" />
		<liferay-ui:error exception="<%= CookieNotSupportedException.class %>" message="authentication-failed-please-enable-browser-cookies" />
		<liferay-ui:error exception="<%= NoSuchUserException.class %>" message="please-enter-a-valid-login" />
		<liferay-ui:error exception="<%= PasswordExpiredException.class %>" message="your-password-has-expired" />
		<liferay-ui:error exception="<%= UserEmailAddressException.class %>" message="please-enter-a-valid-login" />
		<liferay-ui:error exception="<%= UserLockoutException.class %>" message="this-account-has-been-locked" />
		<liferay-ui:error exception="<%= UserPasswordException.class %>" message="please-enter-a-valid-password" />
		<liferay-ui:error exception="<%= UserScreenNameException.class %>" message="please-enter-a-valid-screen-name" />

		<fieldset class="block-labels">
			<div class="ctrl-holder">
				<%
				String loginLabel = null;
				if (authType.equals(CompanyConstants.AUTH_TYPE_EA)) {
					loginLabel = "email-address";
				} else if (authType.equals(CompanyConstants.AUTH_TYPE_SN)) {
					loginLabel = "screen-name";
				} else if (authType.equals(CompanyConstants.AUTH_TYPE_ID)) {
					loginLabel = "id";
				}
				%>

				<%--
				<label for="<portlet:namespace />login"><liferay-ui:message key="<%= loginLabel %>" /></label>
				--%>
				<label for="<portlet:namespace />login">Username</label>
				<input name="<portlet:namespace />login" type="text" value="<%= HtmlUtil.escape(login) %>" />
			</div>

			<div class="ctrl-holder">
				<label for="<portlet:namespace />password"><liferay-ui:message key="password" /></label>
				<input id="<portlet:namespace />password" name="<portlet:namespace />password" type="password" value="<%= password %>" />
				<span id="<portlet:namespace />passwordCapsLockSpan" style="display: none;"><liferay-ui:message key="caps-lock-is-on" /></span>
			</div>
			
			<div class="ctrl-holder">
				<label for="identityProvider">Identity Provider</label>
				<select name="identityProvider" id="<portlet:namespace/>identityProvider">
					<option>Loading...</option>
				</select>
			</div>

<%--
			<c:if test="<%= company.isAutoLogin() && !PropsValues.SESSION_DISABLED %>">
				<div class="ctrl-holder inline-label">
					<label for="<portlet:namespace />rememberMeCheckbox"><liferay-ui:message key="remember-me" /></label>
					<input <%= rememberMe ? "checked" : "" %> id="<portlet:namespace />rememberMeCheckbox" type="checkbox" />
				</div>
			</c:if>
--%>
			<div class="button-holder">
				<input type="submit" value="<liferay-ui:message key="sign-in" />" />
			</div>
		</fieldset>
		</form>

<%--
		<%@ include file="/html/portlet/login/navigation.jspf" %>
		--%>

		<script type="text/javascript">
			jQuery(
				function() {
					jQuery('#<portlet:namespace />password').keypress(
						function(event) {
							Liferay.Util.showCapsLock(event, '<portlet:namespace />passwordCapsLockSpan');
						}
					);

					jQuery('#<portlet:namespace />rememberMeCheckbox').click(
						function() {
							var checked = 'off';
							if (this.checked) {
								checked = 'on';
							}
							jQuery('#<portlet:namespace />rememberMe').val(checked);
						}
					);
				}
			);
			
			function <portlet:namespace/>listIdPs(){
				CredentialManagerFacade.listIdPsFromDorian()(
				{
					callback: function(idps){
						var idpOpts = "";
						for(var i = 0; i < idps.length; i++){
							var idpBean = idps[i];
							idpOpts += "<option value='" + idpBean.url + "'" + (i == 0 ? " selected" : "") + ">" + idpBean.label + "</option>";
						}
						jQuery("#<portlet:namespace/>identityProvider").html(idpOpts);						
					},
					errorHandler: function(errorString, exception){
						alert("Error listing identity providers: " + errorString);
					}
				});				
			}
			
			jQuery(document).ready(
				<portlet:namespace/>listIdPs();
			);

			<c:if test="<%= windowState.equals(WindowState.MAXIMIZED) %>">
				Liferay.Util.focusFormField(document.<portlet:namespace />fm.<portlet:namespace />login);
			</c:if>
		</script>
	</c:otherwise>

</c:choose>