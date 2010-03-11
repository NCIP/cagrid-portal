<%@tag %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="id" required="false" %>
<%@attribute name="rowValue" required="true" %>
<%@attribute name="rowLabel" required="true" %>
<%@attribute name="useLabel" type="java.lang.Boolean" required="false"
             description="Will use label and not div to display the lable field" %>


<%--will only render row if the Value is not null--%>
<c:if test="${!empty rowValue}">
    <div class="row"
            <c:if test="${!empty id}">
                id="${id}"
            </c:if>
            >
        <c:choose>
            <c:when test="${!useLabel}">
                <div class="label"><c:out value="${rowLabel}"/></div>
            </c:when>
            <c:otherwise>
                <label><c:out value="${rowLabel}"/></label>
            </c:otherwise>
        </c:choose>
        <div class="value"><c:out value="${rowValue}"/></div>
    </div>

</c:if>