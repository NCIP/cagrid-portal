<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="name" required="true" %>

<c:if test="${!empty name}">
    (<c:out value="${name}"/>)
</c:if>
