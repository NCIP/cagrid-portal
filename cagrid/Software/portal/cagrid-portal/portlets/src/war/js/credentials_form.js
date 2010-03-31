var CGP_CredentialsForm = Class.create({
	initialize: function(prefix, mgrName, userId, statusImageUrl){
		
		this.mgrName = mgrName;
		this.statusImageUrl = statusImageUrl;
		
		this.credentialsTableContainerId = prefix + "credentialsTableContainer";
		
		this.addButtonLabel = "Add";
		this.addButtonId = prefix + "addButton";
		this.addButtonContainerId = this.addButtonId + "Container";
		
		this.deleteButtonLabel = "Delete";
		this.deleteButtonId = prefix + "deleteButton";
		this.deleteButtonContainerId = this.deleteButtonId + "Container";
		
		this.refreshButtonLabel = "Refresh";
		this.refreshButtonId = prefix + "refreshButton";
		this.refreshButtonContainerId = this.refreshButtonId + "Container";
		
		this.setDefaultButtonLabel = "Set Default";
		this.setDefaultButtonId = prefix + "setDefaultButton";
		this.setDefaultButtonContainerId = this.setDefaultButtonId + "Container";
		
		this.authnDialogContainerId = prefix + "authnDialogContainer";
		
		this.authnButtonLabel = "Authenticate";
		this.authnButtonId = prefix + "authnButton";
		this.authnButtonContainerId = this.authnButtonId + "Container";
		
		this.cancelAuthnButtonLabel = "Cancel";
		this.cancelAuthnButtonId = prefix + "cancelAuthnButton";
		this.cancelAuthnButtonContainerId = this.cancelAuthnButtonId + "Container";
		
		this.authnErrorMessageContainerId = prefix + "authnErrorMessageContainer";
		this.authnSuccessMessageContainerId = prefix + "authnSuccessMessageContainer";
		this.authnStatusContainerId = prefix + "authnStatusContainer";
		this.updateStatusContainerId = prefix + "updateStatusContainer";
		
		this.identityProviderFieldId = prefix + "identityProvider";
		this.usernameFieldId = prefix + "username";
		this.passwordFieldId = prefix + "password";
		this.userId = userId;
		
		this.addDialog = null;
	},
	render: function(){
		var thisObj = this;
		
		this.mgr = eval(this.mgrName);
		
		//Set up Buttons
		this.authnButton = new YAHOO.widget.Button({
			label: this.authnButtonLabel,
			id: this.authnButtonId,
			container: this.authnButtonContainerId
		});
	
		this.authnButton.on("click", function(evt){
			thisObj.authenticate();
		});
		
		this.cancelAuthnButton = new YAHOO.widget.Button({
			label: this.cancelAuthnButtonLabel,
			id: this.cancelAuthnButtonId,
			container: this.cancelAuthnButtonContainerId
		});
	
		this.cancelAuthnButton.on("click", function(evt){
			thisObj.cancelAuthn();
		});
		
		
		this.addButton = new YAHOO.widget.Button({
			label: this.addButtonLabel,
			id: this.addButtonId,
			container: this.addButtonContainerId
		});
	
		this.addButton.on("click", function(evt){
			thisObj.add();
		});
		
		this.deleteButton = new YAHOO.widget.Button({
			label: this.deleteButtonLabel,
			id: this.deleteButtonId,
			container: this.deleteButtonContainerId
		});
		this.deleteButton.set("disabled", true);
		this.deleteButton.on("click", function(evt){
			thisObj.deleteIt();
		});
		
		this.refreshButton = new YAHOO.widget.Button({
			label: this.refreshButtonLabel,
			id: this.refreshButtonId,
			container: this.refreshButtonContainerId
		});
		this.refreshButton.set("disabled", true);
		this.refreshButton.on("click", function(evt){
			thisObj.refresh();
		});
		
		this.setDefaultButton = new YAHOO.widget.Button({
			label: this.setDefaultButtonLabel,
			id: this.setDefaultButtonId,
			container: this.setDefaultButtonContainerId
		});
		this.setDefaultButton.set("disabled", true);
		this.setDefaultButton.on("click", function(evt){
			thisObj.setDefault();
		});
		
		//Set up the table
		this.colDefs = [
			{key: "idp", label: "Identity Provider", resizable: true, sortable: true},
			{key: "defaults", label: "Default?", resizable: true, sortable: true},
			{key: "validUntil", label: "Valid Until", resizable: true, sortable: true},
			{key: "ident", label: "Grid Identity", resizable: true, sortable: true}
		];
		
		this.dataSource = new YAHOO.util.DataSource([]);
		this.dataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
		this.dataSource.responseSchema = {
			fields: ["idp", "defaults", "idpUrl", "validUtil", "ident"]
		};
		this.dataTable = 
			new YAHOO.widget.DataTable(this.credentialsTableContainerId, this.colDefs, this.dataSource, {
				selectionMode: "single"
			});
		this.dataTable.subscribe("rowMouseoverEvent", this.dataTable.onEventHighlightRow);
		this.dataTable.subscribe("rowMouseoutEvent", this.dataTable.onEventUnhighlightRow);
		this.dataTable.subscribe("rowClickEvent", this.handleSelectRow, this);
		
		
		jQuery("#" + this.authnDialogContainerId).hide("normal");
		
		this.populateCredentials();
		this.populateIdps();
		
	},
	populateIdps: function(){
		var thisObj = this;
		this.mgr.listIdPsFromDorian(
		{
			callback: function(idps){
				thisObj.listIdPsSuccessHandler(idps);
			},
			errorHandler: function(errorString, exception){
				thisObj.listIdPsErrorHandler(errorString, exception);
			}
		});
	},
	populateCredentials: function(){
		var thisObj = this;
		this.mgr.listCredentials(this.userId,
		{
			callback: function(creds){
				thisObj.populateCredentialsSuccessHandler(creds);
			},
			errorHandler: function(errorString, exception){
				thisObj.populateCredentialsErrorHandler(errorString, exception);
			}
		});
	},
	add: function(){
		jQuery("#" + this.authnDialogContainerId).show("normal");
		this.addButton.set("disabled", true);
	},
	authenticate: function(){
	
		var selectedIdpEl = jQuery("#" + this.identityProviderFieldId + " option:selected");
		
    	if(selectedIdpEl.length == 0){
    		alert('Please selected and Identity Provider');
    	}else{
    		jQuery("#" + this.authnStatusContainerId).html("<img src='" + this.statusImageUrl + "'/>");
    	
    		var thisObj = this;
    		var username = jQuery("#" + this.usernameFieldId).val();
    		var password = jQuery("#" + this.passwordFieldId).val();
    		
    		
    		var url = jQuery(selectedIdpEl).val();

    		CredentialManagerFacade.authenticate(this.userId, username, password, url, '4', '0',
    		{
    			callback:function(credBean){
					thisObj.authenticateSuccessHandler(credBean);
    			},
    			errorHandler:function(errorString, exception){
    				thisObj.authenticateErrorHandler(errorString, exception);
    			}
    		});
    	}		
	},
	cancelAuthn: function(){
		this.addButton.set("disabled", false);
		jQuery("#" + this.authnDialogContainerId).hide("normal");
	},
	deleteIt: function(){
		var selectedRows = this.dataTable.getSelectedRows();
		var record = this.dataTable.getRecord(selectedRows[0]);
		var recordId = record.getId();
		var identity = record.getData("ident");
		
		if(confirm("Are you sure you want to delete credential '" + identity + "'?")){
					
			jQuery("#" + this.updateStatusContainerId).html("<img src='" + this.statusImageUrl + "'/>");
			
			var thisObj = this;
			this.mgr.deleteCredential(this.userId, identity,
			{
				callback: function(){
					thisObj.deleteCredentialSuccessHandler(recordId, identity);	
				},
				errorHandler: function(errorString, exception){
					thisObj.deleteCredentialErrorHandler(errorString, exception);
				}
			});
		}
	},
	refresh: function(){
	
		jQuery("#" + this.updateStatusContainerId).html("<img src='" + this.statusImageUrl + "'/>");
	
		var selectedRows = this.dataTable.getSelectedRows();
		var record = this.dataTable.getRecord(selectedRows[0]);
		var recordId = record.getId();
		var identity = record.getData("ident");
				
		var thisObj = this;
		this.mgr.refresh(this.userId, identity,
		{
			callback: function(credBean){
				thisObj.refreshSuccessHandler(recordId, credBean);	
			},
			errorHandler: function(errorString, exception){
				thisObj.refreshErrorHandler(errorString, exception);
			}
		});	
	},
	setDefault: function(){
		
		jQuery("#" + this.updateStatusContainerId).html("<img src='" + this.statusImageUrl + "'/>");
	
		var selectedRows = this.dataTable.getSelectedRows();
		var record = this.dataTable.getRecord(selectedRows[0]);
		var recordId = record.getId();
		var identity = record.getData("ident");
				
		var thisObj = this;
		this.mgr.setDefaultCredential(this.userId, identity,
		{
			callback: function(){
				thisObj.setDefaultCredentialSuccessHandler(identity);	
			},
			errorHandler: function(errorString, exception){
				thisObj.setDefaultCredentialErrorHandler(errorString, exception);
			}
		});	
	},
	
	//Private operations
	
	listIdPsSuccessHandler: function(idps){
		var idpOpts = "";
		for(var i = 0; i < idps.length; i++){
			var idpBean = idps[i];
			idpOpts += "<option value='" + idpBean.url + "'" + (i == 0 ? " selected" : "") + ">" + idpBean.label + "</option>";
		}
		jQuery("#" + this.identityProviderFieldId).html(idpOpts);
	},
	listIdPsErrorHandler: function(errorString, exception){
		alert("Error getting IdP list: " + errorString);
	},
	
	authenticateSuccessHandler: function(credBean){
	
		jQuery("#" + this.authnErrorMessageContainerId).html("");
		jQuery("#" + this.authnStatusContainerId).html("")
	
		var rec = {
			idp: credBean.idpBean.label, 
			defaults: credBean.defaultCredential,
			idpUrl: credBean.idpBean.url, 
			validUntil: YAHOO.util.Date.format(credBean.validUntil, {format: "%Y-%m-%d %T"}), 
			ident: credBean.identity
		};

		//See if authentication exists for this identity		
		var recs = this.dataTable.getRecordSet();
		var existingRec = null;
		for(var i = 0; i < recs.getLength(); i++){
			var r = recs.getRecord(i);
			var ident = r.getData("ident");
			if(ident == credBean.identity){
				existingRec = r;
			}
		}

		if(existingRec != null){
			this.dataTable.deleteRow(existingRec.getId());
		}
		this.dataTable.addRow(rec, 0);
		this.updateDefaultCredentialRow(credBean.identity);
		
		jQuery("#" + this.authnSuccessMessageContainerId).html("Authenticated " + credBean.identity).show("normal");
		setTimeout("jQuery('#" + this.authnSuccessMessageContainerId + "').hide('normal').html('');", 3000);
		jQuery("#" + this.authnDialogContainerId).hide("normal");
		this.addButton.set("disabled", false);
		
	},
	authenticateErrorHandler: function(errorString, exception){
		jQuery("#" + this.authnStatusContainerId).html("")
		jQuery("#" + this.authnErrorMessageContainerId).html("Could not authenticate: " + errorString);
	},
	populateCredentialsSuccessHandler: function(creds){
		for(var i = 0; i < creds.length; i++){
			var credBean = creds[i];
			var rec = {
				idp: credBean.idpBean.label, 
				defaults: credBean.defaultCredential,
				idpUrl: credBean.idpBean.url, 
				validUntil: YAHOO.util.Date.format(credBean.validUntil, {format: "%Y-%m-%d %T"}), 
				ident: credBean.identity
			};
			this.dataTable.addRow(rec, 0);
		}	
	},
	populateCredentialsErrorHandler: function(errorString, exception){
		alert("Error retrieving credentials: " + errorString);
	},
	handleSelectRow: function(oArgs, thisObj){
		
		thisObj.dataTable.onEventSelectRow(oArgs);
		thisObj.deleteButton.set("disabled", false);
		thisObj.refreshButton.set("disabled", false);
		thisObj.setDefaultButton.set("disabled", false);
	},
	deleteCredentialSuccessHandler: function(recordId, identity){
		this.dataTable.deleteRow(recordId);
		
		jQuery("#" + this.updateStatusContainerId).html("");
		jQuery("#" + this.authnSuccessMessageContainerId).html("Deleted credential " + identity).show("normal");
		setTimeout("jQuery('#" + this.authnSuccessMessageContainerId + "').hide('normal').html('');", 3000);
		
		this.deleteButton.set("disabled", true);
		this.refreshButton.set("disabled", true);
		this.setDefaultButton.set("disabled", true);
	},
	deleteCredentialErrorHandler: function(errorString, exception){
		jQuery("#" + this.updateStatusContainerId).html("");
		alert("Error deleting credential: " + errorString);
	},
	setDefaultCredentialSuccessHandler: function(identity){
		
		jQuery("#" + this.updateStatusContainerId).html("");
		this.updateDefaultCredentialRow(identity);
		jQuery("#" + this.authnSuccessMessageContainerId).html("Set default credential " + identity).show("normal");;
		setTimeout("jQuery('#" + this.authnSuccessMessageContainerId + "').hide('normal').html('');", 3000);

	},
	setDefaultCredentialErrorHandler: function(errorString, exception){
		jQuery("#" + this.updateStatusContainerId).html("");
		alert("Error deleting credential: " + errorString);
	},
	updateDefaultCredentialRow: function(identity){
		var recs = this.dataTable.getRecordSet();
		for(var i = 0; i < recs.getLength(); i++){
			var r = recs.getRecord(i);
			var ident = r.getData("ident");
			if(ident == identity){
				var newRec = {
					idp: r.getData("idp"),
					defaults: "true",
					idpUrl: r.getData("idpUrl"),
					validUntil: r.getData("validUntil"),
					ident: r.getData("ident")
				};
				this.dataTable.updateRow(r, newRec);
			}else{
				var newRec = {
					idp: r.getData("idp"),
					defaults: "false",
					idpUrl: r.getData("idpUrl"),
					validUntil: r.getData("validUntil"),
					ident: r.getData("ident")
				};
				this.dataTable.updateRow(r, newRec);			
			}
		}	
	},
	refreshSuccessHandler: function(recordId, credBean){
		jQuery("#" + this.updateStatusContainerId).html("");

		var rec = {
			idp: credBean.idpBean.label, 
			defaults: credBean.defaultCredential,
			idpUrl: credBean.idpBean.url, 
			validUntil: YAHOO.util.Date.format(credBean.validUntil, {format: "%Y-%m-%d %T"}), 
			ident: credBean.identity
		};

		this.dataTable.deleteRow(this.dataTable.getRecord(recordId).getId());
		this.dataTable.addRow(rec, 0);
		this.updateDefaultCredentialRow(credBean.identity);
		
		jQuery("#" + this.authnSuccessMessageContainerId).html("Refreshed " + credBean.identity).show("normal");
		setTimeout("jQuery('#" + this.authnSuccessMessageContainerId + "').hide('normal').html('');", 3000);	
	},
	refreshErrorHandler: function(errorString, exception){
		jQuery("#" + this.updateStatusContainerId).html("");
		alert("Error refreshing credential: " + errorString);	
	}
});

