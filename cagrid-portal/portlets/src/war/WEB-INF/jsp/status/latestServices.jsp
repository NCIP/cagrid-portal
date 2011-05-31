<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<c:forEach var="serviceInfo" items="${latestServices}">

    <div class="row">
        <div class="label">

            <tags:serviceInfoPopup id="${serviceInfo.id}"
                                   link_href="javascript:navigateToService(${serviceInfo.id});"
                                   link_text="${serviceInfo.nameAbbrv}"
                                   serviceInfo="${serviceInfo}"/>

        </div>

        <div class="value">
            <c:choose>
                <c:when test="${serviceInfo.type == 'DATA'}">
                    <img alt="Data Services" src="<c:url value="/images/data-services.png"/>"/>
                </c:when>
                <c:otherwise>
                    <img alt="Analytical Services" src="<c:url value="/images/analytical_services.png"/>"/>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

</c:forEach>
