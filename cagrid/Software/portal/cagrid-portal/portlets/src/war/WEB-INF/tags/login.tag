<%@tag %>
<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<%@ include file="/WEB-INF/jsp/include/liferay-includes.jspf" %>

<%@ attribute name="portalUser" required="false" %>
<%@ attribute name="dontRedirect" type="java.lang.Boolean"  required="false" %>
<%--if redirect should use window.location.href--%>
<%@ attribute name="useHrefRedirect" required="false" type="java.lang.Boolean" %>
<%@ attribute name="id" required="false" %>
<%--href text--%>
<%@ attribute name="loginLinkText" required="false" %>
<%--follow up text after the href--%>
<%@ attribute name="notLoggedInText" required="false" %>
<%--if a user is logged in, this is what they will see--%>
<%@ attribute name="loggedInText" required="false" %>


<portlet:renderURL var="currentRenderURL"/>

<liferay-portlet:renderURL var="loginLink" portletName="cagriddirectauthn_WAR_cagridportlets"
                           portletMode="view">
    <c:if test="${!dontRedirect}">
    <liferay-portlet:param name="redirectUrl" value="CURRENT_URL"/>
    </c:if>
</liferay-portlet:renderURL>

<c:choose>
    <c:when test="${empty portalUser}">
        <a
                <c:if test="${!empty id}">
                    id="${id}"
                </c:if>
                href="" onclick="${id}login(); return false;">
            <c:choose>
                <c:when test="${!empty loginLinkText}">
                    ${loginLinkText}
                </c:when>
                <c:otherwise>
                    Sign In
                </c:otherwise>
            </c:choose>
        </a>
        <c:if test="${!empty notLoggedInText}">
            ${notLoggedInText}
        </c:if>
    </c:when>
    <c:otherwise>
        <c:if test="${!empty loggedInText}">
            ${loggedInText}
        </c:if>
    </c:otherwise>
</c:choose>

<script type="text/javascript">
    function ${id}login() {
        var loginLink = "${loginLink}";
        loginLink = loginLink.replace(/;jsessionid=[a-zA-Z0-9]*/, "");
        loginLink = loginLink.replace(/\/guest\/[a-zA-Z]+\?/, "/guest/login?");
        loginLink = loginLink.replace(/\/guest\/[a-zA-Z]+\/[a-zA-Z]+\?/, "/guest/login?");

    <%--set appropriate location for redirect--%>
        var currentLocation = escape(window.location.href);
        <c:if test="${!useHrefRedirect}">
        currentLocation = "${currentRenderURL}";
    </c:if>
        loginLink = loginLink.replace("CURRENT_URL", currentLocation);
        window.location.href = loginLink;
        return false;

    }
</script>
