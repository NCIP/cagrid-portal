<%@ include file="/WEB-INF/jsp/include/includes.jspf"%>
<tags:yui-minimum/>
<link rel="stylesheet" type="text/css" href="<c:url value="/js/yui/container/assets/skins/sam/container.css"/>" />

<script type="text/javascript" src="<c:url value="/js/yui/container/container-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/scriptaculous/prototype.js"/>"></script>

<script type="text/javascript" src="<c:url value="/js/state_country_lists-min.js"/>"></script>

<c:set var="ns"><portlet:namespace/></c:set>

<span id="${ns}profileTipContainer" class="yui-skin-sam"></span>
<span id="${ns}loginTipContainer" class="yui-skin-sam"></span>
<span id="${ns}logoutTipContainer" class="yui-skin-sam"></span>
<span id="${ns}registerTipContainer" class="yui-skin-sam"></span>
<div id="logIn">
<c:choose>
	<c:when test="${empty portalUser}">
                <tags:login useHrefRedirect="false" loginLinkText="Log In"
                            notLoggedInText="&nbsp;|&nbsp;" id="loginLink" />
		<a href="javascript:${ns}showRegisterDialog();" id="${ns}registerLink" style="text-decoration:none"><b>Register</b></a>
	</c:when>
	<c:otherwise>
        Hello
        <a id="${ns}catalogLink" href="/web/guest/catalog/people?p_p_id=BrowsePortlet_WAR_cagridportlets&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_count=1&_BrowsePortlet_WAR_cagridportlets_operation=viewProfile"
                style="text-decoration:none">
            <b><c:out value="${portalUser.person.firstName}"/> <c:out value="${portalUser.person.lastName}"/></b></a>&nbsp;&nbsp;|&nbsp;
		<portlet:actionURL var="logoutActionUrl">
			<portlet:param name="operation" value="logout"/>
		</portlet:actionURL>
		<a href="<c:out value="${logoutActionUrl}"/>" id="${ns}logoutLink" style="text-decoration:none"><b>Log Out</b></a>
	</c:otherwise>
</c:choose>

</div>



<script type="text/javascript">


var ${ns}registerDialog;

function ${ns}showRegisterDialog(){
	${ns}registerDialog =
			new Liferay.Popup({title: "Register", modal:true, width:800 , height:500});
	jQuery(
		${ns}registerDialog
	).load('<c:url value="/browse/personView/register.html"><c:param name="ns" value="${ns}"/></c:url>', {userGuideUrl:"${usersGuideUrl}"});
}

jQuery(document).ready(function(){

     ${ns}catalogTip = new YAHOO.widget.Tooltip("catalogTip", {
            context:"${ns}catalogLink",
            text:"View and Edit the details of your Portal profile",
            container:"${ns}profileTipContainer",
            showDelay:200 });

     ${ns}logoutTip = new YAHOO.widget.Tooltip("logoutTip", {
            context:"${ns}logoutLink",
            text:"Log out from the Portal",
            container:"${ns}logoutTipContainer",
            showDelay:200 });

     ${ns}loginTip = new YAHOO.widget.Tooltip("loginTip", {
            context:"loginLink",
            text:"Log in to the Portal. <br>This will let you use <br>your Grid credentials to <br>invoke secured Data Services<br> and create/edit content <br>in the Portal",
            container:"${ns}loginTipContainer",
            showDelay:200 });

    ${ns}registerTip = new YAHOO.widget.Tooltip("registerTip", {
            context:"${ns}registerLink",
            text:"Register for a caGrid Dorian Account.<br> You can also use this account <br>to login to the Portal",
             container:"${ns}registerTipContainer",
            showDelay:200,
            width:50});


	var myStates = new CGP_StateCountryLists("${ns}stateProvince", "${ns}countryCode");
	myStates.render();

	 
});


</script>