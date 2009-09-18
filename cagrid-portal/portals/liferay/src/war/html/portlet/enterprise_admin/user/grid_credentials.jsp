<%@ page import="com.liferay.portal.model.*" %>
<%@ page import="com.liferay.portal.theme.ThemeDisplay" %>
<%@ page import="com.liferay.portal.service.permission.PortletPermissionUtil" %>
<%@ page import="com.liferay.portal.util.WebKeys" %>
<%@ page import="com.liferay.portal.util.PortletKeys" %>
<%
User selUser = (User)request.getAttribute("user.selUser");
String userId = selUser.getCompanyId() + ":" + selUser.getUserId();
%>

<script type='text/javascript'
	src='/cagridportlets/dwr/interface/CredentialManagerFacade.js'></script>
<script type='text/javascript' src='/cagridportlets/dwr/engine.js'></script>
<script type='text/javascript' src='/cagridportlets/dwr/util.js'></script>
<h1>Credential Mgr</h1>
<p/>
Manager
<p/>
<div id="cagrid_credsDiv">
Loading...
</div>
<br/>
<div id="cagrid_idPFormDiv">
Add Credential:<br/>
<form>
Username: <input type="text" name="cagrid_idpUsername"/><br/>
Password: <input type="password" name="cagrid_idpPassword"/><br/>
Identity Providers:<br/>
<div id="cagrid_idpsDiv">
Loading...
</div>
</form><br/>
<span id="cagrid_addCredential">Add</span> | <span id="cagrid_deleteCredential">Delete</span> | <span id="cagrid_setDefault">Make Default</span>
</div><br/>
<div id="cagrid_message">

</div>
<% 
ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute (WebKeys.THEME_DISPLAY);
%>
<% if(PortletPermissionUtil.contains(themeDisplay.getPermissionChecker(), themeDisplay.getLayout().getPlid(), PortletKeys.ENTERPRISE_ADMIN, "ADD_IDP")){ %>

Add/Remove Identity Providers:<br/>
<form>
<div id="cagrid_editIdpsDiv">
Loading...
</div>
New Identity Provider:<br/>
Label: <input type="text" name="cagrid_idpLabel"/><br/>
URL: <input type="text" name="cagrid_idpUrl"/><br/>
</form><br/>
<span id="cagrid_addIdp">Add</span> | <span id="cagrid_deleteIdp">Delete</span>
<% } %>

<script type="text/javascript">

function listCredentials(){
    CredentialManagerFacade.listCredentials('<%=userId%>',
    {
    	callback:function(creds){
    		credentials = creds;
    		if(credentials.length == 0){
				jQuery('#cagrid_credsDiv').html('No credentials');
			}else{
				jQuery('#cagrid_credsDiv').html('<select size="3"></select>');
				for(var i = 0; i < credentials.length; i++){
					if(credentials[i].defaultCredential){
						jQuery('#cagrid_credsDiv > select').append('<option value="' + credentials[i].identity + '"><b>' + credentials[i].identity + '</b></option>');
					}else{	
						jQuery('#cagrid_credsDiv > select').append('<option value="' + credentials[i].identity + '">' + credentials[i].identity + '</option>');
					}	
				}
			}
    	},
    	errorHandler:function(errorString, exception){
    		alert("Error getting credentials: " + errorString);
    	}
    });
}

var credentials = new Array();

function createIdpList(divId, idps, size){
	if(idps.length == 0){
		jQuery('#' + divId).html('No identity providers');
	}else{
		jQuery('#' + divId).html('<select name="' + divId + '" size="' + size + '"></select>');
		for(var i = 0; i < idps.length; i++){
			if(i == 0){
				jQuery('#' + divId + ' > select').append('<option selected value=\"' + idps[i].url + '\">' + idps[i].label + '</option>');
			}else{
				jQuery('#' + divId + ' > select').append('<option value=\"' + idps[i].url + '\">' + idps[i].label + '</option>');
			}	
		}
	}
}

