<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/styleSheet.css"/>"/>

<script type='text/javascript'
        src='/cagridportlets/dwr/interface/CredentialManagerFacade.js'></script>
<script type='text/javascript' src='/cagridportlets/dwr/engine.js'></script>
<script type='text/javascript' src='/cagridportlets/dwr/util.js'></script>


<tags:yui-minimum/>
<link rel="stylesheet" type="text/css" href="<c:url value="/js/yui/container/assets/skins/sam/container.css"/>"/>

<script type="text/javascript" src="<c:url value="/js/yui/button/button-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/container/container-min.js"/>"></script>

<c:set var="ns"><portlet:namespace/></c:set>

<div class="yui-skin-sam">
    <c:choose>
        <c:when test="${empty portalUser}">
            <spring:message code="login.message"/> If you have not yet registered, you may do so by
            clicking <a href="javascript:${ns}showRegisterDialog();">here</a>.


            <span style="vertical-align:top;">
                <tags:helpLink helpURL="${usersGuideUrl}-UsingthecaGridPortalfortheFirstTime"/>
            </span>

            <br/>
            <br/>

            <c:choose>
                <c:when test="${! empty redirectUrl}">
                    <c:set var="portalAuthnUrl" value="${redirectUrl}"/>
                </c:when>
                <c:otherwise>
                    <portlet:renderURL var="portalAuthnUrl"/>
                </c:otherwise>
            </c:choose>

            <portlet:actionURL var="action">
                <portlet:param name="operation" value="login"/>
            </portlet:actionURL>

            <form:form id="${ns}LoginForm" commandName="directLoginCommand" method="POST">
                <c:if test="${!empty authnErrorMessage}">
                    <span style="color:red"><c:out value="${authnErrorMessage}"/></span>
                </c:if>
                <span style="color:red"><form:errors path="*"/></span>
                <table>
                    <thead>
                    <tr>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td style="padding-right:5px; text-align:right">Username:</td>
                        <td><label for="username"> <form:input id="username" path="username" size="29"/></label></td>
                    </tr>
                    <tr>
                        <td style="padding-right:5px; text-align:right;">Password:</td>
                        <td><label for="password"/><form:password id="password" path="password" size="29"/></label></td>
                    </tr>
                    <tr>
                        <td style="padding-right:5px; text-align:right;">Identity Provider:</td>
                        <td>
                            <input name="idpUrl" id="idpUrl" alt="Hidden" type="hidden"/>
                            <label for="${ns}idpSelect">
                            <select id="${ns}idpSelect">
                                <option>Loading...</option>
                            </select>
                            </label>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td style="padding-top:10px;">

                            <span id="${ns}loginButtonContainer"></span>

                        </td>
                    </tr>
                    </tbody>
                </table>
                <input type="hidden" alt="Hidden" name="portalAuthnUrl" value="<c:out value="${portalAuthnUrl}"/>"/>
            </form:form>

            <div class="loginNotice">
                <div class="row">
                    <div class="label">
                        Notice:
                    </div>
                </div>
                <div class="row">
                    This is a U.S. Government computer system, which may be accessed and
                    used only for authorized Government business by authorized personnel.
                    Unauthorized access or use of this computer system may subject
                    violators to criminal, civil, and/or administrative action.
                </div>
                <div class="row">
                    All information on this computer system may be intercepted, recorded,
                    read, copied, and disclosed by and to authorized personnel for official
                    purposes, including criminal investigations. Such information includes
                    sensitive data encrypted to comply with confidentiality and privacy
                    requirements. Access or use of this computer system by any person,
                    whether authorized or unauthorized, constitutes consent to these terms.
                    There is no right of privacy in this system.
                </div>


            </div>


        </c:when>
        <c:otherwise>
            <a href="/web/guest/home" style="text-decoration:none;">&lt;&lt; To Home Page</a><br/><br/>
            <%@ include file="/WEB-INF/jsp/directauthn/greeting.jspf" %>
        </c:otherwise>
    </c:choose>

</div>

<script language="JavaScript">

    ${ns}loginButton = null;

    function ${ns}listIdPs() {

        CredentialManagerFacade.listIdPsFromDorian(
        {
            callback: function(idps) {

                var idpOpts = "";
                for (var i = 0; i < idps.length; i++) {
                    var idpBean = idps[i];
                    idpOpts += "<option value='" + idpBean.url + "'" + (i == 0 ? " selected" : "") + ">" + idpBean.label + "</option>";
                }
                jQuery("#${ns}idpSelect").html(idpOpts);
                ${ns}loginButton.set("disabled", false);


            },
            errorHandler: function(errorString, exception) {
                alert("Error listing identity providers: " + errorString);
            }
        });
    }

    var ${ns}registerDialog;

    function ${ns}showRegisterDialog() {
        ${ns}registerDialog =
                new Liferay.Popup({title: "Register", modal:true, width:800 , height:500});
        jQuery(
                ${ns}registerDialog
                ).load('<c:url value="/browse/personView/register.html"><c:param name="ns" value="${ns}"/></c:url>', {});
    }


    jQuery(document).ready(function() {


        ${ns}loginButton = new YAHOO.widget.Button({
            label: "Login",
            id: "${ns}loginButton",
            disabled : true,
            container: "${ns}loginButtonContainer"
        });


        ${ns}loginButton.on("click", function(evt) {
            jQuery("#idpUrl").val(jQuery("#${ns}idpSelect :selected").val());
            jQuery("#${ns}LoginForm").attr("action", "${action}");
            jQuery("#${ns}LoginForm").submit();
        }
                );

        ${ns}listIdPs();
    });

</script>