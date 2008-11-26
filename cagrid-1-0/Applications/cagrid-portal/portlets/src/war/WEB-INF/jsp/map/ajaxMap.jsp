<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<style type="text/css">
<!--
<%@ include file="/css/map.css" %>
-->
</style>

<c:set var="mapNodeId">namespace-gmap</c:set>

<script type="text/javascript">
//<![CDATA[
jQuery(document).ready(function() {


if (GBrowserIsCompatible()) {

<c:if test="${!empty mapBean.participantNodes}">
<c:forEach
items="${mapBean.participantNodes}"
var="pNode"
varStatus="pNodeStatus">

<c:set var="markerId">pMarker<c:out value="${pNodeStatus.index}"/></c:set>

var <c:out value="${markerId}"/> =
new GMarker(
new GLatLng(
<c:out value="${pNode.latitude}"/>,
<c:out value="${pNode.longitude}"/>),
{icon:partCtr}
);
GEvent.addListener(
<c:out value="${markerId}"/>,
"click",
function() {
<c:out value="${markerId}"/>
.openInfoWindowHtml(
"<div class=\"mapInfoPopup\">" +

<c:choose>
<c:when test="${fn:length(pNode.participants) eq 1}">
<c:set var="participant" value="${pNode.participants[0]}"/>
"<a href=\"javascript:selectItemForDiscovery('${participant.id}','PARTICIPANT');\" " +
"><c:out value="${participant.name}"/></a><br/>" +
"<b>Homepage:</b> <a target=\"_blank\" href=\"<c:out value="${participant.homepageUrl}"/>\"><c:out value="${participant.homepageUrl}"/></a>" +
</c:when>
<c:otherwise>
<c:set var="numParticipants" value="${fn:length(pNode.participants)}"/>
<c:set var="participantIds"><c:forEach var="participant" items="${pNode.participants}" varStatus="status"><c:out value="${participant.id}"/><c:if test="${status.count lt numParticipants}">,</c:if></c:forEach></c:set>
"There are <b>" + "<c:out value="${numParticipants}"/></b> participants at this location. " +
"<a href=\"javascript:selectItemsForDiscovery('${participantIds}','PARTICIPANT');\" " +
">View...</a><br/>" +
</c:otherwise>
</c:choose>

"</div>");
}
);
map.addOverlay(<c:out value="${markerId}"/>);
</c:forEach>
</c:if>


<c:if test="${!empty mapBean.serviceNodes}">
<c:forEach
items="${mapBean.serviceNodes}"
var="svcNode"
varStatus="svcNodeStatus">

<c:set var="markerId">svcMarker<c:out value="${svcNodeStatus.index}"/></c:set>
<c:set var="numSvcs" value="${fn:length(svcNode.serviceInfos)}"/>
var <c:out value="${markerId}"/> =
new GMarker(
new GLatLng(
<c:out value="${svcNode.latitude}"/>,
<c:out value="${svcNode.longitude}"/>),
<c:choose>
<c:when test="${numSvcs gt 1}">
<c:choose>
<c:when test="${svcNode.fullyActive}">
{icon:ctrUp}
</c:when>
<c:when test="${svcNode.partiallyActive}">
{icon:ctrUp}
</c:when>
<c:otherwise>
{icon:ctrDown}
</c:otherwise>
</c:choose>
</c:when>
<c:otherwise>
<c:choose>
<c:when test="${svcNode.fullyActive}">
<c:choose>
<c:when test="${'DATA' eq svcNode.serviceInfos[0].type}">
{icon:dSvcUp}
</c:when>
<c:otherwise>
{icon:aSvcUp}
</c:otherwise>
</c:choose>
</c:when>
<c:otherwise>
<c:choose>
<c:when test="${'DATA' eq svcNode.serviceInfos[0].type}">
{icon:dSvcDown}
</c:when>
<c:otherwise>
{icon:aSvcDown}
</c:otherwise>
</c:choose>
</c:otherwise>
</c:choose>
</c:otherwise>

</c:choose>
);
GEvent.addListener(
<c:out value="${markerId}"/>,
"click",
function() {
<c:out value="${markerId}"/>
.openInfoWindowHtml(
"<div class=\"mapInfoPopup\">" +

<c:choose>
<c:when test="${numSvcs eq 1}">
<c:set var="svcInfo" value="${svcNode.serviceInfos[0]}"/>
"<a href=\"javascript:selectItemForDiscovery('${svcInfo.id}','SERVICE');\" " +
"><c:out value="${svcInfo.name}"/></a><br/>" +
"<b>Center:</b> <c:out value="${svcInfo.center}"/><br/>" +
"<b>Status:</b> <c:out value="${svcInfo.status}"/><br/>" +
</c:when>
<c:otherwise>
<c:set var="serviceIds"><c:forEach var="svcInfo" items="${svcNode.serviceInfos}" varStatus="status"><c:out value="${svcInfo.id}"/><c:if test="${status.count lt numSvcs}">,</c:if></c:forEach></c:set>
"There are <b>" + "<c:out value="${numSvcs}"/></b> services at this location. " +
"<a href=\"javascript:selectItemsForDiscovery('${serviceIds}','SERVICE');\" " +
">View...</a>" +
</c:otherwise>
</c:choose>

"</div>");
}
);
map.addOverlay(<c:out value="${markerId}"/>);
</c:forEach>
</c:if>


 <c:if test="${!empty mapBean.pointOfContactNodes}">
<c:forEach
items="${mapBean.pointOfContactNodes}"
var="pocNode"
varStatus="pocNodeStatus">

<c:set var="markerId">pMarker<c:out value="${pocNodeStatus.index}"/></c:set>

var <c:out value="${markerId}"/> =
new GMarker(
new GLatLng(
<c:out value="${pocNode.latitude}"/>,
<c:out value="${pocNode.longitude}"/>),
{icon:partPoc}
);
GEvent.addListener(
<c:out value="${markerId}"/>,
"click",
function() {
<c:out value="${markerId}"/>
.openInfoWindowHtml(
"<div class=\"mapInfoPopup\">" +

<c:choose>
<c:when test="${fn:length(pocNode.pointOfContacts) eq 1}">
<c:set var="poc" value="${pocNode.pointOfContacts[0]}"/>
"<a href=\"javascript:selectItemForDiscovery('${poc.id}','POC');\" " +
"><c:out value="${poc.firstName}"/><c:out value="${poc.lastName}"/></a><br/>" +
"<b>Email:</b> <a target=\"_blank\" href=\"mailto:<c:out value="${poc.emailAddress}"/>\"><c:out value="${poc.emailAddress}"/></a><br/>" +
"<b>Phone:</b><c:out value="${poc.phoneNumber}"/><br/>" +
</c:when>
<c:otherwise>
<c:set var="numPocs" value="${fn:length(pocNode.pointOfContacts)}"/>
<c:set var="pocIds"><c:forEach var="poc" items="${pocNode.pointOfContacts}" varStatus="status"><c:out value="${poc.id}"/><c:if test="${status.count lt numPocs}">,</c:if></c:forEach></c:set>
"There are <b>" + "<c:out value="${numPocs}"/></b> Points of Contact at this location. " +
"<a href=\"javascript:selectItemsForDiscovery('${pocIds}','POC');\" " +
">View...</a><br/>" +
</c:otherwise>
</c:choose>

"</div>");
}
);
map.addOverlay(<c:out value="${markerId}"/>);
</c:forEach>
</c:if>
}
});
//]]>
</script>