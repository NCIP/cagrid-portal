<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%@attribute name="name" required="true"%>
<%@attribute name="height" required="false"%>
<%@attribute name="alt" required="false"%>

<img src="<c:url value="/images/${name}"/>"
        <c:if test="${!empty height}">
            height="${height}" 
        </c:if>
        <c:if test="${!empty alt}">
            alt="${alt}"
        </c:if>
/>

