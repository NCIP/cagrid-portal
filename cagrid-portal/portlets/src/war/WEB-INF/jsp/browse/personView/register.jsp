<%@ include file="/WEB-INF/jsp/include/servlet_includes.jsp" %>

<link rel="stylesheet" type="text/css" href="<c:url value="/js/yui/button/assets/skins/sam/button.css"/>" />
<link rel="stylesheet" type="text/css" href="<c:url value="/js/yui/container/assets/skins/sam/container.css"/>" />
<link rel="stylesheet" type="text/css" href="<c:url value="/js/yui/menu/assets/skins/sam/menu.css"/>" />
<link type="text/css" rel="stylesheet" href="<c:url value="/js/yui/autocomplete/assets/skins/sam/autocomplete.css"/>">

<script type="text/javascript" src="<c:url value="/js/yui/yahoo-dom-event/yahoo-dom-event.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/element/element-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/container/container-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/button/button-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/logger/logger-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/menu/menu-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/scriptaculous/prototype.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/ajaxform.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/state_country_lists.js"/>"></script>

<script type="text/javascript"
	src="<c:url value="/dwr/interface/RegistrationManager.js"/>"></script>
<script type="text/javascript" src="<c:url value="/dwr/engine.js"/>"></script>
<script type="text/javascript" src="<c:url value="/dwr/util.js"/>"></script>

<c:set var="ns" value="${param.ns}"/>

<style type="text/css">
div.row div.label {
	display:block;
	float:left;
	font-weight:bold;
	width:90px;
	margin-right:10px;
	text-align:right;	
}
div.row div.value {
	margin-left: 100px;
}

div.row label {
	width: 90px;
}
div.row label:after {
	margin-left: 100px;
}
</style>
<div align="left" style="overflow:auto; height:800px;padding-bottom:10px;" class="yui-skin-sam">

	<form name="editForm">
		<div class="row">
			<div class="label">First Name</div>
			<div class="value"><input type="text" name="firstName" class="autoSet required"/></div>
		</div>
		<div class="row">
			<label for="lastName">Last Name</label>
			<input type="text" name="lastName" class="autoSet required"/>
		</div>
		<div class="row">
			<label for="username">User Name</label>
			<input type="text" name="username" class="autoSet required"/>
		</div>
		<div class="row">
			<label for="password">Password</label>
			<input type="password" name="password" class="autoSet required"/>
		</div>
		<div class="row">
			<label for="email">email</label>
			<input type="text" name="email" class="autoSet required"/>
		</div>
		<div class="row">
			<label for="phone">Phone</label>
			<input type="text" name="phone" class="autoSet required"/>
		</div>
		<div class="row">
			<label for="organization">Organization</label>
			<input type="text" name="organization" class="autoSet required"/>
		</div>
		<div class="row">
			<label for="street1">Street 1</label>
			<input type="text" name="street1" class="autoSet required"/>
		</div>
		<div class="row">
			<label for="street2">Street 2</label>
			<input type="text" name="street2" class="autoSet required"/>
		</div>
		<div class="row">
			<label for="locality">City/Locality</label>
			<input type="text" name="locality" class="autoSet required"/>
		</div>
		<div class="row">
			<label for="stateProvince">State/Province</label>
			<div class="value">
				<select name="stateProvince" id="stateProvince" class="autoSet"></select>
			</div>
		</div>
		<div class="row">
			<label for="postalCode">Postal Code</label>
			<input type="text" name="postalCode" class="autoSet required"/>
		</div>
		<div class="row">
			<label for="country">Country</label>
			<div class="value">
				<select name="country" id="country" class="autoSet required"></select>
			</div>
		</div>
	</form>

	<div class="flow-buttons">
		<span id="cancelButtonContainer"></span>
		<span id="submitButtonContainer"></span>
	</div>
<div>

<script type="text/javascript">

var RegisterForm = Class.create(CGP_BaseAjaxForm, {
	
	handleSubmitSuccess: function(response){
		alert(response);
		Liferay.Popup.close(${ns}registerDialog);
	},

	handleSubmitError: function(errorString, exception){
		if(errorString == null){
			alert("Error registering.");
		}else{
			var errorMatch = "Error: Invalid User Property: ";
			var uPwdErr = "Unacceptable password, ";
			var iPwdErr = "Invalid password, ";
			var emailErr = "Invalid email";
			var uUsrErr = "Unacceptable User ID, ";
			var eUsrErr = "The user";
			
			var idx = errorString.indexOf(errorMatch);
			if(idx > -1){
				var msg = errorString.substring(idx + errorMatch.length);
				if(msg.startsWith(uPwdErr)){
					this.setInputMessage("password", msg.substring(uPwdErr.length));
					
				}else if(msg.startsWith(iPwdErr)){
					this.setInputMessage("password", msg.substring(iPwdErr.length));
					
				}else if(msg.startsWith(emailErr)){
					this.setInputMessage("email", msg);
					
				}else if(msg.startsWith(uUsrErr)){
					this.setInputMessage("username", msg.substring(uUsrErr.length));
					
				}else if(msg.startsWith(eUsrErr)){
					this.setInputMessage("username", msg);
					
				}else{
					alert(msg);
				}
			}
		}
	},
	
	cancel: function(){
		Liferay.Popup.close(${ns}registerDialog);
	}
});

jQuery(document).ready(function(){
	var myForm = new RegisterForm("", "editForm", "RegistrationManager");
	myForm.submitButtonLabel = "Register";
	myForm.render();
	var myStates = new CGP_StateCountryLists("stateProvince", "country");
	myStates.render();
});

</script>
