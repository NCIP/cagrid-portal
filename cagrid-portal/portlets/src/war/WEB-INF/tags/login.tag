<%@tag %>
<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>

<%@ attribute name="loginUrl" required="false" %>
<%@ attribute name="portalUser" required="false" %>
<%@ attribute name="notLoggedInText" required="false" %>
<%@ attribute name="loggedInText" required="false" %>


<c:choose>
    <c:when test="${empty portalUser}">
        Please
        <a href='<c:out value="${loginUrl}"/>'> sign in</a>
        <c:if test="${!empty loggedInText}">
            ${loggedInText}
        </c:if>
    </c:when>
    <c:otherwise>
        <c:if test="${!empty notLoggedInText}">
            ${notLoggedInText}
        </c:if>
    </c:otherwise>
</c:choose>

