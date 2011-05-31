<%@tag %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%@attribute name="name" required="true"%>
<%@attribute name="id" required="false"%>
<%@attribute name="currentValue" required="true"%>
<%@attribute name="optionValues" required="true" type="java.lang.Object[]"%>
<%@attribute name="cssClass" required="false"%>
<%@attribute name="cssStyle" required="false"%>

<select name="${name}"
<c:if test="${!empty id}"> id=${id}" </c:if>
<c:if test="${!empty cssClass}"> class="${cssClass}" </c:if>
<c:if test="${!empty cssStyle}"> style="${cssStyle}" </c:if>
>
<c:forEach var="op" items="${optionValues}">
	<option value="<c:out value="${op[0]}"/>" <c:if test="${op[0] eq currentValue}">selected</c:if>><c:out value="${op[1]}"/></option>
</c:forEach>

</select>


