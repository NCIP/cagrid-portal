<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@attribute name="result" required="true"
             type="gov.nih.nci.cagrid.portal.portlet.diagnostics.DiagnosticResult" %>


<c:choose>
    <c:when test="${result.status == 'Passed'}">
        <img src="<c:url value="/images/diagnostic_passed_icon.jpg"/>"
    </c:when>
    <c:when test="${result.status == 'Failed'}">
        <img src="<c:url value="/images/diagnostic_failed_icon.png"/>"
    </c:when>
    <c:otherwise>
        <img src="<c:url value="/images/diagnostic_problem_icon.png"/>"
    </c:otherwise>
</c:choose>
    <c:if test="${not empty result.message}">
        alt="${result.message}"
    </c:if>
  />
    <c:if test="${not empty result.detail}">
       <tags:infoPopup id="dunno" popup_text="${result.detail}"/>
    </c:if>


