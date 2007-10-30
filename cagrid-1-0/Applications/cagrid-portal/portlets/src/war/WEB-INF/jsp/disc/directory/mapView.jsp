<%@ include file="/WEB-INF/jsp/include.jsp" %>

<%@ include file="/WEB-INF/jsp/disc/tabs.jspf" %>

<portlet:actionURL var="action"/>

<table>
<tr>
<td>
<form:form action="${action}" commandName="mapCommand">
<%@ include file="/WEB-INF/jsp/disc/directory/directoriesSelect.jspf" %>
	<input type="hidden" name="operation" value="selectDirectoryMap"/>
</form:form>
</td>
<td>
<form:form action="${action}" commandName="mapCommand">
<%@ include file="/WEB-INF/jsp/disc/directory/searchResultsSelect.jspf" %>
	<input type="hidden" name="operation" value="selectResultsMap"/>
</form:form>
</td>
</tr>
</table>

<script
	src="<c:out value="${mapCommand.baseUrl}"/><c:out value="${mapCommand.apiKey}"/>"
	type="text/javascript"></script>

<c:set var="mapNodeId"><portlet:namespace />-gmap</c:set>
<style type="text/css">
<!--
#<c:out value="${mapNodeId}"/> {
    border: 4px solid #5C5C5C;
    margin-left: auto;
    margin-right: auto;
    width: 95%;
    height: 300px;
}
-->
</style>
<div id="<c:out value="${mapNodeId}"/>"></div>

<script type="text/javascript">
    //<![CDATA[

	var baseIcon = new GIcon();
	baseIcon.iconSize = new GSize(32, 32);
	baseIcon.shadowSize = new GSize(59, 32);
	baseIcon.iconAnchor = new GPoint(9, 34);
	baseIcon.infoWindowAnchor = new GPoint(9, 2);
	baseIcon.infoShadowAnchor = new GPoint(18, 25);
	
    var pinIcon = new GIcon(baseIcon);
	pinIcon.shadow = "<c:url value="/images/pushpin_shadow.png"/>";
	var gPinIcon = new GIcon(pinIcon);
	gPinIcon.image = "<c:url value="/images/grn-pushpin.png"/>";
	var rPinIcon = new GIcon(pinIcon);
	rPinIcon.image = "<c:url value="/images/red-pushpin.png"/>";
	
    var manIcon = new GIcon(baseIcon);
    manIcon.image = "<c:url value="/images/man.png"/>";
	manIcon.shadow = "<c:url value="/images/man.shadow.png"/>";
	
    function load() {
      if (GBrowserIsCompatible()) {
        var map = new GMap2(document.getElementById("<c:out value="${mapNodeId}"/>"));
        map.setCenter(
        	new GLatLng(
        			<c:out value="${mapCommand.centerLatitude}"/>, 
        			<c:out value="${mapCommand.centerLongitude}"/>
        		), <c:out value="${mapCommand.zoomLevel}"/>);
        
        map.enableDoubleClickZoom();
        map.addControl(new GSmallMapControl());
        
        <c:if test="${!empty mapCommand.serviceNodes}">
        	<c:forEach 
        		items="${mapCommand.serviceNodes}" 
        		var="svcNode" 
        		varStatus="svcNodeStatus">
        		
        		<c:set var="markerId">svcMarker<c:out value="${svcNodeStatus.index}"/></c:set>
	        
	        	var <c:out value="${markerId}"/> = 
	        		new GMarker(
	        			new GLatLng(
	        				<c:out value="${svcNode.latitude}"/>, 
	        				<c:out value="${svcNode.longitude}"/>),
	        				<c:choose>
	        					<c:when test="${svcNode.active}">
	        						{icon:gPinIcon}
	        					</c:when>
	        					<c:otherwise>
	        						{icon:rPinIcon}
	        					</c:otherwise>
	        				</c:choose>
	        				);
	        	GEvent.addListener(
	        		<c:out value="${markerId}"/>, 
	        		"click", 
	        		function() {
						<c:out value="${markerId}"/>
							.openInfoWindowHtml(
								"<div style=\"font-size: 0.9em; width: 200px; height: 75px; overflow: auto;\">" + 
								
								<c:forEach items="${svcNode.serviceInfos}" var="svcInfo">
								
									"<a href=\"<portlet:actionURL><portlet:param name="operation" value="selectService"/><portlet:param name="selectedId"><jsp:attribute name="value"><c:out value="${svcInfo.id}"/></jsp:attribute></portlet:param></portlet:actionURL>\" " +
                      			   	"alt=\"View Service Details\"><c:out value="${svcInfo.name}"/></a><br/>" +
									"Hosting Center: <c:out value="${svcInfo.center}"/><br/>" +
									"Status: <c:out value="${svcInfo.status}"/><br/><hr/>" +
									
								</c:forEach>
								
								"</div>");
					}
				);
				map.addOverlay(<c:out value="${markerId}"/>);
        	</c:forEach>
        </c:if>
        
        
		<c:if test="${!empty mapCommand.participantNodes}">
        	<c:forEach 
        		items="${mapCommand.participantNodes}" 
        		var="pNode" 
        		varStatus="pNodeStatus">
        		
        		<c:set var="markerId">pMarker<c:out value="${pNodeStatus.index}"/></c:set>
	        
	        	var <c:out value="${markerId}"/> = 
	        		new GMarker(
	        			new GLatLng(
	        				<c:out value="${pNode.latitude}"/>, 
	        				<c:out value="${pNode.longitude}"/>),
	        				{icon:manIcon}
	        				);
	        	GEvent.addListener(
	        		<c:out value="${markerId}"/>, 
	        		"click", 
	        		function() {
						<c:out value="${markerId}"/>
							.openInfoWindowHtml(
								"<div style=\"font-size: 0.9em; width: 200px; height: 75px; overflow: auto;\">" + 
								
								<c:forEach items="${pNode.participants}" var="participant">
									"<b><c:out value="${participant.name}"/></b><br/>" +
									"Homepage: <a target=\"_blank\" href=\"<c:out value="${participant.homepageUrl}"/>\"><c:out value="${participant.homepageUrl}"/></a><br/>" +
									"Workspaces: <ul><c:forEach items="${participant.participation}" var="participation"><li><c:out value="${participation.workspace.name}"/></li></c:forEach></ul><hr/>" + 
								</c:forEach>
								
								"</div>");
					}
				);
				map.addOverlay(<c:out value="${markerId}"/>);
        	</c:forEach>
        </c:if>        
      }
    }
	load();
    //]]>
</script>