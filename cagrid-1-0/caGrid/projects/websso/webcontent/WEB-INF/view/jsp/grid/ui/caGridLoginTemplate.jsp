<jsp:include page="includes/top.jsp"></jsp:include>
<tr>
	<td height="100%"><!-- target of anchor to skip menus -->
	<a name="content" />

	<table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
		<tr>
			<td width="70%"><!-- welcome begins -->
			<table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
				<tr>
					<td class="welcomeTitle" height="20">WELCOME TO SITE NAME</td>
				</tr>
				<tr>
					<td class="welcomeContent" valign="top">The WebSSO framework
					provides a comprehensive,Single Sign On solution for web
					application using caGrids GAARDS Framework. Users may navigate
					between participating applications without being challenged to
					provide login credentials. Likewise, WebSSO establishes a grid
					session for the user, allowing them access to other grid services
					unchallenged.</td>
				</tr>
			</table>
			<!-- welcome ends --></td>
			<td valign="top" width="30%"><!-- sidebar begins -->
			<table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">

				<!-- login begins -->
				<jsp:include page="casLoginView.jsp" />
				<!-- login ends -->

				<!-- what's new begins -->
				<tr>
					<td valign="top">
					<table summary="" cellpadding="0" cellspacing="0" border="0"
						width="100%" class="sidebarSection">
						<tr>
							<td class="sidebarTitle" height="20">WHAT'S NEW</td>
						</tr>
						<tr>
							<td class="sidebarContent">What's new message. What's new
							message. What's new message. What's new message. What's new
							message.</td>
						</tr>
					</table>
					</td>
				</tr>
				<!-- what's new ends -->

				<!-- did you know? begins -->
				<tr>
					<td valign="top">
					<table summary="" cellpadding="0" cellspacing="0" border="0"
						width="100%" height="100%" class="sidebarSection">
						<tr>
							<td class="sidebarTitle" height="20">DID YOU KNOW?</td>
						</tr>
						<tr>
							<td class="sidebarContent" valign="top">Did you know
							message. Did you know message. Did you know message. Did you know
							message.</td>
						</tr>
					</table>
					</td>
				</tr>
				<!-- did you know? ends -->

				<!-- spacer cell begins (keep for dynamic expanding) -->
				<tr>
					<td valign="top" height="100%">
					<table summary="" cellpadding="0" cellspacing="0" border="0"
						width="100%" height="100%" class="sidebarSection">
						<tr>
							<td class="sidebarContent" valign="top">&nbsp;</td>
						</tr>
					</table>
					</td>
				</tr>
				<!-- spacer cell ends -->

			</table>
			<!-- sidebar ends --></td>
		</tr>
	</table></td>
</tr>
</table>
</td>
</tr>
<!--_____ main content ends _____-->
<jsp:include page="includes/bottom.jsp"></jsp:include>