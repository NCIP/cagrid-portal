<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@attribute name="status" required="true"
             type="gov.nih.nci.cagrid.portal.portlet.diagnostics.DiagnosticResultStatus" %>


<c:choose>
    <c:when test="${status == 'Passed'}">
        <tags:image name="diagnostic_passed_icon.jpg"/>
    </c:when>
    <c:when test="${status == 'Failed'}">
        <tags:image name="diagnostic_failed_icon.png"/>
    </c:when>
    <c:otherwise>
        <tags:image name="diagnostic_problem_icon.png"/>
    </c:otherwise>
</c:choose>