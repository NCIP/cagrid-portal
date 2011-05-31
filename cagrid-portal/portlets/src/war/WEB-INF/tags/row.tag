<%@tag %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="rowValue" required="true" %>
<%@attribute name="rowLabel" required="true" %>
<%@attribute name="useLabel" type="java.lang.Boolean" required="false"
             description="Will use label and not div to display the lable field" %>


<div class="row">
    <c:choose>
        <c:when test="${!useLabel}">
            <div class="title"><c:out value="${rowLabel}"/></div>
        </c:when>
        <c:otherwise>
            <label for="${rowLabel}"><c:out value="${rowLabel}"/></label>
        </c:otherwise>
    </c:choose>
    <div class="value"><c:out value="${rowValue}"/></div>
</div>