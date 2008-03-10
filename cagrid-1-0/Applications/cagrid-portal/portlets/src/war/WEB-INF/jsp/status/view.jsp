<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script
        src="<c:url value="/js/scriptaculous/prototype.js"/>"
        type="text/javascript"></script>
<script
        src="<c:url value="/js/scriptaculous/scriptaculous.js"/>"
        type="text/javascript"></script>


<div style="height:500px">
    Here are the five newest services...
    <table cellpadding="10" width="100%">
        <thead>
            <tr>
                <th><b>Name</b></th><th><b>Type</b></th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="serviceInfo" items="${statusBean.latestServices}">
                <tr>
                    <td style="padding-top:8px; padding-right:10px">
                        <portlet:actionURL var="selectItemAction">
                            <portlet:param name="operation" value="selectItemForDiscovery"/>
                            <portlet:param name="selectedId" value="${serviceInfo.id}"/>
                            <portlet:param name="type" value="SERVICE"/>
                        </portlet:actionURL>

                        <tags:serviceInfoPopup id="${serviceInfo.id}"
                                               link_href="${selectItemAction}"
                                               link_text="${serviceInfo.name}"
                                               serviceInfo="${serviceInfo}"/>
                    </td>

                    <td>
                        <c:choose>
                            <c:when test="${serviceInfo.type == 'DATA'}">
                                <img src="<c:url value="/images/data-services.gif"/>" height="20" />
                            </c:when>
                            <c:otherwise>
                                <img src="<c:url value="/images/analytical_services.gif"/>" height="20" />
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>

        </tbody>
    </table>
    <br/>
    There are currently...<br/>
    <table cellpadding="3">
        <tr>
            <td style="padding-right:5px;">
                <portlet:actionURL var="participantsAction">
                    <portlet:param name="operation" value="selectDirectoryForDiscovery"/>
                    <portlet:param name="selectedDirectory" value="${statusBean.participantsDirectory.id}"/>
                </portlet:actionURL>
                <a href="<c:out value="${participantsAction}"/>">
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