function listIdPs(){
    CredentialManagerFacade.listIdPs(
    {
    	callback:function(idps){
    		createIdpList('cagrid_idpsDiv', idps, 1);
    		createIdpList('cagrid_editIdpsDiv', idps, 3);
    	},
    	errorHandler:function(errorString, exception){
    		alert("Error getting idps: " + errorString);
    	}
    });
}

jQuery(document).ready(function(){

	listCredentials();
	listIdPs();	

    jQuery('#cagrid_addCredential').bind('click', function(evt){
    	
    	if(jQuery('#cagrid_idpsDiv > select > option:selected').length == 0){
    		alert('Please selected and Identity Provider');
    	}else{
    		var username = jQuery("input[name='cagrid_idpUsername']").val();
    		var password = jQuery("input[name='cagrid_idpPassword']").val();
    		var url = jQuery('#cagrid_idpsDiv > select > option:selected').attr("value");
    		CredentialManagerFacade.login('<%=userId%>', username, password, url, '4', '0',
    		{
    			callback:function(message){
    				alert(message);
    				jQuery('#cagrid_message').html(message);
    				listCredentials();
    			},
    			errorHandler:function(errorString, exception){
    				alert("Error authenticating: " + errorString);
    			}
    		});
    	}
    });
    jQuery('#cagrid_deleteCredential').bind('click', function(evt){
    
    	if(jQuery('#cagrid_credsDiv > select > option:selected').length == 0){
    		alert('Please selected a credential to delete.');
    	}else{
    		var identity = jQuery('#cagrid_credsDiv > select > option:selected').attr("value");
    		CredentialManagerFacade.deleteCredential('<%=userId%>', identity,
    		{
    			callback:function(message){
    				alert(message);
    				jQuery('#cagrid_message').html(message);
    				listCredentials();
    			},
    			errorHandler:function(errorString, exception){
    				alert("Error deleting credential: " + errorString);
    			}
    		});
    	}
    });
    jQuery('#cagrid_setDefault').bind('click', function(evt){
    
    	if(jQuery('#cagrid_credsDiv > select > option:selected').length == 0){
    		alert('Please selected a credential to set as default.');
    	}else{
    		var identity = jQuery('#cagrid_credsDiv > select > option:selected').attr("value");
    		CredentialManagerFacade.setDefaultCredential('<%=userId%>', identity,
    		{
    			callback:function(message){
    				alert(message);
    				jQuery('#cagrid_message').html(message);
    				listCredentials();
    			},
    			errorHandler:function(errorString, exception){
    				alert("Error setting default credential: " + errorString);
    			}
    		});
    	}
    });
    
    
    jQuery('#cagrid_addIdp').bind('click', function(evt){
    	var idpLabel = jQuery("input[name='cagrid_idpLabel']").val();
    	var idpUrl = jQuery("input[name='cagrid_idpUrl']").val();
    	if(idpLabel == ''){
    		alert('Please enter a label for the identity provider.');
    	}else if(idpUrl == ''){
    		alert('Please enter a URL for the identity provider.');
    	}else{
    		CredentialManagerFacade.addIdP(idpLabel, idpUrl,
    		{
    			callback:function(message){
    				jQuery('#cagrid_idpMessage').html(message);
    				listIdPs();
    			},
    			errorHandler:function(errorString, exception){
    				alert("Error adding identity provider: " + errorString);
    			}
    		});
    	}
    });
    jQuery('#cagrid_deleteIdp').bind('click', function(evt){
    
    	if(jQuery('#cagrid_editIdpsDiv > select > option:selected').length == 0){
    		alert('Please selected an identity provider to delete.');
    	}else{
    		var url = jQuery('#cagrid_editIdpsDiv > select > option:selected').attr("value");
    		CredentialManagerFacade.deleteIdP(url,
    		{
    			callback:function(message){
    				alert(message);
    				jQuery('#cagrid_idpMessage').html(message);
    				listIdPs();
    			},
    			errorHandler:function(errorString, exception){
    				alert("Error deleting identity provider: " + errorString);
    			}
    		});
    	}
    });    
    
});

</script>
