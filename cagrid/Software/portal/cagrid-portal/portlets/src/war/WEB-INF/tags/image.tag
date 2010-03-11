<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%@attribute name="name" required="true"%>
<%@attribute name="id" required="false"%>
<%@attribute name="height" required="false"%>
<%@attribute name="alt" required="false"%>
<%@attribute name="title" required="false"%>
<%@attribute name="cssClass" required="false"%>
<%@attribute name="cssStyle" required="false"%>


<img    <c:if test="${!empty id}">
            id="${id}"
        </c:if>
        src="<c:url value="/images/${name}"/>"
        <c:if test="${!empty height}">
            height="${height}"
        </c:if>
        <c:if test="${!empty alt}">
            alt="${alt}"
        </c:if>
          <c:if test="${!empty title}">
            title="${title}"
        </c:if>
         <c:if test="${!empty cssClass}">
            class="${cssClass}"
        </c:if>
        <c:if test="${!empty cssStyle}">
           style="${cssStyle}"
       </c:if>

/>

