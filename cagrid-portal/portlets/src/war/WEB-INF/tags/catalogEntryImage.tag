<%@tag%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<%@attribute name="entry" required="true" type="gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry"%>
<%@attribute name="thumbnail" required="true"%>
<%@attribute name="id" required="false"%>
<%@attribute name="height" required="false"%>
<%@attribute name="alt" required="false"%>
<%@attribute name="title" required="false"%>
<%@attribute name="cssClass" required="false"%>
<%@attribute name="cssStyle" required="false"%>

<img    

<c:choose>
	<c:when test="${thumbnail}">
		<c:set var="imageId" value="${entry.thumbnail.id}"/>
		<c:set var="imagePixels" value="50"/>
	</c:when>
	<c:otherwise>
		<c:set var="imageId" value="${entry.image.id}"/>
		<c:set var="imagePixels" value="180"/>
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${!empty entry.image}">
		src="<c:url value="/img/get.html"><c:param name="id" value="${imageId}"/></c:url>"	
	</c:when>
	<c:otherwise>
		src="<c:url value="/images/${fn:toLowerCase(entry.class.simpleName)}_placeholder_${imagePixels}px.png"/>"
	</c:otherwise>
</c:choose>

	

		<c:if test="${!empty id}">
            id="${id}"
        </c:if>
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