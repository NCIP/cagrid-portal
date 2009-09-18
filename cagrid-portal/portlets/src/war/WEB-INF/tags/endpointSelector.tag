<%@tag %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@attribute name="input_name" required="true" %>
<%@attribute name="endpoint" required="true"
             type="gov.nih.nci.cagrid.portal.domain.catalog.GridServiceEndPointCatalogEntry" %>
<%@attribute name="id_prefix" required="false" %>

<c:set var="rtsId">${id_prefix}${endpoint.id}</c:set>
<input type="checkbox" name="${input_name}" value="${endpoint.about.url}"/>

<a id="${rtsId}-infoPopup-control"
   class="infoPopupLink
   <c:if test="${endpoint.about.currentStatus !='ACTIVE'}">
        inactiveEndpoint
       </c:if>
       "
   onmouseover="$('${rtsId}-infoPopup-content').style.display='inline'"
   onmouseout="$('${rtsId}-infoPopup-content').style.display='none'">
    <c:out value="${endpoint.name}"/>
</a>&nbsp;

        <span id="${rtsId}-infoPopup-content" class="infoPopup">
        
    <div>

        <c:if test="${not empty endpoint.about.serviceMetadata.serviceDescription.version}">
            <div class="row">
                <div class="label">
                    Version:
                </div>
                <div class="infoPopupValue value">
                        ${endpoint.about.serviceMetadata.serviceDescription.version}
                </div>
            </div>
        </c:if>


        <div class="row">
            <div class="label">
                Type:
            </div>
            <div class="infoPopupValue value">
                <c:choose>
                    <c:when test="${endpoint.about.class.simpleName eq 'GridDataService'}">
                        DATA
                    </c:when>
                    <c:otherwise>
                        ANALYTICAL
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="row">
            <div class="label">
                Status:
            </div>
            <div class="infoPopupValue value">
                ${endpoint.about.currentStatus}
            </div>
        </div>


        <c:if test="${not empty endpoint.about.serviceMetadata.hostingResearchCenter.shortName}">
            <div class="row">
                <div class="label">
                    Center:
                </div>
                <div class="infoPopupValue value">
                        ${endpoint.about.serviceMetadata.hostingResearchCenter.shortName}
                </div>
            </div>
        </c:if>


        <div class="row">
            <div class="label">
                URL:
            </div>
            <div class="infoPopupValue value">
                ${endpoint.about.url}
            </div>
        </div>

        <div class="row">
            <div class="label">
                Secured:
            </div>
            <div class="infoPopupValue value">
                <c:choose>

                    <c:when test="${fn:startsWith(endpoint.about.url, 'https')}">
                        TRUE
                    </c:when>
                    <c:otherwise>
                        FALSE
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

    </div>



<span class="infoPopup-pointer">&nbsp;</span></span>
