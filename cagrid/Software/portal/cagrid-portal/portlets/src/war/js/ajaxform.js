var CGP_AbstractAjaxForm = Class.create({
	
	initialize: function(prefix, formName){
		
		this.prefix = prefix;
		
		this.formName = this.prefix + formName;
		
		this.submitButtonLabel = "Submit";
		this.submitButtonContainerId = this.prefix + "submitButtonContainer";
		
		this.cancelButtonLabel = "Cancel";
		this.cancelButtonContainerId = this.prefix + "cancelButtonContainer";
		
		this.requiredFieldClass = "required";
		this.autoSetFieldClass = "autoSet";
		
		this.requiredFieldsPatt = "form[name='" + this.formName + "'] :input[class*='" + this.requiredFieldClass + "']"; 
		this.autoSetFieldsPatt = "form[name='" + this.formName + "'] :input[class*='" + this.autoSetFieldClass + "']";
		this.isExcludeIdField = false;
		this.idFieldName = "id";
		this.showCancelButton = true;
		
		this.messageClass = "message";

	},

	render: function(){
	
		this.submitButtonId = this.submitButtonContainerId + "_Button"; 
		this.cancelButtonId = this.cancelButtonContainerId + "_Button"; 
	
		var thisObj = this;
	
		this.submitButton = new YAHOO.widget.Button({
			label: this.submitButtonLabel,
			id: this.submitButtonId,
			container: this.submitButtonContainerId
		});
	
		this.submitButton.on("click", function(evt){
			thisObj.submit();
		});
		
		if(this.showCancelButton){
			this.cancelButton = new YAHOO.widget.Button({
				label: this.cancelButtonLabel,
				id: this.cancelButtonId,
				container: this.cancelButtonContainerId
			});
		
			this.cancelButton.on("click", function(evt){
				thisObj.cancel();
			});
		}

		this.checkEnableSubmit();
		this.bindCheckEnabled();
		this.bindSetField();
		
		
		//alert("(" + this.autoSetFieldsPatt + "), length: " + jQuery(this.autoSetFieldsPatt).length);
		
	},
	
	submit: function(){
		alert("Submit!");
	},
	
	cancel: function(){
		alert("Cancel!");
	},
	
	setField: function(fieldName, fieldValue){
		this.setInputMessage(fieldName, "Set " + fieldName + " with value: " + fieldValue);
	},
	
	bindCheckEnabled: function(){
		var patt = this.requiredFieldsPatt;
		if(this.isExcludeIdField){
			patt = this.requiredFieldsNoIdPatt();
		}
		var thisObj = this;
		jQuery(patt).bind("keyup", function(evt){
			thisObj.checkEnableSubmit();
		});	
	},
	
	bindSetField: function(){
		var patt = this.autoSetFieldsPatt;
		if(this.isExcludeIdField){
			patt = this.autoSetFieldsNoIdPatt();
		}
		var thisObj = this;
		
		jQuery(this.autoSetFieldsPatt).bind("blur", function(evt){
			thisObj.setField(evt.target.name, evt.target.value);
		});	
	},
	
	checkEnableSubmit: function(){

		var emptyCount = 0;
		var patt = this.requiredFieldsPatt;
		if(this.isExcludeIdField){
			patt = requiredFieldsNoIdPatt();
		}
		var requiredFields = jQuery(patt).get(); 
		for(var i = 0; i < requiredFields.length; i++){
			if(jQuery.trim(jQuery(requiredFields[i]).val()) == ""){
				emptyCount++;
			}
		}

		if(emptyCount > 0){
			this.submitButton.set("disabled", true);
		}else{
			this.submitButton.set("disabled", false);
		}		
	},
	
	requiredFieldsNoIdPatt: function(){
		return this.requiredFieldsPath + "[name!='" + this.idFieldName + "']"; 
	},
	
	autoSetFieldsNoIdPatt: function(){
		return this.autoSetFieldsPath + "[name!='" + this.idFieldName + "']"; 
	},
	
	setInputMessage: function(fieldName, message){
		var fieldEl = jQuery("form[name='" + this.formName + "'] :input[name='" + fieldName + "']");
		var messageEl = jQuery(fieldEl).next("." + this.messageClass);
		if(message == null){
			if(messageEl.length > 0){
				jQuery(messageEl).remove();
			}
		}else{
			if(messageEl.length == 0){
				jQuery(fieldEl).after("<span class='" + this.messageClass + "'></span>");
			}
			jQuery(fieldEl).next("." + this.messageClass).html(message == null ? "" : message);
		}
	}
	
});


var CGP_BaseAjaxForm = Class.create(CGP_AbstractAjaxForm, {

	initialize: function($super, prefix, formName, mgrName){
		$super(prefix, formName);
		this.mgrName = mgrName;
		
		this.cancelUrl = "";
	},
	
	setField: function(fieldName, fieldValue){
		
		if(!this.errorFlag){
			var thisObj = this;
			var setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length);
			var toEval =
			this.mgrName + "." + setterName + "('" + fieldValue + "'," + 
			"{" +
			"	callback:function(message){" +
			"		thisObj.setInputMessage('" + fieldName + "', message);" +
			"		if(message == null){" +
			"			thisObj.checkEnableSubmit();" + 
			"		}" +
			"	}," +
			"	errorHandler:function(errorString, exception){" +
			"		alert(\"Error setting input '" + fieldName + "': \" + errorString);" +
			"		this.errorFlag = true;" +
   			"	}" +
			"});" 
			eval(toEval);
		}	
	},
	
	submit: function(){
		this.validateThenSubmit(false);
	},
	
	cancel: function(){
		window.location = this.cancelUrl;
	},
	
	validateThenSubmit: function(validated){
		
		var thisObj = this;
		var mgr = eval(this.mgrName);
		if(!validated){
			mgr.validate(
			{
				callback: function(response){
					thisObj.handleValidateSuccess(response);
    			},
    			errorHandler: function(errorString, exception){
    				thisObj.handleValidateError(errorString, exception);
    			}
			});
		}else{
			mgr.submit(
			{
				callback:function(response){
					thisObj.handleSubmitSuccess(response);
    			},
    			errorHandler:function(errorString, exception){
    				thisObj.handleSubmitError(errorString, exception);
    			}
			});
		}	
	},
	
	handleValidateSuccess: function(response){
		if(response != null){
			alert(response);
		}else{
			this.validateThenSubmit(true);
		}
	},
	handleValidateError: function(errorString, exception){
		alert("Error validating: " + errorString);
	},
	handleSubmitSuccess: function(response){
		alert("Successfully submitted.");
	},
	handleSubmitError: function(errorString, exception){
		alert("Error saving: " + errorString);
	}

});
