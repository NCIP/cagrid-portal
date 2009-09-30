<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<script type='text/javascript'
        src='/cagridportlets/dwr/interface/CredentialManagerFacade.js'></script>
<script type='text/javascript' src='/cagridportlets/dwr/engine.js'></script>
<script type='text/javascript' src='/cagridportlets/dwr/util.js'></script>


<c:set var="ns"><portlet:namespace/></c:set>

<c:choose>
    <c:when test="${empty portalUser}">

        If you have an NIH username and password, then
        select the NCICB AuthenticationService IdP before pressing the Log
        In button. If you do not have an NIH username and password, but you
        have already registered through the portal (or through the GAARDS UI), then select the
        Dorian Identity Provider. If you have not yet registered, you may do so by
        clicking <a href="${ns}showRegisterDialog();">here</a>.
        <br/>
        <br/>

        <portlet:renderURL var="portalAuthnUrl"/>
        <portlet:actionURL var="action">
            <portlet:param name="operation" value="login"/>
        </portlet:actionURL>

        <form:form id="${ns}LoginForm" commandName="directLoginCommand" method="POST">
            <c:if test="${!empty authnErrorMessage}">
                <span style="color:red"><c:out value="${authnErrorMessage}"/></span>
            </c:if>
            <span style="color:red"><form:errors path="*"/></span>
            <table>
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
                        <input alt="Log In" type="button" id="${ns}submitBtn" value="Log In"/>
                    </td>
                </tr>
            </table>
            <input type="hidden" name="portalAuthnUrl" value="<c:out value="${portalAuthnUrl}"/>"/>
        </form:form>


    </c:when>
    <c:otherwise>
        <a href="/web/guest/home" style="text-decoration:none;">&lt;&lt; To Full Page</a><br/><br/>
        <%@ include file="/WEB-INF/jsp/directauthn/greeting.jspf" %>
    </c:otherwise>
</c:choose>


<script language="JavaScript">
    jQuery(
            function() {
                jQuery('#${ns}submitBtn').click(
                        function() {
                            jQuery("#idpUrl").val(jQuery("#${ns}idpSelect :selected").val());
                            jQuery("#${ns}LoginForm").attr("action", "${action}");
                            jQuery("#${ns}LoginForm").submit();
                        }

                        );
            });

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

            },
            errorHandler: function(errorString, exception) {
                alert("Error listing identity providers: " + errorString);
            }
        });
    }
    jQuery(document).ready(function() {
        ${ns}listIdPs();
    });
</script>
<script type="text/javascript">

    var ${ns}registerDialog;

    function ${ns}showRegisterDialog() {
        ${ns}registerDialog =
        new Liferay.Popup({title: "Register", modal:true, width:500 , height:600});
        jQuery(
                ${ns}registerDialog
                ).load('<c:url value="/browse/personView/register.html"><c:param name="ns" value="${ns}"/></c:url>', {});
    }

    jQuery(document).ready(function() {

        var myStates = new CGP_StateCountryLists("${ns}stateProvince", "${ns}countryCode");
        myStates.render();

        ${ns}registerButton = new YAHOO.widget.Button({
            label: "Register",
            id: "${ns}registerButton",
            container: "${ns}registerButtonContainer"
        });

        ${ns}registerButton.on("click", function(evt) {
            ${ns}showRegisterDialog();
        });

    });


</script>