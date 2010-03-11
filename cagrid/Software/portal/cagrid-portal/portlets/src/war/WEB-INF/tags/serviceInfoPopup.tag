<%@tag %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="id" required="true" %>
<%@attribute name="serviceInfo" required="true" type="gov.nih.nci.cagrid.portal.domain.ServiceInfo" %>
<%@attribute name="link_href" required="false"
             description="Alternate link. Will not use popup image. Requires link_text" %>
<%@attribute name="link_text" required="false" description="Alternate link text.Required when specifying link_href" %>


<a id="${id}-infoPopup-control"
   class="infoPopupLink"
   onmouseover="$('${id}-infoPopup-content').style.display='inline'"
   onmouseout="$('${id}-infoPopup-content').style.display='none'"
        <c:if test="${not empty link_href}">
            &nbsp; href='${link_href}'
        </c:if>
        >
    <c:choose>
        <c:when test="${not empty link_text}">
            ${link_text}
            <c:if test="${serviceInfo.secure}">
                <tags:image name="icon_padlock.gif" height="11" alt="Secure Service"/>
            </c:if>
        </c:when>
        <c:otherwise>
            <tags:image name="information_icon.png" height="13"/>
        </c:otherwise>
    </c:choose>
</a>&nbsp;

    <span id="${id}-infoPopup-content" class="infoPopup">
    <div>

        <c:if test="${not empty serviceInfo.name}">
            <div class="row">
                <div class="label">
                    Name:
                </div>
                <div class="infoPopupValue value">
                        ${serviceInfo.name}
                </div>
            </div>
        </c:if>

        <c:if test="${not empty serviceInfo.version}">
            <div class="row">
                <div class="label">
                    Version:
                </div>
                <div class="infoPopupValue value">
                        ${serviceInfo.version}
                </div>
            </div>
        </c:if>

        <c:if test="${not empty serviceInfo.type}">
            <div class="row">
                <div class="label">
                    Type:
                </div>
                <div class="infoPopupValue value">
                        ${serviceInfo.type}
                </div>
            </div>
        </c:if>


        <c:if test="${not empty serviceInfo.status}">
            <div class="row">
                <div class="label">
                    Status:
                </div>
                <div class="infoPopupValue value">
                        ${serviceInfo.status}
                </div>
            </div>
        </c:if>


        <c:if test="${not empty serviceInfo.center}">
            <div class="row">
                <div class="label">
                    Center:
                </div>
                <div class="infoPopupValue value">
                        ${serviceInfo.center}
                </div>
            </div>
        </c:if>

        <c:if test="${not empty serviceInfo.url}">
            <div class="row">
                <div class="label">
                    URL:
                </div>
                <div class="infoPopupValue value">
                        ${serviceInfo.urlAbbrv}
                </div>
            </div>
        </c:if>

        <c:if test="${not empty serviceInfo.secure}">
            <div class="row">
                <div class="label">
                    Secured:
                </div>
                <div class="infoPopupValue value">
                        ${serviceInfo.secure}
                </div>
            </div>
        </c:if>

    </div>

    <span class="infoPopup-pointer">&nbsp;</span></span>
