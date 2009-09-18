<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>


<c:forEach var="catalog" items="${latestContent}">
    <div class="oneLatestEntry">

            <%-- <c:choose>
                <c:when test="${catalog.class.simpleName == 'GridServiceEndPointCatalogEntry'}">
                    <c:choose>
                        <c:when test="${catalog.data== 'true'}">
                            <tags:image name="data-services.png"/>
                        </c:when>
                        <c:otherwise>
                            <tags:image name="analytical_services.png"/>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:when test="${catalog.class.simpleName == 'InstitutionCatalogEntry'}">
                    <tags:image name="hosting-research-center.png"/>
                </c:when>
                <c:when test="${catalog.class.simpleName == 'PersonCatalogEntry'}">
                    <tags:image name="man.png"/>
                </c:when>
            </c:choose>--%>
        <tags:catalogEntryPopup id="${catalog.id}" entry="${catalog}" link_text="${catalog.name}"
                                link_text_max_length="30"
                                link_href="javascript:viewDetails(${catalog.id})"/>
    </div>

</c:forEach>

<div style="text-align:right;">
    <a href="/web/guest/catalog/all">More</a>
</div>