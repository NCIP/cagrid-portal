<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<script
        src="<c:url value="/js/scriptaculous/prototype.js"/>"
        type="text/javascript"></script>
<script
        src="<c:url value="/js/scriptaculous/effects.js"/>"
        type="text/javascript"></script>


<div style="height:500px">
<div class="label">
    Here are the five newest services...
</div>

<div class="row">
    <div class="label">
        Name
    </div>
    <div class="value" style="font-weight:bold;">
        Type
    </div>
</div>


<c:forEach var="serviceInfo" items="${statusBean.latestServices}">
<div class="row">
    <div class="label">
        <portlet:actionURL var="selectItemAction">
            <portlet:param name="operation" value="selectItemForDiscovery"/>
            <portlet:param name="selectedId" value="${serviceInfo.id}"/>
            <portlet:param name="type" value="SERVICE"/>
        </portlet:actionURL>

        <tags:serviceInfoPopup id="${serviceInfo.id}"
                               link_href="${selectItemAction}"
                               link_text="${serviceInfo.nameAbbrv}"
                               serviceInfo="${serviceInfo}"/>

    </div>

    <div class="value">
        <c:choose>
            <c:when test="${serviceInfo.type == 'DATA'}">
                <img src="<c:url value="/images/data-services.gif"/>" height="20"/>
            </c:when>
            <c:otherwise>
                <img src="<c:url value="/images/analytical_services.gif"/>" height="20"/>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</c:forEach>


<br/>

<div class="row">
    <div class="label">
        There are currently...
    </div>

    <table cellpadding="5">
        <tr>
            <td style="padding-right:5px;text-align:left;">
                <portlet:actionURL var="participantsAction">
                    <portlet:param name="operation" value="selectDirectoryForDiscovery"/>
                    <portlet:param name="selectedDirectory" value="${statusBean.participantsDirectory.id}"/>
                </portlet:actionURL>
                <a style="text-align:left;" href="<c:out value="${participantsAction}"/>">
                    <c:out value="${fn:length(statusBean.participantsDirectory.objects)}"/>
                </a>
            </td>
            <td>caBIG Participants,</td>
        </tr>
        <tr>
            <td style="padding-right:5px;">
                <portlet:actionURL var="servicessAction">
                    <portlet:param name="operation" value="selectDirectoryForDiscovery"/>
                    <portlet:param name="selectedDirectory" value="${statusBean.servicesDirectory.id}"/>
                </portlet:actionURL>
                <a href="<c:out value="${servicessAction}"/>">
                    <c:out value="${fn:length(statusBean.servicesDirectory.objects)}"/>
                </a>
            </td>
            <td>grid services, which include</td>
        </tr>
        <tr>

            <td style="padding-right:5px;">
                <portlet:actionURL var="dataServicesAction">
                    <portlet:param name="operation" value="selectDirectoryForDiscovery"/>
                    <portlet:param name="selectedDirectory" value="${statusBean.dataServicesDirectory.id}"/>
                </portlet:actionURL>
                <a href="<c:out value="${dataServicesAction}"/>">
                    <c:out value="${fn:length(statusBean.dataServicesDirectory.objects)}"/>
                </a>
            </td>
            <td>data services, and</td>
        </tr>
        <tr>

            <td style="padding-right:5px;">
                <portlet:actionURL var="analyticalServicesAction">
                    <portlet:param name="operation" value="selectDirectoryForDiscovery"/>
                    <portlet:param name="selectedDirectory" value="${statusBean.analyticalServicesDirectory.id}"/>
                </portlet:actionURL>
                <a href="<c:out value="${analyticalServicesAction}"/>">
                    <c:out value="${fn:length(statusBean.analyticalServicesDirectory.objects)}"/>
                </a>
            </td>
            <td>analytical services.</td>
        </tr>
    </table>
</div>


<br/>
<c:if test="${not empty statusBean.lastUpdated}">
<div>
    <div>
        <tags:infoPopup id="<portlet:namespace>status" popup_href="javascript:void();"
                        popup_name="Last Updated:"
                        popup_text="Portal frequently searches for
                            new services and updates its database."/>
        <span>${statusBean.lastUpdated}</span>
    </div>
</div>
</c:if>
