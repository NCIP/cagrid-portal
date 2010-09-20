<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
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
                        <td><form:input path="username" size="29"/></td>
                    </tr>
                    <tr>
                        <td style="padding-right:5px; text-align:right;">Password:</td>
                        <td><form:password path="password" size="29"/></td>
                    </tr>
                    <tr>
                        <td style="padding-right:5px; text-align:right;">Identity Provider:</td>
                        <td>
                            <input name="idpUrl" id="idpUrl" type="hidden"/>
                            <select id="${ns}idpSelect">
                                <option>Loading...</option>
                            </select>
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
                <input type="hidden" name="portalAuthnUrl" value="<c:out value="${portalAuthnUrl}"/>"/>
            </form:form>


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