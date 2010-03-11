<%@ include file="/WEB-INF/jsp/include/servlet_includes.jsp" %>

<script type="text/javascript" src="<c:url value="/js/yui/button/button-min.js"/>"></script>

<script type="text/javascript" src="<c:url value="/js/ajaxform.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/state_country_lists.js"/>"></script>

<script type="text/javascript"
        src="<c:url value="/dwr/interface/RegistrationManager.js"/>"></script>

<c:set var="ns" value="${param.ns}"/>

<style type="text/css">
    div.row div.label {
        display: block;
        float: left;
        font-weight: bold;
        width: 90px;
        margin-right: 10px;
        text-align: right;
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

    .rightpanel {
        float: left;
    }

    .instructions {
        margin: 20px;
    }
</style>

<div align="left" style="overflow:auto;height:800px;padding-bottom:10px;" class="yui-skin-sam">

    <div class="instructions">
        <spring:message code="register.message"/>
    <span>
            <a id="registerHelpLink" href="${usersGuideUrl}-RegisterasaNewUser" target="_blank">
                <tags:image name="help.gif"/>
            </a>
        </span>
    </div>


    <form name="editForm">
        <div class="row">
            <div class="leftpanel">
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
                    <input type="password" name="password" id="password"/>
                </div>
                <div class="row">
                    <label for="password">ReType Password</label>
                    <input type="password" id="password2"/>

                    <div id="passwordError" class="errorMsg" style="display:none;">
                        Passwords don't match
                    </div>
                </div>

                <div class="row">
                    <label for="email">e-mail</label>
                    <input type="text" name="email" class="autoSet required"/>
                </div>
                <div class="row">
                    <label for="phone">Phone</label>
                    <input type="text" name="phone" class="autoSet required"/>
                </div>
            </div>
            <div class="rightpanel">
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
                    <input type="text" name="street2" class="autoSet"/>
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
            </div>
        </div>
    </form>

    <div class="flow-buttons">
        <span id="cancelButtonContainer"></span>
        <span id="submitButtonContainer"></span>
    </div>
</div>

<script type="text/javascript">

    var RegisterForm = Class.create(CGP_BaseAjaxForm, {

        handleSubmitSuccess: function(response) {
            alert(response);
            Liferay.Popup.close(${ns}registerDialog);
        },

        submit: function() {
            var passwdChk = checkPasswords();
            if (passwdChk != null)
                return false;
        <%--set the country and state to defaults--%>
            this.setField("stateProvince", jQuery("#stateProvince option:selected").val());
            this.setField("country", jQuery("#country option:selected").val());
            this.setField("password", jQuery("#password").val());
            this.validateThenSubmit(false);
        },

        handleSubmitError: function(errorString, exception) {
            if (errorString == null) {
                alert("Error registering.");
            } else {
                var errorMatch = "Error: Invalid User Property: ";
                var uPwdErr = "Unacceptable password, ";
                var iPwdErr = "Invalid password, ";
                var emailErr = "Invalid email";
                var uUsrErr = "Unacceptable User ID, ";
                var eUsrErr = "The user";
                var iPwdMatchErr = "Passwords don't match";

                var idx = errorString.indexOf(errorMatch);
                if (idx > -1) {
                    var msg = errorString.substring(idx + errorMatch.length);
                    if (msg.startsWith(uPwdErr)) {
                        this.setInputMessage("password", msg.substring(uPwdErr.length));

                    } else if (msg.startsWith(iPwdErr)) {
                        this.setInputMessage("password", msg.substring(iPwdErr.length));

                    } else if (msg.startsWith(emailErr)) {
                        this.setInputMessage("email", msg);

                    } else if (msg.startsWith(uUsrErr)) {
                        this.setInputMessage("username", msg.substring(uUsrErr.length));

                    } else if (msg.startsWith(eUsrErr)) {
                        this.setInputMessage("username", msg);
                    } else if (msg.startsWith(iPwdMatchErr)) {
                        this.setInputMessage("password2", msg);
                    } else {
                        alert(msg);
                    }
                }
            }
        },

        cancel: function() {
            Liferay.Popup.close(${ns}registerDialog);
        }
    });

    jQuery(document).ready(function() {
        var myForm = new RegisterForm("", "editForm", "RegistrationManager");
        myForm.submitButtonLabel = "Register";
        myForm.render();
        var myStates = new CGP_StateCountryLists("stateProvince", "country");
        myStates.render();

        jQuery("#passwordError").hide();

        jQuery("#password2").blur(checkPasswords);

    });

    function checkPasswords()
    {
        if (jQuery("#password2").val() != jQuery("#password").val()) {
            jQuery("#passwordError").show();
            return "Passwords don't match";
        }
        else {
            jQuery("#passwordError").hide();
            return null;
        }
    }
</script>
